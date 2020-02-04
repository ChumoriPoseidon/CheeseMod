package cmpsd.cheesemod.plugin;

import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.item.ModFoodBase;
import mknutils.MKNUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PluginMKNUtils {

	public static Item cheesedBoiledCrabLeg;
	public static Item cheesedChocolateBar;
	public static Item cheesedChocolateCookie;
	public static Item cheesedMilkCookie;

	public static void registerItems() {

		cheesedBoiledCrabLeg = new ModFoodBase("item_cheesed_boiled_crab_leg", "cheesedBoiledCrabLeg", new ItemStack(MKNUtils.foodmisc, 1, 16));
		cheesedChocolateBar = new ModFoodBase("item_cheesed_chocolate_bar", "cheesedChocolateBar", new ItemStack(MKNUtils.foodmisc, 1, 17));
		cheesedChocolateCookie = new ModFoodBase("item_cheesed_chocolate_cookie", "cheesedChocolateCookie", new ItemStack(MKNUtils.foodmisc, 1, 18));
		cheesedMilkCookie = new ModFoodBase("item_cheesed_milk_cookie", "cheesedMilkCookie", new ItemStack(MKNUtils.foodmisc, 1, 9));
	}

	public static void registerModels() {

		ModItem.registerItemRender(cheesedBoiledCrabLeg);
		ModItem.registerItemRender(cheesedChocolateBar);
		ModItem.registerItemRender(cheesedChocolateCookie);
		ModItem.registerItemRender(cheesedMilkCookie);
	}
}
