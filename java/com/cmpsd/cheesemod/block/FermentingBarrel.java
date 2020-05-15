package com.cmpsd.cheesemod.block;

import java.util.Random;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;
import com.cmpsd.cheesemod.tileentity.FermentingBarrelTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class FermentingBarrel extends ContainerBlock {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_2;

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

	public FermentingBarrel() {
		super(Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5F, 13.0F));
		this.setRegistryName("block_fermenting_barrel");
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));

		RegistryEvents.BLOCKS.add(this);
		RegistryEvents.ITEMS.add(new BlockItem(this, new Item.Properties().group(RegistryEvents.CHEESE_MOD_GROUP)).setRegistryName(this.getRegistryName()));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new FermentingBarrelTileEntity();
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof FermentingBarrelTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)tileEntity, tileEntity.getPos());
			}
			else {
				throw new IllegalStateException("Out named container provider is missing!");
			}
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof IInventory) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileEntity);
			}
		}
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		double dx = (double)pos.getX() + 0.5D;
		double dy = (double)pos.getY() + 1.0D;
		double dz = (double)pos.getZ() + 0.5D;
		switch(stateIn.get(AGE)) {
		case 0:
			break;
		case 1:
			worldIn.addParticle(ParticleTypes.SMOKE, dx + rand.nextDouble() * 0.6D - 0.3D, dy, dz + rand.nextDouble() * 0.6 - 0.3D, 0.0D, 0.0D, 0.0D);
			break;
		case 2:
			worldIn.addParticle(ParticleTypes.EFFECT, dx + rand.nextDouble() * 0.6D - 0.3D, dy, dz + rand.nextDouble() * 0.6 - 0.3D, 0.0D, 0.0D, 0.0D);
			break;
		}
		super.animateTick(stateIn, worldIn, pos, rand);
	}
}
