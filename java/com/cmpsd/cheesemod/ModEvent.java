package com.cmpsd.cheesemod;

import java.util.Random;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;
import com.cmpsd.cheesemod.block.CheeseFonduePot;
import com.cmpsd.cheesemod.item.CheesedFoodBase;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvent {

	@SubscribeEvent
	public void onEatFood(LivingEntityUseItemEvent.Finish event) {
		LivingEntity livingEntity = event.getEntityLiving();
		if(livingEntity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)livingEntity;
			if(!player.world.isRemote) {
				ItemStack subHeld = player.getHeldItemOffhand();
				if(!subHeld.isEmpty() && subHeld.getItem() == RegistryEvents.CHEESE_IN_BOWL) {
					if(this.addFoodStats(event.getItem(), player)) {
						subHeld.shrink(1);
					}
				}
			}
		}
	}

	private boolean addFoodStats(ItemStack stack, PlayerEntity player) {
		if(!stack.isEmpty()) {
			Random random = new Random();
			ItemStack cheesedItem = CheeseFonduePot.getCookResult(stack);
			if(!cheesedItem.isEmpty()) {
				int heal = random.nextInt(2) + 2;
				player.getFoodStats().addStats(heal, 0.5F);
				return true;
			}
			else if(stack.getItem().isFood()) {
				if(stack.getItem() instanceof CheesedFoodBase) {
					Advancement advancement = player.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(CheeseMod.MODID, "main/greedy"));
					if(advancement != null) {
						ServerPlayerEntity serverPlayer = player.getServer().getPlayerList().getPlayerByUUID(player.getUniqueID());
						AdvancementProgress progress = serverPlayer.getAdvancements().getProgress(advancement);
						if(!progress.isDone()) {
							for(String criteria : progress.getRemaningCriteria()) {
								serverPlayer.getAdvancements().grantCriterion(advancement, criteria);
							}
						}
					}
					else {
					}
				}
				int heal = random.nextInt(2);
				float saturation = (random.nextInt(10) + 1) * 0.25F;
				player.getFoodStats().addStats(heal, saturation);
				return true;
			}
		}
		return false;
	}
}
