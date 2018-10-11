package com.jaoafa.MyMaid2.Lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class EBan extends MyMaid2Premise {
	/*
	 * punishing: 処罰続行中
	 * end: 処罰終了(解放済み)
	 */
	//private static Set<String> EBan = new HashSet<String>();
	/**
	 * プレイヤーを理由つきでEBanする
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @return 実行できたかどうか
	 * @author mine_book000
	 */
	public static boolean Add(Player player, CommandSender banned_by, String reason){
		if(player == null){
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("EBanAdd Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}

		if(isEBan(player)){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされているため実行できません。");
			return false;
		}
		//EBan.add(player.getUniqueId().toString());

		if(player.getGameMode() == GameMode.SPECTATOR) player.setGameMode(GameMode.CREATIVE);

		World Jao_Afa = Bukkit.getServer().getWorld("Jao_Afa");
		Location minami = new Location(Jao_Afa, 2856, 69, 2888);
		player.teleport(minami); // テレポート

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String date = format.format(new Date());

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO eban (player, uuid, banned_by, reason, status, date) VALUES (?, ?, ?, ?, ?, ?);");
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, banned_by.getName());
			statement.setString(4, reason);
			statement.setString(5, "punishing");
			statement.setString(6, date);
			statement.executeUpdate();

			PreparedStatement statement2 = MySQL.getNewPreparedStatement("INSERT INTO banlist (player, uuid, type, bannedby, reason, time) VALUES (?, ?, ?, ?, ?, ?);");
			statement2.setString(1, player.getName());
			statement2.setString(2, player.getUniqueId().toString());
			statement2.setString(3, "eban");
			statement2.setString(4, banned_by.getName());
			statement2.setString(5, reason);
			statement2.setString(6, date);
			statement2.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[EBan] " + ChatColor.RED + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由でEBanされました。");
		DiscordSend("223582668132974594", "__**EBan[追加]**__: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でEBanされました。");
		player.sendMessage("[EBan] " + ChatColor.RED + "解除申請の方法や、Banの方針などは以下ページをご覧ください。");
		player.sendMessage("[EBan] " + ChatColor.RED + "https://jaoafa.com/rule/management/ban");
		EBanCache.put(player.getUniqueId(), true);
		return true;
	}

	/**
	 * プレイヤーを理由つきでEBanする
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @return 実行できたかどうか
	 * @author mine_book000
	 */
	public static boolean Add(OfflinePlayer player, CommandSender banned_by, String reason){
		if(player == null){
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("EBanAdd Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}

		if(isEBan(player)){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされているため実行できません。");
			return false;
		}
		//EBan.add(player.getUniqueId().toString());

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String date = format.format(new Date());

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO eban (player, uuid, banned_by, reason, status, date) VALUES (?, ?, ?, ?, ?, ?);");
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, banned_by.getName());
			statement.setString(4, reason);
			statement.setString(5, "punishing");
			statement.setString(6, date);
			statement.executeUpdate();

			PreparedStatement statement2 = MySQL.getNewPreparedStatement("INSERT INTO banlist (player, uuid, type, bannedby, reason, time) VALUES (?, ?, ?, ?, ?, ?);");
			statement2.setString(1, player.getName());
			statement2.setString(2, player.getUniqueId().toString());
			statement2.setString(3, "eban");
			statement2.setString(4, banned_by.getName());
			statement2.setString(5, reason);
			statement2.setString(6, date);
			statement2.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[EBan] " + ChatColor.RED + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由でEBanしました。");
		DiscordSend("223582668132974594", "__**EBan[追加]**__: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でEBanしました。");
		if(player.isOnline()){
			((Player) player).sendMessage("[EBan] " + ChatColor.RED + "解除申請の方法や、Banの方針などは以下ページをご覧ください。");
			((Player) player).sendMessage("[EBan] " + ChatColor.RED + "https://jaoafa.com/rule/management/ban");
		}

		EBanCache.put(player.getUniqueId(), true);
		return true;
	}

	/**
	 * プレイヤーのEBanを解除
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	 */
	public static boolean Remove(Player player, CommandSender banned_by){
		if(player == null){
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("EBanAdd Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!isEBan(player)){
			// 既に牢獄にいないので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされていないため実行できません。");
			return false;
		}
		//EBan.remove(player.getUniqueId().toString());

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("UPDATE eban SET status = ? WHERE id = ?;");
				statement2.setString(1, "end");
				statement2.setInt(2, res.getInt("id"));
				statement2.executeUpdate();
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[EBan] " + ChatColor.RED + "プレイヤー:「" + player.getName() + "」のEBanを解除しました。");
		DiscordSend("223582668132974594", "__**EBan[解除]**__: プレイヤー「" + player.getName() +"」のEBanを「" + banned_by.getName() +"」によって解除されました。");
		EBanCache.put(player.getUniqueId(), false);
		return true;
	}

	/**
	 * プレイヤーのEBanを解除
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	 */
	public static boolean Remove(OfflinePlayer player, CommandSender banned_by){
		if(player == null){
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("EBanAdd Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!isEBan(player)){
			// 既に牢獄にいないので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされていないため実行できません。");
			return false;
		}
		//EBan.remove(player.getUniqueId().toString());

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("UPDATE eban SET status = ? WHERE id = ?;");
				statement2.setString(1, "end");
				statement2.setInt(2, res.getInt("id"));
				statement2.executeUpdate();
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[EBan] " + ChatColor.RED + "プレイヤー:「" + player.getName() + "」のEBanを解除しました。");
		DiscordSend("223582668132974594", "__**EBan[解除]**__: プレイヤー「" + player.getName() +"」のEBanを「" + banned_by.getName() +"」によって解除されました。");
		EBanCache.put(player.getUniqueId(), false);
		return true;
	}

	/**
	 * プレイヤーのEBanをjaotanが解除
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	 */
	public static boolean Remove(OfflinePlayer player){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("EBanAdd Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!isEBan(player)){
			// 既に牢獄にいないので無理
			return false;
		}
		//EBan.remove(player.getUniqueId().toString());

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("UPDATE eban SET status = ? WHERE id = ?;");
				statement2.setString(1, "end");
				statement2.setInt(2, res.getInt("id"));
				statement2.executeUpdate();
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[EBan] " + ChatColor.RED + "プレイヤー:「" + player.getName() + "」のEBanを解除しました。");
		DiscordSend("223582668132974594", "__**EBan[解除]**__: プレイヤー「" + player.getName() +"」のEBanを「jaotan」によって解除されました。");
		EBanCache.put(player.getUniqueId(), false);
		return true;
	}

	/**
	 * EBanステータスを表示
	 * @param sender コマンド実行者
	 * @author mine_book000
	 */
	public static void Status(CommandSender sender){
		sender.sendMessage("[EBan] " + ChatColor.RED + "----- EBan Status -----");
		//int ebancount = EBan.size();
		int ebancount = Count();
		sender.sendMessage("[EBan] " + ChatColor.RED + "現在、" + ebancount + "人のプレイヤーがEBanされています。");
		if(ebancount != 0) sender.sendMessage("[EBan] " + ChatColor.RED + implode(EBanList(), ", "));
	}

	/**
	 * プレイヤーのEBanステータスを表示
	 * @param player プレイヤー
	 * @param sender コマンド実行者
	 * @author mine_book000
	 */
	public static void Status(Player player, CommandSender sender){
		if(isEBan(player)){
			sender.sendMessage("[EBan] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」は現在EBanされています。");
			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC");
				statement.setString(1, player.getUniqueId().toString());
				ResultSet res = statement.executeQuery();
				if(res.next()){
					sender.sendMessage("[EBan] " + ChatColor.RED + "Banned_By: " + res.getString("banned_by"));
					sender.sendMessage("[EBan] " + ChatColor.RED + "Reason: " + res.getString("reason"));
					sender.sendMessage("[EBan] " + ChatColor.RED + "Date: " + res.getString("date"));
				}else{
					sender.sendMessage("[EBan] " + ChatColor.RED + "データの取得に失敗しました。");
				}
			} catch (SQLException | ClassNotFoundException e) {
				BugReporter(e);
			}

		}else{
			sender.sendMessage("[EBan] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」は現在EBanされていません。");
		}
	}

	public static int Count(){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE status = ?");
			statement.setString(1, "punishing");
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getInt(1);
			}else{
				return 0;
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
			return 0;
		}
	}

	public static Set<String> EBanList(){
		Set<String> EBanList = new HashSet<String>();
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE status = ?");
			statement.setString(1, "punishing");
			ResultSet res = statement.executeQuery();
			while(res.next()){
				EBanList.add(res.getString("player"));
			}
			return EBanList;
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
			return EBanList;
		}
	}

	public static String getLastEBanReason(Player player){
		if(!isEBan(player)) return null;
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC LIMIT 1");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return res.getString("reason");
			}else{
				return null;
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
			return null;
		}
	}

	static Map<UUID, Boolean> EBanCache = new HashMap<>();
	/**
	 * プレイヤーがEBanされているかどうか調べる
	 * @param player 調べるプレイヤー
	 * @return EBanされていればtrue, されていなければfalse
	 */
	public static boolean isEBan(Player player){
		if(EBanCache.containsKey(player.getUniqueId()) && EBanCache.get(player.getUniqueId()) != null){
			return EBanCache.get(player.getUniqueId());
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC LIMIT 1");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				if(res.getString("status").equalsIgnoreCase("punishing")){
					EBanCache.put(player.getUniqueId(), true);
					return true;
				}else{
					EBanCache.put(player.getUniqueId(), false);
					return false;
				}
			}else{
				return false;
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
			return false;
		}
		//return EBan.contains(player.getUniqueId().toString());
	}

	/**
	 * オフラインプレイヤーがEBanされているかどうか調べる
	 * @param player 調べるオフラインプレイヤー
	 * @return EBanされていればtrue, されていなければfalse
	 */
	public static boolean isEBan(OfflinePlayer player){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC LIMIT 1");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				if(res.getString("status").equalsIgnoreCase("punishing")){
					EBanCache.put(player.getUniqueId(), true);
					return true;
				}else{
					EBanCache.put(player.getUniqueId(), false);
					return false;
				}
			}else{
				return false;
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
			return false;
		}
	}

	/**
	 * プレイヤーのEBanステータスを表示
	 * @param player オフラインプレイヤー
	 * @param sender コマンド実行者
	 * @author mine_book000
	 */
	public static void Status(OfflinePlayer player, CommandSender sender){
		if(isEBan(player)){
			sender.sendMessage("[EBan] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」は現在EBanされています。");
			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM eban WHERE uuid = ? ORDER BY id DESC");
				statement.setString(1, player.getUniqueId().toString());
				ResultSet res = statement.executeQuery();
				if(res.next()){
					sender.sendMessage("[EBan] " + ChatColor.RED + "Banned_By: " + res.getString("banned_by"));
					sender.sendMessage("[EBan] " + ChatColor.RED + "Reason: " + res.getString("reason"));
					sender.sendMessage("[EBan] " + ChatColor.RED + "Date: " + res.getString("date"));
				}else{
					sender.sendMessage("[EBan] " + ChatColor.RED + "データの取得に失敗しました。");
				}
			} catch (SQLException | ClassNotFoundException e) {
				BugReporter(e);
			}

		}else{
			sender.sendMessage("[EBan] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」は現在EBanされていません。");
		}
	}
	public static void ClearCache(Player player){
		EBanCache.put(player.getUniqueId(), null);
	}

	public static <T> String implode(Set<T> list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (T e : list) {
			sb.append(glue).append(e);
		}
		return sb.substring(glue.length());
	}
}
