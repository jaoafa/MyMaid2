package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.jaoafa.MyMaid2.MyMaid2;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Command.Cmd_Hide;

public class Event_Hide extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(!Cmd_Hide.hided.contains(player.getUniqueId())){
			return;
		}
		player.sendMessage("[Hide] " + ChatColor.RED + "あなたは現在hideモードです。発言をするには/showを実行し解除してください。");
		event.setCancelled(true);
	}
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			if(p.getUniqueId().equals(player.getUniqueId())){
				continue;
			}
			if(!Cmd_Hide.hided.contains(p.getUniqueId())){
				player.showPlayer(MyMaid2.mymaid2, p);
				continue;
			}
			player.hidePlayer(MyMaid2.mymaid2, p);

			p.sendMessage("[Hide] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」のあなたのhideモードを反映しました。");
		}

	}
}
