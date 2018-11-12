package com.jaoafa.MyMaid2.Task;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Event.Event_Tips;

public class Task_LoginjaoafaTips extends BukkitRunnable {
	JavaPlugin plugin;
	Player player;
	public Task_LoginjaoafaTips(JavaPlugin plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run() {
		if(!player.isOnline()){
			return;
		}
		if(Event_Tips.jaoafa.contains(player.getUniqueId())){
			Event_Tips.jaoafa.remove(player.getUniqueId());
			return;
		}
		player.sendMessage("[Tips] " + ChatColor.GREEN + "サーバへのログイン後には、「jao」「afa」と分けて発言すると当サーバにおける挨拶となります！");
		player.sendMessage("[Tips] " + ChatColor.GREEN + "次回ログイン後からはぜひそのように発言してみてください！");
		player.sendMessage("[Tips] " + ChatColor.GREEN + "詳しくは以下記事をご覧ください。 https://jaoafa.com/community/lang/jao");
	}
}
