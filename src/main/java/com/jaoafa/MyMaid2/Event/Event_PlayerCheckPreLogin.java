package com.jaoafa.MyMaid2.Event;

import java.net.InetAddress;
import java.sql.SQLException;
import java.sql.Statement;
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

import eu.theindra.geoip.api.GeoIP;

public class Event_PlayerCheckPreLogin extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_PlayerCheckPreLogin(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnEvent_PlayerCheckPreLogin(AsyncPlayerPreLoginEvent event){
		String name = event.getName();
		UUID uuid = event.getUniqueId();
		InetAddress ia = event.getAddress();
		String ip = event.getAddress().getHostAddress();
		String host = event.getAddress().getHostName();

		if(ia.isAnyLocalAddress() || ia.isLoopbackAddress()){
			// localhost
			return;
		}
		GeoIP geoip = new GeoIP(ia);
		String countryName = geoip.countryName;
		String city = geoip.city;

		String permission = null;
		try{
			permission = PermissionsManager.getPermissionMainGroup(name);
		}catch(Exception e){
			permission = "Limited";
		}

		if(name.equalsIgnoreCase("jaotan")){
			// jaotanというIDは許可しない
			disallow(event,
					ChatColor.WHITE + "あなたのMinecraftIDは、システムの運用上の問題によりログイン不可能と判断されました。\n"
							+ ChatColor.RESET + ChatColor.AQUA + "ログインするには、MinecraftIDを変更してください。",
					"UserName");
			return;
		}

		if(!countryName.equalsIgnoreCase("Japan")){
			// 日本国外からのアクセスをすべて規制
			disallow(event,
					ChatColor.WHITE + "海外からのログインと判定されました。\n"
							+ ChatColor.RESET + ChatColor.AQUA + "当サーバでは、日本国外からのログインを禁止しています。",
					"Region Error",
					countryName + " " + city);
			return;
		}

		/*Bukkit.broadcastMessage(event.getName() + ": " + ip + " (" + host + ") - " + permission);
		Bukkit.broadcastMessage(event.getName() + ": " + countryName + " " + city);*/

		try {
			Statement statement = MySQL.getNewStatement();
			statement.executeUpdate("INSERT INTO login (player, uuid, ip, host, countryName, city, permission, date) VALUES ('" + name + "', '" + uuid.toString() + "', '" + ip + "', '" + host + "', '" + countryName + "', '" + city + "', '" + permission + "', CURRENT_TIMESTAMP);");
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}

	private void disallow(AsyncPlayerPreLoginEvent event, String reason, String message){
		event.disallow(Result.KICK_FULL,
				ChatColor.RED + "[Login Denied! - Reason: " + reason + "]\n"
						+ ChatColor.RESET + message
						+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、サイト内お問い合わせからお問い合わせを行ってください。\n"
						+ "公式Discordでお問い合わせをして頂いても構いません。");
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator") || group.equalsIgnoreCase("Regular")) {
				p.sendMessage("[MyMaid2-LoginChecker] " + ChatColor.GREEN + event.getName() + "==>" + reason);
			}
		}
		DiscordSend("223582668132974594", "[MyMaid2-LoginChecker] " + event.getName() + "==>" + reason);
	}
	private void disallow(AsyncPlayerPreLoginEvent event, String reason, String message, String data){
		event.disallow(Result.KICK_FULL,
				ChatColor.RED + "[Login Denied! - Reason: " + reason + "]\n"
						+ ChatColor.RESET + message
						+ ChatColor.RESET + ChatColor.WHITE + "もしこの判定が誤判定と思われる場合は、サイト内お問い合わせからお問い合わせを行ってください。\n"
						+ "公式Discordでお問い合わせをして頂いても構いません。");
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator") || group.equalsIgnoreCase("Regular")) {
				p.sendMessage("[MyMaid2-LoginChecker] " + ChatColor.GREEN + event.getName() + "==>" + reason + " (" + data + ")");
			}
		}
		DiscordSend("223582668132974594", "[MyMaid2-LoginChecker] " + event.getName() + "==>" + reason + " (" + data + ")");
	}
}
