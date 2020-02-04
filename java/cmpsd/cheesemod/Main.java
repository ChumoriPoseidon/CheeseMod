package cmpsd.cheesemod;

import cmpsd.cheesemod.handler.GuiHandler;
import cmpsd.cheesemod.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.NAME, acceptedMinecraftVersions = Reference.MC_VERSION)
public class Main {

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	static {

		FluidRegistry.enableUniversalBucket();
	}

	@Mod.EventHandler
	public void construct(FMLConstructionEvent event) {

		ModTab.init();
		MinecraftForge.EVENT_BUS.register(new ModRegister());
		MinecraftForge.EVENT_BUS.register(new ModEvent());
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {

//		ModPackets.init();
		ModPlugin.preInit();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		ModItem.registerOreDict();
		ModRegister.registerTileEntity();
		ModPlugin.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		proxy.init(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		proxy.postInit(event);
	}
}
