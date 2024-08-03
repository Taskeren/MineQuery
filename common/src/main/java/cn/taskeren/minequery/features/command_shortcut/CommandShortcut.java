package cn.taskeren.minequery.features.command_shortcut;

import cn.taskeren.minequery.config.MineQueryConfig;
import cn.taskeren.minequery.screen.CommandShortcutConfigScreen;
import cn.taskeren.minequery.utils.ScreenUtils;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CommandShortcut {

	public static final int SIZE = MineQueryConfig.commandShortcutSize;

	private static final KeyBinding KB_OPEN_EDIT_SCREEN = new KeyBinding("key.minequery.command_shortcut.open_edit_screen", InputUtil.GLFW_KEY_J, "category.minequery");

	private static final CommandShortcutHandle[] HANDLES = new CommandShortcutHandle[SIZE];

	public static void init() {
		ClientTickEvent.CLIENT_POST.register(CommandShortcut::handle);

		KeyMappingRegistry.register(KB_OPEN_EDIT_SCREEN);

		for(int i = 0; i < SIZE; i++) {
			var kb = new KeyBinding("key.minequery.command_shortcut."+i, -1, "category.minequery");
			HANDLES[i] = CommandShortcutHandle.of(i, kb);
			KeyMappingRegistry.register(kb);
		}
	}

	@Nullable
	public static CommandShortcutHandle get(int i) {
		return ArrayUtils.get(HANDLES, i, null);
	}

	private static void handle(MinecraftClient minecraftClient) {
		if(KB_OPEN_EDIT_SCREEN.wasPressed()) {
			ScreenUtils.setScreen(CommandShortcutConfigScreen::new);
		}

		for(CommandShortcutHandle handle : HANDLES) {
			var kb = handle.getKeyBinding();
			if(kb.wasPressed()) {
				var command = handle.getCommand();
				if(command != null && !command.isEmpty()) {
					var p = Objects.requireNonNull(MinecraftClient.getInstance().player);
					if(command.startsWith("/")) {
						p.networkHandler.sendChatCommand(command.substring(1));
					} else {
						p.networkHandler.sendChatMessage(command);
					}
				}
			}
		}
	}

}
