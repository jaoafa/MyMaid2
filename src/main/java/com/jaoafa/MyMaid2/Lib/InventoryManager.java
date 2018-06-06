package com.jaoafa.MyMaid2.Lib;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class InventoryManager extends MyMaid2Premise {
	static JavaPlugin plugin;
	static File dir;
	public static void start(JavaPlugin plugin) {
		InventoryManager.plugin = plugin;

		File dir = new File(plugin.getDataFolder() + File.separator + "inventory");
		if(!dir.exists()){
			if(dir.mkdirs()){
				Bukkit.getLogger().info("Inventoryデータディレクトリを作成しました。");
			}else{
				Bukkit.getLogger().info("Inventoryデータディレクトリの作成に失敗しました。");
				dir = null;
			}
		}else{
			Bukkit.getLogger().info("Inventoryデータディレクトリは既に作成されていました。続行します。");
		}
		InventoryManager.dir = dir;
	}
	public static boolean saveInventory(Player player, String saveName) {
		if(dir == null) return false;
		File f = new File(dir, player.getName() + ".yml");
		FileConfiguration c = YamlConfiguration.loadConfiguration(f);
		c.set(saveName + ".armor", player.getInventory().getArmorContents());
		c.set(saveName + ".content", player.getInventory().getContents());
		try {
			c.save(f);
		} catch (IOException e) {
			BugReporter(e);
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean loadInventory(Player player, String loadName) {
		if(dir == null) return false;
		File f = new File(dir, player.getName() + ".yml");
		if(!f.exists()) return false;
		FileConfiguration c = YamlConfiguration.loadConfiguration(f);
		if(!(c.contains(loadName + ".armor") && c.contains(loadName + ".content"))){
			return false;
		}
		ItemStack[] content = ((List<ItemStack>) c.get(loadName + ".armor")).toArray(new ItemStack[0]);
		player.getInventory().setArmorContents(content);

		content = ((List<ItemStack>) c.get(loadName + ".content")).toArray(new ItemStack[0]);
		player.getInventory().setContents(content);
		player.updateInventory();
		return true;
	}

	@SuppressWarnings("unchecked")
	public static boolean restoreInventory(Player from_player, Player to_player, String loadName) {
		if(dir == null) return false;
		File f = new File(dir, from_player.getName() + ".yml");
		if(!f.exists()) return false;
		FileConfiguration c = YamlConfiguration.loadConfiguration(f);
		if(!(c.contains(loadName + ".armor") && c.contains(loadName + ".content"))){
			return false;
		}
		ItemStack[] content = ((List<ItemStack>) c.get(loadName + ".armor")).toArray(new ItemStack[0]);
		to_player.getInventory().setArmorContents(content);

		content = ((List<ItemStack>) c.get(loadName + ".content")).toArray(new ItemStack[0]);
		to_player.getInventory().setContents(content);
		to_player.updateInventory();
		return true;
	}
}
