package cmpsd.cheesemod;

import cmpsd.cheesemod.block.CheeseFondue;
import cmpsd.cheesemod.block.ModBlockFluidBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModFluid {

	public static Fluid fluidMeltedCheese;

	public static ModBlockFluidBase blockFluidMeltedCheese;

	public static void register() {

		fluidMeltedCheese = new Fluid("melted_cheese", new ResourceLocation(Reference.MODID, "blocks/liquid_cheese_still"), new ResourceLocation(Reference.MODID, "blocks/liquid_cheese_flow"))
				.setUnlocalizedName("meltedCheese").setDensity(1000).setViscosity(4000).setTemperature(350);
		registerFluid(fluidMeltedCheese);

		blockFluidMeltedCheese = new ModBlockFluidBase("liquid_melted_cheese", "liquidMeltedCheese", fluidMeltedCheese, Material.WATER) {

//			@Override
//			public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
//
//				if(world != null && pos != null && state != null) {
//
//					if(world.getBlockState(pos.down()).getBlock() == Blocks.ICE) {
//
//						return;
//					}
//				}
//				super.updateTick(world, pos, state, rand);
//			}

			@Override
			public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {

				if(this.isSourceBlock(worldIn, pos) && entityIn instanceof EntityItem) {

					ItemStack itemStack = ((EntityItem)entityIn).getItem();
					ItemStack result = CheeseFondue.getCookResult(itemStack);
					if(!result.isEmpty()) {

						result.setCount(Math.min(itemStack.getCount(), 10));
						itemStack.shrink(result.getCount());
						if(worldIn.rand.nextInt(11 - result.getCount()) == 0) {

							worldIn.setBlockToAir(pos);
						}
						EntityItem entityItem = new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, result);
						entityItem.addVelocity(0.0D, 0.1D, 0.0D);
						if(!worldIn.isRemote) {

							worldIn.spawnEntity(entityItem);
						}
						if(!itemStack.isEmpty()) {

							EntityItem remain = new EntityItem(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, itemStack);
							remain.addVelocity(0.0D, 0.1D, 0.0D);
							if(!worldIn.isRemote) {

								worldIn.spawnEntity(remain);
							}
						}
						entityIn.setDead();
						return;
					}
				}

				entityIn.motionX *= 0.5D;
				entityIn.motionY *= 0.5D;
				entityIn.motionZ *= 0.5D;
			}
		};
		blockFluidMeltedCheese.setQuantaPerBlock(4);
	}

	private static void registerFluid(Fluid fluid) {

		if(!FluidRegistry.registerFluid(fluid)) {

			fluid = FluidRegistry.getFluid(fluid.getName());
		}
		FluidRegistry.addBucketForFluid(fluid);
	}

	public static void registerModel() {

		registeBlockFluidRender(blockFluidMeltedCheese);
	}

	public static void registeBlockFluidRender(BlockFluidClassic blockFluid) {

		Item item = Item.getItemFromBlock(blockFluid);
		FluidStateMapper fluidStateMapper = new FluidStateMapper(blockFluid.getFluid());

		ModelLoader.registerItemVariants(item);
		ModelLoader.setCustomMeshDefinition(item, fluidStateMapper);
		ModelLoader.setCustomStateMapper(blockFluid, fluidStateMapper);
	}

	public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

		private final Fluid fluid;

		public FluidStateMapper(Fluid fluid) {

			this.fluid = fluid;
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {

			return new ModelResourceLocation(new ResourceLocation(Reference.MODID, "block_fluid"), this.fluid.getName());
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {

			return new ModelResourceLocation(new ResourceLocation(Reference.MODID, "block_fluid"), this.fluid.getName());
		}
	}
}
