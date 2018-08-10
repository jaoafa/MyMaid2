package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_PlayerCommandSendRegular extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_PlayerCommandSendRegular(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		String command = e.getMessage();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Limited") && !group.equalsIgnoreCase("QPPE") && !group.equalsIgnoreCase("Default")){
			return;
		}
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String p_group = PermissionsManager.getPermissionMainGroup(p);
			if(p_group.equalsIgnoreCase("Regular") && (!player.getName().equals(p.getName()))){
				//if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator"))){
				p.sendMessage(ChatColor.GRAY + "(" + group +") " + player.getName() + ": " + ChatColor.YELLOW + command);
			}
		}
	}
}
