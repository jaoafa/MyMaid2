package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Task.Task_getUsingMods;

public class Event_getUsingMods extends MyMaid2Premise implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginVoteCheck(PlayerJoinEvent event){
		new Task_getUsingMods(event.getPlayer()).runTaskLater(JavaPlugin(), 20L);
	}
}
