package com.jaoafa.MyMaid2.Lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class EBan extends MyMaid2Premise {
	/*
	 * punishing: 処罰続行中
	 * end: 処罰終了(解放済み)
	 */
	private static Set<String> EBan = new HashSet<String>();
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

		if(EBan.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされているため実行できません。");
			return false;
		}
		EBan.add(player.getUniqueId().toString());

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

		Bukkit.broadcastMessage("[EBan] " + ChatColor.RED + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由でEBanされました。。");
		DiscordSend("223582668132974594", "__**EBan[追加]**__: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でEBanされました。");
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

		if(EBan.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされているため実行できません。");
			return false;
		}
		EBan.add(player.getUniqueId().toString());

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
		if(!EBan.contains(player.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされていないため実行できません。");
			return false;
		}
		EBan.remove(player.getUniqueId().toString());

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
		if(!EBan.contains(player.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			banned_by.sendMessage("[EBan] " + ChatColor.RED + "指定されたプレイヤーはすでにEBanされていないため実行できません。");
			return false;
		}
		EBan.remove(player.getUniqueId().toString());

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
		if(!EBan.contains(player.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			return false;
		}
		EBan.remove(player.getUniqueId().toString());

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
		return true;
	}

	/**
	 * EBanステータスを表示
	 * @param sender コマンド実行者
	 * @author mine_book000
	 */
	public static void Status(CommandSender sender){
		sender.sendMessage("[EBan] " + ChatColor.RED + "----- EBan Status -----");
		int ebancount = EBan.size();
		sender.sendMessage("[EBan] " + ChatColor.RED + "現在、" + ebancount + "人のプレイヤーがEBanされています。");
		if(ebancount != 0) sender.sendMessage("[EBan] " + ChatColor.RED + implode(EBan, ", "));
	}

	/**
	 * プレイヤーのEBanステータスを表示
	 * @param player プレイヤー
	 * @param sender コマンド実行者
	 * @author mine_book000
	 */
	public static void Status(Player player, CommandSender sender){
		if(EBan.contains(player.getUniqueId().toString())){
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

	/**
	 * プレイヤーがEBanされているかどうか調べる
	 * @param player 調べるプレイヤー
	 * @return EBanされていればtrue, されていなければfalse
	 */
	public static boolean isEBan(Player player){
		return EBan.contains(player.getUniqueId().toString());
	}

	/**
	 * オフラインプレイヤーがEBanされているかどうか調べる
	 * @param player 調べるオフラインプレイヤー
	 * @return EBanされていればtrue, されていなければfalse
	 */
	public static boolean isEBan(OfflinePlayer player){
		return EBan.contains(player.getUniqueId().toString());
	}

	/**
	 * プレイヤーのEBanステータスを表示
	 * @param player オフラインプレイヤー
	 * @param sender コマンド実行者
	 * @author mine_book000
	 */
	public static void Status(OfflinePlayer player, CommandSender sender){
		if(EBan.contains(player.getUniqueId().toString())){
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

	public static <T> String implode(Set<T> list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (T e : list) {
			sb.append(glue).append(e);
		}
		return sb.substring(glue.length());
	}

	/**
	 * EBan情報をセーブする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception IOExceptionの発生時に発生
	 */
	@SuppressWarnings("unchecked")
	public static boolean SaveEBanData() throws Exception{
		JSONArray EBanJSON = new JSONArray();
		for(String player : EBan){
			EBanJSON.add(player);
		}
		JSONObject ALLJSON = new JSONObject();
		ALLJSON.put("EBan", EBanJSON);

		try{
			JavaPlugin plugin = JavaPlugin();
			File file = new File(plugin.getDataFolder(), "EBan.json");
			FileWriter filewriter = new FileWriter(file);

			filewriter.write(ALLJSON.toJSONString());

			filewriter.close();
		}catch(IOException e){
			BugReporter(e);
			throw new Exception("IOException");
		}
		return true;
	}

	/**
	 * EBan情報をロードする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception 何かしらのExceptionが発生したときに発生(FileNotFoundException, IOException)
	 */
	public static boolean LoadEBanData() throws Exception{
		JSONParser parser = new JSONParser();
		String json = "";
		try{
			JavaPlugin plugin = JavaPlugin();
			File file = new File(plugin.getDataFolder(), "EBan.json");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String separator = System.getProperty("line.separator");

			String str;
			while((str = br.readLine()) != null){
				json += str + separator;
			}
			br.close();
		}catch(FileNotFoundException e1){
			BugReporter(e1);
			throw new FileNotFoundException(e1.getMessage());
		}catch(IOException e1){
			BugReporter(e1);
			throw new IOException(e1.getMessage());
		}
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(json);
		} catch (ParseException e1) {
			obj = new JSONObject();
		}
		JSONArray EBanJSON;
		if(obj.containsKey("EBan")){
			EBanJSON = (JSONArray) obj.get("EBan");
			for (int i = 0; i < EBanJSON.size(); i++){
				String player = (String) EBanJSON.get(i);
				EBan.add(player);
			}
		}
		return true;
	}
}
