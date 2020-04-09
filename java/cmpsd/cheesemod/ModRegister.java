package cmpsd.cheesemod;

import cmpsd.cheesemod.plugin.PluginHaC;
import cmpsd.cheesemod.plugin.PluginMKNUtils;
import cmpsd.cheesemod.tileentity.TileEntity_Barrel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModRegister {

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {

		ModItem.register();
		if(ModPlugin.loadedHaC) {

			PluginHaC.registerItems();
		}
		if(ModPlugin.loadedMKNUtils) {

			PluginMKNUtils.registerItems();
		}
		event.getRegistry().registerAll(ModItem.ITEMS.toArray(new Item[0]));
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {

		ModBlock.register();
		ModFluid.register();
		event.getRegistry().registerAll(ModBlock.BLOCKS.toArray(new Block[0]));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {

		ModItem.registerModel();
		ModBlock.registerModel();
		ModFluid.registerModel();
		if(ModPlugin.loadedHaC) {

			PluginHaC.registerModels();
		}
		if(ModPlugin.loadedMKNUtils) {

			PluginMKNUtils.registerModels();
		}
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		registerFurnaceRecipes();
		if(ModPlugin.loadedHaC) {

			PluginHaC.registerRecipes(event.getRegistry());
		}
	}

	public static void registerFurnaceRecipes() {

		GameRegistry.addSmelting(new ItemStack(ModItem.cheese), new ItemStack(ModItem.meltedCheese), 0.2F);
	}

	public static void registerTileEntity() {

		GameRegistry.registerTileEntity(TileEntity_Barrel.class, new ResourceLocation(Reference.MODID, "te.barrel"));
	}
}