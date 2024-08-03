package cn.taskeren.minequery;

import cn.taskeren.minequery.command.MineQueryCommand;
import cn.taskeren.minequery.config.CommandShortcutData;
import cn.taskeren.minequery.config.MineQueryConfig;
import cn.taskeren.minequery.features.*;
import cn.taskeren.minequery.features.command_shortcut.CommandShortcut;
import eu.midnightdust.lib.config.MidnightConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MineQuery {
	public static final String MOD_ID = "minequery";

	public static final Logger LOGGER = LoggerFactory.getLogger("MineQuery");

	public static void init() {
		LOGGER.info("MineQuery is here!");

		registerAllFeatures();

		MineQueryCommand.init();

		MidnightConfig.init(MOD_ID, MineQueryConfig.class);

		CommandShortcutData.load();
	}

	private static void registerAllFeatures() {
		HarvestX.init();
		NotAttack.init();
		NotPlace.init();
		KeyBindings.init();
		TickTaskScheduler.init();
		CommandShortcut.init();
	}
}
