package com.jaoafa.MyMaid2.Task;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Task_AFKING extends BukkitRunnable {
	JavaPlugin plugin;
	Player player;
	public Task_AFKING(JavaPlugin plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run() {
		//player.getWorld().playSound(player.getLocation(),Sound.EXPLODE,1,1);
		player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		String listname = player.getPlayerListName();
		if(!listname.contains(ChatColor.DARK_GRAY + player.getName())){
			listname = listname.replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
			player.setPlayerListName(listname);
		}
	}
}
