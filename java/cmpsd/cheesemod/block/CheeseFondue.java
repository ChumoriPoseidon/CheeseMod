package cmpsd.cheesemod.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import cmpsd.cheesemod.ModBlock;
import cmpsd.cheesemod.ModFluid;
import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModPlugin;
import cmpsd.cheesemod.ModTab;
import cmpsd.cheesemod.plugin.PluginHaC;
import cmpsd.cheesemod.plugin.PluginMKNUtils;
import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.IClimate;
import defeatedcrow.hac.main.MainInit;
import mknutils.MKNUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class CheeseFondue extends Block {

	public static final int MAX = 8;
	public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, MAX);
	protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 0.5D, 1.0D);

    public CheeseFondue() {

    	super(Material.IRON);
    	this.setRegistryName("block_cheese_fondue");
    	this.setUnlocalizedName("cheeseFondue");
    	this.setCreativeTab(ModTab.tabCheeseMod);
    	this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, Integer.valueOf(0)));
    	this.setSoundType(SoundType.METAL);
    	this.setHardness(2.0F);

    	ModItem.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		ModBlock.BLOCKS.add(this);
    }

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		ItemStack heldItem = playerIn.getHeldItem(hand);
		if(worldIn.isRemote) {

			return !heldItem.isEmpty();
		}
		else {

			if(!heldItem.isEmpty()) {

				int level = ((Integer) state.getValue(LEVEL)).intValue();
				if(level > 0) {

					if(level == MAX && this.unloadCheese(worldIn, pos, state, playerIn, hand, heldItem, level)) {

						return true;
					}
					if(this.cookCheeseFood(worldIn, pos, state, playerIn, heldItem, level)) {

						return true;
					}
				}
				if(level < MAX && this.meltCheese(worldIn, pos, state, playerIn, hand, heldItem, level)) {

					return true;
				}

//				if(level < META) {
//
//					if(heldItem.getItem() == ModItem.meltedCheese) {
//
//						worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(Math.min(level + 4, META))));
//						heldItem.shrink(1);
//						return true;
//					}
//					if(heldItem.getItem() == ModItem.cheesePiece) {
//
//						worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(Math.min(level + 1, META))));
//						heldItem.shrink(1);
//						return true;
//					}
//					if(heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
//
//						IFluidHandler fluidHandler = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
//						FluidStack fluidStack = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);
//						if(fluidStack != null && fluidStack.getFluid() == ModFluid.fluidMeltedCheese) {
//
//							FluidTank fluidTank = new FluidTank(Fluid.BUCKET_VOLUME);
//							boolean flag = FluidUtil.interactWithFluidHandler(playerIn, hand, fluidTank);
////							FluidActionResult fluidResult = FluidUtil.tryEmptyContainerAndStow(heldItem, fluidTank, itemHandler, Fluid.BUCKET_VOLUME, playerIn, true);
////							FluidActionResult fluidResult = FluidUtil.tryEmptyContainer(heldItem, new FluidTank(Fluid.BUCKET_VOLUME), Fluid.BUCKET_VOLUME, playerIn, true);
//							if(flag) {
//
////								playerIn.setHeldItem(hand, fluidResult.getResult());
//								worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(Math.min(level + 8, META))));
//								return true;
//							}
//						}
//					}
//				}
//				if(level > 0 && level <= META) {
//
//					if(this.canCook(worldIn, pos)) {
//
//						ItemStack resultCook = this.getCookResult(heldItem);
//						if(!resultCook.isEmpty()) {
//
//							ItemHandlerHelper.giveItemToPlayer(playerIn, resultCook);
//							worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(level - 1)), 3);
//							heldItem.shrink(1);
//							return true;
//						}
//						if(heldItem.getItem() == Items.BOWL) {
//
//							ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(ModItem.pocketCheeseFondue));
//							worldIn.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(level - 1)), 3);
//							heldItem.shrink(1);
//							return true;
//						}
//						if(level == META && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
//
////							IFluidHandler fluidHandler = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
////							FluidStack fluidStack = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);
////							if(fluidStack == null) {
//
//								FluidTank fluidTank = new FluidTank(ModFluid.fluidMeltedCheese, Fluid.BUCKET_VOLUME, Fluid.BUCKET_VOLUME);
//								boolean flag = FluidUtil.interactWithFluidHandler(playerIn, hand, fluidTank);
//								if(flag) {
//
//									worldIn.setBlockState(pos, this.getDefaultState(), 3);
//									return true;
//								}
////							}
//						}
//					}
//					else {
//
//						if(heldItem.getItem() == Items.STICK) {
//
//							ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(ModItem.cheesePiece, level));
//							worldIn.setBlockState(pos, this.getDefaultState(), 3);
//							return true;
//						}
//					}
//				}
			}
		}
		return false;
	}

	private boolean unloadCheese(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, int meta) {

		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

			FluidTank fluidTank = new FluidTank(ModFluid.fluidMeltedCheese, Fluid.BUCKET_VOLUME, Fluid.BUCKET_VOLUME);
			boolean flag = FluidUtil.interactWithFluidHandler(player, hand, fluidTank);
			if(flag) {

				world.setBlockState(pos, this.getDefaultState(), 3);
				return true;
			}
		}
		return false;
	}

	private boolean cookCheeseFood(World world, BlockPos pos, IBlockState state, EntityPlayer player, ItemStack stack, int meta) {

		if(this.canCook(world, pos)) {

			ItemStack resultCook = this.getCookResult(stack);
			if(!resultCook.isEmpty()) {

				ItemHandlerHelper.giveItemToPlayer(player, resultCook);
				world.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(meta - 1)), 3);
				stack.shrink(1);
				return true;
			}
			if(stack.getItem() == Items.BOWL) {

				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItem.pocketCheeseFondue));
				world.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(meta - 1)), 3);
				stack.shrink(1);
				return true;
			}
		}
		else {

			if(stack.getItem() == Items.STICK) {

				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItem.cheesePiece, meta));
				world.setBlockState(pos, this.getDefaultState(), 3);
				return true;
			}
		}
		return false;
	}

	private boolean meltCheese(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, int meta) {

		if(this.canCook(world, pos)) {

			if(stack.getItem() == ModItem.meltedCheese) {

				world.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(Math.min(meta + 4, MAX))));
				stack.shrink(1);
				return true;
			}
			if(stack.getItem() == ModItem.cheesePiece) {

				world.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(Math.min(meta + 1, MAX))));
				stack.shrink(1);
				return true;
			}
			if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

				IFluidHandler fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				FluidStack fluidStack = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);
				if(fluidStack != null && fluidStack.getFluid() == ModFluid.fluidMeltedCheese) {

					FluidTank fluidTank = new FluidTank(Fluid.BUCKET_VOLUME);
					boolean flag = FluidUtil.interactWithFluidHandler(player, hand, fluidTank);
					if(flag) {

						world.setBlockState(pos, state.withProperty(LEVEL, Integer.valueOf(Math.min(meta + 8, MAX))));
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean canCook(World world, BlockPos pos) {

		if(ModPlugin.loadedHaCLib) {

			IClimate clm = ClimateAPI.calculator.getClimate(world, pos);
			if(clm.getHeat().getTier() >= 3) {
				return true;
			}
		}
		Block block = world.getBlockState(pos.down()).getBlock();
		return block == Blocks.LIT_FURNACE || block == Blocks.FIRE || block == Blocks.MAGMA || block == Blocks.LAVA || block == Blocks.FLOWING_LAVA || world.getBiome(pos) == Biomes.HELL;
	}

	public static ItemStack getCookResult(ItemStack heldItem) {

		if(ModPlugin.loadedHaC) {

			if(heldItem.isItemEqual(new ItemStack(MainInit.bakedApple, 1, 1))) return new ItemStack(PluginHaC.cheesedBoiledEgg);
			if(heldItem.isItemEqual(new ItemStack(MainInit.bakedApple, 1, 3))) return new ItemStack(PluginHaC.cheesedBoiledSausage);
		}
		if(ModPlugin.loadedMKNUtils) {

			if(heldItem.isItemEqual(new ItemStack(MKNUtils.foodmisc, 1, 16))) return new ItemStack(PluginMKNUtils.cheesedBoiledCrabLeg);
			if(heldItem.isItemEqual(new ItemStack(MKNUtils.foodmisc, 1, 17))) return new ItemStack(PluginMKNUtils.cheesedChocolateBar);
			if(heldItem.isItemEqual(new ItemStack(MKNUtils.foodmisc, 1, 18))) return new ItemStack(PluginMKNUtils.cheesedChocolateCookie);
			if(heldItem.isItemEqual(new ItemStack(MKNUtils.foodmisc, 1, 9))) return new ItemStack(PluginMKNUtils.cheesedMilkCookie);
		}
		if(heldItem.getItem() == Items.APPLE) return new ItemStack(ModItem.cheesedApple);
		if(heldItem.getItem() == Items.BAKED_POTATO) return new ItemStack(ModItem.cheesedBakedPotato);
		if(heldItem.getItem() == Items.BREAD) return new ItemStack(ModItem.cheesedBread);
		if(heldItem.getItem() == Items.CARROT) return new ItemStack(ModItem.cheesedCarrot);
		if(heldItem.getItem() == Items.COOKED_BEEF) return new ItemStack(ModItem.cheesedCookedBeef);
		if(heldItem.getItem() == Items.COOKED_CHICKEN) return new ItemStack(ModItem.cheesedCookedChicken);
		if(heldItem.isItemEqual(new ItemStack(Items.COOKED_FISH, 1, 0))) return new ItemStack(ModItem.cheesedCookedCod);
		if(heldItem.getItem() == Items.COOKED_MUTTON) return new ItemStack(ModItem.cheesedCookedMutton);
		if(heldItem.getItem() == Items.COOKED_PORKCHOP) return new ItemStack(ModItem.cheesedCookedPorkchop);
		if(heldItem.getItem() == Items.COOKED_RABBIT) return new ItemStack(ModItem.cheesedCookedRabbit);
		if(heldItem.isItemEqual(new ItemStack(Items.COOKED_FISH, 1, 1))) return new ItemStack(ModItem.cheesedCookedSalmon);
		if(heldItem.getItem() == Items.COOKIE) return new ItemStack(ModItem.cheesedCookie);
		return ItemStack.EMPTY;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return Item.getItemFromBlock(this);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {

		return new ItemStack(this);
	}

	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LEGS);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
	}

	public boolean isOpaqueCube(IBlockState state) {

        return false;
    }

	public boolean isFullCube(IBlockState state) {

		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {

		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {

		if(((Integer)worldIn.getBlockState(pos).getValue(LEVEL)).intValue() > 0 && this.canCook(worldIn, pos)) {

			worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D, null);
		}
	}

	public IBlockState getStateFromMeta(int meta) {

		return this.getDefaultState().withProperty(LEVEL, Integer.valueOf(meta));
	}

	public int getMetaFromState(IBlockState state) {

		return ((Integer)state.getValue(LEVEL)).intValue();
	}

	protected BlockStateContainer createBlockState() {

		return new BlockStateContainer(this, new IProperty[] {LEVEL});
	}
}
