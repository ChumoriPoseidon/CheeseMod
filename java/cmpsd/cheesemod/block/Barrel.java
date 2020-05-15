package cmpsd.cheesemod.block;

import java.util.Random;

import cmpsd.cheesemod.ModBlock;
import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModTab;
import cmpsd.cheesemod.Reference;
import cmpsd.cheesemod.handler.GuiHandler;
import cmpsd.cheesemod.tileentity.TileEntity_Barrel;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;

public class Barrel extends BlockContainer {

	private static boolean keepInventory;

	public Barrel() {

		super(Material.WOOD);
		this.setRegistryName("block_barrel");
		this.setUnlocalizedName("barrel");
		this.setCreativeTab(ModTab.tabCheeseMod);
		this.setSoundType(SoundType.WOOD);
		this.setHardness(2.5F);
		this.setResistance(13.0F);

		ModItem.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		ModBlock.BLOCKS.add(this);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {

		return new TileEntity_Barrel();
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity != null && tileEntity instanceof TileEntity_Barrel) {

			TileEntity_Barrel te_barrel = (TileEntity_Barrel)tileEntity;
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt != null) {

				te_barrel.setNBT(nbt);
			}
		}
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if(worldIn.isRemote) {

			return true;
		}
		else {

			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof TileEntity_Barrel) {

				TileEntity_Barrel te_barrel = (TileEntity_Barrel)tileEntity;
				FluidTank fluidTank = te_barrel.tank;
				ItemStack heldItem = playerIn.getHeldItem(hand);
				if(!heldItem.isEmpty()) {

//					if(heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
//
//						IFluidHandler fluidHandler = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
//						FluidStack fluidStack = fluidHandler.drain(te_barrel.maxCapacity, false);
						if(FluidUtil.interactWithFluidHandler(playerIn, hand, fluidTank)) {

							te_barrel.setField(2, (int)MathHelper.ceil(te_barrel.getField(2) / 2.0D));
							worldIn.notifyBlockUpdate(pos, state, state, 3);
							te_barrel.markDirty();
							return true;
						}
//					}
					if(heldItem.getItem() == ModItem.bacteria) {

						te_barrel.setField(2, te_barrel.getField(2) + 1);
						worldIn.notifyBlockUpdate(pos, state, state, 3);
						te_barrel.markDirty();
						heldItem.shrink(1);
						playerIn.inventory.placeItemBackInInventory(worldIn, new ItemStack(Items.GLASS_BOTTLE));
						return true;
					}
				}
				playerIn.openGui(Reference.MODID, GuiHandler.guiID_Barrel, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
			return true;
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return null; //Item.getItemFromBlock(this);
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {

		return new ItemStack(this);
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		if(!keepInventory) {

			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof TileEntity_Barrel) {

				TileEntity_Barrel te_barrel = (TileEntity_Barrel)tileEntity;
				InventoryHelper.dropInventoryItems(worldIn, pos, te_barrel);
//				if(te_barrel.tank.getFluidAmount() > 0) {

					NBTTagCompound nbt = te_barrel.getNBT(new NBTTagCompound());
					if(nbt != null) {

						ItemStack itemStack = new ItemStack(this);
						itemStack.setTagCompound(nbt);
						if(!worldIn.isRemote) {

							float f = 0.5F;
							double dx = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
							double dy = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
							double dz = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
							EntityItem entityItem = new EntityItem(worldIn, (double)pos.getX() + dx, (double)pos.getY() + dy, (double)pos.getZ() + dz, itemStack);
							entityItem.setDefaultPickupDelay();
							worldIn.spawnEntity(entityItem);
						}
					}
//				}
			}
		}
		super.breakBlock(worldIn, pos, state);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		float f = 1 / 16.0F;
		return new AxisAlignedBB(f, 0.0F, f, 1.0F - f, 1.0F, 1.0F - f);
	}

	public EnumBlockRenderType getRenderType(IBlockState state) {

		return EnumBlockRenderType.MODEL;
	}

	public boolean isOpaqueCube(IBlockState state) {

        return false;
    }

	public boolean isFullCube(IBlockState state) {

		return false;
	}
}
