package cn.taskeren.minequery.screen;

import cn.taskeren.minequery.MineQuery;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/**
 * @see CommandShortcutConfigScreen
 */
@Deprecated
public class CommandShortcutEditScreen extends Screen {

	private final Screen parent;

	public TextFieldWidget commandText;
	public ButtonWidget submitButton;
	public ButtonWidget cancelButton;

	public ChatInputSuggestor commandSuggester;

	public CommandShortcutEditScreen(Screen parent) {
		super(Text.of("Command Shortcut Editor"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		this.submitButton = addDrawableChild(
				ButtonWidget
						.builder(Text.translatable("minequery.gui.command_shortcut.edit.submit"), button -> submitAndClose())
						.dimensions(this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20)
						// .tooltip(Tooltip.of(Text.literal("Tooltip of Button")))
						.build()
		);
		this.cancelButton = addDrawableChild(
				ButtonWidget
						.builder(Text.translatable("minequery.gui.command_shortcut.edit.cancel"), button -> cancelAndClose())
						.dimensions(this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20)
						.build()
		);

		this.commandText = addSelectableChild(
				new TextFieldWidget(this.textRenderer, this.width / 2 - 150, 50, 300, 20, Text.of("123"))
		);
		this.commandText.setMaxLength(32500);
		this.commandText.setChangedListener(this::onCommandChanged);

		this.commandSuggester = new ChatInputSuggestor(this.client, this, this.commandText, this.textRenderer, false, true, 0, 7, false, Integer.MIN_VALUE);
		this.commandSuggester.setWindowActive(true);
		this.commandSuggester.refresh();
	}

	@Override
	protected void setInitialFocus() {
		this.setInitialFocus(this.commandText);
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		var str = this.commandText.getText();
		this.init(client, width, height);
		this.commandText.setText(str);
		this.commandSuggester.refresh();
	}

	private void onCommandChanged(String s) {
		this.commandSuggester.refresh();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(this.commandSuggester.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if(super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if(keyCode != 257 && keyCode != 335) {
			return false;
		} else {
			this.submitAndClose();
			return true;
		}
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		return this.commandSuggester.mouseScrolled(verticalAmount) ? true : super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.commandSuggester.mouseClicked(mouseX, mouseY, button) ? true : super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("minequery.gui.command_shortcut.edit.title"), this.width / 2, 20, 16777215);
		this.commandText.render(context, mouseX, mouseY, delta);
		this.commandSuggester.render(context, mouseX, mouseY);
	}

	@Override
	public void close() {
		client.setScreen(parent);
	}

	private void submitAndClose() {
		MineQuery.LOGGER.info("!!!: {}", commandText.getText());

		this.client.setScreen(parent);
	}

	private void cancelAndClose() {
		this.client.setScreen(parent);
	}

}
