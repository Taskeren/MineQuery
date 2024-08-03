package cn.taskeren.minequery.utils;

import cn.taskeren.minequery.MineQuery;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Function;

public class ScreenUtils {

	public static void setScreen(Screen screen) {
		var mc = MinecraftClient.getInstance();
		mc.setScreen(screen);
	}

	public static void setScreen(Function<Screen, Screen> screenProvider) {
		var mc = MinecraftClient.getInstance();
		var screen = screenProvider.apply(mc.currentScreen);
		mc.setScreen(screen);
	}

	public static void openConfigScreen() {
		setScreen(parent -> MidnightConfig.getScreen(parent, MineQuery.MOD_ID));
	}

}
