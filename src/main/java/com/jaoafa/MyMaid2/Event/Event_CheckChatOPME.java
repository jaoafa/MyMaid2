package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_CheckChatOPME extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_CheckChatOPME(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
		String message = event.getMessage();
		if(!message.equalsIgnoreCase("#opme") && !message.equalsIgnoreCase("opme")){
			return;
		}
		Player player = event.getPlayer();
		DiscordSend("223582668132974594", "__**[OPME]**__" + player.getName() + "が「" + message + "」をチャットしました。\n"
				+ "これはOPHackのトリガーとしているようです。注意してください。");
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
				p.sendMessage("[" + ChatColor.RED + "OPME" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + "が「" + message + "」をチャットしました。これはOPHackのトリガーとしているようです。注意してください。");
			}
		}
	}
}
