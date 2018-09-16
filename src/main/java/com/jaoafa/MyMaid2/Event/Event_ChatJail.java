package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Lib.ChatJail;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_ChatJail implements Listener {
	JavaPlugin plugin;
	public Event_ChatJail(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginChatJailCheck(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(!ChatJail.isChatJail(player)){
			return;
		}
		String reason = ChatJail.getLastReason(player);
		if(reason == null){
			return;
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
				continue;
			}
			p.sendMessage("[EBan] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」は、「" + reason + "」という理由でChatJailされています。");
			p.sendMessage("[EBan] " + ChatColor.RED + "詳しい情報はユーザーページよりご確認ください。");
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		if(!ChatJail.isChatJail(player)){
			return;
		}
		String message = event.getMessage();
		player.sendMessage("[ChatJail] " + ChatColor.GREEN + "あなたはチャット規制をされています。解除するにはDiscord#supportなどで解除申請をしてください。");
		ChatJail.ChatJailDBMessageAdd(player, message);
		event.setCancelled(true);
	}

	@EventHandler
    public void onJoinClearCache(PlayerJoinEvent event){
		Player player = event.getPlayer();
		ChatJail.ClearCache(player);
	}
	@EventHandler
    public void onQuitClearCache(PlayerQuitEvent event){
		Player player = event.getPlayer();
		ChatJail.ClearCache(player);
	}

}
