package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_ChatSpamKickDisable extends MyMaid2Premise implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent event){
		if(event.getReason().equals("Kicked for spamming")){
			event.setCancelled(true);
			return;
		}
	}
}
