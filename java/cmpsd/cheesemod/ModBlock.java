package cmpsd.cheesemod;

import java.util.ArrayList;
import java.util.List;

import cmpsd.cheesemod.block.Barrel;
import cmpsd.cheesemod.block.CheeseCake;
import cmpsd.cheesemod.block.CheeseFondue;
import cmpsd.cheesemod.block.RawCheese;
import cmpsd.cheesemod.block.WholeCheese;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ModBlock {

	public static List<Block> BLOCKS = new ArrayList<Block>();

	public static Block rawCheese;
	public static Block wholeCheese;

	public static Block cheeseCake;
	public static Block cheeseFondue;

	public static Block barrel;

	public static void register() {

		rawCheese = new RawCheese();
		wholeCheese = new WholeCheese();

		cheeseCake = new CheeseCake();
		cheeseFondue = new CheeseFondue();

		barrel = new Barrel();
	}

	public static void registerModel() {

		registerBlockRender(rawCheese);
		registerBlockRender(wholeCheese);

		registerBlockRender(cheeseCake);
		registerBlockRender(cheeseFondue);

		registerBlockRender(barrel);
	}

	public static void registerBlockRender(Block block) {

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "Inventory"));
	}
}
