package com.cmpsd.cheesemod.block;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class WholeCheese extends Block {

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);

	public WholeCheese() {
		super(Properties.create(Material.CAKE).sound(SoundType.CLOTH).hardnessAndResistance(0.5F));
		this.setRegistryName("block_whole_cheese");

		RegistryEvents.BLOCKS.add(this);
		RegistryEvents.ITEMS.add(new BlockItem(this, new Item.Properties().group(RegistryEvents.CHEESE_MOD_GROUP)).setRegistryName(this.getRegistryName()));
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
