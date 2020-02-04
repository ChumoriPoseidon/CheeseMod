package cmpsd.cheesemod.item;

import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModTab;
import net.minecraft.item.Item;

public class PocketCheeseFondue extends Item {

//	private int useDuration = 48;
//	private boolean alwaysEdible;

	public PocketCheeseFondue() {

		this.setRegistryName("item_pocket_cheese_fondue");
		this.setUnlocalizedName("pocketCheeseFondue");
		this.setCreativeTab(ModTab.tabCheeseMod);

		ModItem.ITEMS.add(this);
	}

//	public EnumAction getItemUseAction(ItemStack stack) {
//
//		return EnumAction.EAT;
//	}

//	public int getMaxItemUseDuration(ItemStack stack) {
//
//		return this.useDuration;
//	}

//	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
//
//		ItemStack heldMain = playerIn.getHeldItemMainhand();
//		ItemStack heldOff = playerIn.getHeldItemOffhand();
//		if(canCookPocketCheeseFondue(playerIn)) {
//
//			if(playerIn.canEat(this.alwaysEdible)) {
//
//				playerIn.setActiveHand(handIn);
//				return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
//			}
//		}
//		return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
//	}

//	public static boolean canCookPocketCheeseFondue(EntityPlayer entityPlayer) {
//
//		ItemStack heldMain = entityPlayer.getHeldItemMainhand();
//		ItemStack heldOff = entityPlayer.getHeldItemOffhand();
//		if(!heldMain.isEmpty() && !heldOff.isEmpty()) {
//
//			return heldMain.getItem() == ModItem.pocketCheeseFondue && (!CheeseFondue.getCookResult(heldOff).isEmpty() || heldOff.getItem() instanceof ItemFood);
//		}
//		return false;
//	}

//	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
//
//		EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
//		ItemStack heldMain = entityPlayer.getHeldItemMainhand();
//		ItemStack heldOff = entityPlayer.getHeldItemOffhand();
//		ItemStack result = CheeseFondue.getCookResult(heldOff);
//		if(!result.isEmpty()) {
//
//			ModFoodBase food = (ModFoodBase)result.getItem();
//			entityPlayer.getFoodStats().addStats(food.getHealAmount(), food.getSaturationModifier());
//		}
//		else {
//
//			ItemFood food = (ItemFood)heldOff.getItem();
//			int healAmound = food.getHealAmount(null) / 2 == 0 ? food.getHealAmount(null) : food.getHealAmount(null) / 2;
//			entityPlayer.getFoodStats().addStats(healAmound, food.getSaturationModifier(null) / 2.0F);
//		}
//		entityPlayer.playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, 1.0F);
//		this.onFoodEaten(stack, worldIn, entityPlayer);
//		entityPlayer.addStat(StatList.getObjectBreakStats(this));
//		entityPlayer.addStat(StatList.getObjectBreakStats(heldOff.getItem()));
//		heldMain.shrink(1);
//		heldOff.shrink(1);
//		return stack;
//	}

//	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
//
//	}

//	public PocketCheeseFondue setAlwaysEdible() {
//
//		this.alwaysEdible = true;
//		return this;
//	}
}
