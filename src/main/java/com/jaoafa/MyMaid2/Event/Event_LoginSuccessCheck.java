package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE login SET login_success = ? WHERE uuid = ? ORDER BY id DESC LIMIT 1");
			statement.setInt(1, 1);
			statement.setString(2, uuid);
			statement.executeQuery();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}
}
