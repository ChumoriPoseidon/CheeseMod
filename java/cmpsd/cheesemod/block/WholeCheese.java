package cmpsd.cheesemod.block;

import cmpsd.cheesemod.ModBlock;
import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WholeCheese extends Block {

	public WholeCheese() {
		super(Material.CAKE);
		this.setRegistryName("block_whole_cheese");
		this.setUnlocalizedName("wholeCheese");
		this.setCreativeTab(ModTab.tabCheeseMod);
		this.setSoundType(SoundType.CLOTH);
		this.setHardness(0.5F);
		this.setResistance(0.5F);

		ModItem.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		ModBlock.BLOCKS.add(this);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	}

	private boolean canBlockStay(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getMaterial().isSolid();
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!this.canBlockStay(worldIn, pos)) {
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		float f = 1.0F / 16.0F;
		return new AxisAlignedBB(f * 1, 0.0F, f * 1, f * 15, f * 7, f * 15);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
