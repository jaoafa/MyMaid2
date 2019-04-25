package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_BanChecker extends MyMaid2Premise implements Listener {
	@EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event){
		try{
			UUID uuid = event.getUniqueId();
			PreparedStatement statement_gban = MySQL.getNewPreparedStatement("SELECT * FROM banlist WHERE uuid = ? AND type = ? AND disabled = ?");
			statement_gban.setString(1, uuid.toString());
			statement_gban.setString(2, "gban");
			statement_gban.setBoolean(3, false);
			ResultSet res_gban = statement_gban.executeQuery();
			if(res_gban.next()){
				String reason = res_gban.getString("reason");
				String time = res_gban.getString("time");
				event.disallow(Result.KICK_FULL,
						ChatColor.RED + "[Login Denied! - Reason: " + "GLOBAL BAN" + "]\n"
								+ ChatColor.RESET + "あなたは、" + time + "に「" + reason + "」という理由にてGLOBAL BANされました。\n"
								+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、公式Discordからお問い合わせを行ってください。\n"
								+ "サイト内お問い合わせでお問い合わせをして頂いても構いません。");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator") || group.equalsIgnoreCase("Regular")) {
						p.sendMessage("[MyMaid2-LoginChecker] " + ChatColor.GREEN + event.getName() + "==>[" + "GLOBAL BAN" + "] " + ChatColor.stripColor(reason) + " (" + time + ")");
					}
				}
				DiscordSend("223582668132974594", "[MyMaid2-LoginChecker] " + event.getName() + "==>[" + "GLOBAL BAN" + "] " + ChatColor.stripColor(reason) + " (" + time + ")");
			}

			PreparedStatement statement_lban = MySQL.getNewPreparedStatement("SELECT * FROM banlist WHERE uuid = ? AND type = ? AND disabled = ?");
			statement_lban.setString(1, uuid.toString());
			statement_lban.setString(2, "lban");
			statement_lban.setBoolean(3, false);
			ResultSet res_lban = statement_lban.executeQuery();
			if(res_lban.next()){
				String reason = res_lban.getString("reason");
				String time = res_lban.getString("time");
				event.disallow(Result.KICK_FULL,
						ChatColor.RED + "[Login Denied! - Reason: " + "LOCAL BAN" + "]\n"
								+ ChatColor.RESET + "あなたは、" + time + "に「" + reason + "」という理由にてLOCAL BANされました。\n"
								+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、公式Discordからお問い合わせを行ってください。\n"
								+ "サイト内お問い合わせでお問い合わせをして頂いても構いません。");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator") || group.equalsIgnoreCase("Regular")) {
						p.sendMessage("[MyMaid2-LoginChecker] " + ChatColor.GREEN + event.getName() + "==>[" + "LOCAL BAN" + "] " + ChatColor.stripColor(reason) + " (" + time + ")");
					}
				}
				DiscordSend("223582668132974594", "[MyMaid2-LoginChecker] " + event.getName() + "==>[" + "LOCAL BAN" + "] " + ChatColor.stripColor(reason) + " (" + time + ")");
			}
		}catch(SQLException | ClassNotFoundException e){
			BugReporter(e);
		}


	}
}
