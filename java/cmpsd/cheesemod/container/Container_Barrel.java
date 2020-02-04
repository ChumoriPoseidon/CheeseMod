package cmpsd.cheesemod.container;

import cmpsd.cheesemod.tileentity.TileEntity_Barrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Container_Barrel extends Container {

	private TileEntity_Barrel te_barrel;
	private int lastBurnTime;
	private int lastCurrentBurnTime;
	private int lastProgress;
	private int lastTotalProgress;

	public Container_Barrel(InventoryPlayer inventoryPlayer, TileEntity_Barrel tileEntity) {

		this.te_barrel = tileEntity;

		this.addSlotToContainer(new Slot(tileEntity, 0, 143, 17));
		this.addSlotToContainer(new SlotFurnaceFuel(tileEntity, 1, 26, 53));
		this.addSlotToContainer(new SlotFurnaceOutput(inventoryPlayer.player, tileEntity, 2, 143, 53));

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {

				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		for(int i = 0; i < 9; i++) {

			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {

		return this.te_barrel.isUsableByPlayer(playerIn);
	}

	public void addListener(IContainerListener listener) {

		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.te_barrel);
	}

	public void detectAndSendChanges() {

		super.detectAndSendChanges();
		for(int i = 0; i < this.listeners.size(); i++) {

			IContainerListener listener = (IContainerListener)this.listeners.get(i);
			if(this.lastBurnTime != this.te_barrel.getField(0)) {

				listener.sendWindowProperty(this, 0, this.te_barrel.getField(0));
			}
			if(this.lastCurrentBurnTime != this.te_barrel.getField(1)) {

				listener.sendWindowProperty(this, 1, this.te_barrel.getField(1));
			}
			if(this.lastProgress != this.te_barrel.getField(2)) {

				listener.sendWindowProperty(this, 2, this.te_barrel.getField(2));
			}
			if(this.lastTotalProgress != this.te_barrel.getField(3)) {

				listener.sendWindowProperty(this, 3, this.te_barrel.getField(3));
			}
		}
		this.lastBurnTime = this.te_barrel.getField(0);
		this.lastCurrentBurnTime = this.te_barrel.getField(1);
		this.lastProgress = this.te_barrel.getField(2);
		this.lastTotalProgress = this.te_barrel.getField(3);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {

		this.te_barrel.setField(id, data);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot)this.inventorySlots.get(index);
		if(slot != null && slot.getHasStack()) {

			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index == 2) {

				if(!this.mergeItemStack(itemstack1, 3, 39, true)) {

					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(index != 1 && index != 0) {

				if(TileEntity_Barrel.isItemFuel(itemstack1)) {

					if(!this.mergeItemStack(itemstack1, 1, 2, false)) {

						return ItemStack.EMPTY;
					}
				}
				else if(index >= 3 && index < 30) {

					if(!this.mergeItemStack(itemstack1, 30, 39, false)) {

						return ItemStack.EMPTY;
					}
				}
				else if(index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {

					return ItemStack.EMPTY;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 3, 39, false)) {

				return ItemStack.EMPTY;
			}
			if(itemstack1.isEmpty()) {

				slot.putStack(ItemStack.EMPTY);
			}
			else {

				slot.onSlotChanged();
			}
			if(itemstack1.getCount() == itemstack.getCount()) {

				return null;
			}
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}
}
