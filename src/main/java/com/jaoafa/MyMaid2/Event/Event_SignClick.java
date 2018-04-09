package com.jaoafa.MyMaid2.Event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Command.Cmd_Sign;

public class Event_SignClick implements Listener {
	JavaPlugin plugin;
	public Event_SignClick(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
	    if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.STICK) return;
	    Block clickedBlock = event.getClickedBlock();
	    Material material = clickedBlock.getType();
	    if (material == Material.SIGN_POST || material == Material.WALL_SIGN) {
	        Sign sign = (Sign) clickedBlock.getState();
	        Cmd_Sign.signlist.put(event.getPlayer().getName(), sign);
	        int x = sign.getX();
	        int y = sign.getY();
	        int z = sign.getZ();
	        event.getPlayer().sendMessage("[Sign] " + ChatColor.GREEN + "看板を選択しました。[" + x + " " + y + " " + z + "]");
	    }
	}
}
