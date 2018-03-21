package com.jaoafa.MyMaid2.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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
		Location loc = block.getLocation();
		List<Particle> particles = new ArrayList<>(Arrays.asList(Particle.values()));
		particles.remove(Particle.MOB_APPEARANCE);


		Random rnd = new Random();
		int i = rnd.nextInt(particles.size());
		Particle particle = particles.get(i);

		loc.getWorld().spawnParticle(particle, loc, 30, 0.3, 0, 0.3);
		event.setCancelled(true);
	}
}
