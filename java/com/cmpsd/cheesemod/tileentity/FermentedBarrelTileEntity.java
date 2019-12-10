package com.cmpsd.cheesemod.tileentity;

import java.util.Map;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;
import com.cmpsd.cheesemod.block.FermentedBarrel;
import com.cmpsd.cheesemod.container.FermentedBarrelContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FermentedBarrelTileEntity extends TileEntity implements ISidedInventory, ITickableTileEntity, INamedContainerProvider {

	private static final int[] SLOTS_UP = new int[] {0};
	private static final int[] SLOTS_DOWN = new int[] {2, 1};
	private static final int[] SLOTS_HORIZONTAL = new int[] {1};

	protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

	private int burnTime;
	private int burnTimeTotal;
	private int progress;
	private int progressTotal = 10000;

	public final int maxCapacity = 16000;
	public FluidTank tank = new FluidTank(maxCapacity, fluid -> {
		return fluid.getFluid() == Fluids.WATER;
	});
	private final LazyOptional<IFluidHandler> capability = LazyOptional.of(() -> this.tank);

	protected final IIntArray data = new IIntArray() {

		@Override
		public int get(int index) {
			switch(index) {
			case 0:
				return FermentedBarrelTileEntity.this.burnTime;
			case 1:
				return FermentedBarrelTileEntity.this.burnTimeTotal;
			case 2:
				return FermentedBarrelTileEntity.this.progress;
			case 3:
				return FermentedBarrelTileEntity.this.progressTotal;
			case 4:
				return FermentedBarrelTileEntity.this.tank.getFluidAmount();
			case 5:
				return FermentedBarrelTileEntity.this.tank.getCapacity();
			default:
				return 0;
			}
		}

		@Override
		public void set(int index, int value) {
			switch(index) {
			case 0:
				FermentedBarrelTileEntity.this.burnTime = value;
				break;
			case 1:
				FermentedBarrelTileEntity.this.burnTimeTotal = value;
				break;
			case 2:
				FermentedBarrelTileEntity.this.progress = value;
				break;
			case 3:
				FermentedBarrelTileEntity.this.progressTotal = value;
				break;
			case 4:
				FluidTank tank4 = FermentedBarrelTileEntity.this.tank;
				tank4.fill(new FluidStack(Fluids.WATER, value), FluidAction.EXECUTE);
				break;
			case 5:
				FluidTank tank5 = FermentedBarrelTileEntity.this.tank;
				tank5.setCapacity(value);
				break;
			}
		}

		@Override
		public int size() {
			return 6;
		}
	};

	public static Map<Item, Integer> getBurnTimes(){
		Map<Item, Integer> map = AbstractFurnaceTileEntity.getBurnTimes();
		return map;
	}

	private static void addItemTagBurnTime(Map<Item, Integer> map, Tag<Item> tag, int time) {
		for(Item item : tag.getAllElements()) {
			map.put(item, time);
		}
	}

	private static void addItemBurnTime(Map<Item, Integer> map, IItemProvider item, int time) {
		map.put(item.asItem(), time);
	}

	public FermentedBarrelTileEntity() {
		super(RegistryEvents.FERMENTED_BARREL_TILEENTITY);
	}

	@Override
	public int getSizeInventory() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack stack : this.items) {
			if(!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.items, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.items, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemStack = this.items.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(stack, itemStack);
		this.items.set(index, stack);
		if(stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		if(index == 0 && !flag) {
			this.markDirty();
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if(this.world.getTileEntity(this.pos) != this) {
			return false;
		}
		else {
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clear() {
		this.items.clear();
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new FermentedBarrelContainer(id, inventory, this, this.data);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container.cheesemod.fermented_barrel");
	}

	int elapsedTick = 0;

	@Override
	public void tick() {
		boolean flagBurning = this.isBurning();
		boolean flagComplete = this.isComplete();
		boolean update = false;
		if(this.isBurning()) {
			this.burnTime--;
		}
		if(!this.world.isRemote) {
			ItemStack stack = this.items.get(1);
			if(this.isBurning() || !stack.isEmpty()) {
				if(!this.isBurning() && this.canProgressive()) {
					this.burnTime = this.initBurnTime(stack);
					this.burnTimeTotal = this.burnTime;
					if(this.isBurning()) {
						update = true;
						if(stack.hasContainerItem()) {
							this.items.set(1, stack.getContainerItem());
						}
						else {
							if(!stack.isEmpty()) {
								stack.shrink(1);
								if(stack.isEmpty()) {
									this.items.set(1, stack.getContainerItem());
								}
							}
						}
					}
				}
				if(this.isBurning() && this.canProgressive()) {
					this.progress++;
//					this.progress += 10;
					if(this.progress > this.progressTotal) {
						this.progress = this.progressTotal;
					}
				}
			}
			this.elapsedTick--;
			if(elapsedTick <= 0) {
				this.elapsedTick = 10;
				this.load();
				this.unload();
				this.update();
			}
			if(flagBurning != this.isBurning()) {
				update = true;
				if(!this.isComplete()) {
					this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(FermentedBarrel.AGE, Integer.valueOf(this.isBurning() ? 1 : 0)), 3);
				}
			}
			if(flagComplete != this.isComplete()) {
				update = true;
				this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(FermentedBarrel.AGE, Integer.valueOf(this.isComplete() ? 2 : this.isBurning() ? 1 : 0)), 3);
			}
		}
		if(update) {
			this.markDirty();
		}
	}

	private boolean isBurning() {
		return this.burnTime > 0;
	}

	public boolean isComplete() {
		return this.progress >= this.progressTotal;
	}

	private boolean canProgressive() {
		return this.tank.getFluidAmount() > 0 && (this.progress > 0 && this.progress < this.progressTotal);
	}

	private int initBurnTime(ItemStack stack) {
		if(stack.isEmpty()) {
			return 0;
		}
		else {
			Item item = stack.getItem();
			return getBurnTimes().getOrDefault(item, 0);
		}
	}

	private void update() {

	}

	private void load() {
		if(this.canLoad()) {
			ItemStack slotIn = this.items.get(0);
			ItemStack slotOut = this.items.get(2);
			if(slotIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) != null) {
				FluidActionResult fluidResult = FluidUtil.tryEmptyContainer(slotIn, this.tank, this.tank.getCapacity(), null, true);
				if(fluidResult.isSuccess()) {
					this.progress = (int)Math.ceil(this.progress / 2.0);
					if(slotOut.isEmpty()) {
						this.items.set(2, fluidResult.getResult());
					}
					else {
						slotOut.grow(fluidResult.getResult().getCount());
					}
					slotIn.shrink(1);
				}
			}
			if(slotIn.getItem() == RegistryEvents.BACTERIA) {
				this.progress++;
				if(this.progress > this.progressTotal) {
					this.progress = this.progressTotal;
				}
				if(slotOut.isEmpty()) {
					this.items.set(2, slotIn.getContainerItem());
				}
				else {
					slotOut.grow(slotIn.getContainerItem().getCount());
				}
				slotIn.shrink(1);
			}
		}
	}

	private boolean canLoad() {
		ItemStack slotIn = this.items.get(0);
		ItemStack slotOut = this.items.get(2);
		if(slotIn.isEmpty()) {
			return false;
		}
		if(slotOut.isEmpty()) {
			return true;
		}
		ItemStack result = slotIn.getContainerItem();
		if(!result.isEmpty() && slotOut.isItemEqual(result)) {
			return slotOut.getCount() + result.getCount() <= slotOut.getMaxStackSize();
		}
		return false;
	}

	private void unload() {
		if(this.canUnload()) {
			ItemStack slotIn = this.items.get(0);
			ItemStack slotOut = this.items.get(2);
			this.tank.drain(300, FluidAction.EXECUTE);
			slotIn.shrink(1);
			if(slotOut.isEmpty()) {
				this.items.set(2, new ItemStack(RegistryEvents.BACTERIA));
			}
			else {
				slotOut.grow(1);
			}
		}
	}

	private boolean canUnload() {
		ItemStack slotIn = this.items.get(0);
		ItemStack slotOut = this.items.get(2);
		if(slotIn.isEmpty() || this.tank.getFluidAmount() < 300 || this.progress < this.progressTotal) {
			return false;
		}
		if(slotIn.getItem() == Items.GLASS_BOTTLE) {
			if(slotOut.isEmpty()) {
				return true;
			}
			return slotOut.getItem() == RegistryEvents.BACTERIA && slotOut.getCount() + 1 <= slotOut.getMaxStackSize();
		}
		return false;
	}

	@Override
	public int[] getSlotsForFace(Direction side) {
		if(side == Direction.DOWN) {
			return SLOTS_DOWN;
		}
		else {
			return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
		if(direction == Direction.DOWN && index == 1) {

			return stack.getItem() == Items.BUCKET;
		}
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == 2) {
			return false;
		}
		else if(index != 1) {
			return true;
		}
		else {
			return isFuel(stack) || stack.getItem() == Items.BUCKET && stack.getItem() != Items.BUCKET;
		}
	}

	public static boolean isFuel(ItemStack stack) {
		return getBurnTimes().getOrDefault(stack.getItem(), 0) > 0;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);
		this.burnTime = compound.getInt("BurnTime");
		this.burnTimeTotal = this.initBurnTime(this.items.get(1));
		this.progress = compound.getInt("Progress");
		this.tank.readFromNBT(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("BurnTime", this.burnTime);
		compound.putInt("Progress", this.progress);
		ItemStackHelper.saveAllItems(compound, this.items);
		this.tank.writeToNBT(compound);
		return compound;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			this.update();
			return this.capability.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 25, this.getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
}
