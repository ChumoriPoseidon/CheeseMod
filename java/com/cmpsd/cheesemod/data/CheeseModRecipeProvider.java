package com.cmpsd.cheesemod.data;

import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

public class CheeseModRecipeProvider extends RecipeProvider implements IConditionBuilder {

	public CheeseModRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	public void registerRecipes(Consumer<IFinishedRecipe> consumer) {
//		System.out.println("registerRecipes(): " + CheeseModConfig.ENABLE_COOKING_WHOLE_CHEESE_EASILY.get());
//		if(CheeseModConfig.ENABLE_COOKING_WHOLE_CHEESE_EASILY.get()) {
//			ShapelessRecipeBuilder.shapelessRecipe(RegistryEvents.WHOLE_CHEESE)
//			.addIngredient(Items.MILK_BUCKET)
//			.addIngredient(Items.NETHER_WART)
//			.addIngredient(Items.BROWN_MUSHROOM)
//			.addIngredient(Items.BOWL)
//			.addCriterion("has_item", this.hasItem(RegistryEvents.WHOLE_CHEESE))
//			.setGroup("whole_cheese")
//			.build(consumer, RegistryEvents.WHOLE_CHEESE.getRegistryName() + "_0");
//
//			ShapelessRecipeBuilder.shapelessRecipe(RegistryEvents.WHOLE_CHEESE)
//			.addIngredient(Items.MILK_BUCKET)
//			.addIngredient(Items.NETHER_WART)
//			.addIngredient(Items.RED_MUSHROOM)
//			.addIngredient(Items.BOWL)
//			.addCriterion("has_item", this.hasItem(RegistryEvents.WHOLE_CHEESE))
//			.setGroup("whole_cheese")
//			.build(consumer, RegistryEvents.WHOLE_CHEESE.getRegistryName() + "_1");
//
//			ShapelessRecipeBuilder.shapelessRecipe(RegistryEvents.WHOLE_CHEESE)
//			.addIngredient(Items.MILK_BUCKET)
//			.addIngredient(RegistryEvents.BACTERIA)
//			.addCriterion("has_item", this.hasItem(RegistryEvents.WHOLE_CHEESE))
//			.setGroup("whole_cheese")
//			.build(consumer, RegistryEvents.WHOLE_CHEESE.getRegistryName() + "_2");
//		}
//		else {
//			ShapelessRecipeBuilder.shapelessRecipe(RegistryEvents.RAW_CHEESE)
//			.addIngredient(Items.MILK_BUCKET)
//			.addIngredient(Items.NETHER_WART)
//			.addIngredient(Items.BROWN_MUSHROOM)
//			.addIngredient(Items.BOWL)
//			.addCriterion("has_item", this.hasItem(RegistryEvents.RAW_CHEESE))
//			.setGroup("raw_cheese")
//			.build(consumer, RegistryEvents.RAW_CHEESE.getRegistryName() + "_0");
//
//			ShapelessRecipeBuilder.shapelessRecipe(RegistryEvents.RAW_CHEESE)
//			.addIngredient(Items.MILK_BUCKET)
//			.addIngredient(Items.NETHER_WART)
//			.addIngredient(Items.RED_MUSHROOM)
//			.addIngredient(Items.BOWL)
//			.addCriterion("has_item", this.hasItem(RegistryEvents.RAW_CHEESE))
//			.setGroup("raw_cheese")
//			.build(consumer, RegistryEvents.RAW_CHEESE.getRegistryName() + "_1");
//
//			ShapelessRecipeBuilder.shapelessRecipe(RegistryEvents.RAW_CHEESE)
//			.addIngredient(Items.MILK_BUCKET)
//			.addIngredient(RegistryEvents.BACTERIA)
//			.addCriterion("has_item", this.hasItem(RegistryEvents.RAW_CHEESE))
//			.setGroup("raw_cheese")
//			.build(consumer, RegistryEvents.RAW_CHEESE.getRegistryName() + "_2");
//		}
	}
}
