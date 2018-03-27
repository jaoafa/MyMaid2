package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Task.TPSChange;

public class Event_JoinHeaderFooterChange extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_JoinHeaderFooterChange(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEvent_JoinHeaderFooterChange(PlayerJoinEvent event){
		new TPSChange().runTaskLater(plugin, 10L);
	}
}
