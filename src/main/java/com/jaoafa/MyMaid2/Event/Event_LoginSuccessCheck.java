package com.jaoafa.MyMaid2.Event;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Event_LoginSuccessCheck extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_LoginSuccessCheck(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnEvent_LoginSuccessCheck(PlayerJoinEvent event){
		String uuid = event.getPlayer().getUniqueId().toString();
		try {
			Statement statement = MySQL.getNewStatement();
			statement.executeUpdate("UPDATE login SET login_success = '1' WHERE uuid = '" + uuid + "' ORDER BY id DESC LIMIT 1");
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}
}
