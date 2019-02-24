package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.Pointjao;

public class Event_JoinjaoPoint extends MyMaid2Premise implements Listener {
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
			Date start = format.parse("2019/02/14 00:00:00");
			Date end = format.parse("2019/02/14 23:59:59");
			if(isPeriod(start, end)){
				int point = 100;
				Pjao.add(point, sdf.format(new Date()) + "のログインボーナス (バレンタインイベント分)");

				Bukkit.broadcastMessage("[jaoPoint] " + ChatColor.GREEN + player.getName() + "さんがjaotanからバレンタインプレゼントを貰いました！おめでとうございます！");
				DiscordSend(player.getName() + "さんがjaotanからバレンタインプレゼントを貰いました！おめでとうございます！");

				ItemStack cookie = new ItemStack(Material.COOKIE);
				ItemMeta cookiemeta = cookie.getItemMeta();
				List<String> lore = new ArrayList<>();
				lore.add("jaotanからのバレンタインプレゼント(2019年)");
				cookiemeta.setLore(lore);
				cookie.setItemMeta(cookiemeta);
				SimpleDateFormat sdfchat = new SimpleDateFormat("HH:mm:ss");
				if(player.getInventory().firstEmpty() == -1){
					player.getLocation().getWorld().dropItem(player.getLocation(), cookie);
					player.sendMessage(ChatColor.GRAY + "["+ sdfchat.format(new Date()) + "]" + ChatColor.GOLD + "[private]" + ChatColor.RESET + "jaotan=>" + player.getName() + ": " + "ボクからのバレンタインプレゼントだよ！インベントリがいっぱいだったから、足元に置いておいたよ！拾ってね！");
				}else{
					player.getInventory().addItem(cookie);
					player.sendMessage(ChatColor.GRAY + "["+ sdfchat.format(new Date()) + "]" + ChatColor.GOLD + "[private]" + ChatColor.RESET + "jaotan=>" + player.getName() + ": " + "ボクからのバレンタインプレゼントだよ！");
				}
			}
		} catch (ParseException e) {
			BugReporter(e);
		}catch(ClassNotFoundException | SQLException e){
			BugReporter(e);
			return;
		}
	}
}
