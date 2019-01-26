package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_ChatSpamKickDisable extends MyMaid2Premise implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent event){
		if(event.getReason().equals("Kicked for spamming")){
			event.setCancelled(true);
			return;
		}
		Player player = event.getPlayer();
		String reason = event.getLeaveMessage();
		for(Player p : Bukkit.getOnlinePlayers()){
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
				continue;
			}
			p.sendMessage("[MyMaid] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」は、「" + reason + "」という理由でキックされました。");
		}
	}
}
