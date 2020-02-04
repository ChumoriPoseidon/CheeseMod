package cmpsd.cheesemod.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cmpsd.cheesemod.ModItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntity_Barrel extends TileEntity implements ITickable, ISidedInventory {

	private static final int[] SLOT_TOP = new int[] {0};
	private static final int[] SLOT_SIDE = new int[] {1};
	private static final int[] SLOT_BOTTOM = new int[] {2, 1};
	private NonNullList<ItemStack> slot = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);

	private int burnTime;
	private int currentBurnTime;
	private int progress;
	private int totalProgress = 10000;

	public final int maxCapacity = 16000;
	public FluidTank tank = new FluidTank(new FluidStack(FluidRegistry.WATER, 0), this.maxCapacity);

	@Override
	public int getSizeInventory() {

		return this.slot.size();
	}

	@Override
	public boolean isEmpty() {

		return this.slot.isEmpty();
	}

	@Override
	public ItemStack getStackInSlot(int index) {

		return this.slot.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {

		return ItemStackHelper.getAndSplit(this.slot, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {

		return ItemStackHelper.getAndRemove(this.slot, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {

		ItemStack itemStack = this.slot.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemStack) && ItemStack.areItemStackShareTagsEqual(stack, itemStack);
		this.slot.set(index, stack);
		if(stack.getCount() > this.getInventoryStackLimit()) {

			stack.setCount(this.getInventoryStackLimit());
		}
		if (index == 0 && !flag) {

			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit() {

		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {

		if(this.world.getTileEntity(this.pos) != this) {

			return false;
		}
		return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {

		if(index == 2) {

			return false;
		}
		if(index != 1) {

			return true;
		}
		ItemStack itemStack = this.slot.get(1);
		return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemStack.getItem() != Items.BUCKET;
	}

	public static boolean isItemFuel(ItemStack stack) {

		return getItemBurnTime(stack) > 0;
	}

	@Override
	public int getField(int id) {

		switch(id) {
		case 0:

			return this.burnTime;
		case 1:

			return this.currentBurnTime;
		case 2:

			return this.progress;
		case 3:

			return this.totalProgress;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value) {

		switch(id) {
		case 0:

			this.burnTime = value;
			break;
		case 1:

			this.currentBurnTime = value;
			break;
		case 2:

			this.progress = value;
			break;
		case 3:

			this.totalProgress = value;
		}
	}

	@Override
	public int getFieldCount() {

		return 4;
	}

	@Override
	public void clear() {

		this.slot.clear();
	}

	@Override
	public String getName() {

		return "container.barrel";
	}

	@Override
	public boolean hasCustomName() {

		return false;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {

		if(side == EnumFacing.DOWN) {

			return SLOT_BOTTOM;
		}
		return side == EnumFacing.UP ? SLOT_TOP : SLOT_SIDE;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {

		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {

		if(direction == EnumFacing.DOWN && index == 1) {

			return stack.getItem() == Items.BUCKET;
		}
		return true;
	}

	int elapsedTick = 0;

	@Override
	public void update() {

		if(!this.world.isRemote) {

			ItemStack itemStack = this.slot.get(1);
			if(this.isBurning() || !itemStack.isEmpty()) {

				if(!this.isBurning() && this.canSmelt()) {

					this.burnTime = getItemBurnTime(itemStack);
					this.currentBurnTime = this.burnTime;
					if(this.isBurning()) {

						if(!itemStack.isEmpty()) {

							Item item = itemStack.getItem();
							itemStack.shrink(1);
							if(itemStack.isEmpty()) {

								ItemStack containerItem = item.getContainerItem(itemStack);
								this.slot.set(1, containerItem);
							}
						}
					}
				}
				if(this.isBurning()) {

					this.burnTime--;
					if(this.canSmelt()) {

						this.progress++;
						if(this.progress > this.totalProgress) {

							this.progress = this.totalProgress;
						}
					}
				}
			}
		}
		this.elapsedTick++;
		if(elapsedTick == 10) {

			this.loadLiquid();
			this.unLoadLiquid();
			this.elapsedTick = 0;

			this.sendUpdate();
		}
	}

	private boolean isBurning() {

		return this.burnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {

		return inventory.getField(0) > 0;
	}

	private boolean canSmelt() {

		return (this.tank.getFluidAmount() > 0 && this.tank.getFluidAmount() <= this.maxCapacity) && (this.progress > 0 && this.progress < this.totalProgress);
	}

	private static int getItemBurnTime(ItemStack itemStack) {

		return TileEntityFurnace.getItemBurnTime(itemStack);
	}

	private void loadLiquid() {

		ItemStack itemStack = this.slot.get(0).copy();
		if(!itemStack.isEmpty() && this.canLoad()) {

			if(itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

				IFluidHandler fluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				FluidStack fluidStack = fluidHandler.drain(this.tank.getCapacity(), false);
				FluidActionResult fluidResult = FluidUtil.tryEmptyContainer(itemStack, this.tank, this.tank.getCapacity(), null, true);
				if(fluidResult.isSuccess()) {

					this.progress = (int)MathHelper.ceil(this.progress / 2.0D);
					this.slot.get(0).shrink(1);
					if(this.slot.get(2).isEmpty()) {

						this.setInventorySlotContents(2, fluidResult.result);
					}
					else {

						this.slot.get(2).grow(fluidResult.result.getCount());
					}
				}
			}
			else if(itemStack.getItem() == ModItem.bacteria) {

				this.progress++;
				this.slot.get(0).shrink(1);
				if(this.slot.get(2).isEmpty()) {

					this.setInventorySlotContents(2, new ItemStack(itemStack.getItem().getContainerItem()));
				}
				else {

					this.slot.get(2).grow(1);
				}
			}
		}
	}

	private boolean canLoad() {

		ItemStack itemStack = this.slot.get(2);
		// スロット2が空っぽ → true
		if(itemStack.isEmpty()) {

			return true;
		}
		// スロット2がスロット0の容器 →
		ItemStack containerItem = this.slot.get(0).getItem().getContainerItem(this.slot.get(0));
		if(!containerItem.isEmpty() && itemStack.isItemEqual(containerItem)) {
			// スロット2のスタック数が最大スタック数より小さい → true
			return itemStack.getCount() < itemStack.getMaxStackSize();
		}
		return false;
	}

	private void unLoadLiquid() {

		ItemStack itemStack = this.slot.get(0).copy();
		if(!itemStack.isEmpty() && this.canUnload()) {

			this.tank.drain(300, true);
			this.slot.get(0).shrink(1);
			if(this.slot.get(2).isEmpty()) {

				this.setInventorySlotContents(2, new ItemStack(ModItem.bacteria));
			}
			else {

				this.slot.get(2).grow(1);
			}
		}
	}

	private boolean canUnload() {

		ItemStack itemStack = this.slot.get(2);
		if(this.tank.getFluidAmount() < 300 || this.progress < this.totalProgress) {

			return false;
		}
		// スロット1のアイテムが「空のビン」 →
		if(this.slot.get(0).getItem() == Items.GLASS_BOTTLE) {
			// スロット2が空っぽ → true
			if(itemStack.isEmpty()) {

				return true;
			}
			return itemStack.getItem() == ModItem.bacteria && itemStack.getCount() < itemStack.getMaxStackSize();
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {

		super.readFromNBT(compound);
		this.slot = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.slot);
		this.burnTime = compound.getInteger("BurnTime");
		this.currentBurnTime = compound.getInteger("CurrentBurnTime");
		this.progress = compound.getInteger("Progress");
		this.tank.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, this.slot);
		compound.setInteger("BurnTime", this.burnTime);
		compound.setInteger("CurrentBurnTime", this.currentBurnTime);
		compound.setInteger("Progress", this.progress);
		this.tank.writeToNBT(compound);
		return compound;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {

			sendUpdate();
			return (T)tank;
		}
		return super.getCapability(capability, facing);
	}

	private void sendUpdate() {

		this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
		this.world.notifyBlockUpdate(pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
		this.world.scheduleBlockUpdate(this.pos, this.blockType, 0, 0);
		this.markDirty();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {

		return new SPacketUpdateTileEntity(this.pos, 99, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {

		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {

		super.onDataPacket(net, packet);
		this.handleUpdateTag(packet.getNbtCompound());
	}

	public void setNBT(NBTTagCompound nbt) {

		this.progress = nbt.getInteger("Progress");
		this.tank.readFromNBT(nbt);
	}

	public NBTTagCompound getNBT(NBTTagCompound nbt) {

		nbt.setInteger("Progress", this.progress);
		this.tank.writeToNBT(nbt);
		return nbt;
	}
}
