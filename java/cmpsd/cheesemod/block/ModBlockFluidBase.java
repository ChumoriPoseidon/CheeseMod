package cmpsd.cheesemod.block;

import cmpsd.cheesemod.ModTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlockFluidBase extends BlockFluidClassic {

	public ModBlockFluidBase(String registryName, String unlocalizedName, Fluid fluid, Material material) {

		super(fluid, material);
		this.setRegistryName(registryName);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(ModTab.tabCheeseMod);

		fluid.setBlock(this);

		GameRegistry.findRegistry(Block.class).register(this);
//		GameRegistry.findRegistry(Item.class).register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
}
