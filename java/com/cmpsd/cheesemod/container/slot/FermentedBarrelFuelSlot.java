package com.cmpsd.cheesemod.container.slot;

import com.cmpsd.cheesemod.container.FermentedBarrelContainer;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FermentedBarrelFuelSlot extends Slot {

	private final FermentedBarrelContainer container;

	public FermentedBarrelFuelSlot(FermentedBarrelContainer containerIn, IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		this.container = containerIn;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return this.container.isFuel(stack);
	}
}
