package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_DeathToDeath extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_DeathToDeath(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
		return;
		/*
		String message = event.getMessage();
		if(!message.contains("死")){
			return;
		}
		Player player = event.getPlayer();
		player.chat("私は喋ってはならない言葉を話してしまった…");
		player.setHealth(0.0D);
		*/
	}
}
