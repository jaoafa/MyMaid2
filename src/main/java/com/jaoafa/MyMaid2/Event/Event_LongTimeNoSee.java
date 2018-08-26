package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Event_LongTimeNoSee extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_LongTimeNoSee(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnEvent_LongTimeNoSee(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT unix_timestamp(date) as ts FROM login WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, uuid);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				String last_str = res.getString("ts");
				long last = new Long(last_str);
				long now = System.currentTimeMillis() / 1000L;
				long sa = now - last;
				Bukkit.getLogger().info("[LongTimeNoSee] " + player.getName() + ": " + sa + "s (LAST: " + last + " / NOW: " + now + ")");
				Bukkit.getLogger().info("[LongTimeNoSee] " + player.getName() + ": last_str: " + last_str);
				if(sa >= 2592000L){
					StringBuilder builder = new StringBuilder();

					int year = (int) (sa / 31536000L);
					int year_remain = (int) (sa % 31536000L);
					if(year != 0){
						builder.append(year + "年");
					}
					int month = (int) (year_remain / 2592000L);
					int month_remain = (int) (year_remain % 2592000L);
					if(month != 0){
						builder.append(month + "か月");
					}
					int day = (int) (month_remain / 86400L);
					int day_remain = (int) (month_remain % 86400L);
					if(day != 0){
						builder.append(day + "日");
					}
					int hour = (int) (day_remain / 3600L);
					int hour_remain = (int) (day_remain % 3600L);
					if(hour != 0){
						builder.append(hour + "時間");
					}
					int minute = (int) (hour_remain / 60L);
					if(minute != 0){
						builder.append(minute + "分");
					}
					int sec = (int) (hour_remain % 60L);
					if(sec != 0){
						builder.append(sec + "秒");
					}

					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan" +  ": " + player + "さん、お久しぶりです！" + builder.toString() + "ぶりですね！");
					DiscordSend("**jaotan**: " + player + "さん、お久しぶりです！" + builder.toString() + "ぶりですね！");
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}
}
