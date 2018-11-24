package com.jaoafa.MyMaid2.Event;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class Event_AntiTNTMinecart implements Listener {
	@EventHandler
	public void onVehicleCreateEvent(VehicleCreateEvent event){
		Vehicle vehicle = event.getVehicle();
		if(vehicle.getType() == EntityType.MINECART_TNT){
			Location loc = vehicle.getLocation();
			loc = loc.add(0, 0.5, 0);
			loc.getWorld().spawnParticle(Particle.BARRIER, loc, 1, 0, 0, 0);
			vehicle.remove();
		}
	}
}
