package cmpsd.cheesemod.block;

import java.util.Random;

import cmpsd.cheesemod.ModBlock;
import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModPlugin;
import cmpsd.cheesemod.ModTab;
import mknutils.MKNUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CheeseCake extends Block {

	public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 7);

	public CheeseCake() {

		super(Material.CAKE);
		this.setRegistryName("block_cheese_cake");
		this.setUnlocalizedName("cheeseCake");
		this.setCreativeTab(ModTab.tabCheeseMod);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
		this.setSoundType(SoundType.CLOTH);
		this.setHardness(0.5F);

		ModItem.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		ModBlock.BLOCKS.add(this);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if(!worldIn.isRemote) {

			return this.eatCake(worldIn, pos, state, playerIn);
		}
		else {

			ItemStack itemStack = playerIn.getHeldItem(hand);
			return this.eatCake(worldIn, pos, state, playerIn) || itemStack.isEmpty();
		}
	}

	private boolean eatCake(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

		if(!player.canEat(false)) {

			return false;
		}
		else {

			player.addStat(StatList.CAKE_SLICES_EATEN);
			player.getFoodStats().addStats(3, 0.3F);
			int meta = ((Integer)state.getValue(BITES)).intValue();
			if(meta < 7) {

				world.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(meta + 1)), 3);
			}
			else {

				world.setBlockToAir(pos);
			}
			return true;
		}
	}

	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {

		if(worldIn.isRemote) {

			return;
		}
		ItemStack heldItem = playerIn.getHeldItemMainhand();
		if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemSword) {

			IBlockState state = worldIn.getBlockState(pos);
			int meta = ((Integer)state.getValue(BITES)).intValue();
			int numPiece = this.getPiece(heldItem.getItem(), 8 - meta);
			EntityItem dropPiece = new EntityItem(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(ModItem.cheeseCakePiece, numPiece));
			worldIn.spawnEntity(dropPiece);
			heldItem.damageItem(1, playerIn);
			worldIn.setBlockToAir(pos);
		}
	}

	private int getPiece(Item heldItem, int remain) {

		Random random = new Random();
		if(ModPlugin.loadedMKNUtils && heldItem == MKNUtils.kitchenKnife) {

			return remain;
		}
		ItemSword sword = (ItemSword)heldItem;
		switch(sword.getToolMaterialName()) {
		case "WOOD":
		case "STONE":

			return remain - (int)(remain * (random.nextFloat() * 0.8F));
		case "IRON":

			return remain - (int)(remain * (random.nextFloat() * 0.7F));
		case "DIAMOND":

			return remain - (int)(remain * (random.nextFloat() * 0.6F));
		case "GOLD":

			return remain;
		default:

			return 0;
		}
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {

		return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;

	}

	private boolean canBlockStay(World world, BlockPos pos) {

		return world.getBlockState(pos.down()).getMaterial().isSolid();
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {

		if (!this.canBlockStay(worldIn, pos)) {

			worldIn.setBlockToAir(pos);
		}
	}

	public int quantityDropped(Random random) {

		return 0;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return Items.AIR;
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {

		return new ItemStack(ModBlock.cheeseCake);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		float f = 1.0F / 16.0F;
		return new AxisAlignedBB(f * 1, 0.0F, f * 1, f * 15, f * 8, f * 15);
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

	public IBlockState getStateFromMeta(int meta) {

		return this.getDefaultState().withProperty(BITES, Integer.valueOf(meta));
	}

	public int getMetaFromState(IBlockState state) {

		return ((Integer)state.getValue(BITES)).intValue();
	}

	protected BlockStateContainer createBlockState() {

		return new BlockStateContainer(this, new IProperty[] {BITES});
	}
}
