package cn.taskeren.minequery.screen;

import cn.taskeren.minequery.MineQuery;
import cn.taskeren.minequery.features.command_shortcut.CommandShortcut;
import cn.taskeren.minequery.features.command_shortcut.CommandShortcutHandle;
import cn.taskeren.minequery.utils.ScreenUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO: improve the appearance
// TODO: use `ChatInputSuggestor`
public class CommandShortcutConfigScreen extends Screen {

	private static final int LIST_ITEM_HEIGHT = 25;

	private static final MinecraftClient MINECRAFT_CLIENT = MinecraftClient.getInstance();
	private static final GameOptions GAME_OPTIONS = MINECRAFT_CLIENT.options;

	private final Screen parent;

	private CommandShortcutListWidget list;

	@Nullable
	private KeyBinding selectedKeyBinding;

	public CommandShortcutConfigScreen(Screen parent) {
		super(Text.of("123"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		this.list = this.addSelectableChild(new CommandShortcutListWidget(this.client, this.width, this.height - 57, 24, LIST_ITEM_HEIGHT));

		for(int i = 0; i < CommandShortcut.SIZE; i++) {
			this.list.addEntry(i);
		}
	}

	@Override
	public void close() {
		ScreenUtils.setScreen(parent);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.list.render(context, mouseX, mouseY, delta);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(this.selectedKeyBinding != null) {
			if(keyCode == InputUtil.GLFW_KEY_ESCAPE) {
				GAME_OPTIONS.setKeyCode(this.selectedKeyBinding, InputUtil.UNKNOWN_KEY);
			} else {
				GAME_OPTIONS.setKeyCode(this.selectedKeyBinding, InputUtil.fromKeyCode(keyCode, scanCode));
			}

			this.selectedKeyBinding = null;
			this.list.update();
			return true;
		} else {
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
	}

	private class CommandShortcutListWidget extends ElementListWidget<CommandShortcutListWidget.CommandShortcutEntry> {

		private final List<CommandShortcutEntry> entries = new ArrayList<>();

		public CommandShortcutListWidget(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight) {
			super(minecraftClient, width, height, y, itemHeight);
		}

		@Override
		protected int getScrollbarX() {
			return this.width - 7;
		}

		public void addEntry(int index) {
			var entry = new CommandShortcutEntry(index);
			this.entries.add(entry);
			this.addEntry(entry);
		}

		public void update() {
			KeyBinding.updateKeysByCode();
			updateEntries();
		}

		public void updateEntries() {
			this.entries.forEach(CommandShortcutEntry::update);
		}

		@Override
		public int getRowWidth() {
			return this.width / 2;
		}

		@Override
		protected void renderEntry(DrawContext context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
			super.renderEntry(context, mouseX, mouseY, delta, index, x, y, entryWidth, entryHeight);
		}

		private class CommandShortcutEntry extends ElementListWidget.Entry<CommandShortcutEntry> {

			private static final int LIST_ITEM_GAP = 4;
			private static final int LIST_BUTTON_WIDTH = 75;

			private final KeyBinding keyBinding;

			private final CommandShortcutHandle handle;

			private final TextFieldWidget textField;
			private final ClickableWidget saveButton;
			private final ClickableWidget keyButton;

			// stores the cache of the command, used to check if the "new value" is really new value.
			private String text;

			public CommandShortcutEntry(int index) {
				this(Objects.requireNonNull(CommandShortcut.get(index), "the required index " + index + " is null!"));
			}

			public CommandShortcutEntry(CommandShortcutHandle handle) {
				this.handle = handle;
				this.keyBinding = handle.getKeyBinding();

				this.saveButton = ButtonWidget.builder(Text.translatable("minequery.gui.command_shortcut.set"), this::onSaveButton).width(LIST_BUTTON_WIDTH).build();
				this.saveButton.active = false;

				this.keyButton = ButtonWidget.builder(keyBinding.getBoundKeyLocalizedText(), this::onKeyBindingButton).width(LIST_BUTTON_WIDTH).build();

				this.textField = new TextFieldWidget(textRenderer, 0, 0, 150, 20, Text.of(""));
				this.textField.setMaxLength(32500);
				this.textField.setChangedListener(this::onTextChange);

				text = handle.getCommandOrEmpty();
				this.textField.setText(text);
			}

			private void onTextChange(String s) {
				if(!this.saveButton.active && !text.contentEquals(s)) {
					// set active when the text is changed
					this.saveButton.active = true;
				}
			}

			private void onSaveButton(ButtonWidget buttonWidget) {
				text = textField.getText();
				handle.setCommand(text);
				MineQuery.LOGGER.info("Saving new command text {} to {}", text, handle.index());
				// set inactive to prevent multiple clicks
				this.saveButton.active = false;
			}

			private void onKeyBindingButton(ButtonWidget buttonWidget) {
				selectedKeyBinding = keyBinding;
				CommandShortcutListWidget.this.update();
			}

			@Override
			public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
				var consumableX = x;

				this.textField.setX(consumableX);
				this.textField.setY(y);
				this.textField.setWidth(entryWidth / 3 * 2);
				this.textField.render(context, mouseX, mouseY, tickDelta);
				consumableX += entryWidth / 3 * 2 + LIST_ITEM_GAP;

				this.saveButton.setX(consumableX);
				this.saveButton.setY(y);
				this.saveButton.render(context, mouseX, mouseY, tickDelta);
				consumableX += LIST_BUTTON_WIDTH + LIST_ITEM_GAP;

				this.keyButton.setX(consumableX);
				this.keyButton.setY(y);
				this.keyButton.render(context, mouseX, mouseY, tickDelta);
			}

			void update() {
				keyButton.setMessage(this.keyBinding.getBoundKeyLocalizedText());
				var duplicate = false;
				var duplicateInfo = Text.empty();
				if(!keyBinding.isUnbound()) {
					var allKeyBindings = GAME_OPTIONS.allKeys;
					for(KeyBinding keyBinding : allKeyBindings) {
						if(keyBinding != this.keyBinding && this.keyBinding.equals(keyBinding)) {
							if(duplicate) {
								duplicateInfo.append(", ");
							}
							duplicate = true;
							duplicateInfo.append(Text.translatable(keyBinding.getTranslationKey()));
						}
					}
				}

				if(duplicate) {
					keyButton.setMessage(Text.literal("[ ").append(keyButton.getMessage().copy().formatted(Formatting.WHITE)).append(" ]").formatted(Formatting.RED));
					keyButton.setTooltip(Tooltip.of(Text.translatable("controls.keybinds.duplicateKeybinds", duplicateInfo)));
				} else {
					keyButton.setTooltip(null);
				}

				if(selectedKeyBinding == keyBinding) {
					this.keyButton.setMessage(Text.literal("> ").append(this.keyButton.getMessage().copy().formatted(Formatting.WHITE, Formatting.UNDERLINE)).append(" <").formatted(Formatting.YELLOW));
				}
			}

			@Override
			public List<? extends Selectable> selectableChildren() {
				return List.of(this.textField, this.saveButton, this.keyButton);
			}

			@Override
			public List<? extends Element> children() {
				return List.of(this.textField, this.saveButton, this.keyButton);
			}
		}
	}

}
