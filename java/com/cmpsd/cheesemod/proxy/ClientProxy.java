package com.cmpsd.cheesemod.proxy;

import com.cmpsd.cheesemod.CheeseMod.RegistryEvents;
import com.cmpsd.cheesemod.container.FermentingBarrelScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

	@Override
	public void init() {
		ScreenManager.registerFactory(RegistryEvents.FERMENTING_BARREL_CONTAINER, FermentingBarrelScreen::new);
	}

	@Override
	public World getWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public PlayerEntity getPlayer() {
		return Minecraft.getInstance().player;
	}
}
