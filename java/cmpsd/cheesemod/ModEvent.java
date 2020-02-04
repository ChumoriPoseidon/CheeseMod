package cmpsd.cheesemod;

import java.util.Random;

import cmpsd.cheesemod.block.CheeseFondue;
import cmpsd.cheesemod.item.ModFoodBase;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModEvent {

//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent
//	public void onEatPokectCheeseFondue(RenderSpecificHandEvent event) {
//
//		boolean flag = event.getHand() == EnumHand.MAIN_HAND;
//		ItemStack itemStack = event.getItemStack();
//		if(flag) {
//
//			Minecraft mc = Minecraft.getMinecraft();
//			AbstractClientPlayer player = mc.player;
//			if(PocketCheeseFondue.canCookPocketCheeseFondue(player)) {
//
//				EnumHandSide enumHandSide = player.getPrimaryHand();
//
//				GlStateManager.pushMatrix();
//
//				boolean flag1 = enumHandSide == EnumHandSide.RIGHT;
//				ItemRenderer itemRenderer = mc.getItemRenderer();
//				if(player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == event.getHand()) {
//
//					int i = flag1 ? 1 : -1;
//
//					if(itemStack.getItemUseAction() == EnumAction.EAT) {
//
//						this.transformEatFirstPerson(mc, event.getPartialTicks(), enumHandSide, itemStack);
//						this.transformSideFirstPerson(enumHandSide, event.getEquipProgress());
//					}
//				}
//				else {
//
//					int i = flag1 ? 1 : -1;
//					float f = -0.4F * MathHelper.sin(MathHelper.sqrt(event.getSwingProgress()) * (float)Math.PI);
//					float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(event.getSwingProgress()) * ((float)Math.PI * 2.0F));
//					float f2 = -0.2F * MathHelper.sin(event.getSwingProgress() * (float)Math.PI);
//					GlStateManager.translate((float)i * f, f1, f2);
//					this.transformSideFirstPerson(enumHandSide, event.getEquipProgress());
//					this.transformFirstPerson(enumHandSide, event.getSwingProgress());
//				}
//				itemRenderer.renderItemSide(player, itemStack, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
//
//				GlStateManager.popMatrix();
//
//				event.setCanceled(true);
//			}
//		}
//	}
//
//	private void transformEatFirstPerson(Minecraft mc, float partialTicks, EnumHandSide hand, ItemStack itemStack) {
//
//		float f = (float)mc.player.getItemInUseCount() - partialTicks + 1.0F;
//		float f1 = f / (float)itemStack.getMaxItemUseDuration();
//		if(f1 < 0.8F) {
//
//			float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);
//			GlStateManager.translate(0.0F, f2, 0.0F);
//		}
//		float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
//		int i = hand == EnumHandSide.RIGHT ? 1 : -1;
//		GlStateManager.translate(f3 * 0.6F * (float)i, f3 * -0.5F, f3 * 0.0F);
//		GlStateManager.rotate((float)i * f3 * 90.0F, 0.0F, 1.0F, 0.0F);
//		GlStateManager.rotate(f3 + 10.0F, 1.0F, 0.0F, 0.0F);
//		GlStateManager.rotate((float)i * f3 * 30.0F, 0.0F, 0.0F, 1.0F);
//	}
//
//	private void transformSideFirstPerson(EnumHandSide hand, float equipProgress) {
//
//		int i = hand == EnumHandSide.RIGHT ? 1 : -1;
//		GlStateManager.translate((float)i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
//	}
//
//	private void transformFirstPerson(EnumHandSide hand, float swingProgress) {
//
//		int i = hand == EnumHandSide.RIGHT ? 1 : -1;
//		float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
//		GlStateManager.rotate((float)i * (45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
//		float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
//		GlStateManager.rotate((float)i * f1 * -20.0F, 0.0F, 0.0F, 1.0F);
//		GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
//		GlStateManager.rotate((float)i * -45.0F, 0.0F, 1.0F, 0.0F);
//	}


//	private Map<EntityPlayer, ItemStack> itemUsing = new HashMap<EntityPlayer, ItemStack>();
//
//	@SubscribeEvent
//	public void startEatPocketCheeseFondue(LivingEntityUseItemEvent.Start event) {
//
//		System.out.println("Start Eating");
//		if(event.getEntity() instanceof EntityPlayer) {
//
//			EntityPlayer entityPlayer = (EntityPlayer)event.getEntity();
//			this.itemUsing.put(entityPlayer, event.getItem());
//			System.out.println("Input");
//		}
//	}
//
//	@SubscribeEvent
//	public void stopEatPocketCheeseFondue(LivingEntityUseItemEvent.Stop event) {
//
//		System.out.println("Stop Eating");
//		if(event.getEntity() instanceof EntityPlayer) {
//
//			EntityPlayer entityPlayer = (EntityPlayer)event.getEntity();
//			if(this.itemUsing.containsKey(entityPlayer)) {
//
//				System.out.println("Stop Item: " + this.itemUsing.remove(entityPlayer).getDisplayName());
//			}
//		}
//	}
//
//	@SubscribeEvent
//	public void finishEatPocketCheeseFondue(LivingEntityUseItemEvent.Finish event) {
//
//		System.out.println("Finish Eating");
//		if(event.getEntity() instanceof EntityPlayer) {
//
//			EntityPlayer entityPlayer = (EntityPlayer)event.getEntity();
//			if(this.itemUsing.containsKey(entityPlayer)) {
//
//				System.out.println("Finish Item: " + this.itemUsing.remove(entityPlayer).getDisplayName());
//			}
//		}
//	}

	@SubscribeEvent
	public void onLootTableLoad(LootTableLoadEvent event) {

		ResourceLocation lootTable = event.getName();
		if(lootTable.equals(LootTableList.CHESTS_VILLAGE_BLACKSMITH) || lootTable.equals(LootTableList.CHESTS_JUNGLE_TEMPLE) || lootTable.equals(LootTableList.CHESTS_DESERT_PYRAMID)) {

			final LootPool lootPool = event.getTable().getPool("main");
			if(lootPool != null) {

				lootPool.addEntry(new LootEntryItem(ModItem.bacteria, 100, 0, new LootFunction[0], new LootCondition[0], Reference.MODID + ":" + ModItem.bacteria));
			}
		}
	}

//	@SubscribeEvent
//	public void onEatStart(LivingEntityUseItemEvent.Start event) {
//		EntityLivingBase entityLiving = event.getEntityLiving();
//		if(entityLiving instanceof EntityPlayer && !((EntityPlayer)entityLiving).world.isRemote) {
//			System.out.println("LivingEntityUseItemEvent.Start");
//			System.out.println("\n" +
//					"[EntityLiving]: " + event.getEntityLiving().toString() + ",\n" +
//					"[ItemStack]: " + event.getItem().getDisplayName()
//					);
//		}
//	}

	@SubscribeEvent
	public void onEatFinish(LivingEntityUseItemEvent.Finish event) {
		EntityLivingBase entityLiving = event.getEntityLiving();
//		if(entityLiving instanceof EntityPlayer && !((EntityPlayer)entityLiving).world.isRemote) {
//			System.out.println("LivingEntityUseItemEvent.Finish");
//			System.out.println("\n" +
//					"[EntityLiving]: " + event.getEntityLiving().toString() + ",\n" +
//					"[ItemStack]: " + event.getItem().getDisplayName() + ",\n" +
//					"[Duration]: " + event.getDuration()
//					);
//
//		}
		if(entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)entityLiving;
			if(!player.world.isRemote) {
				ItemStack subHeld = player.getHeldItemOffhand();
				if(!subHeld.isEmpty() && subHeld.getItem() == ModItem.pocketCheeseFondue) {
					if(this.addFoodStats(event.getItem(), player)) {
						subHeld.shrink(1);
					}
				}
			}
		}
	}

	private boolean addFoodStats(ItemStack stack, EntityPlayer player) {
		if(!stack.isEmpty()) {
			Random random = new Random();
			ItemStack cheesedItem = CheeseFondue.getCookResult(stack);
			if(!cheesedItem.isEmpty()) {
				int heal = random.nextInt(2) + 2;
				player.getFoodStats().addStats(heal, 0.5F);
				return true;
			}
			else if(stack.getItem() instanceof ItemFood) {
				int heal = random.nextInt(2);
				float saturation = (random.nextInt(10) + 1) * 0.25F;
				player.getFoodStats().addStats(heal, saturation);
				return true;
			}
			else if(stack.getItem() instanceof ModFoodBase) {
				Advancement advancement = player.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MODID, "main/greedy"));
				if(advancement != null) {
					EntityPlayerMP playerMP = player.getServer().getPlayerList().getPlayerByUUID(player.getUniqueID());
					AdvancementProgress progress = playerMP.getAdvancements().getProgress(advancement);
					if(!progress.isDone()) {
						for(String criteria : progress.getRemaningCriteria()) {
							playerMP.getAdvancements().grantCriterion(advancement, criteria);
						}
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
