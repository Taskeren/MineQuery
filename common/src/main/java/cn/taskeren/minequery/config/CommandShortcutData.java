package cn.taskeren.minequery.config;

import cn.taskeren.minequery.MineQuery;
import com.google.gson.*;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CommandShortcutData {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static final File CONFIG_FILE = new File(MinecraftClient.getInstance().runDirectory, "config/minequery-command-shortcut.json");

	private static Map<String, String> data;

	public static void load() {
		if(!CONFIG_FILE.getParentFile().exists()) {
			var ignored = CONFIG_FILE.getParentFile().mkdirs();
		}
		if(!CONFIG_FILE.isFile()) {
			var ignored = CONFIG_FILE.renameTo(new File(CONFIG_FILE.getParentFile(), "minequery-command-shortcut.json.corrupted"));
			MineQuery.LOGGER.warn("The command shortcut data is corrupted, and it is moved to .corrupted file.");
		}

		if(!CONFIG_FILE.exists()) {
			data = new HashMap<>();
			try(var fw = new FileWriter(CONFIG_FILE)) {
				fw.write(GSON.toJson(data));
			} catch(IOException e) {
				MineQuery.LOGGER.warn("Unable to write command shortcut data! The changes are not saved.", e);
			}
			return;
		}

		try(var fr = new FileReader(CONFIG_FILE)) {
			//noinspection unchecked
			data = (HashMap<String, String>) GSON.fromJson(fr, HashMap.class);
		} catch(IOException | JsonIOException fnf) {
			MineQuery.LOGGER.warn("Unable to read command shortcut data! Unreachable!", fnf);
			throw new RuntimeException(fnf);
		} catch(JsonSyntaxException jse) {
			data = new HashMap<>();
			MineQuery.LOGGER.warn("The json syntax is corrupted; all data is ignored.", jse);
		}
	}

	public static void save() {
		try(var fw = new FileWriter(CONFIG_FILE)) {
			fw.write(GSON.toJson(data));
		} catch(IOException e) {
			MineQuery.LOGGER.warn("Unable to write command shortcut data! The changes are not saved.", e);
		}
	}

	@Nullable
	public static String getCommandText(int i) {
		if(data == null) {
			load();
		}

		return data.getOrDefault("C-" + i, null);
	}

	public static String getCommandTextOrEmpty(int i) {
		var text = getCommandText(i);
		return text == null ? "" : text;
	}

	/**
	 * Set the command, or {@code null} to remove.
	 * @param i the index
	 * @param s the command string
	 */
	public static void setCommandText(int i, @Nullable String s) {
		if(data == null) {
			load();
		}

		if(s != null) {
			data.put("C-" + i, s);
		} else {
			data.remove("C-" + i);
		}

		save();
	}

}
