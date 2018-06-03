package com.jaoafa.MyMaid2.Task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Command.Cmd_DedRain;

public class Task_DedRain_Stop extends BukkitRunnable {
	JavaPlugin plugin;
	Player player;
	public Task_DedRain_Stop(JavaPlugin plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run() {
		Bukkit.broadcastMessage("[Weather] " + ChatColor.GRAY + "10分経ったため、降水禁止設定をオンにしました。");
		Cmd_DedRain.flag = true;
		for(World world : Bukkit.getWorlds()){
			world.setStorm(false);
			world.setWeatherDuration(0);
			world.setThundering(false);
			world.setThunderDuration(0);
		}
	}
}
