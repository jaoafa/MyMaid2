package com.jaoafa.MyMaid2.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Task.Task_LoginjaoafaTips;

public class Event_Tips extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_Tips(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	private static Set<UUID> jaoafa_jao = new HashSet<>();
	public static Set<UUID> jaoafa = new HashSet<>();

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginjaoafaTips(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Limited") &&
			!group.equalsIgnoreCase("QPPE") &&
			!group.equalsIgnoreCase("Default")){
			return;
		}
		if(jaoafa_jao.contains(player.getUniqueId())){
			jaoafa_jao.remove(player.getUniqueId());
		}
		if(jaoafa.contains(player.getUniqueId())){
			jaoafa.remove(player.getUniqueId());
		}
		new Task_LoginjaoafaTips(plugin, player).runTaskLater(JavaPlugin(), 1200L); // 1分
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChatCheckjaoafa(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Limited") &&
			!group.equalsIgnoreCase("QPPE") &&
			!group.equalsIgnoreCase("Default")){
			return;
		}
		String message = event.getMessage();
		if(message.equalsIgnoreCase("jao")){
			jaoafa_jao.add(player.getUniqueId()); // jaoを言ったのでAdd
			jaoafa.remove(player.getUniqueId()); // jaoafaまで言ってないからRemove
		}else if(message.equalsIgnoreCase("afa")){
			if(!jaoafa_jao.contains(player.getUniqueId())){ // jao言ってなかったら無視
				return;
			}
			jaoafa.add(player.getUniqueId()); // jaoafa言ったからAdd
			jaoafa_jao.remove(player.getUniqueId()); // jaoの方を消す
		}
		// jaiuwa, rejaoafa等後日追加
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LogoutjaojaoTips(PlayerQuitEvent event){
		Player player = event.getPlayer();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Limited") &&
			!group.equalsIgnoreCase("QPPE") &&
			!group.equalsIgnoreCase("Default")){
			return;
		}
		if(jaojao.contains(player.getUniqueId())){
			return;
		}
		// 後日作成
		// ロジック的には、この時点でjaojaoを言ってなかったらファイルに書き込んで、次回ログイン時に通知する
		/*
		player.sendMessage("[Tips] " + ChatColor.GREEN + "サーバからのログアウト前には、「jaojao」と分けて発言すると当サーバにおける退出時の挨拶となります！");
		player.sendMessage("[Tips] " + ChatColor.GREEN + "次回ログアウト前にはぜひそのように発言してみてください！");
		player.sendMessage("[Tips] " + ChatColor.GREEN + "詳しくは以下記事をご覧ください。 https://jaoafa.com/community/lang/jao");
		jaojao.remove(player.getUniqueId());
		*/
	}

	private static Set<UUID> jaojao = new HashSet<>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChatCheckjaojao(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Limited") &&
			!group.equalsIgnoreCase("QPPE") &&
			!group.equalsIgnoreCase("Default")){
			return;
		}
		String message = event.getMessage();
		if(message.equalsIgnoreCase("jaojao")){
			jaojao.add(player.getUniqueId());
		}else{
			jaojao.remove(player.getUniqueId());
		}
	}
}
