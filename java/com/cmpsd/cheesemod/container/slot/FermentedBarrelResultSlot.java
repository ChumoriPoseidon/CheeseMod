package com.cmpsd.cheesemod.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FermentedBarrelResultSlot extends Slot {

	private final PlayerEntity player;

	public FermentedBarrelResultSlot(PlayerEntity playerIn, IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.player = playerIn;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
