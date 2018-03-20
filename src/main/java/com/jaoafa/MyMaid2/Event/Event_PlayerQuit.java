package com.jaoafa.MyMaid2.Event;

import java.net.InetAddress;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

import eu.theindra.geoip.api.GeoIP;

public class Event_PlayerQuit extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_PlayerQuit(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnEvent_PlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		String name = player.getName();
		UUID uuid = player.getUniqueId();
		InetAddress ia = player.getAddress().getAddress();
		String ip = ia.getHostAddress();
		String host = ia.getHostName();

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

		try {
			Statement statement = MySQL.getNewStatement();
			statement.executeUpdate("INSERT INTO logout (player, uuid, ip, host, countryName, city, permission, date) VALUES ('" + player.getName() + "', '" + uuid.toString() + "', '" + ip + "', '" + host + "', '" + countryName + "', '" + city + "', '" + permission + "', CURRENT_TIMESTAMP);");
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}

}
