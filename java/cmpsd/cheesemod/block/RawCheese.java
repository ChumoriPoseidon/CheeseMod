package cmpsd.cheesemod.block;

import java.util.Random;

import cmpsd.cheesemod.ModBlock;
import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModPlugin;
import cmpsd.cheesemod.ModTab;
import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHumidity;
import defeatedcrow.hac.api.climate.IClimate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeHell;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.biome.BiomeMushroomIsland;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.biome.BiomeSwamp;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RawCheese extends Block {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);

	public RawCheese() {
		super(Material.CAKE);
		this.setRegistryName("block_raw_cheese");
		this.setUnlocalizedName("rawCheese");
		this.setCreativeTab(ModTab.tabCheeseMod);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setSoundType(SoundType.CLOTH);
		this.setHardness(0.5F);
		this.setResistance(0.5F);
		this.setTickRandomly(true);

		ModItem.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		ModBlock.BLOCKS.add(this);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if(!worldIn.isRemote) {
//			if(playerIn.isSneaking()) {
//				int meta = ((Integer)state.getValue(AGE)).intValue();
//				float chance = this.getChance(worldIn, state, pos);
//				playerIn.sendMessage(new TextComponentString("AGE: " + meta + ", Chance: " + chance));
//				if(this.RANDOM.nextInt((int)(25.0F / chance) + 1) == 0) {
//					meta++;
//					if(meta > 7) {
//						worldIn.setBlockState(pos, ModBlock.wholeCheese.getDefaultState(), 3);
//					}
//					else {
//						worldIn.setBlockState(pos, state.withProperty(AGE, meta), 3);
//					}
//				}
//			}
//			else {
				playerIn.sendStatusMessage(new TextComponentString(new TextComponentTranslation("tile.rawCheese.getAge", new Object[0]).getFormattedText() + " " + ((Integer)state.getValue(AGE)).intValue() + " / 8"), true);
//			}
		}
		return true;
	}

	private float getChance(World worldIn, IBlockState state, BlockPos pos) {
		float chance = 1.0F;
		if(worldIn.getLightFromNeighbors(pos) < 13) {
			chance += 1.0F;
		}
		if(pos.getY() >= 90) {
			chance += 1.0F;
		}
		float chanceTemperature = getChanceTemperature(worldIn, pos);
		float chanceHumidity = getChanceHumidity(worldIn, pos);
		chance += (chanceTemperature + chanceHumidity);
		return chance;
	}

	public static float getChanceTemperature(World world, BlockPos pos) {
		float chance = 1.0F;
		if(ModPlugin.loadedHaCLib) {
			if(world.getBiome(pos) instanceof BiomeMushroomIsland) {
				chance += 2.0F;
			}
			else {
				IClimate clm = ClimateAPI.calculator.getClimate(world, pos);
				if(clm.getHeat().getTier() >= 3) {
					chance /= 2.0F;
				}
				else if(clm.getHeat().getTier() <= -1) {
					chance += 2.0F;
				}
			}
		}
		else {
			Biome biome = world.getBiome(pos);
			if(biome instanceof BiomeHell) {
				chance /= 4.0F;
			}
			else if(biome instanceof BiomeDesert) {
				if(world.isDaytime()) {
					chance /= 2.0F;
				}
				else {
					chance += 1.0F;
				}
			}
			else if(biome instanceof BiomeMesa || biome instanceof BiomeSavanna) {
				if(world.isDaytime()) {
					chance /= 2.0F;
				}
			}
			else if(biome instanceof BiomeTaiga || biome instanceof BiomeHills || biome instanceof BiomeSnow || biome instanceof BiomeEnd) {
				chance += 2.0F;
			}
			else if(biome instanceof BiomeMushroomIsland) {
				chance += 2.0F;
			}
		}
		return chance;
	}

	public static float getChanceHumidity(World world, BlockPos pos) {
		float chance = 1.0F;
		if(ModPlugin.loadedHaCLib) {
			IClimate clm = ClimateAPI.calculator.getClimate(world, pos);
			if(clm.getHumidity() == DCHumidity.DRY) {
				chance /= 2.0F;
			}
			else if(clm.getHumidity() == DCHumidity.WET || clm.getHumidity() == DCHumidity.UNDERWATER) {
				chance += 2.0F;
			}
		}
		else {
			Biome biome = world.getBiome(pos);
			if(biome instanceof BiomeHell) {
				chance /= 4.0F;
			}
			else if(biome instanceof BiomeDesert || biome instanceof BiomeMesa || biome instanceof BiomeSavanna) {
				chance /= 2.0F;
			}
			else if(biome instanceof BiomeJungle || biome instanceof BiomeSwamp) {
				chance += 2.0F;
			}
			else if(biome instanceof BiomeMushroomIsland) {
				chance += 2.0F;
			}
		}
		return chance;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
		int meta = ((Integer)state.getValue(AGE)).intValue();
		float chance = this.getChance(worldIn, state, pos);
		if(rand.nextInt((int)(25.0F / chance) + 1) == 0) {
			meta++;
			if(meta > 7) {
				worldIn.setBlockState(pos, ModBlock.wholeCheese.getDefaultState(), 3);
			}
			else {
				worldIn.setBlockState(pos, state.withProperty(AGE, meta), 3);
			}
		}
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
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this);
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

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer)state.getValue(AGE)).intValue();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {AGE});
	}
}
