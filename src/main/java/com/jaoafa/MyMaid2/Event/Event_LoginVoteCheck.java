package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Task.Task_LoginVoteCheck;

public class Event_LoginVoteCheck extends MyMaid2Premise implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginVoteCheck(PlayerJoinEvent event){
		new Task_LoginVoteCheck(event.getPlayer()).runTaskLater(JavaPlugin(), 200L);
	}

}
