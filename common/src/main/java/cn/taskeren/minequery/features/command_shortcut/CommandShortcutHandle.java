package cn.taskeren.minequery.features.command_shortcut;

import cn.taskeren.minequery.config.CommandShortcutData;
import net.minecraft.client.option.KeyBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandShortcutHandle {

	/**
	 * @return the index in the command shortcuts array.
	 */
	int index();

	/**
	 * The command to be sent.
	 * <p>
	 * It must start with a slash(/), or it will be treated as a chat.
	 *
	 * @return the command string or null.
	 */
	@Nullable
	String getCommand();

	/**
	 * @return the command or empty string if the command is {@code null}.
	 * @see #getCommand()
	 */
	@NotNull
	default String getCommandOrEmpty() {
		return getCommand() == null ? "" : getCommand();
	}

	/**
	 * Set the command string.
	 *
	 * @param command the command string.
	 */
	void setCommand(@Nullable String command);

	/**
	 * Get the binding key.
	 *
	 * @return the binding key.
	 */
	@NotNull
	KeyBinding getKeyBinding();

	static CommandShortcutHandle of(final int index, final KeyBinding keyBinding) {
		return new CommandShortcutHandle() {
			@Override
			public int index() {
				return index;
			}

			@Override
			@Nullable
			public String getCommand() {
				return CommandShortcutData.getCommandText(index());
			}

			@Override
			public void setCommand(@Nullable String command) {
				CommandShortcutData.setCommandText(index(), command);
			}

			@Override
			@NotNull
			public KeyBinding getKeyBinding() {
				return keyBinding;
			}
		};
	}

}
