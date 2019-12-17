package com.cmpsd.cheesemod.container;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;
import com.cmpsd.cheesemod.container.slot.FermentedBarrelFuelSlot;
import com.cmpsd.cheesemod.container.slot.FermentedBarrelResultSlot;
import com.cmpsd.cheesemod.tileentity.FermentedBarrelTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FermentedBarrelContainer extends Container {

	private final IInventory inventory;
	private final IIntArray data;
	protected final World world;
//	public final IntReferenceHolder fluidAmount = IntReferenceHolder.single();

	public FermentedBarrelContainer(int id, PlayerInventory playerInventory) {
		this(id, playerInventory, new Inventory(3), new IntArray(6));
	}

	public FermentedBarrelContainer(int id, PlayerInventory playerInventory, IInventory inventoryIn, IIntArray dataIn) {
		super(RegistryEvents.FERMENTED_BARREL_CONTAINER, id);
		assertInventorySize(inventoryIn, 3);
		assertIntArraySize(dataIn, 6);
		this.inventory = inventoryIn;
		this.data = dataIn;
		this.world = playerInventory.player.world;

		this.addSlot(new Slot(inventoryIn, 0, 143, 17));
		this.addSlot(new FermentedBarrelFuelSlot(this, inventoryIn, 1, 26, 53));
		this.addSlot(new FermentedBarrelResultSlot(playerInventory.player, inventoryIn, 2, 143, 53));

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for(int k = 0; k < 9; k++) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

		this.trackIntArray(dataIn);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.inventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack()) {
			ItemStack itemStack = slot.getStack();
			stack = itemStack.copy();
			if(index == 2) {
				if(!this.mergeItemStack(itemStack, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemStack, stack);
			}
			else if(index != 1 && index != 0){
				if(this.isContainWater(itemStack)) {
					if(!this.mergeItemStack(itemStack, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if(this.isFuel(itemStack)) {
					if(!this.mergeItemStack(itemStack, 1, 2, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if(index >= 3 && index < 30) {
					if(!this.mergeItemStack(itemStack, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				}
				else if(index >= 30 && index < 39) {
					if(!this.mergeItemStack(itemStack, 3, 30, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			else if(!this.mergeItemStack(itemStack, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if(itemStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}

			if(itemStack.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemStack);
		}
		return stack;
	}

	private boolean isContainWater(ItemStack stack) {
		if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
			if(handler.getFluidInTank(0).getFluid() == Fluids.WATER) {
				return true;
			}
			return false;
		}).orElse(false)) {
			return true;
		}
		Item item = stack.getItem();
		if(item == Items.GLASS_BOTTLE || item == RegistryEvents.BACTERIA) {
			return true;
		}
		return false;
	}

	public boolean isFuel(ItemStack stack) {
		return FermentedBarrelTileEntity.isFuel(stack);
	}

	@OnlyIn(Dist.CLIENT)
	public int getBurnLeftScaled() {
		int i = this.data.get(1);
		if(i == 0) {
			i = 200;
		}
		return this.data.get(0) * 13 / i;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isBurning() {
		return this.data.get(0) > 0;
	}

	@OnlyIn(Dist.CLIENT)
	public int getFluidAmountScaled() {
		return this.data.get(4) * 48 / this.data.get(5);
	}

	@OnlyIn(Dist.CLIENT)
	public String getTankInfo() {
		String info = "";
		info += "Amount: " + this.data.get(4) + " / " + this.data.get(5) + "mb";
		return info;
	}

	@OnlyIn(Dist.CLIENT)
	public String getProgressInfo() {
		String progress = "";
		progress += "Progress: " + this.data.get(2) * 100.0F / this.data.get(3) + "%";
		return progress;
	}

	@OnlyIn(Dist.CLIENT)
	public int getProgress() {
		return (int)(this.data.get(2) * 100.0F / this.data.get(3));
	}
}
