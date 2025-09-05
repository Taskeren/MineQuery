package cn.taskeren.minequery.features;

import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;

import static cn.taskeren.minequery.config.MineQueryConfig.enableNotPlace;

public class NotPlace {

	private static final String STICK_NAME = "NotPlace";

	// this map stores the state of allow/deny of each side.
	// this map should be set to initial when game restarts, imo. maybe add a config later?
	private static final EnumMap<Direction, Boolean> NOT_PLACE_MAP = new EnumMap<>(Direction.class);

	static {
		for(Direction direction : Direction.values()) {
			NOT_PLACE_MAP.put(direction, false);
		}
	}

	public static void init() {
		InteractionEvent.RIGHT_CLICK_BLOCK.register(NotPlace::onRightClick);
		ClientTooltipEvent.ITEM.register(NotPlace::onItemTooltip);
	}

	private static boolean isMagicStick(ItemStack stack) {
		return stack.getItem() == Items.STICK && stack.getName().getString().equalsIgnoreCase(STICK_NAME);
	}

	private static boolean canPlaceAt(Direction direction) {
		return !NOT_PLACE_MAP.get(direction);
	}

	private static void flipNotPlaceState(Direction direction) {
		NOT_PLACE_MAP.put(direction, canPlaceAt(direction));
	}

	private static MutableText getDirectionLocalizedName(Direction direction) {
		return Text.translatable("minequery.tooltip.not_place." + direction.name().toLowerCase(Locale.ROOT));
	}

	private static Text getDirectionStateColoredLocalizedText(Direction direction, boolean useText) {
		if(canPlaceAt(direction)) {
			return (useText
					? Text.translatable("minequery.tooltip.not_place.allow.text")
					: Text.translatable("minequery.tooltip.not_place.allow")
			).formatted(Formatting.GREEN);
		} else {
			return (useText
					? Text.translatable("minequery.tooltip.not_place.deny.text")
					: Text.translatable("minequery.tooltip.not_place.deny")
			).formatted(Formatting.RED);
		}
	}

	private static ActionResult onRightClick(PlayerEntity player, Hand hand, BlockPos pos, Direction direction) {
		if(!enableNotPlace) {
			return ActionResult.PASS;
		}

		var item = player.getStackInHand(hand);
		if(player.isSneaking() && isMagicStick(item)) {
			flipNotPlaceState(direction);
			player.sendMessage(Text.translatable(
					"minequery.tooltip.not_place.updated",
					getDirectionLocalizedName(direction).formatted(Formatting.YELLOW),
					getDirectionStateColoredLocalizedText(direction, true)
			), false);
			return ActionResult.FAIL;
		}

		if(item.getItem() instanceof BlockItem && !canPlaceAt(direction)) {
			return ActionResult.FAIL;
		}

		return ActionResult.PASS;
	}


	private static void onItemTooltip(ItemStack stack, List<Text> texts, Item.TooltipContext context, TooltipType type) {
		if(isMagicStick(stack)) {
			var tickLong = MinecraftClient.getInstance().world.getLevelProperties().getTime();
			tickLong /= 5;
			var tick = (int) tickLong;

			var title = Text.translatable("minequery.tooltip.not_place").copy()
					.formatted(Formatting.values()[tick % Formatting.values().length]);
			texts.add(title);
			for(Direction direction : Direction.values()) {
				var directionText = getDirectionLocalizedName(direction).formatted(Formatting.GRAY);
				var stateText = getDirectionStateColoredLocalizedText(direction, false);
				texts.add(directionText.append(Text.of(": ")).append(stateText));
			}
		}
	}

}
