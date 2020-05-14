package cmpsd.cheesemod.plugin;

import cmpsd.cheesemod.ModFluid;
import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModPlugin;
import cmpsd.cheesemod.Reference;
import cmpsd.cheesemod.item.ModFoodBase;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.recipe.RecipeAPI;
import defeatedcrow.hac.food.FoodInit;
import defeatedcrow.hac.main.MainInit;
import mknutils.MKNUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class PluginHaC {

	public static Item herbCheese;
	public static Item cheesedBoiledEgg;
	public static Item cheesedBoiledSausage;
	public static Item pizzaBread;

	public static void registerItems() {

		herbCheese = new ModFoodBase("item_herb_cheese", "herbCheese", 3, 0.4F);
		cheesedBoiledEgg = new ModFoodBase("item_cheesed_boiled_egg", "cheesedBoiledEgg", new ItemStack(MainInit.bakedApple, 1, 1));
		cheesedBoiledSausage = new ModFoodBase("item_cheesed_boiled_sausage", "cheesedBoiledSausage", new ItemStack(MainInit.bakedApple, 1, 3));
		pizzaBread = new ModFoodBase("item_pizza_bread", "pizzaBread", 5, 0.9F);
	}

	public static void registerModels() {

		ModItem.registerItemRender(herbCheese);
		ModItem.registerItemRender(cheesedBoiledEgg);
		ModItem.registerItemRender(cheesedBoiledSausage);
		ModItem.registerItemRender(pizzaBread);
	}

	public static void registerRecipes(IForgeRegistry<IRecipe> registry) {

		registry.registerAll(
				new ShapelessOreRecipe(new ResourceLocation("recipe_herb_cheese"), new ItemStack(herbCheese, 4), "bucketMilk", Items.NETHER_WART, Items.BOWL, Blocks.BROWN_MUSHROOM, new ItemStack(FoodInit.crops, 1, 9)).setRegistryName(Reference.MODID, "craft_herb_cheese0"),
				new ShapelessOreRecipe(new ResourceLocation("recipe_herb_cheese"), new ItemStack(herbCheese, 4), "bucketMilk", Items.NETHER_WART, Items.BOWL, Blocks.RED_MUSHROOM, new ItemStack(FoodInit.crops, 1, 9)).setRegistryName("craft_herb_cheese1"),
				new ShapelessOreRecipe(new ResourceLocation("recipe_herb_cheese"), new ItemStack(herbCheese, 2), ModItem.bacteria,  "bucketMilk", Items.BOWL, new ItemStack(FoodInit.crops, 1, 9)).setRegistryName("craft_herb_cheese2"),
				new ShapelessOreRecipe(new ResourceLocation("recipe_pizza_bread"), new ItemStack(pizzaBread, 3), new ItemStack(FoodInit.bread, 1, 3), "cropTomato", ModItem.meltedCheese).setRegistryName("craft_pizza_bread"));
	}

	public static void init() {

		registerCollaboRecipes();
	}

	private static void registerCollaboRecipes() {

		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedApple), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.APPLE));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedBakedPotato), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.BAKED_POTATO));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedBread), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.BREAD));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCarrot), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.CARROT));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookedBeef), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_BEEF));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookedChicken), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_CHICKEN));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookedCod), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_FISH, 1, 0));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookedMutton), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_MUTTON));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookedPorkchop), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_PORKCHOP));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookedRabbit), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_RABBIT));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookedSalmon), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_FISH, 1, 1));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(ModItem.cheesedCookie), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKIE));

		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(cheesedBoiledEgg), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MainInit.bakedApple, 1, 1));
		RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(cheesedBoiledSausage), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MainInit.bakedApple, 1, 3));


//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCarrot), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.CARROT)); // Attention(BOIL-OVEN)
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookedBeef), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_BEEF));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookedChicken), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_CHICKEN));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookedCod), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_FISH, 1, 0));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookedMutton), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_MUTTON));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookedPorkchop), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_PORKCHOP));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookedRabbit), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_RABBIT));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookedSalmon), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKED_FISH, 1, 1));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(ModItem.cheesedCookie), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(Items.COOKIE));
//
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(cheesedBoiledEgg), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MainInit.bakedApple, 1, 1));
//		FoodFluidRecipe.regBoilrecipe(new ItemStack(cheesedBoiledSausage), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MainInit.bakedApple, 1, 3));

		if(ModPlugin.loadedMKNUtils) {

			RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(PluginMKNUtils.cheesedBoiledCrabLeg), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 16));
			RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(PluginMKNUtils.cheesedChocolateBar), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 17));
			RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(PluginMKNUtils.cheesedChocolateCookie), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 18));
			RecipeAPI.registerFluidRecipes.addRecipe(new ItemStack(PluginMKNUtils.cheesedMilkCookie), ItemStack.EMPTY, 0.0F, null, DCHeatTier.BOIL, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 9));

//			FoodFluidRecipe.regBoilrecipe(new ItemStack(PluginMKNUtils.cheesedBoiledCrabLeg), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 16));
//			FoodFluidRecipe.regBoilrecipe(new ItemStack(PluginMKNUtils.cheesedChocolateBar), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 17));
//			FoodFluidRecipe.regBoilrecipe(new ItemStack(PluginMKNUtils.cheesedChocolateCookie), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 18));
//			FoodFluidRecipe.regBoilrecipe(new ItemStack(PluginMKNUtils.cheesedMilkCookie), null, 0F, null, null, null, false, new FluidStack(ModFluid.fluidMeltedCheese, 100), new ItemStack(MKNUtils.foodmisc, 1, 9));
		}
	}
}
