package cn.taskeren.minequery.command;

import cn.taskeren.minequery.config.MineQueryConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent.ClientCommandSourceStack;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

public class MineQueryCommand {

	public static void init() {
		ClientCommandRegistrationEvent.EVENT.register(MineQueryCommand::register);
	}

	public static LiteralArgumentBuilder<ClientCommandSourceStack> lit(String name) {
		return literal(name);
	}

	public static <T> RequiredArgumentBuilder<ClientCommandSourceStack, T> arg(String name, ArgumentType<T> argumentType) {
		return argument(name, argumentType);
	}

	private static void register(CommandDispatcher<ClientCommandSourceStack> dispatcher, CommandRegistryAccess access) {
		LiteralArgumentBuilder<ClientCommandSourceStack> builder =
				lit("minequery").then(
						lit("flip-use-right-harvest").executes((ctx) -> {
							MineQueryConfig.harvestX_useRightHarvest = !MineQueryConfig.harvestX_useRightHarvest;
							ctx.getSource().arch$getPlayer().sendMessage(Text.of("The value has been updated to " + MineQueryConfig.harvestX_useRightHarvest), false);
							return 0;
						})
				);

		dispatcher.register(builder);
	}

}
