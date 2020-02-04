package cmpsd.cheesemod.item;

import cmpsd.cheesemod.ModItem;
import cmpsd.cheesemod.ModPlugin;
import cmpsd.cheesemod.ModTab;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ModFoodBase extends Item {

	private final int healAmount;
	private final float healHide;
	private final int useDuration;
	private boolean alwaysEdible;

	private final ItemStack ingredient;

	public ModFoodBase(String registryName, String unlocalizedName, int amount, float hide, int duration, ItemStack foodStack) {
		this.setRegistryName(registryName);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(ModTab.tabCheeseMod);
		this.healAmount = amount;
		this.healHide = hide;
		this.useDuration = duration;
		this.ingredient = foodStack;

		ModItem.ITEMS.add(this);
	}

	public ModFoodBase(String registryName, String unlocalizedName, ItemStack foodStack) {

		Item food = foodStack.getItem();
		this.setRegistryName(registryName);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(ModTab.tabCheeseMod);
		if(food instanceof ItemFood) {

			this.healAmount = ((ItemFood)food).getHealAmount(foodStack) + 2;
			this.healHide = ((ItemFood)food).getSaturationModifier(foodStack) + 0.3F;
		}
		else {

			this.healAmount = 0;
			this.healHide = 0.0F;
		}
		this.useDuration = 28;
		this.ingredient = foodStack;

		ModItem.ITEMS.add(this);
	}

	public ModFoodBase(String registryName, String unlocalizedName, int amount, float hide) {

		this.setRegistryName(registryName);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(ModTab.tabCheeseMod);
		this.healAmount = amount;
		this.healHide = hide;
		this.useDuration = 28;
		this.ingredient = ItemStack.EMPTY;

		ModItem.ITEMS.add(this);
	}

	public ModFoodBase(String registryName, String unlocalizedName, int amount, float hide, int duration) {

		this.setRegistryName(registryName);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(ModTab.tabCheeseMod);
		this.healAmount = amount;
		this.healHide = hide;
		this.useDuration = duration;
		this.ingredient = ItemStack.EMPTY;

		ModItem.ITEMS.add(this);
	}

	public String getItemStackDisplayName(ItemStack stack) {

		if(!this.ingredient.isEmpty()) {

			return I18n.format("item.cheesed", new Object[0]) + " " + this.ingredient.getDisplayName();
		}
		return super.getItemStackDisplayName(stack);
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {

		if (entityLiving instanceof EntityPlayer) {

			EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
			entityPlayer.getFoodStats().addStats(this.healAmount, this.healHide);
			worldIn.playSound((EntityPlayer)null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			this.onFoodEaten(stack, worldIn, entityPlayer);
			entityPlayer.addStat(StatList.getObjectUseStats(this));
			if(entityPlayer instanceof EntityPlayerMP) {

				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityPlayer, stack);
			}
		}
		stack.shrink(1);
		return stack;
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {

		if(!this.ingredient.isEmpty() && ModPlugin.loadedMKNUtils) {

			switch(this.ingredient.getMetadata()) {
			case 9:

				player.clearActivePotions();
				break;
			case 17:

				player.heal(2.0F);
				break;
			case 18:

				player.heal(1.0F);
				break;
			}
		}
	}

	public int getMaxItemUseDuration(ItemStack stack) {

		return this.useDuration;
	}

	public EnumAction getItemUseAction(ItemStack stack) {

		return EnumAction.EAT;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		ItemStack itemStack = playerIn.getHeldItem(handIn);
		if(playerIn.canEat(this.alwaysEdible)) {

			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
		}
		else {

			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
		}
	}

	public int getHealAmount(ItemStack stack) {

		return this.healAmount;
	}

	public int getHealAmount() {

		return this.healAmount;
	}

	public float getSaturationModifier(ItemStack stack) {

		return this.healHide;
	}

	public float getSaturationModifier() {

		return this.healHide;
	}

	public ModFoodBase setAlwaysEdible() {

		this.alwaysEdible = true;
		return this;
	}
}
