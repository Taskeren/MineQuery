package cn.taskeren.minequery.fabric;

import cn.taskeren.minequery.MineQuery;
import net.fabricmc.api.ClientModInitializer;

public final class MineQueryFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		MineQuery.init();
	}
}
