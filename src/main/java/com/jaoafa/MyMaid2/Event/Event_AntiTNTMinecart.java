package com.jaoafa.MyMaid2.Event;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Event_AntiTNTMinecart {
	JavaPlugin plugin;
	public Event_AntiTNTMinecart(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onVehicleCreateEvent(VehicleCreateEvent event){
		Vehicle vehicle = event.getVehicle();
		if(vehicle.getType() == EntityType.MINECART_TNT){
			Location loc = vehicle.getLocation();
			loc.getWorld().spawnParticle(Particle.BARRIER, loc, 1, 0.3, 0, 0.3);
			vehicle.remove();
		}
	}
}
