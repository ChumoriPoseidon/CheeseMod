package com.cmpsd.cheesemod.item;

import org.apache.commons.lang3.tuple.Pair;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CheesedFoodBase extends Item {

	private final ItemStack ingredient;

	public CheesedFoodBase(ItemStack ingredientIn) {
		super(initProperties(ingredientIn));
		if(!ingredientIn.isEmpty() && ingredientIn.getItem().isFood()) {
			this.ingredient = ingredientIn;
		}
		else {
			this.ingredient = ItemStack.EMPTY;
		}
		this.setRegistryName("item_cheesed_" + ingredientIn.getItem().getRegistryName().getPath());

		RegistryEvents.ITEMS.add(this);
	}

	private static Properties initProperties(ItemStack ingredientIn) {
		Item.Properties properties = new Item.Properties().group(RegistryEvents.CHEESE_MOD_GROUP);
		if(!ingredientIn.isEmpty() && ingredientIn.getItem().isFood()) {
			Item item = ingredientIn.getItem();
			Food baseFood = item.getFood();
			int value = baseFood.getHealing() + 2;
			float saturation = baseFood.getSaturation() + 0.3F;

			Food.Builder builder = new Food.Builder().hunger(value).saturation(saturation);
			if(!baseFood.getEffects().isEmpty()) {
				for(Pair<EffectInstance, Float> effect : baseFood.getEffects()) {
					builder.effect(effect.getKey(), effect.getValue());
				}
			}
			Food food = builder.build();
			properties.food(food);
		}
		return properties;
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		return new TranslationTextComponent("item.cheesemod.item_cheesed").appendText(this.ingredient.getDisplayName().getString());
	}
}
