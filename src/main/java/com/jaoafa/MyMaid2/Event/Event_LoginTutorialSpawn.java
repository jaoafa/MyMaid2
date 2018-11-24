package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_LoginTutorialSpawn extends MyMaid2Premise implements Listener {
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginTutorialSpawn(PlayerJoinEvent event){
		Player player = event.getPlayer();

		// Limited, QPPEが対象。ログイン後どこにいようともチュートリアルに飛ばす

		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Limited") && !group.equalsIgnoreCase("QPPE")){
			// それ以外
			return;
		}

		player.teleport(new Location(Bukkit.getWorld("Jao_Afa"), 0, 15, -22, 0, 0));
	}
}
