package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_ChatSpamKickDisable extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_ChatSpamKickDisable(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent event){
		if(event.getReason().equals("Kicked for spamming")){
			event.setCancelled(true);
			return;
		}
	}
}
