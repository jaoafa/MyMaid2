package com.jaoafa.MyMaid2.Lib;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

/**
 * ミュート機能の制御
 * @author Tomachi
 */
public class MuteManager extends MyMaid2Premise {
	static JavaPlugin plugin;
	static File file;
	public static void start(JavaPlugin plugin) {
		MuteManager.plugin = plugin;

		String path = "mutes.yml";
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
	/**
	 * Muteリストにプレイヤーを追加します。
	 * @param player 追加するプレイヤー
	 * @return 追加し、保存できたかどうか
	 */
	public static boolean Add(Player player) {
		List<String> mutes = loadMutes();
		if(mutes == null) return false;
		mutes.add(player.getUniqueId().toString());
		return saveMutes(mutes);
	}
	/**
	 * Muteリストにプレイヤーがあるかどうかを調べます。
	 * @param player 調べるプレイヤー
	 * @return Muteリストにあるかどうか
	 */
	public static boolean Exists(Player player) {
		List<String> mutes = loadMutes();
		if(mutes == null) return false;
		return mutes.contains(player.getUniqueId().toString());
	}
	/**
	 * Muteリストからプレイヤーを削除します。
	 * @param player 削除するプレイヤー
	 * @return 削除し、保存できたかどうか
	 */
	public static boolean Remove(Player player) {
		List<String> mutes = loadMutes();
		if(mutes == null) return false;
		if(!mutes.contains(player.getUniqueId().toString())) {
			// not found
			return false;
		}
		mutes.remove(player.getUniqueId().toString());
		return saveMutes(mutes);
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