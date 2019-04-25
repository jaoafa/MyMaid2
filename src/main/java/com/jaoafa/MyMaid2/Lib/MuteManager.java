package com.jaoafa.MyMaid2.Lib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

/**
 * ミュート機能の制御<br>
 *
 * @author Tomachi
 *
 */
public class MuteManager extends MyMaid2Premise {
	static JavaPlugin plugin;
	static File file;
	public static void start(JavaPlugin plugin) {
		MuteManager.plugin = plugin;

		String path = plugin.getDataFolder() + "mute.yml";
		File file = new File(path);
		if(!file.exists()){
			Bukkit.getLogger().info("「" + path + "」が存在しません。作成します。");
			List<String> mutes = new ArrayList<String>();
			saveMutes(mutes);

			MuteManager.file = file;
		}else if(file.exists() && file.canRead() && file.canWrite()){
			Bukkit.getLogger().info("「" + path + "」に読み込み・書き込み権限がありました。続行します。");
			MuteManager.file = file;
		}else{
			Bukkit.getLogger().info("「" + path + "」に対する操作ができません。機能を無効化します。");
			MuteManager.file = null;
		}
	}
	public static boolean saveMutes(List<String> mutes) {
		if(file == null) return false;
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		c.set("mutes", mutes);
		try {
			c.save(file);
		} catch (IOException e) {
			BugReporter(e);
			return false;
		}
		return true;
	}

	@Nullable
	public static List<String> loadMutes() {
		if(file == null) return null;
		if(!file.exists()) return null;
		FileConfiguration c = YamlConfiguration.loadConfiguration(file);
		if(!(c.contains("mutes"))){
			return null;
		}
		List<String> mutes = c.getStringList("mutes");
		return mutes;
	}
}