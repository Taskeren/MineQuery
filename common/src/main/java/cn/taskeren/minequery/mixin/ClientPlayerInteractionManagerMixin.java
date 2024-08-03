package cn.taskeren.minequery.mixin;

import cn.taskeren.minequery.api.ClientBlockEvent;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

	@Shadow @Final private MinecraftClient client;

	/**
	 * From net.fabricmc.fabric.mixin.event.interaction.client.ClientPlayerInteractionManagerMixin#fabric$onBlockBroken.
	 */
	@Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void mq$onBlockBroken(BlockPos pos, CallbackInfoReturnable<Boolean> cir, World world, BlockState blockState) {
		ClientBlockEvent.CLIENT_BLOCK_BREAK_EVENT.invoker().onBlockBroken(client.world, client.player, pos, blockState);
	}

}
