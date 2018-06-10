package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.EBan;
import com.jaoafa.MyMaid2.Lib.Jail;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.mcbans.firestar.mcbans.events.PlayerBannedEvent;

public class Event_BanLogger extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_BanLogger(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerBanned(PlayerBannedEvent event){
		String playername = event.getPlayerName();
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
		if(offplayer == null){
			BugReporter(new NullPointerException("offplayerがnullです。"));
			return;
		}
		String sender = event.getSenderName();
		String reason = event.getReason();

		if(event.isGlobalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + playername + "」がプレイヤー「" + sender +"」によってGBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			DiscordSend("223582668132974594", "プレイヤー「" + playername + "」がプレイヤー「" + sender +"」によってGBanされました。(理由: " + reason + ")");
			DiscordSend("**" + playername + " GLOBAL BANNED.**");

			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO banlist (player, uuid, type, bannedby, reason) VALUES (?, ?, ?, ?, ?)");
				statement.setString(1, playername);
				statement.setString(2, offplayer.getUniqueId().toString());
				statement.setString(3, "gban");
				statement.setString(4, sender);
				statement.setString(5, reason);
			}catch(SQLException | ClassNotFoundException e){
				BugReporter(e);
			}

			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(group.equalsIgnoreCase("Admin")) {
					p.sendMessage("[BANDATA] " + ChatColor.GREEN + "サーバの評価値を上げるため、MCBansに証拠画像を提供してください！ http://mcbans.com/server/jaoafa.com");
				}
			}

			OfflinePlayer jaotan = Bukkit.getOfflinePlayer("jaotan");
			if(Jail.isJail(offplayer)){
				Jail.JailRemove(offplayer, jaotan);
			}
			if(EBan.isEBan(offplayer)){
				EBan.Remove(offplayer);
			}
		}else if(event.isLocalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + playername + "」がプレイヤー「" + sender +"」によってLBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			DiscordSend("223582668132974594", "プレイヤー「" + playername + "」がプレイヤー「" + sender +"」によってLBanされました。(理由: " + reason + ")");
			DiscordSend("**" + playername + " LOCAL BANNED.**");

			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO banlist (player, uuid, type, bannedby, reason) VALUES (?, ?, ?, ?, ?)");
				statement.setString(1, playername);
				statement.setString(2, offplayer.getUniqueId().toString());
				statement.setString(3, "lban");
				statement.setString(4, sender);
				statement.setString(5, reason);
			}catch(SQLException | ClassNotFoundException e){
				BugReporter(e);
			}

			OfflinePlayer jaotan = Bukkit.getOfflinePlayer("jaotan");
			if(Jail.isJail(offplayer)){
				Jail.JailRemove(offplayer, jaotan);
			}
			if(EBan.isEBan(offplayer)){
				EBan.Remove(offplayer);
			}
		}else if(event.isTempBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + playername + "」がプレイヤー「" + sender +"」によってTBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			DiscordSend("223582668132974594", "プレイヤー「" + playername + "」がプレイヤー「" + sender +"」によってTBanされました。(理由: " + reason + ")");
			DiscordSend("**" + playername + " TEMP BANNED.**");

			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO banlist (player, uuid, type, bannedby, reason) VALUES (?, ?, ?, ?, ?)");
				statement.setString(1, playername);
				statement.setString(2, offplayer.getUniqueId().toString());
				statement.setString(3, "tban");
				statement.setString(4, sender);
				statement.setString(5, reason);
			}catch(SQLException | ClassNotFoundException e){
				BugReporter(e);
			}
		}
	}
}
