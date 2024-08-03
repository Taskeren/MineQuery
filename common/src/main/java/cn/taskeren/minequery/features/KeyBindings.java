package cn.taskeren.minequery.features;

import cn.taskeren.minequery.utils.ScreenUtils;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;

import java.util.HashSet;
import java.util.Objects;

import static cn.taskeren.minequery.config.MineQueryConfig.feedEmDiameter;

public class KeyBindings {

	public static final KeyBinding KB_OPEN_CONFIG = new KeyBinding("key.minequery.open_config", InputUtil.GLFW_KEY_G, "category.minequery");
	public static final KeyBinding KB_FEED_EM = new KeyBinding("key.minequery.feed_em", -1, "category.minequery");

	public static void init() {
		ClientTickEvent.CLIENT_POST.register(KeyBindings::handle);

		KeyMappingRegistry.register(KB_OPEN_CONFIG);
		KeyMappingRegistry.register(KB_FEED_EM);
	}

	private static void handle(MinecraftClient client) {
		if(KB_OPEN_CONFIG.wasPressed()) {
			ScreenUtils.openConfigScreen();
		}

		if(KB_FEED_EM.isPressed()) {
			procFeedEm();
		} else {
			stopFeedEm();
		}
	}

	private static final HashSet<LivingEntity> FED_ENTITIES = new HashSet<>();

	private static void procFeedEm() {
		var mc = MinecraftClient.getInstance();
		var world = Objects.requireNonNull(mc.world);
		var player = Objects.requireNonNull(mc.player);

		world.getEntitiesByClass(
				AnimalEntity.class,
				Box.of(player.getPos(), feedEmDiameter, feedEmDiameter, feedEmDiameter),
				(e) -> !e.isBaby()
		).forEach(e -> {
			if(!FED_ENTITIES.contains(e)) {
				FED_ENTITIES.add(e);
				player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(e, false, Hand.MAIN_HAND));
				e.setInvisible(true); // set to invisible
			}
		});
	}

	private static void stopFeedEm() {
		if(!FED_ENTITIES.isEmpty()) {
			FED_ENTITIES.forEach(e -> e.setInvisible(false));
			FED_ENTITIES.clear();
		}
	}

}
