package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.SKKColors;
import com.jaoafa.MyMaid2.Task.TabListSKKReloader;

public class Event_SKKColor extends MyMaid2Premise implements Listener {
	private JavaPlugin plugin;
	public Event_SKKColor(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent_ChatSKK(AsyncPlayerChatEvent event){
		event.setFormat(
				SKKColors.ReplacePlayerSKKChatColor(
						event.getPlayer(),
						"%1",
						event.getFormat()
						)
				);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent_JoinChangeMessage(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String JoinMessage = SKKColors.getPlayerSKKJoinMessage(player);
		if(JoinMessage != null){
			event.setJoinMessage(JoinMessage);
		}

		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			new TabListSKKReloader(p).runTaskLater(plugin, 20L);
		}
	}
}
