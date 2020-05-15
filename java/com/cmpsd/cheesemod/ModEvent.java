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
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvent {

	@SubscribeEvent
	public void onEatFoodFinish(LivingEntityUseItemEvent.Finish event) {
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
			int type = getItemType(stack);
			int heal;
			switch(type) {
			case 0:
				heal = random.nextInt(2) + 2;
				player.getFoodStats().addStats(heal, 0.5F);
				return true;
			case 1:
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
			case 2:
				heal = random.nextInt(2);
				float saturation = (random.nextInt(10) + 1) * 0.25F;
				player.getFoodStats().addStats(heal, saturation);
				return true;
				default:
					return false;
			}
		}
		return false;
	}

	// 0: can be Cheesed Food, 1: already Cheesed Food, 2: normal food, -1: other
	private int getItemType(ItemStack stack) {
		ItemStack cheesedItem = CheeseFonduePot.getCookResult(stack);
		if(!cheesedItem.isEmpty()) {
			return 0;
		}
		if(stack.getItem().isFood()) {
			if(stack.getItem() instanceof CheesedFoodBase) {
				return 1;
			}
			return 2;
		}
		return -1;
	}

	@SubscribeEvent
	public void onLoadLootTable(LootTableLoadEvent event) {
		String name = event.getName().toString();
		String prefix = "minecraft:chests/";
		if(name.startsWith(prefix)) {
			String file = name.substring(prefix.length());
			if(file.startsWith("village/")) {
				event.getTable().addPool(LootPool.builder()
						.addEntry(TableLootEntry.builder(new ResourceLocation(CheeseMod.MODID, "add/bacteria_from_chest")).weight(1))
						.bonusRolls(0, 1)
						.name("cheesemod_add_chest")
						.build());
			}
			else {
				switch(file) {
				case "desert_pyramid":
				case "jungle_temple":
				case "shipwreck_treasure":
					event.getTable().addPool(LootPool.builder()
							.addEntry(TableLootEntry.builder(new ResourceLocation(CheeseMod.MODID, "add/bacteria_from_treature")).weight(1))
							.bonusRolls(1, 1)
							.name("cheesemod_add_treature")
							.build());
					break;
				default:
					break;
				}
			}
		}
	}
}
