package cn.taskeren.minequery.config;

import cn.taskeren.minequery.features.NotAttack;
import eu.midnightdust.lib.config.MidnightConfig;

public class MineQueryConfig extends MidnightConfig {

	private static final String CATE_GENERAL = "general";

	@Entry(category = CATE_GENERAL)
	public static double feedEmDiameter = 8.0;
	@Entry(category = CATE_GENERAL)
	public static int commandShortcutSize = 12;
	@Entry(category = CATE_GENERAL)
	public static boolean enableHarvestX = true;
	@Entry(category = CATE_GENERAL)
	public static boolean enableNotPlace = true;

	private static final String CATE_HARVEST_X = "harvestX";

	@Entry(category = CATE_HARVEST_X)
	public static boolean harvestX_handleSneaking = false;
	@Entry(category = CATE_HARVEST_X)
	public static boolean harvestX_useRightHarvest = true;
	@Entry(category = CATE_HARVEST_X)
	public static boolean harvestX_handleCrops = true;
	@Entry(category = CATE_HARVEST_X)
	public static boolean harvestX_handleNetherWart = true;
	@Entry(category = CATE_HARVEST_X)
	public static boolean harvestX_handleStems = true;
	@Entry(category = CATE_HARVEST_X)
	public static boolean harvestX_handleCactus = true;
	@Entry(category = CATE_HARVEST_X)
	public static boolean harvestX_handleSugarCane = true;

	private static final String CATE_NOT_ATTACK = "notAttack";

	@Entry(category = CATE_NOT_ATTACK)
	public static boolean notAttack_allowAttackVillager = false;
	@Entry(category = CATE_NOT_ATTACK)
	public static NotAttack.AllowAttackIronGolem notAttack_allowAttackIronGolem = NotAttack.AllowAttackIronGolem.NONE;

}
