package com.cmpsd.cheesemod;

import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

//@EventBusSubscriber
public class CheeseModConfig {

//	public static final String CATEGORY_MAIN = "main";
//
//	public static ForgeConfigSpec COMMON_CONFIG;
//
//	public static ForgeConfigSpec.BooleanValue ENABLE_COOKING_WHOLE_CHEESE_EASILY;
//
//	static {
//		ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
//
//		COMMON_BUILDER.comment("CheeseMod's common settings.").push(CATEGORY_MAIN);
//
//		ENABLE_COOKING_WHOLE_CHEESE_EASILY = COMMON_BUILDER.comment("Enable cooking Whole Cheese easily.")
//				.define("enableCookingWholeCheeseEasily", false);
//
//		COMMON_BUILDER.pop();
//
//		COMMON_CONFIG = COMMON_BUILDER.build();
//	}

	public static class Common {
		public final BooleanValue isEnabledCookingWholeCheeseEasily;
		public Supplier<Boolean> isEnabledCookingWholeCheeseEasilyInServer;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("CheeseMod's common settings.").push("common");

			isEnabledCookingWholeCheeseEasily =
					builder.comment("Enable cooking Whole Cheese easily.\n")
					.translation("config.common.enableCookingWholeCheeseEasily")
					.define("enableCookingWholeCheeseEasily", false);

			builder.pop();
		}
	}

	static final ForgeConfigSpec common;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
		common = pair.getRight();
		COMMON = pair.getLeft();

		COMMON.isEnabledCookingWholeCheeseEasilyInServer = COMMON.isEnabledCookingWholeCheeseEasily::get;
	}

	public static class ServerConfig {
		public boolean isEnabledCookingWholeCheeseEasily;

		public ServerConfig() {
			isEnabledCookingWholeCheeseEasily = COMMON.isEnabledCookingWholeCheeseEasily.get();
		}
	}

	@SubscribeEvent
	public static void onChangeConfig(final ModConfig.Reloading event) {
		if(event.getConfig().getType() == ModConfig.Type.COMMON && ServerLifecycleHooks.getCurrentServer() != null) {
			ServerConfig config = new ServerConfig();

		}
	}

//	public static class Client {
//		Client(ForgeConfigSpec.Builder builder) {
//			builder.comment("").push("client");
//
//			builder.pop();
//		}
//	}
//
//	static final ForgeConfigSpec client;
//	public static final Client CLIENT;
//
//	static {
//		final Pair<Client, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Client::new);
//		client = pair.getRight();
//		CLIENT = pair.getLeft();
//	}
}
