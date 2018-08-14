package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.Pointjao;

public class Event_JoinjaoPoint extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_JoinjaoPoint(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnEvent_JoinjaoPoint(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		SimpleDateFormat date_full = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_full.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		String today = date_full.format(cal.getTime());

		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM login WHERE uuid = ? AND date >= cast(? as datetime) AND login_success = ?");
			statement.setString(1, uuid);
			statement.setString(2, today);
			statement.setBoolean(3, true);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return;
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		try {
			Pointjao Pjao = new Pointjao(player);
			Pjao.add(10, date.format(new Date()) + "のログインボーナス");
		}catch(NullPointerException e){
			BugReporter(e);
			return;
		}catch(ClassNotFoundException | SQLException e){
			BugReporter(e);
			return;
		}

		try {
			Pointjao Pjao = new Pointjao(player);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2018/08/12 00:00:00");
			Date end = format.parse("2018/08/31 23:59:59");
			if(isPeriod(start, end)){
				Random rnd = new Random();
				int random = rnd.nextInt(20)+11;
				Pjao.add(random, sdf.format(new Date()) + "のログインボーナス (サバイバルイベント分)");

				Bukkit.broadcastMessage("[jaoPoint] " + ChatColor.GREEN + player.getName() + "さんがサバイバルイベントログインボーナスによってjaoポイントを追加で" + random + "ポイント追加しました。");
				DiscordSend(player.getName() + "さんがサバイバルイベントログインボーナスによってjaoポイントを追加で" + random + "ポイント追加しました。");
			}
		} catch (ParseException e) {
			BugReporter(e);
		}catch(ClassNotFoundException | SQLException e){
			BugReporter(e);
			return;
		}

	}
}
