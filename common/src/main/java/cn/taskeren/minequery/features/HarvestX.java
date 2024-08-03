package cn.taskeren.minequery.features;

import cn.taskeren.minequery.api.ClientBlockEvent;
import cn.taskeren.minequery.utils.BlockUtils;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import static cn.taskeren.minequery.config.MineQueryConfig.*;

/**
 * Feature: Harvest X
 * <p>
 * There are two modes:
 * <ol>
 *  <li>left-clicking harvest</li>
 *  <li>right-clicking harvest</li>
 * </ol>
 * <p>
 * <i>In left-clicking harvest mode</i>, the <b>crops</b> (Wheat, Carrots, Potatoes, etc.) are harvested by left-clicking, and
 * will be prevented if not fully grown.
 * If the crops are harvested, it will trigger {@link HarvestX#handleBlockBreak(ClientWorld, ClientPlayerEntity, BlockPos, BlockState)}
 * and will schedule a right-clicking on the block after two ticks, which seeds the item in the main hand.
 * <p>
 * <i>In right-clicking harvest mode</i>, the <b>crops</b> are harvested by right-clicking, and will send
 * a breaking block packet if it is fully grown. The re-seeding is achieved by the player itself, because he is right-clicking.
 * <p>
 * For <b>Cactus and Sugar Canes</b>, it is prevented to break the bottom block. And there is no re-seeding functionality because
 * nobody needs it, right?
 */
public class HarvestX {

	public static void init() {
		InteractionEvent.LEFT_CLICK_BLOCK.register(HarvestX::handleBreak);
		ClientBlockEvent.CLIENT_BLOCK_BREAK_EVENT.register(HarvestX::handleBlockBreak);
		InteractionEvent.RIGHT_CLICK_BLOCK.register(HarvestX::handleInteract);
	}

	private static EventResult handleBreak(PlayerEntity player, Hand hand, BlockPos pos, Direction face) {
		var world = player.getWorld();
		var state = world.getBlockState(pos);
		var type = state.getBlock();

		if(!enableHarvestX) {
			return EventResult.pass();
		}

		if(player.isSneaking() && !harvestX_handleSneaking) {
			return EventResult.pass();
		}

		if(!harvestX_useRightHarvest) {
			if(type instanceof CropBlock cropBlock && harvestX_handleCrops) {
				var age = cropBlock.getAge(state);
				var maxAge = cropBlock.getMaxAge();
				if(age < maxAge) {
					return EventResult.interruptFalse();
				}
			}

			if(type instanceof NetherWartBlock && harvestX_handleNetherWart) {
				var age = state.get(NetherWartBlock.AGE);
				if(age < NetherWartBlock.MAX_AGE) {
					return EventResult.interruptFalse();
				}
			}
		}

		if(type instanceof StemBlock && harvestX_handleStems) {
			return EventResult.interruptFalse();
		}

		if(type instanceof CactusBlock && harvestX_handleCactus) {
			var blockBelow = world.getBlockState(pos.down());
			if(blockBelow.getBlock() != Blocks.CACTUS) {
				return EventResult.interruptFalse();
			}
		}

		if(type instanceof SugarCaneBlock && harvestX_handleSugarCane) {
			var blockBelow = world.getBlockState(pos.down());
			if(blockBelow.getBlock() != Blocks.SUGAR_CANE) {
				return EventResult.interruptFalse();
			}
		}

		return EventResult.pass();
	}

	private static void placeAndSwingHand(BlockPos pos) {
		var minecraft = MinecraftClient.getInstance();
		var player = minecraft.player;

		if(player == null) {
			throw new IllegalStateException(new NullPointerException("player"));
		}

		var posDown = pos.down();

		// send the block place packet
		var bhr = new BlockHitResult(Vec3d.ofCenter(posDown), Direction.UP, posDown, false);
		var pkt = new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, bhr, 0);

		TickTaskScheduler.addTask(2, () -> player.networkHandler.sendPacket(pkt));

		// play animation
		player.swingHand(Hand.MAIN_HAND);
	}

	private static EventResult handleBlockBreak(ClientWorld world, ClientPlayerEntity player, BlockPos pos, BlockState blockState) {
		if(!enableHarvestX) {
			return EventResult.pass();
		}

		if(player.isSneaking() && !harvestX_handleSneaking) {
			return EventResult.pass();
		}

		var blockType = blockState.getBlock();

		// only works in "left-clicking mode"
		if(!harvestX_useRightHarvest && (blockType instanceof CropBlock || blockType instanceof NetherWartBlock)) {
			if(BlockUtils.canPlaceAt(blockType, blockState, world, pos)) {
				placeAndSwingHand(pos);
			}
		}

		return EventResult.pass();
	}

	private static void breakBlockAndSwingHand(BlockPos pos, Direction side) {
		var minecraft = MinecraftClient.getInstance();
		var interactionManager = minecraft.interactionManager;
		var player = minecraft.player;

		if(interactionManager != null) {
			interactionManager.attackBlock(pos, side);
			if(player != null) {
				player.swingHand(Hand.MAIN_HAND);
			}
		}
	}

	private static EventResult handleInteract(PlayerEntity player, Hand hand, BlockPos pos, Direction direction) {
		if(!enableHarvestX) {
			return EventResult.pass();
		}

		var world = player.getWorld();
		var state = world.getBlockState(pos);
		var type = state.getBlock();

		if(harvestX_useRightHarvest) {
			if(type instanceof CropBlock cropBlock && harvestX_handleCrops) {
				var age = cropBlock.getAge(state);
				var maxAge = cropBlock.getMaxAge();
				if(age >= maxAge) {
					breakBlockAndSwingHand(pos, direction);
				}
			}

			if(type instanceof NetherWartBlock && harvestX_handleNetherWart) {
				var age = state.get(NetherWartBlock.AGE);
				if(age >= NetherWartBlock.MAX_AGE) {
					breakBlockAndSwingHand(pos, direction);
				}
			}
		}

		return EventResult.pass();
	}

}
