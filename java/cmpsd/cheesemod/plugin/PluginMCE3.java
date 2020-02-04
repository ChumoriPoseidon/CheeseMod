package cmpsd.cheesemod.plugin;

import cmpsd.cheesemod.ModBlock;
import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModPlugin;
import net.minecraft.item.ItemStack;
import shift.mceconomy3.api.MCEconomyAPI;

public class PluginMCE3 {

	public static void init() {

		registerMP();
	}

	public static void registerMP() {

		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheese), 125);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesePiece), 30);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.meltedCheese), 130);

		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedApple), 50);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedBakedPotato), 60);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedBread), 120);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCarrot), 50);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookedBeef), 140);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookedChicken), 90);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookedCod), 110);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookedMutton), 100);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookedPorkchop), 60);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookedRabbit), 70);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookedSalmon), 330);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheesedCookie), 60);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheeseCakePiece), 100);
		MCEconomyAPI.addPurchaseItem(new ItemStack(ModItem.cheeseBurger), 320);

		MCEconomyAPI.addPurchaseItem(new ItemStack(ModBlock.cheeseCake), 800);

		if(ModPlugin.loadedHaC) {

			MCEconomyAPI.addPurchaseItem(new ItemStack(PluginHaC.herbCheese), 150);
			MCEconomyAPI.addPurchaseItem(new ItemStack(PluginHaC.cheesedBoiledEgg), 60);
			MCEconomyAPI.addPurchaseItem(new ItemStack(PluginHaC.cheesedBoiledSausage), 110);
			MCEconomyAPI.addPurchaseItem(new ItemStack(PluginHaC.pizzaBread), 60);
		}
	}
}
