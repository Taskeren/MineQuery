package cn.taskeren.minequery.api;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public interface ClientBlockEvent {

	Event<ClientBlockBreak> CLIENT_BLOCK_BREAK_EVENT = EventFactory.createLoop();

	interface ClientBlockBreak {
		void onBlockBroken(ClientWorld world, ClientPlayerEntity player, BlockPos pos, BlockState state);
	}

}
