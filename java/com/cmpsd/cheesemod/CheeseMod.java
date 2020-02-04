package com.cmpsd.cheesemod;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cmpsd.cheesemod.block.CheeseCake;
import com.cmpsd.cheesemod.block.CheeseFonduePot;
import com.cmpsd.cheesemod.block.FermentedBarrel;
import com.cmpsd.cheesemod.block.RawCheese;
import com.cmpsd.cheesemod.block.WholeCheese;
import com.cmpsd.cheesemod.container.FermentedBarrelContainer;
import com.cmpsd.cheesemod.item.Bacteria;
import com.cmpsd.cheesemod.item.CheesedFoodBase;
import com.cmpsd.cheesemod.proxy.ClientProxy;
import com.cmpsd.cheesemod.proxy.IProxy;
import com.cmpsd.cheesemod.proxy.ServerProxy;
import com.cmpsd.cheesemod.tileentity.FermentedBarrelTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("cheesemod")
public class CheeseMod {

	public static final String MODID = "cheesemod";

	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	// Directly reference a log4j logger.
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public CheeseMod() {
		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the enqueueIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new ModEvent());
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		proxy.init();
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
		// do something when the server starts
	}

	// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
	// Event bus for receiving Registry Events)
	@SuppressWarnings("unchecked")
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		public static List<Item> ITEMS = new ArrayList<>();
		public static List<Block> BLOCKS = new ArrayList<>();

		// Item
		public static Item CHEESE;
		public static Item CHEESE_PIECE;
		public static Item MELTED_CHEESE;
		public static Item CHEESED_APPLE;
		public static Item CHEESED_BAKED_POTATO;
		public static Item CHEESED_BREAD;
		public static Item CHEESED_CARROT;
		public static Item CHEESED_COOKED_BEEF;
		public static Item CHEESED_COOKED_CHICKEN;
		public static Item CHEESED_COOKED_COD;
		public static Item CHEESED_COOKED_MUTTON;
		public static Item CHEESED_COOKED_PORKCHOP;
		public static Item CHEESED_COOKED_RABBIT;
		public static Item CHEESED_COOKED_SALMON;
		public static Item CHEESED_COOKIE;
		public static Item CHEESE_CAKE_PIECE;
		public static Item CHEESE_BURGER;
		public static Item CHEESE_BAR;

		public static Item BACTERIA;
		public static Item CHEESE_IN_BOWL;


		// Block
		public static Block RAW_CHEESE;
		public static Block WHOLE_CHEESE;
		public static Block CHEESE_CAKE;
		public static Block CHEESE_FONDUE_POT;
		public static Block FERMENTED_BARREL;


		// Container
		public static ContainerType<FermentedBarrelContainer> FERMENTED_BARREL_CONTAINER;


		// TileEntity
		public static TileEntityType<FermentedBarrelTileEntity> FERMENTED_BARREL_TILEENTITY;


		// Fluid
		public static Item LIQUID_CHEESE_BUCKET;
		public static FlowingFluidBlock LIQUID_CHEESE_FLUID_BLOCK;
		public static ForgeFlowingFluid.Properties LIQUID_CHEESE_PROPERTIES;
		public static FlowingFluid LIQUID_CHEESE_FLUID;
		public static FlowingFluid LIQUID_CHEESE_FLUID_FLOWING;


		// Recipe(Original)


		// ItemGroup
		public static ItemGroup CHEESE_MOD_GROUP = new ItemGroup("cheesemod") {

			@Override
			public ItemStack createIcon() {
				return new ItemStack(RegistryEvents.CHEESE);
			}

        };

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			// register a new item here
			registerItem();
			event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
		}

		public static void registerItem() {
			CHEESE = new Item(new Item.Properties().group(CHEESE_MOD_GROUP).food(new Food.Builder().hunger(1).saturation(0.2F).build())).setRegistryName("item_cheese");
			CHEESE_PIECE = new Item(new Item.Properties().group(CHEESE_MOD_GROUP).food(new Food.Builder().hunger(0).saturation(0.1F).setAlwaysEdible().fastToEat().build())).setRegistryName("item_cheese_piece");
			MELTED_CHEESE = new Item(new Item.Properties().group(CHEESE_MOD_GROUP).food(new Food.Builder().hunger(2).saturation(0.4F).build())).setRegistryName("item_melted_cheese");
			ITEMS.add(CHEESE);
			ITEMS.add(CHEESE_PIECE);
			ITEMS.add(MELTED_CHEESE);

			CHEESED_APPLE = new CheesedFoodBase(new ItemStack(Items.APPLE));
			CHEESED_BAKED_POTATO = new CheesedFoodBase(new ItemStack(Items.BAKED_POTATO));
			CHEESED_BREAD = new CheesedFoodBase(new ItemStack(Items.BREAD));
			CHEESED_CARROT = new CheesedFoodBase(new ItemStack(Items.CARROT));
			CHEESED_COOKED_BEEF = new CheesedFoodBase(new ItemStack(Items.COOKED_BEEF));
			CHEESED_COOKED_CHICKEN = new CheesedFoodBase(new ItemStack(Items.COOKED_CHICKEN));
			CHEESED_COOKED_COD = new CheesedFoodBase(new ItemStack(Items.COOKED_COD));
			CHEESED_COOKED_MUTTON = new CheesedFoodBase(new ItemStack(Items.COOKED_MUTTON));
			CHEESED_COOKED_PORKCHOP = new CheesedFoodBase(new ItemStack(Items.COOKED_PORKCHOP));
			CHEESED_COOKED_RABBIT = new CheesedFoodBase(new ItemStack(Items.COOKED_RABBIT));
			CHEESED_COOKED_SALMON = new CheesedFoodBase(new ItemStack(Items.COOKED_SALMON));
			CHEESED_COOKIE = new CheesedFoodBase(new ItemStack(Items.COOKIE));

			CHEESE_CAKE_PIECE = new Item(new Item.Properties().group(CHEESE_MOD_GROUP).food(new Food.Builder().hunger(3).saturation(0.3F).build())).setRegistryName("item_cheese_cake_piece");
			CHEESE_BURGER = new Item(new Item.Properties().group(CHEESE_MOD_GROUP).food(new Food.Builder().hunger(10).saturation(0.7F).build())).setRegistryName("item_cheese_burger");
			CHEESE_BAR = new Item(new Item.Properties().group(CHEESE_MOD_GROUP).food(new Food.Builder().hunger(3).saturation(0.8F).fastToEat().build())).setRegistryName("item_cheese_bar");
			ITEMS.add(CHEESE_CAKE_PIECE);
			ITEMS.add(CHEESE_BURGER);
			ITEMS.add(CHEESE_BAR);

			CHEESE_IN_BOWL = new Item(new Item.Properties().group(CHEESE_MOD_GROUP)).setRegistryName("item_cheese_in_bowl");
			ITEMS.add(CHEESE_IN_BOWL);
			BACTERIA = new Bacteria();

			LIQUID_CHEESE_BUCKET = new BucketItem(() -> LIQUID_CHEESE_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(RegistryEvents.CHEESE_MOD_GROUP)).setRegistryName("item_liquid_cheese_bucket");
			ITEMS.add(LIQUID_CHEESE_BUCKET);
		}

		@SubscribeEvent
		public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
			// register a new block here
			registerBlock();
			event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
		}

		public static void registerBlock() {
			RAW_CHEESE = new RawCheese();
			WHOLE_CHEESE = new WholeCheese();
			CHEESE_CAKE = new CheeseCake();
			CHEESE_FONDUE_POT = new CheeseFonduePot();
			FERMENTED_BARREL = new FermentedBarrel();

			LIQUID_CHEESE_FLUID_BLOCK = (FlowingFluidBlock) new FlowingFluidBlock(() -> LIQUID_CHEESE_FLUID, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()) {

				@Override
				public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
					if(!worldIn.isRemote) {
						if(worldIn.getFluidState(pos).isSource() && entityIn instanceof ItemEntity) {
							ItemStack stack = ((ItemEntity)entityIn).getItem();
							ItemStack result = CheeseFonduePot.getCookResult(stack);
							if(!result.isEmpty()) {
								result.setCount(Math.min(stack.getCount(), 10));
								stack.shrink(result.getCount());
								if(this.RANDOM.nextInt(Math.max(11 - result.getCount(), 0) ) == 0) {
								worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
								}
								ItemEntity itemEntity = new ItemEntity(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, result);
								itemEntity.addVelocity(0.0D, 0.1D, 0.0D);
								worldIn.addEntity(itemEntity);
								if(!stack.isEmpty()) {
									ItemEntity remain = new ItemEntity(worldIn, entityIn.posX, entityIn.posY, entityIn.posZ, stack);
									itemEntity.addVelocity(0.0D, 0.1D, 0.0D);
									worldIn.addEntity(remain);
								}
								entityIn.remove();
							}
						}
					}
					entityIn.setMotion(entityIn.getMotion().mul(0.5D, 0.5D, 0.5D));
				}

			}.setRegistryName("block_liquid_cheese");
			BLOCKS.add(LIQUID_CHEESE_FLUID_BLOCK);
		}

		@SubscribeEvent
		public static void onContainersRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
			// register a new container type here
			FERMENTED_BARREL_CONTAINER = (ContainerType<FermentedBarrelContainer>)IForgeContainerType.create((windowId, inventory, extraData) -> {
				return new FermentedBarrelContainer(windowId, inventory);
			}).setRegistryName("containertype_fermented_barrel");

			event.getRegistry().registerAll(
					FERMENTED_BARREL_CONTAINER
					);
		}

		@SubscribeEvent
		public static void onTileEntitysRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
			// register a new tileentity type here
			FERMENTED_BARREL_TILEENTITY = (TileEntityType<FermentedBarrelTileEntity>)TileEntityType.Builder.create(FermentedBarrelTileEntity::new, FERMENTED_BARREL).build(null).setRegistryName("tileentitytype_fermented_barrel");

			event.getRegistry().registerAll(
					FERMENTED_BARREL_TILEENTITY
					);
		}

		@SubscribeEvent
		public static void onFluidRegistry(final RegistryEvent.Register<Fluid> event) {
			registerFluid();
			event.getRegistry().registerAll(
					LIQUID_CHEESE_FLUID,
					LIQUID_CHEESE_FLUID_FLOWING
					);
		}

		private static void registerFluid() {
			LIQUID_CHEESE_PROPERTIES = new ForgeFlowingFluid.Properties(
					() -> LIQUID_CHEESE_FLUID,
					() -> LIQUID_CHEESE_FLUID_FLOWING,
					FluidAttributes.builder(
							new ResourceLocation(MODID, "block/liquid_cheese_still"),
							new ResourceLocation(MODID, "block/liquid_cheese_flow"))
					.viscosity(10000))
					.block(() -> LIQUID_CHEESE_FLUID_BLOCK)
					.bucket(() -> LIQUID_CHEESE_BUCKET);
			LIQUID_CHEESE_FLUID = (FlowingFluid) new ForgeFlowingFluid.Source(LIQUID_CHEESE_PROPERTIES) {

				@Override
				protected int getSlopeFindDistance(IWorldReader worldIn) {
					return 2;
				}

				@Override
				protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
					return 2;
				}

				@Override
				public int getTickRate(IWorldReader world) {
					return 20;
				}

			}.setRegistryName("fluid_liquid_cheese_still");
			LIQUID_CHEESE_FLUID_FLOWING = (FlowingFluid) new ForgeFlowingFluid.Flowing(LIQUID_CHEESE_PROPERTIES) {

				@Override
				protected int getSlopeFindDistance(IWorldReader worldIn) {
					return 2;
				}

				@Override
				protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
					return 2;
				}

				@Override
				public int getTickRate(IWorldReader world) {
					return 20;
				}

			}.setRegistryName("fluid_liquid_cheese_flow");
		}

		@SubscribeEvent
		public static void onRecipesRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
			// register a new recipe serializer here

		}
	}
}
