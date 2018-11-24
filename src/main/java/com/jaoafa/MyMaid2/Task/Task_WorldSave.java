package com.jaoafa.MyMaid2.Task;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Task_WorldSave extends BukkitRunnable {
	JavaPlugin plugin;
	public Task_WorldSave(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		List<World> worlds = Bukkit.getServer().getWorlds();
		for (World w : worlds){
			plugin.getLogger().info("World Save Start... | world: " + w.getName());
			w.save();
			plugin.getLogger().info("World Save End. | world: " + w.getName());
		}
	}
}
