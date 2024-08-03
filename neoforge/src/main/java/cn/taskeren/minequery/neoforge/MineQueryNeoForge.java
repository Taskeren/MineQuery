package cn.taskeren.minequery.neoforge;

import cn.taskeren.minequery.MineQuery;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = MineQuery.MOD_ID, dist = {Dist.CLIENT})
public final class MineQueryNeoForge {
	public MineQueryNeoForge() {
		MineQuery.init();
	}
}
