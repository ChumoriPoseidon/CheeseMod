package cmpsd.cheesemod.item;

import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModTab;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class Bacteria extends Item {

//	private static final TIntObjectHashMap<Set<BlockSwapper>> blockSwapper = new TIntObjectHashMap<>();

	public Bacteria() {

		this.setRegistryName("item_bacteria");
		this.setUnlocalizedName("bacteria");
		this.setCreativeTab(ModTab.tabCheeseMod);

		this.setContainerItem(Items.GLASS_BOTTLE);

		ModItem.ITEMS.add(this);

//		MinecraftForge.EVENT_BUS.register(this);
	}

//	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//
//		IBlockState state = worldIn.getBlockState(pos);
//		if(state.getBlock() == Blocks.DIRT && state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT || state.getBlock() == Blocks.GRASS) {
//
//			if(!worldIn.isRemote) {
//
//				ItemStack itemStack = player.getHeldItem(hand);
//				BlockSwapper swapper = this.addBlockSwapper(worldIn, pos);
//				worldIn.setBlockState(pos, swapper.state, 1 | 2);
//				if(!player.capabilities.isCreativeMode) {
//
//					ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(this.getContainerItem()));
//				}
//				itemStack.shrink(1);
//			}
//			else {
//
//				for(int i = 0; i < 3; i++) {
//
//					double x = -0.25D + this.itemRand.nextDouble() * 0.5D;
//					double z = -0.25D + this.itemRand.nextDouble() * 0.5D;
//					worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, pos.getX() + 0.5D + x, pos.getY() + 1.5D, pos.getZ() + 0.5D + z, 0.55D, 0.275D, 0.588D, null);
//				}
//				player.playSound(SoundEvents.BLOCK_BREWING_STAND_BREW, 1.0F, 1.0F);
//			}
//			return EnumActionResult.SUCCESS;
//		}
//		return EnumActionResult.PASS;
//	}
//
//	private static BlockSwapper addBlockSwapper(World world, BlockPos pos) {
//
//		BlockSwapper swapper = new BlockSwapper(world, Blocks.MYCELIUM.getDefaultState(), pos);
//		int dim = world.provider.getDimension();
//		if(!blockSwapper.containsKey(dim)) {
//
//			blockSwapper.put(dim, new HashSet<>());
//		}
//		blockSwapper.get(dim).add(swapper);
//		return swapper;
//	}
//
//	@SubscribeEvent
//	public void onTickEnd(TickEvent.WorldTickEvent event) {
//
//		if(event.world.isRemote) {
//
//			return;
//		}
//		else {
//
//			if(event.phase == Phase.END) {
//
//				int dim = event.world.provider.getDimension();
//				if(this.blockSwapper.containsKey(dim)) {
//
//					Set<BlockSwapper> swapper = blockSwapper.get(dim);
//					Iterator<BlockSwapper> iteSwap = swapper.iterator();
//					while(iteSwap.hasNext()) {
//
//						BlockSwapper next = iteSwap.next();
//						if(next == null || !next.tick()) {
//
//							iteSwap.remove();
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public static class BlockSwapper {
//
//		public static final int RANGE = 3;
//		public static final int TICK_RANGE = 1;
//
//		private final World world;
//		private final Random random;
//		private final IBlockState state;
//		private final BlockPos pos;
//
//		private int tickExisted = 0;
//
//		public BlockSwapper(World worldIn, IBlockState stateIn, BlockPos posIn) {
//
//			this.world = worldIn;
//			this.random = new Random();
//			this.state = stateIn;
//			this.pos = posIn;
//		}
//
//		public boolean tick() {
//
//			if(++this.tickExisted % 20 == 0) {
//
//				for(BlockPos pos : BlockPos.getAllInBox(this.pos.add(-RANGE, 0, -RANGE), this.pos.add(RANGE, 0, RANGE))) {
//
//					if(this.world.getBlockState(pos) == this.state) {
//
//						this.tickBlock(pos);
//					}
//				}
//			}
//			return this.tickExisted < 80;
//		}
//
//		public void tickBlock(BlockPos pos) {
//
//			List<BlockPos> validCoords = new ArrayList<>();
//			for(int offsetX = -TICK_RANGE; offsetX <= TICK_RANGE; offsetX++) {
//				for(int offsetZ = -TICK_RANGE; offsetZ <= TICK_RANGE; offsetZ++) {
//
//					if(offsetX == 0 && offsetZ == 0) continue;
//					if(this.isValidSwapPosition(pos.add(offsetX, 0, offsetZ))) {
//
//						validCoords.add(pos.add(offsetX, 0, offsetZ));
//					}
//				}
//			}
//			if(!validCoords.isEmpty()) {
//
//				BlockPos toSwap = validCoords.get(this.random.nextInt(validCoords.size()));
//				this.world.setBlockState(toSwap, this.state);
//			}
//		}
//
//		public boolean isValidSwapPosition(BlockPos pos) {
//
//			IBlockState state = this.world.getBlockState(pos);
//			Block block = state.getBlock();
//			return (block == Blocks.DIRT || block == Blocks.GRASS)
//					&& (block != Blocks.DIRT || state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT)
//					&& this.world.getBlockLightOpacity(pos.up()) <= 1;
//		}
//	}
}
