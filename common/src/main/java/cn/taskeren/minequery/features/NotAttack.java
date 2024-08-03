package cn.taskeren.minequery.features;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import static cn.taskeren.minequery.config.MineQueryConfig.notAttack_allowAttackIronGolem;
import static cn.taskeren.minequery.config.MineQueryConfig.notAttack_allowAttackVillager;

/**
 * Feature: Not Attack
 * <p>
 * Prevent unintended attack to the villagers and iron golems. For Iron Golems, you can specify what type is allowed.
 * For example, you can only allow attacks to the "player-built" iron-golems.
 */
public class NotAttack {

	public static void init() {
		PlayerEvent.ATTACK_ENTITY.register(NotAttack::handle);
	}

	public enum AllowAttackIronGolem {
		NONE(false, false),
		VILLAGE_SPAWNED(false, true),
		PLAYER_BUILT(true, false),
		ALL(true, true),
		;

		private final boolean allowAttackPlayerBuilt;
		private final boolean allowAttackVillageSpawn;

		AllowAttackIronGolem(boolean allowAttackPlayerBuilt, boolean allowAttackVillageSpawn) {
			this.allowAttackPlayerBuilt = allowAttackPlayerBuilt;
			this.allowAttackVillageSpawn = allowAttackVillageSpawn;
		}

		public boolean canAttack(IronGolemEntity ironGolem) {
			return ironGolem.isPlayerCreated() ? allowAttackPlayerBuilt : allowAttackVillageSpawn;
		}
	}

	private static EventResult handle(PlayerEntity player, World world, Entity entity, Hand hand, EntityHitResult result) {
		if(entity instanceof VillagerEntity && !notAttack_allowAttackVillager) {
			return EventResult.interruptFalse();
		}

		if(entity instanceof IronGolemEntity ironGolem) {
			if(!notAttack_allowAttackIronGolem.canAttack(ironGolem)) {
				return EventResult.interruptFalse();
			}
		}

		return EventResult.pass();
	}

}
