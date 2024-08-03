package cn.taskeren.minequery.features;

import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TickTaskScheduler {

	private static int ticks;

	private static final Map<Integer, List<Runnable>> SCHEDULED_TASKS = new HashMap<>();

	public static void init() {
		ClientTickEvent.CLIENT_PRE.register(TickTaskScheduler::preTick);
	}

	private static List<Runnable> getOrCreateTasks(int ticks) {
		if(!SCHEDULED_TASKS.containsKey(ticks)) {
			SCHEDULED_TASKS.put(ticks, new ArrayList<>());
		}
		return SCHEDULED_TASKS.get(ticks);
	}

	public static void addTask(int afterTicks, Runnable task) {
		getOrCreateTasks(ticks + afterTicks).add(task);
	}

	private static void preTick(MinecraftClient client) {
		ticks++;

		var scheduledTasks = SCHEDULED_TASKS.remove(ticks);
		if(scheduledTasks != null) {
			scheduledTasks.forEach(Runnable::run);
		}

		if(ticks % 200 == 0) { // remove the gone entries
			var iter = SCHEDULED_TASKS.entrySet().iterator();
			while(iter.hasNext()) {
				var entry = iter.next();
				var tick = entry.getKey();

				if(tick == null) continue;
				if(tick >= ticks) return;

				iter.remove();
			}
		}
	}

}
