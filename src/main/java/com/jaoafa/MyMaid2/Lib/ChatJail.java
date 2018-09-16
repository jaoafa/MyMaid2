package com.jaoafa.MyMaid2.Lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class ChatJail extends MyMaid2Premise {
	// 2017/10/30 Update: UUID管理に変更
	//private static Set<String> ChatJail = new HashSet<String>();

	public static int REQUIRED_jao = 50;

	/**
	 * Jailに理由つきでプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws ClassNotFoundException
	*/
	public static boolean Add(Player player, CommandSender banned_by, String reason) throws ClassNotFoundException, NullPointerException, SQLException{
		if(player == null){
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("ChatJail.Add Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			if(!pointjao.has(REQUIRED_jao)){
				// 所持していない
				banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはChatJailするためのjaoポイントが足りません。");
				return true;
			}
		}
		if(isChatJail(player)){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでにChatJailをされているため追加できません。");
			return false;
		}

		SimpleDateFormat allsdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO chatjail (player, uuid, banned_by, reason, status, date) VALUES (?, ?, ?, ?, ?, ?);");
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, banned_by.getName());
			statement.setString(4, reason);
			statement.setString(5, "punishing");
			statement.setString(6, allsdf.format(new Date()));
			statement.executeUpdate();

			PreparedStatement statement2 = MySQL.getNewPreparedStatement("INSERT INTO banlist (player, uuid, type, bannedby, reason, time) VALUES (?, ?, ?, ?, ?, ?);");
			statement2.setString(1, player.getName());
			statement2.setString(2, player.getUniqueId().toString());
			statement2.setString(3, "chatjail");
			statement2.setString(4, banned_by.getName());
			statement2.setString(5, reason);
			statement2.setString(6, allsdf.format(new Date()));
			statement2.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}


		Bukkit.broadcastMessage("[CHATJAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由でチャット規制リストに追加しました。");
		DiscordSend("223582668132974594", "***Jail[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でチャット規制リストに追加されました。");

		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			pointjao.use(REQUIRED_jao, player.getName() + "をChatJailに追加したため。(理由: " + reason + ")");
		}
		return true;
	}

	/**
	 * Jailに理由つきでプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player オフラインのプレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws ClassNotFoundException
	*/
	/*
	public static boolean Add(OfflinePlayer player, CommandSender banned_by, String reason) throws ClassNotFoundException, NullPointerException, SQLException{
		if(player == null){
			banned_by.sendMessage("[CHATJAIL] " + ChatColor.GREEN + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("ChatJail.Add OfflinePlayer is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			if(!pointjao.has(REQUIRED_jao)){
				// 所持していない
				banned_by.sendMessage("[CHATJAIL] " + ChatColor.GREEN + "あなたはChatJailするためのjaoポイントが足りません。");
				return true;
			}
		}
		if(ChatJail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[CHATJAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでにChatJailをされているため追加できません。");
			return false;
		}
		ChatJail.add(player.getUniqueId().toString());

		SimpleDateFormat allsdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO chatjail (player, uuid, banned_by, reason, date) VALUES (?, ?, ?, ?, ?)");
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, banned_by.getName());
			statement.setString(4, reason);
			statement.setString(5, allsdf.format(new Date()));
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO banlist (player, uuid, type, bannedby, reason, time) VALUES (?, ?, ?, ?, ?, ?)");
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, "chatjail");
			statement.setString(4, banned_by.getName());
			statement.setString(5, reason);
			statement.setString(6, allsdf.format(new Date()));
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由で牢獄リストに追加しました。");
		DiscordSend("223582668132974594", "***Jail[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でチャット規制リストに追加されました。");
		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			pointjao.use(REQUIRED_jao, player.getName() + "をJailに追加したため。(理由: " + reason + ")");
		}
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), reason);
		return true;
	}
	*/

	/**
	 * Jailにいるプレイヤーリストを送信
	 * @param sender コマンド実行者
	 * @param cmd コマンド情報
	 * @author mine_book000
	*/
	public static void SendList(CommandSender sender, Command cmd){
		SendMessage(sender, cmd, "------ チャット規制リスト ------");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM chatjail WHERE status = ?");
			statement.setString(1, "punishing");
			ResultSet res = statement.executeQuery();

			while(res.next()){
				SendMessage(sender, cmd, res.getString("player") + " " + res.getString("reason") + " " + res.getString("date"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
		SendMessage(sender, cmd, "------------------------");
	}

	/**
	 * Jailからプレイヤーを削除
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean Remove(Command cmd, Player player, CommandSender banned_by){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("ChatJail.Remove Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!isChatJail(player)){
			// 既に牢獄にいないので無理
			SendMessage(banned_by, cmd, "指定されたプレイヤーはすでにChatJailをされているため削除できません。");
			return false;
		}

		if(player.getName().equalsIgnoreCase(banned_by.getName())){
			SendMessage(banned_by, cmd, "チャット規制を自分で解除することはできません。");
			return false;
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM chatjail WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("UPDATE chatjail SET status = ? WHERE id = ?;");
				statement2.setString(1, "end");
				statement2.setInt(2, res.getInt("id"));
				statement2.executeUpdate();
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[ChatJAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」をチャット規制リストから削除しました。");
		DiscordSend("223582668132974594", "***ChatJail[削除]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() + "」によってチャット規制リストから削除されました");
		return true;
	}

	/**
	 * Jailからプレイヤーを削除
	 * @param cmd コマンド情報
	 * @param offplayer プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean Remove(Command cmd, OfflinePlayer offplayer, CommandSender banned_by){
		if(offplayer == null){
			try{
				throw new java.lang.NullPointerException("ChatJail.Remove Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!isChatJail(offplayer)){
			// 既に牢獄にいないので無理
			SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため削除できません。");
			return false;
		}
		if(offplayer.getName().equalsIgnoreCase(banned_by.getName())){
			SendMessage(banned_by, cmd, "チャット規制を自分で解除することはできません。");
			return false;
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM chatjail WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, offplayer.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("UPDATE chatjail SET status = ? WHERE id = ?;");
				statement2.setString(1, "end");
				statement2.setInt(2, res.getInt("id"));
				statement2.executeUpdate();
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[CHATJAIL] " + ChatColor.GREEN + "プレイヤー:「" + offplayer.getName() + "」をチャット規制リストから削除しました。");
		DiscordSend("223582668132974594", "***ChatJail[削除]***: プレイヤー「" + offplayer.getName() +"」が「" + banned_by.getName() + "」によってチャット規制リストから削除されました");
		return true;
	}

	/**
	 * Jailからプレイヤーを削除
	 * @param cmd コマンド情報
	 * @param offplayer プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean Remove(OfflinePlayer offplayer, OfflinePlayer banned_by){
		if(offplayer == null){
			try{
				throw new java.lang.NullPointerException("ChatJail.Remove OfflinePlayer(offplayer) is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(banned_by == null){
			try{
				throw new java.lang.NullPointerException("ChatJail.Remove OfflinePlayer(banned_by) is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!isChatJail(offplayer)){
			// 既に牢獄にいないので無理
			return false;
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM chatjail WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, offplayer.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("UPDATE chatjail SET status = ? WHERE id = ?;");
				statement2.setString(1, "end");
				statement2.setInt(2, res.getInt("id"));
				statement2.executeUpdate();
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + offplayer.getName() + "」をチャット規制リストから削除しました。");
		DiscordSend("223582668132974594", "***ChatJail[削除]***: プレイヤー「" + offplayer.getName() +"」が「" + banned_by.getName() + "」によってチャット規制リストから削除されました");
		return true;
	}

	public static String getLastReason(Player player){
		if(!isChatJail(player)) return null;
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM chatjail WHERE uuid = ? ORDER BY id DESC LIMIT 1");
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

	static Map<UUID, Boolean> ChatJailCache = new HashMap<>();

	/**
	 * チャット規制リストにいるかどうか調べる
	 * @param player プレイヤー
	 * @return 居たらtrue、居なかったらfalse
	 * @author mine_book000
	*/
	public static boolean isChatJail(Player player){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("isChatJail Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM chatjail WHERE uuid = ? ORDER BY id DESC LIMIT 1");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				if(res.getString("status").equalsIgnoreCase("punishing")){
					ChatJailCache.put(player.getUniqueId(), true);
					return true;
				}else{
					ChatJailCache.put(player.getUniqueId(), false);
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
	 * チャット規制リストにいるかどうか調べる
	 * @param offplayer プレイヤー
	 * @return 居たらtrue、居なかったらfalse
	 * @author mine_book000
	*/
	public static boolean isChatJail(OfflinePlayer offplayer){
		if(offplayer == null){
			try{
				throw new java.lang.NullPointerException("isChatJail Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM chatjail WHERE uuid = ? ORDER BY id DESC LIMIT 1");
			statement.setString(1, offplayer.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				if(res.getString("status").equalsIgnoreCase("punishing")){
					ChatJailCache.put(offplayer.getUniqueId(), true);
					return true;
				}else{
					ChatJailCache.put(offplayer.getUniqueId(), false);
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


	public static void ClearCache(Player player){
		ChatJailCache.put(player.getUniqueId(), null);
	}

	public static void ChatJailDBMessageAdd(Player player, String message){
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO chatjailmsg (player, uuid, message) VALUES (?, ?, ?);");
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, message);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}
}
