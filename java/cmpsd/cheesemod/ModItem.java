package cmpsd.cheesemod;

import java.util.ArrayList;
import java.util.List;

import cmpsd.cheesemod.item.Bacteria;
import cmpsd.cheesemod.item.ModFoodBase;
import cmpsd.cheesemod.item.PocketCheeseFondue;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.oredict.OreDictionary;

public class ModItem {

	public static List<Item> ITEMS = new ArrayList<Item>();

	public static Item cheese;
	public static Item cheesePiece;
	public static Item meltedCheese;

	public static Item cheesedApple;
	public static Item cheesedBakedPotato;
	public static Item cheesedBread;
	public static Item cheesedCarrot;
	public static Item cheesedCookedBeef;
	public static Item cheesedCookedChicken;
	public static Item cheesedCookedCod;
	public static Item cheesedCookedMutton;
	public static Item cheesedCookedPorkchop;
	public static Item cheesedCookedRabbit;
	public static Item cheesedCookedSalmon;
	public static Item cheesedCookie;
	public static Item cheeseCakePiece;
	public static Item cheeseBurger;
	public static Item cheeseBar;

	public static Item bacteria;
	public static Item pocketCheeseFondue;

	public static void register() {

		cheese = new ModFoodBase("item_cheese", "cheese", 1, 0.2F);
		cheesePiece = new ModFoodBase("item_cheese_piece", "cheesePiece", 0, 0.0F);
		meltedCheese = new ModFoodBase("item_melted_cheese", "meltedCheese", 2, 0.4F);

		cheesedApple = new ModFoodBase("item_cheesed_apple", "cheesedApple", new ItemStack(Items.APPLE));
		cheesedBakedPotato = new ModFoodBase("item_cheesed_baked_potato", "cheesedBakedPotato", new ItemStack(Items.BAKED_POTATO));
		cheesedBread = new ModFoodBase("item_cheesed_bread", "cheesedBread", new ItemStack(Items.BREAD));
		cheesedCarrot = new ModFoodBase("item_cheesed_carrot", "cheesedCarrot", new ItemStack(Items.CARROT));
		cheesedCookedBeef = new ModFoodBase("item_cheesed_cooked_beef", "cheesedCookedBeef", new ItemStack(Items.COOKED_BEEF));
		cheesedCookedChicken = new ModFoodBase("item_cheesed_cooked_chicken", "cheesedCookedChicken", new ItemStack(Items.COOKED_CHICKEN));
		cheesedCookedCod = new ModFoodBase("item_cheesed_cooked_cod", "cheesedCookedCod", new ItemStack(Items.COOKED_FISH, 1, 0));
		cheesedCookedMutton = new ModFoodBase("item_cheesed_cooked_mutton", "cheesedCookedMutton", new ItemStack(Items.COOKED_MUTTON));
		cheesedCookedPorkchop = new ModFoodBase("item_cheesed_cooked_porkchop", "cheesedCookedPorkchop", new ItemStack(Items.COOKED_PORKCHOP));
		cheesedCookedRabbit = new ModFoodBase("item_cheesed_cooked_rabbit", "cheesedCookedRabbit", new ItemStack(Items.COOKED_RABBIT));
		cheesedCookedSalmon = new ModFoodBase("item_cheesed_cooked_salmon", "cheesedCookedSalmon", new ItemStack(Items.COOKED_FISH, 1, 1));
		cheesedCookie = new ModFoodBase("item_cheesed_cookie", "cheesedCookie", new ItemStack(Items.COOKIE));
		cheeseCakePiece = new ModFoodBase("item_cheese_cake_piece", "cheeseCakePiece", 3, 0.3F);
		cheeseBurger = new ModFoodBase("item_cheese_burger", "cheeseBurger", 10, 0.7F);
		cheeseBar = new ModFoodBase("item_cheese_bar", "cheeseBar", 3, 0.8F, 16);

		bacteria = new Bacteria();
		pocketCheeseFondue = new PocketCheeseFondue();
		//鉱石辞書登録はinit以降
	}

	public static void registerModel() {

		registerItemRender(cheese);
		registerItemRender(cheesePiece);
		registerItemRender(meltedCheese);

		registerItemRender(cheesedApple);
		registerItemRender(cheesedBakedPotato);
		registerItemRender(cheesedBread);
		registerItemRender(cheesedCarrot);
		registerItemRender(cheesedCookedBeef);
		registerItemRender(cheesedCookedChicken);
		registerItemRender(cheesedCookedCod);
		registerItemRender(cheesedCookedMutton);
		registerItemRender(cheesedCookedPorkchop);
		registerItemRender(cheesedCookedRabbit);
		registerItemRender(cheesedCookedSalmon);
		registerItemRender(cheesedCookie);
		registerItemRender(cheeseCakePiece);
		registerItemRender(cheeseBurger);
		registerItemRender(cheeseBar);

		registerItemRender(bacteria);
		registerItemRender(pocketCheeseFondue);
	}

	public static void registerItemRender(Item item, String[] subName) {

		for(int meta = 0; meta < subName.length; meta++) {

			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName() + "_" + subName[meta], "Inventory"));
		}
	}

	public static void registerItemRender(Item item) {

		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "Inventory"));
	}

	public static void registerOreDict() {

		OreDictionary.registerOre("foodCheese", cheese);
	}
}
