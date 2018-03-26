package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Lib.SKKColors;

public class Event_ChatSKK {
	JavaPlugin plugin;
	public Event_ChatSKK(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent_ChatSKK(AsyncPlayerChatEvent event){
		event.setFormat(
				SKKColors.ReplacePlayerSKKChatColor(
						event.getPlayer(),
						"%1", event.getFormat()
						)
				);
	}
}
