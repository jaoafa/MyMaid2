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
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_BanChecker extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_BanChecker(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event){
		try{
			UUID uuid = event.getUniqueId();
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM banlist WHERE uuid = ? AND type = ? AND disabled = ?");
			statement.setString(1, uuid.toString());
			statement.setString(2, "gban");
			statement.setBoolean(3, false);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				String reason = res.getString("reason");
				String time = res.getString("time");
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
		}catch(SQLException | ClassNotFoundException e){
			BugReporter(e);
		}


	}
}
