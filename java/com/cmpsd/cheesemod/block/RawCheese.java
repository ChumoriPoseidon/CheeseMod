package com.cmpsd.cheesemod.block;

import java.util.Random;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class RawCheese extends Block {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_7;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 7.0D, 15.0D);

	public RawCheese() {
		super(Properties.create(Material.CAKE).tickRandomly().sound(SoundType.CLOTH).hardnessAndResistance(0.5F));
		this.setRegistryName("block_raw_cheese");
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));

		RegistryEvents.BLOCKS.add(this);
		RegistryEvents.ITEMS.add(new BlockItem(this, new Item.Properties().group(RegistryEvents.CHEESE_MOD_GROUP)).setRegistryName(this.getRegistryName()));
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
//			if(player.isSneaking()) {
//				int meta = state.get(AGE);
//				float chance = this.getChance(state, worldIn, pos);
//				player.sendMessage(new StringTextComponent("AGE: " + meta + ", Chance: " + chance));
//				if(this.RANDOM.nextInt((int)(25.0F / chance) + 1) == 0) {
//					meta++;
//					if(meta > 7) {
//						worldIn.setBlockState(pos, RegistryEvents.WHOLE_CHEESE.getDefaultState(), 3);
//					}
//					else {
//						worldIn.setBlockState(pos, state.with(AGE, meta), 3);
//					}
//				}
//			}
//			else {
				player.sendStatusMessage(new StringTextComponent(new TranslationTextComponent("block.cheesemod.block_raw_cheese.get_age").getFormattedText() + state.get(AGE) + " / 8"), true);
//			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		super.tick(state, worldIn, pos, random);
		int meta = state.get(AGE);
		float chance = this.getChance(state, worldIn, pos);
		if(random.nextInt((int)(25.0F / chance) + 1) == 0) {
			meta++;
			if(meta > 7) {
				worldIn.setBlockState(pos, RegistryEvents.WHOLE_CHEESE.getDefaultState(), 3);
			}
			else {
				worldIn.setBlockState(pos, state.with(AGE, meta), 3);
			}
		}
	}

	private float getChance(BlockState state, World world, BlockPos pos) {
		float chance = 1.0F;
//		if(!world.canBlockSeeSky(pos)) {
//			chance += 1.0F;
//		}
//		if(world.getChunk(pos).getWorldLightManager().getLightEngine(LightType.SKY).getLightFor(pos) < 9) {
//			chance += 1.0F;
//		}
		if(world.getLightSubtracted(pos, 0) < 13) {
			chance += 1.0F;
		}
		switch(world.getBiome(pos).getCategory()) {
		case DESERT:
		case MESA:
		case SAVANNA:
			chance /= 2.0F;
			break;
		case SWAMP:
			chance += 2.0F;
			break;
		case MUSHROOM:
			chance += 4.0F;
			break;
		default:
			chance += 1.0F;
		}
		return chance;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
