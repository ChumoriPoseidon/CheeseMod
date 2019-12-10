package com.cmpsd.cheesemod.item;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Bacteria extends Item {

	public Bacteria() {
		super(new Item.Properties().group(RegistryEvents.CHEESE_MOD_GROUP).containerItem(Items.GLASS_BOTTLE));
		this.setRegistryName("item_bacteria");

		RegistryEvents.ITEMS.add(this);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState state = world.getBlockState(pos);
		if(state != null && state.getBlock() == Blocks.DIRT) {
			if(!world.isRemote) {
				WorldRenderer worldRenderer = Minecraft.getInstance().worldRenderer;
				for(int x = -2; x <= 2; x++) {
					for(int z = -2; z <= 2; z++) {
						BlockPos target = pos.add(x, 0, z);
						if(target.equals(pos) || world.getBlockState(target).getBlock() != Blocks.DIRT) {
							continue;
						}
						if(random.nextFloat() < (1.0F - 0.3F * Math.max(x, z))) {
							world.setBlockState(target, Blocks.MYCELIUM.getDefaultState());
							worldRenderer.addParticle(ParticleTypes.SPLASH, true, target.getX() + 0.5D, target.getY() + 1.0D, target.getZ() + 0.5D, 0.0D, 0.1D, 0.0D);
						}
					}
				}
				world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState());
				context.getItem().shrink(1);
			}
			for(int i = 0; i < 5; i++) {
				world.addParticle(ParticleTypes.SPLASH, true, pos.getX() + 0.5D + (random.nextDouble() - 0.5D), pos.getY() + 1.0D, pos.getZ() + 0.5D + (random.nextDouble() - 0.5D), 0.0D, 0.0D, 0.0D);
			}
			context.getPlayer().playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 5.0F);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
