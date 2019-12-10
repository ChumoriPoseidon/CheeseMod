package com.cmpsd.cheesemod.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {

	public void init();

	public World getWorld();

	public PlayerEntity getPlayer();
}
