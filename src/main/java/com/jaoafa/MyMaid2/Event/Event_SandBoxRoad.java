package com.jaoafa.MyMaid2.Event;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_SandBoxRoad implements Listener {
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();

		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
			return;
		}

		Block block = event.getBlock();

		if(check(block)){
			SendMessage(player, "Sorry, but you can't place that block here.");
			event.setCancelled(true);
			return;
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

		if(check(block)){
			SendMessage(player, "Sorry, but you can't break that block here.");
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event){
		for(Block block : event.getBlocks()){
			if(check(block)){
				event.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler
	public void onBlockPistonRetractEvent(BlockPistonRetractEvent event){
		for(Block block : event.getBlocks()){
			if(check(block)){
				event.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Material type = event.getMaterial();
		if(type != null
				&& type != Material.WOOD_HOE
				&& type != Material.STONE_HOE
				&& type != Material.IRON_HOE
				&& type != Material.GOLD_HOE
				&& type != Material.DIAMOND_HOE){
			return;
		}

		Block block = event.getClickedBlock();
		if(check(block)){
			SendMessage(player, "Sorry, but you can't use that block here.");
			event.setCancelled(true);
			return;
		}
	}
	boolean check(Block block){
		Location loc = block.getLocation();

		if(!loc.getWorld().getName().equalsIgnoreCase("SandBox")){
			return false;
		}

		Location loc67 = block.getLocation(); // Y67 : 地面
		loc67.setY(67);
		Block block67 = loc67.getBlock();
		if(block67.getType() == Material.BRICK){ // 地面ブロックがレンガである
			if((loc.getBlockX() % 200 != 0) && (loc.getBlockZ() % 200 != 0)){
				// X,Zが両方200で割り切れない→道路じゃない？
				return false;
			}
			return true;
		}else if(block67.getType() == Material.GRASS_PATH){ // 地面ブロックが草の道である
			// 199 + 1 % 200 = 0
			// 198 + 2 % 200 = 0
			// 201 - 1 % 200 = 0
			// 202 - 2 % 200 = 0

			if(((loc.getBlockX() + 1) % 200 != 0) && ((loc.getBlockX() + 2) % 200 != 0) && ((loc.getBlockX() - 1) % 200 != 0) && ((loc.getBlockX() - 2) % 200 != 0)){
				if(((loc.getBlockZ() + 1) % 200 != 0) && ((loc.getBlockZ() + 2) % 200 != 0) && ((loc.getBlockZ() - 1) % 200 != 0) && ((loc.getBlockZ() - 2) % 200 != 0)){
					// ×？
					return false;
				}
			}
			return true;
		}
		return false;
	}
	void SendMessage(Player player, String message){
		player.sendMessage("[SandBox] " + ChatColor.RED + message);
	}
}
