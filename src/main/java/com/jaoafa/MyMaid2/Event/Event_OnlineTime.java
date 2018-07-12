package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Event_OnlineTime extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_OnlineTime(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginDBInsert(PlayerJoinEvent event){
		Player player = event.getPlayer();
		int onlineTime = player.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
		if(!exists(player)){
			create(player);
		}
		change_onlineTime(player, onlineTime);
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_QuitDBInsert(PlayerQuitEvent event){
		Player player = event.getPlayer();
		int onlineTime = player.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
		if(!exists(player)){
			create(player);
		}
		change_onlineTime(player, onlineTime);
	}

	private void create(Player player){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO onlinetime (player, uuid) VALUES (?, ?);");
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
	}
	private boolean exists(Player player){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM onlinetime WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, player.getUniqueId().toString()); // uuid
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return true;
			}else{
				return false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
		return false;
	}
	private void change_onlineTime(Player player, int value){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE onlinetime SET onlinetime = ? WHERE uuid = ?;");
			statement.setInt(1, value);
			statement.setString(2, player.getUniqueId().toString());
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}
	}
}
