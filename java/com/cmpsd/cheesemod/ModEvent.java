package com.cmpsd.cheesemod;

import java.util.Random;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;
import com.cmpsd.cheesemod.block.CheeseFonduePot;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
				player.getFoodStats().addStats(random.nextInt(1) + 2, 0.5F);
				return true;
			}
			else if(stack.getItem().isFood()) {
				int heal = random.nextInt(1);
				float saturation = random.nextFloat() * 0.25F;
				player.getFoodStats().addStats(heal, saturation);
				return true;
			}
		}
		return false;
	}
}
