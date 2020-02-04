package cmpsd.cheesemod;

import cmpsd.cheesemod.plugin.PluginHaC;
import cmpsd.cheesemod.plugin.PluginMCE3;
import net.minecraftforge.fml.common.Loader;

public class ModPlugin {

	public static boolean loadedHaCLib = false;
	public static boolean loadedHaC = false;
	public static boolean loadedMCE3 = false;
	public static boolean loadedMKNUtils = false;

	public static void preInit() {

		if(Loader.isModLoaded("dcs_lib")) {

			loadedHaCLib = true;
		}
		if(Loader.isModLoaded("dcs_climate")) {

			loadedHaC = true;
		}
		if(Loader.isModLoaded("mceconomy3")) {

			loadedMCE3 = true;
		}
		if(Loader.isModLoaded("mknutils")) {

			loadedMKNUtils = true;
		}
	}

	public static void init() {

		if(loadedHaC) {

			try {

				PluginHaC.init();
			}
			finally {}
		}
		if(loadedMCE3) {

			try {

				PluginMCE3.init();
			}
			finally {}
		}
	}
}
