package com.cmpsd.cheesemod.block;

import java.util.Random;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;

public class CheeseFonduePot extends Block {

	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 8);
	protected static final VoxelShape SHAPE = VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), makeCuboidShape(4.0D, 3.0D, 4.0D, 12.0D, 12.0D, 12.0D), makeCuboidShape(6.0D, 3.0D, 6.0D, 10.0D, 16.0D, 10.0D));

	public CheeseFonduePot() {
		super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0F).notSolid());
		this.setRegistryName("block_cheese_fondue_pot");
		this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, Integer.valueOf(0)));

		RegistryEvents.BLOCKS.add(this);
		RegistryEvents.ITEMS.add(new BlockItem(this, new Item.Properties().group(RegistryEvents.CHEESE_MOD_GROUP)).setRegistryName(this.getRegistryName()));
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItem(handIn);
		if(worldIn.isRemote) {
			if(!stack.isEmpty()) {
				return ActionResultType.CONSUME;
			}
			return ActionResultType.PASS;
		}
		else {
			if(!stack.isEmpty()) {
				int meta = state.get(LEVEL);
				int max = 8;
				if(meta > 0) {
					if(meta == max && this.unloadCheese(state, worldIn, pos, player, handIn, stack, meta)) {
						return ActionResultType.SUCCESS;
					}
					if(this.cookCheesedFood(state, worldIn, pos, player, stack, meta, max)) {
						return ActionResultType.SUCCESS;
					}
				}
				if(meta < max) {
					if(this.meltCheese(state, worldIn, pos, player, handIn, stack, meta, max)) {
						return ActionResultType.SUCCESS;
					}
				}
			}
		}
		return ActionResultType.PASS;
	}

	private boolean unloadCheese(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, int meta) {
		if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
			FluidStack fluidStack = handler.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.SIMULATE);
			if(fluidStack.isEmpty()) {
				FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME);
				tank.setFluid(new FluidStack(RegistryEvents.LIQUID_CHEESE_FLUID, FluidAttributes.BUCKET_VOLUME));
				return FluidUtil.interactWithFluidHandler(player, hand, tank);
			}
			return false;
		}).orElse(false)) {
			world.setBlockState(pos, this.getDefaultState(), 3);
			return true;
		}
		return false;
	}

	private boolean cookCheesedFood(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, int meta, int max) {
		if(this.canCook(world, pos)) {
			ItemStack result = getCookResult(stack);
			if(!result.isEmpty()) {
				stack.shrink(1);
				ItemHandlerHelper.giveItemToPlayer(player, result);
				world.setBlockState(pos, state.with(LEVEL, Integer.valueOf(meta - 1)), 3);
				return true;
			}
			if(stack.getItem() == Items.BOWL) {
				stack.shrink(1);
				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(RegistryEvents.CHEESE_IN_BOWL));
				world.setBlockState(pos, state.with(LEVEL, Integer.valueOf(meta - 1)), 3);
				return true;
			}
		}
		else {
			if(stack.getItem() == Items.STICK) {
				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(RegistryEvents.CHEESE_PIECE, meta));
				world.setBlockState(pos, this.getDefaultState(), 3);
				return true;
			}
			player.sendStatusMessage(new TranslationTextComponent("block.cheesemod.block_cheese_fondue_pot.cant_cook"), true);
		}
		return false;
	}

	private boolean meltCheese(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, int meta, int max) {
		if(this.canCook(world, pos)) {
			Item item = stack.getItem();
			if(item == RegistryEvents.MELTED_CHEESE) {
				world.setBlockState(pos, state.with(LEVEL, Integer.valueOf(Math.min(meta + 4, max))), 3);
				stack.shrink(1);
				return true;
			}
			if(item == RegistryEvents.CHEESE_PIECE) {
				world.setBlockState(pos, state.with(LEVEL, Integer.valueOf(Math.min(meta + 1, max))), 3);
				stack.shrink(1);
				return true;
			}
			if(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).map(handler -> {
				FluidStack fluidStack = handler.drain(FluidAttributes.BUCKET_VOLUME, FluidAction.SIMULATE);
				if(!fluidStack.isEmpty() && fluidStack.getFluid() == RegistryEvents.LIQUID_CHEESE_FLUID) {
					return FluidUtil.interactWithFluidHandler(player, hand, new FluidTank(FluidAttributes.BUCKET_VOLUME));
				}
				return false;
			}).orElse(false)) {
				world.setBlockState(pos, state.with(LEVEL, Integer.valueOf(max)), 3);
				return true;
			}
		}
		else {
			player.sendStatusMessage(new TranslationTextComponent("block.cheesemod.block_cheese_fondue_pot.cant_cook"), true);
		}
		return false;
	}

	private boolean canCook(World world, BlockPos pos) {
		Block block = world.getBlockState(pos.down()).getBlock();
		if(block == Blocks.FURNACE || block == Blocks.BLAST_FURNACE || block == Blocks.SMOKER || block == Blocks.CAMPFIRE) {
			return world.getBlockState(pos.down()).get(BlockStateProperties.LIT);
		}
		if(block == Blocks.FIRE || block == Blocks.MAGMA_BLOCK || block == Blocks.LAVA) {
			return true;
		}
		return world.getBiome(pos) == Biomes.NETHER;
	}

	public static ItemStack getCookResult(ItemStack stack) {
		if(stack.isItemEqual(new ItemStack(Items.APPLE))) return new ItemStack(RegistryEvents.CHEESED_APPLE);
		if(stack.isItemEqual(new ItemStack(Items.BAKED_POTATO))) return new ItemStack(RegistryEvents.CHEESED_BAKED_POTATO);
		if(stack.isItemEqual(new ItemStack(Items.BREAD))) return new ItemStack(RegistryEvents.CHEESED_BREAD);
		if(stack.isItemEqual(new ItemStack(Items.CARROT))) return new ItemStack(RegistryEvents.CHEESED_CARROT);
		if(stack.isItemEqual(new ItemStack(Items.COOKED_BEEF))) return new ItemStack(RegistryEvents.CHEESED_COOKED_BEEF);
		if(stack.isItemEqual(new ItemStack(Items.COOKED_CHICKEN))) return new ItemStack(RegistryEvents.CHEESED_COOKED_CHICKEN);
		if(stack.isItemEqual(new ItemStack(Items.COOKED_COD))) return new ItemStack(RegistryEvents.CHEESED_COOKED_COD);
		if(stack.isItemEqual(new ItemStack(Items.COOKED_MUTTON))) return new ItemStack(RegistryEvents.CHEESED_COOKED_MUTTON);
		if(stack.isItemEqual(new ItemStack(Items.COOKED_PORKCHOP))) return new ItemStack(RegistryEvents.CHEESED_COOKED_PORKCHOP);
		if(stack.isItemEqual(new ItemStack(Items.COOKED_RABBIT))) return new ItemStack(RegistryEvents.CHEESED_COOKED_RABBIT);
		if(stack.isItemEqual(new ItemStack(Items.COOKED_SALMON))) return new ItemStack(RegistryEvents.CHEESED_COOKED_SALMON);
		if(stack.isItemEqual(new ItemStack(Items.COOKIE))) return new ItemStack(RegistryEvents.CHEESED_COOKIE);
		return ItemStack.EMPTY;
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(LEVEL);
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
		if(stateIn.get(LEVEL) > 0 && this.canCook(worldIn, pos)) {
			worldIn.addParticle(ParticleTypes.POOF, dx + rand.nextDouble() * 0.6D - 0.3D, dy, dz + rand.nextDouble() * 0.6 - 0.3D, 0.0D, 0.0D, 0.0D);
		}
		super.animateTick(stateIn, worldIn, pos, rand);
	}
}
