package com.jaoafa.MyMaid2.Event;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_QD_NOTSpectator extends MyMaid2Premise implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if(event.getNewGameMode() != GameMode.SPECTATOR){
			return;
		}
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
			player.sendMessage("[GAMEMODE] " + ChatColor.GREEN + "処理に失敗しました。");
			event.setCancelled(true);
			return;
		}
	}
}
