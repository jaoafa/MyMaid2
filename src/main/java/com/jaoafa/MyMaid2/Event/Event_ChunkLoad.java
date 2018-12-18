package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Task.Task_CheckChunk;

public class Event_ChunkLoad extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onChunkLoadEvent(ChunkLoadEvent event) {
		new Task_CheckChunk(event.getChunk()).runTaskAsynchronously(JavaPlugin());
	}
}
