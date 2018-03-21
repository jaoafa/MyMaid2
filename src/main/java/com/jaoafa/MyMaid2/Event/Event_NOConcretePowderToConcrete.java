package com.jaoafa.MyMaid2.Event;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_NOConcretePowderToConcrete extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onBlockFormEvent(BlockFormEvent event){
		Block block = event.getBlock();
		if(block.getType() != Material.CONCRETE_POWDER){
			return;
		}
		event.setCancelled(true);
	}
}
