package cn.taskeren.minequery.utils;

import cn.taskeren.minequery.mixin.AbstractBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class BlockUtils {

	public static boolean canPlaceAt(Block self, BlockState state, WorldView world, BlockPos pos) {
		return ((AbstractBlockAccessor) self).invokeCanPlaceAt(state, world, pos);
	}

}
