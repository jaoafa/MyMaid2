package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Task.Task_LoginVoteCheck;

public class Event_LoginVoteCheck extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_LoginVoteCheck(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginVoteCheck(PlayerJoinEvent event){
		new Task_LoginVoteCheck(plugin, event.getPlayer()).runTaskLater(JavaPlugin(), 200L);
	}

}
