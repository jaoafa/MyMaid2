package com.jaoafa.MyMaid2.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.connorlinfoot.titleapi.TitleAPI;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Command.Cmd_AFK;
import com.jaoafa.MyMaid2.Task.Task_AFK;

public class Event_AFK extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		Player player = event.getPlayer();

		Task_AFK.afktime.put(player.getName(), System.currentTimeMillis()); // 動いたら更新する

		if(!Cmd_AFK.getAFKing(player)){
			return;
	   	}

		Cmd_AFK.setAFK_False(player);
	}
	@EventHandler
	public void OnEvent_PlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(!Cmd_AFK.getAFKing(player)){
			return;
	   	}

		Cmd_AFK.setAFK_False(player);
	}
	@EventHandler
	public void OnEvent_PlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		TitleAPI.clearTitle(player);
	}
}
