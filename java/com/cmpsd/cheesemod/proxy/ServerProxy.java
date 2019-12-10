package com.cmpsd.cheesemod.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {

	@Override
	public void init() {

	}

	@Override
	public World getWorld() {
		throw new IllegalStateException("Only client side.");
	}

	@Override
	public PlayerEntity getPlayer() {
		throw new IllegalStateException("Only client side.");
	}
}
