package com.jaoafa.MyMaid2.Task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Command.Cmd_DedRain;

public class Task_DedRain_Stop extends BukkitRunnable {
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
