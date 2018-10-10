package com.jaoafa.MyMaid2.Event;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_SandBoxRoad implements Listener {
	JavaPlugin plugin;
	public Event_SandBoxRoad(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();

		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
			return;
		}

		Block block = event.getBlock();
		Location loc = block.getLocation();

		if(!loc.getWorld().getName().equalsIgnoreCase("SandBox")){
			return;
		}

		Location loc67 = block.getLocation(); // Y67 : 地面
		loc67.setY(67);
		Block block67 = loc67.getBlock();
		if(block67.getType() == Material.BRICK){ // 地面ブロックがレンガである
			if((loc.getBlockX() % 200 != 0) && (loc.getBlockZ() % 200 != 0)){
				// X,Zが両方200で割り切れない→道路じゃない？
				return;
			}
			SendMessage(player, "Sorry, but you can't place that block here.");
			event.setCancelled(true);
		}else if(block67.getType() == Material.GRASS_PATH){ // 地面ブロックが草の道である
			// 199 + 1 % 200 = 0
			// 198 + 2 % 200 = 0
			// 201 - 1 % 200 = 0
			// 202 - 2 % 200 = 0

			if(((loc.getBlockX() + 1) % 200 != 0) && ((loc.getBlockX() + 2) % 200 != 0) && ((loc.getBlockX() - 1) % 200 != 0) && ((loc.getBlockX() - 2) % 200 != 0)){
				if(((loc.getBlockZ() + 1) % 200 != 0) && ((loc.getBlockZ() + 2) % 200 != 0) && ((loc.getBlockZ() - 1) % 200 != 0) && ((loc.getBlockZ() - 2) % 200 != 0)){
					// ×？
					return;
				}
			}
			SendMessage(player, "Sorry, but you can't place that block here.");
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event){
		Player player = event.getPlayer();

		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
			return;
		}

		Block block = event.getBlock();
		Location loc = block.getLocation();

		if(!loc.getWorld().getName().equalsIgnoreCase("SandBox")){
			return;
		}

		Location loc67 = block.getLocation(); // Y67 : 地面
		loc67.setY(67);
		Block block67 = loc67.getBlock();
		if(block67.getType() == Material.BRICK){ // 地面ブロックがレンガである
			if((loc.getBlockX() % 200 != 0) && (loc.getBlockZ() % 200 != 0)){
				// X,Zが両方200で割り切れない→道路じゃない？
				return;
			}
			SendMessage(player, "Sorry, but you can't place that block here.");
			event.setCancelled(true);
		}else if(block67.getType() == Material.GRASS_PATH){ // 地面ブロックが草の道である
			// 199 + 1 % 200 = 0
			// 198 + 2 % 200 = 0
			// 201 - 1 % 200 = 0
			// 202 - 2 % 200 = 0

			if(((loc.getBlockX() + 1) % 200 != 0) && ((loc.getBlockX() + 2) % 200 != 0) && ((loc.getBlockX() - 1) % 200 != 0) && ((loc.getBlockX() - 2) % 200 != 0)){
				if(((loc.getBlockZ() + 1) % 200 != 0) && ((loc.getBlockZ() + 2) % 200 != 0) && ((loc.getBlockZ() - 1) % 200 != 0) && ((loc.getBlockZ() - 2) % 200 != 0)){
					// ×？
					return;
				}
			}
			SendMessage(player, "Sorry, but you can't place that block here.");
			event.setCancelled(true);
		}
	}
	void SendMessage(Player player, String message){
		player.sendMessage("[SandBox] " + ChatColor.RED + message);
	}
}
