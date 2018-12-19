package com.jaoafa.MyMaid2.Lib;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NearestPlayer {
	private boolean status;
	private Player player = null;
	private double closest = -1;
	public NearestPlayer(Location loc){
		double closest = Double.MAX_VALUE;
		Player closestp = null;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.getWorld().equals(loc.getWorld())){
				continue;
			}
			double dist = p.getLocation().distance(loc);
			if(closest == Double.MAX_VALUE || dist < closest){
				closest = dist;
				closestp = p;
			}
		}
		if(closestp == null){
			this.status = false;
		}else{
			this.status = true;
			this.player = closestp;
			this.closest = closest;
		}
	}
	public Boolean getStatus(){
		return status;
	}
	public Player getPlayer(){
		return player;
	}
	public Double getClosest(){
		return closest;
	}
}
