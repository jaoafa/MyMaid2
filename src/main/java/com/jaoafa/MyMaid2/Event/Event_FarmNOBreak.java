package com.jaoafa.MyMaid2.Event;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_FarmNOBreak extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event){
		EntityType type = event.getEntityType();
		if(type == EntityType.PLAYER){
			return;
		}else if(type == EntityType.VILLAGER){
			return;
		}else if(type == EntityType.FALLING_BLOCK){
			return;
		}
		event.setCancelled(true);
	}
	@EventHandler
	public void onBlockIgniteEvent(BlockIgniteEvent event){
		Entity entity = event.getIgnitingEntity();
		if ((entity instanceof Villager)) {
			return;
		}
		if ((entity instanceof Player)) {
			return;
		}
		if ((entity instanceof FallingBlock)) {
			return;
		}
		event.setCancelled(true);
	}
	@EventHandler
	public void onFrom(EntityChangeBlockEvent event){
		if(event.getBlock().getType() == Material.SOIL){
			event.setCancelled(true);
		}
	}
}
