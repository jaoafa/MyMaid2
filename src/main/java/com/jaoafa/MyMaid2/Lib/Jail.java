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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.jaoSuperAchievement.AchievementAPI.AchievementAPI;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.AchievementType;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.Achievementjao;

public class Jail extends MyMaid2Premise {
	// 2017/10/30 Update: UUID管理に変更
	private static Set<String> Jail = new HashSet<String>();
	private static Map<String, Boolean> block = new HashMap<String, Boolean>();
	private static Map<String, Boolean> area = new HashMap<String, Boolean>();
	private static Map<String, Boolean> lasttext = new HashMap<String, Boolean>();

	public static int REQUIRED_jao = 50;

	/**
	 * Jailにプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws ClassNotFoundException
	*/
	@Deprecated
	public static boolean JailAdd(Player player, CommandSender banned_by) throws ClassNotFoundException, NullPointerException, SQLException{
		if(player == null){
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("JailAdd Player is null...!");
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
				banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはJailするためのjaoポイントが足りません。");
				return true;
			}
		}
		if(Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getUniqueId().toString());
		block.put(player.getUniqueId().toString(), false); // 設置破壊不可
		area.put(player.getUniqueId().toString(), false); // 範囲外移動
		lasttext.put(player.getUniqueId().toString(), false); // まだ遺言を残してない

		if(player.getGameMode() == GameMode.SPECTATOR) player.setGameMode(GameMode.CREATIVE);

		World Jao_Afa = Bukkit.getServer().getWorld("Jao_Afa");
		Location minami = new Location(Jao_Afa, 2856, 69, 2888);
		player.teleport(minami); // テレポート

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "なにをしてしまったのは知らないけどなにかをしたからここに来たんだと思うんだ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あと、「/testment <LastText>」で遺言を残せるよ！");

		// データベース登録なし

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を牢獄リストに追加しました。");
		DiscordSend("223582668132974594", "***Jail[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によってJailリストに追加されました。");
		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			pointjao.use(REQUIRED_jao, player.getName() + "をJailに追加したため。");
		}
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), "");
		return true;
	}

	/**
	 * Jailにプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player オフラインのプレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws ClassNotFoundException
	*/
	@Deprecated
	public static boolean JailAdd(OfflinePlayer player, CommandSender banned_by) throws ClassNotFoundException, NullPointerException, SQLException{
		if(player == null){
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("JailAdd OfflinePlayer is null...!");
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
				banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはJailするためのjaoポイントが足りません。");
				return true;
			}
		}
		if(Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getUniqueId().toString());
		block.put(player.getUniqueId().toString(), false); // 設置破壊不可
		area.put(player.getUniqueId().toString(), false); // 範囲外移動
		lasttext.put(player.getUniqueId().toString(), false); // まだ遺言を残してない

		// データベース登録なし

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を牢獄リストに追加しました。");
		DiscordSend("223582668132974594", "***Jail[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によってJailリストに追加されました。");
		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			pointjao.use(REQUIRED_jao, player.getName() + "をJailに追加したため。");
		}
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), "");
		return true;
	}

	/**
	 * Jailに理由つきでプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @param InvRemove インベントリを削除するか
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws ClassNotFoundException
	*/
	public static boolean JailAdd(Player player, CommandSender banned_by, String reason, boolean InvRemove) throws ClassNotFoundException, NullPointerException, SQLException{
		if(player == null){
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("JailAdd Player is null...!");
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
				banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはJailするためのjaoポイントが足りません。");
				return true;
			}
		}
		if(Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getUniqueId().toString());
		block.put(player.getUniqueId().toString(), false); // 設置破壊不可
		area.put(player.getUniqueId().toString(), false); // 範囲外移動
		lasttext.put(player.getUniqueId().toString(), false); // まだ遺言を残してない

		if(InvRemove){
			player.getInventory().clear();
			// player.getEnderChest().clear(); - さすがにエンダーチェスト消すのはかわいそうかなと思ってきた
		}

		if(player.getGameMode() == GameMode.SPECTATOR) player.setGameMode(GameMode.CREATIVE);

		World Jao_Afa = Bukkit.getServer().getWorld("Jao_Afa");
		Location minami = new Location(Jao_Afa, 2856, 69, 2888);
		player.teleport(minami); // テレポート

		SimpleDateFormat allsdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "話を聞けば、「" + reason + "」という理由でここにきたみたいだね。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あと、「/testment <LastText>」で遺言を残せるよ！");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jail (player, uuid, banned_by, reason, date) VALUES (?, ?, ?, ?, ?)");
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
			statement.setString(3, "jail");
			statement.setString(4, banned_by.getName());
			statement.setString(5, reason);
			statement.setString(6, allsdf.format(new Date()));
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由で牢獄リストに追加しました。");
		DiscordSend("223582668132974594", "***Jail[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でJailリストに追加されました。");

		if(!Achievementjao.getAchievement(player, new AchievementType(20))){
			player.sendMessage(AchievementAPI.getPrefix() + "実績の解除中に問題が発生しました。");
		}

		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			pointjao.use(REQUIRED_jao, player.getName() + "をJailに追加したため。(理由: " + reason + ")");
		}
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), reason);
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
	public static boolean JailAdd(OfflinePlayer player, CommandSender banned_by, String reason) throws ClassNotFoundException, NullPointerException, SQLException{
		if(player == null){
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーは見つかりません。");
			try{
				throw new java.lang.NullPointerException("JailAdd OfflinePlayer is null...!");
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
				banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはJailするためのjaoポイントが足りません。");
				return true;
			}
		}
		if(Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getUniqueId().toString());
		block.put(player.getUniqueId().toString(), false); // 設置破壊不可
		area.put(player.getUniqueId().toString(), false); // 範囲外移動
		lasttext.put(player.getUniqueId().toString(), false); // まだ遺言を残してない

		SimpleDateFormat allsdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jail (player, uuid, banned_by, reason, date) VALUES (?, ?, ?, ?, ?)");
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
			statement.setString(3, "jail");
			statement.setString(4, banned_by.getName());
			statement.setString(5, reason);
			statement.setString(6, allsdf.format(new Date()));
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由で牢獄リストに追加しました。");
		DiscordSend("223582668132974594", "***Jail[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でJailリストに追加されました。");
		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			pointjao.use(REQUIRED_jao, player.getName() + "をJailに追加したため。(理由: " + reason + ")");
		}
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), reason);
		return true;
	}

	/**
	 * Jailに理由つきでプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @param InvRemove インベントリを削除するか
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws ClassNotFoundException
	*/
	public static boolean JailAdd(Player player, OfflinePlayer banned_by, String reason, boolean InvRemove) throws ClassNotFoundException, NullPointerException, SQLException{
		if(player == null){
			try{
				throw new java.lang.NullPointerException("JailAdd Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(banned_by == null){
			try{
				throw new java.lang.NullPointerException("JailAdd OfflinePlayer(banned_by) is null...!");
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
				((Player) banned_by).sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはJailするためのjaoポイントが足りません。");
				return false;
			}
		}
		if(Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいるので無理
			return false;
		}
		Jail.add(player.getUniqueId().toString());
		block.put(player.getUniqueId().toString(), false); // 設置破壊不可
		area.put(player.getUniqueId().toString(), false); // 範囲外移動
		lasttext.put(player.getUniqueId().toString(), false); // まだ遺言を残してない

		if(InvRemove){
			player.getInventory().clear();
			// player.getEnderChest().clear(); - さすがにエンダーチェスト消すのはかわいそうかなと思ってきた
		}

		if(player.getGameMode() == GameMode.SPECTATOR) player.setGameMode(GameMode.CREATIVE);

		World Jao_Afa = Bukkit.getServer().getWorld("Jao_Afa");
		Location minami = new Location(Jao_Afa, 2856, 69, 2888);
		player.teleport(minami); // テレポート

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat allsdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "話を聞けば、「" + reason + "」という理由でここにきたみたいだね。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あと、「/testment <LastText>」で遺言を残せるよ！");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jail (player, uuid, banned_by, reason, date) VALUES (?, ?, ?, ?, ?)");
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
			statement.setString(3, "jail");
			statement.setString(4, banned_by.getName());
			statement.setString(5, reason);
			statement.setString(6, allsdf.format(new Date()));
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由で牢獄リストに追加しました。");
		DiscordSend("223582668132974594", "***Jail[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でJailリストに追加されました。");

		if(!Achievementjao.getAchievement(player, new AchievementType(20))){
			player.sendMessage(AchievementAPI.getPrefix() + "実績の解除中に問題が発生しました。");
		}

		if(banned_by instanceof Player){
			Player banned_by_player = (Player) banned_by;
			Pointjao pointjao = new Pointjao(banned_by_player);
			pointjao.use(REQUIRED_jao, player.getName() + "をJailに追加したため。(理由: " + reason + ")");
		}
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), reason);
		return true;
	}

	/**
	 * Jailにいるプレイヤーリストを送信
	 * @param sender コマンド実行者
	 * @param cmd コマンド情報
	 * @author mine_book000
	*/
	public static void SendList(CommandSender sender, Command cmd){
		SendMessage(sender, cmd, "------ 牢獄リスト ------");
		for(String player : Jail){
			UUID uuid = UUID.fromString(player);
			OfflinePlayer offplayer = Bukkit.getOfflinePlayer(uuid);
			String name;
			if(offplayer != null){
				name = offplayer.getName();
			}else{
				name = player;
			}
			String text = "";
			if(area.containsKey(player)){
				if(area.get(player)){
					text = "過度移動許可";
				}else{
					text = "過度移動不許可";
				}
			}else{
				text = "情報無し";
			}
			text += " ";
			if(block.containsKey(player)){
				if(block.get(player)){
					text += "設置破壊許可";
				}else{
					text += "設置破壊不許可";
				}
			}else{
				text += "情報無し";
			}
			SendMessage(sender, cmd, name + " " + text);
		}
		SendMessage(sender, cmd, "------------------------");
	}

	/**
	 * 遺言を残す
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param LastText 遺言
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailLastText(Command cmd, Player player, String LastText){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("JailLastText Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			SendMessage(player, cmd, "あなたはすでに牢獄にいないため遺言を残せません。");
			return false;
		}
		if(lasttext.containsKey(player.getUniqueId().toString())){
			if(lasttext.get(player.getUniqueId().toString())){
				// 既に残してる
				SendMessage(player, cmd, "あなたはすでに遺言を残しています。");
				return false;
			}
		}
		lasttext.put(player.getUniqueId().toString(), true);

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jail WHERE uuid = ? ORDER BY id DESC");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();

			if(!res.next()){
				SendMessage(player, cmd, "遺言を残せませんでした。");
			}

			PreparedStatement statement2 = MySQL.getNewPreparedStatement("UPDATE jail SET lasttext = ? WHERE id = ?");
			statement2.setString(1, LastText);
			statement2.setInt(2, res.getInt("id"));
			statement2.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」が遺言を残しました。遺言:「" + LastText +"」");
		DiscordSend("223582668132974594", "***Jail[遺言]***: プレイヤー「" + player.getName() +"」が「" + LastText + "」という遺言を残しました。");
		JailBackupSaveTxt(player.getName(), JailType.LASTTEXT, "", LastText);
		return true;
	}

	public static String getLastJailReason(Player player){
		if(!isJail(player)) return null;
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jail WHERE uuid = ? ORDER BY id DESC LIMIT 1");
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

	/**
	 * 範囲外に移動できるかどうかを設定する
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @param after 変更後の設定
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailArea(Command cmd, Player player, CommandSender banned_by, Boolean after){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("JailArea Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため設定できません。");
			return false;
		}
		area.put(player.getUniqueId().toString(), after);
		if(after){
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を範囲外に移動できるよう設定しました。");
			DiscordSend("223582668132974594", "***Jail[範囲外移動]***: プレイヤー「" + player.getName() +"」が範囲外に移動できるよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.AREATRUE, banned_by.getName(), "");
		}else{
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を範囲外に移動できないよう設定しました。");
			DiscordSend("223582668132974594", "***Jail[範囲外移動]***: プレイヤー「" + player.getName() +"」が範囲外に移動できないよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.AREAFALSE, banned_by.getName(), "");
		}
		return true;
	}

	/**
	 * ブロックを設置破壊できるかどうかを設定する
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @param after 変更後の設定
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailBlock(Command cmd, Player player, CommandSender banned_by, Boolean after){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("JailBlock Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため設定できません。");
			return false;
		}
		block.put(player.getUniqueId().toString(), after);
		if(after){
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できるよう設定しました。");
			DiscordSend("223582668132974594", "***Jail[ブロック編集]***: プレイヤー「" + player.getName() +"」がブロックを設置破壊できるよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.BLOCKTRUE, banned_by.getName(), "");
		}else{
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できるよう設定しました。");
			DiscordSend("223582668132974594", "***Jail[ブロック編集]***: プレイヤー「" + player.getName() +"」がブロックを設置破壊できるよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.BLOCKFALSE, banned_by.getName(), "");
		}
		return true;
	}

	/**
	 * Jailからプレイヤーを削除
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailRemove(Command cmd, Player player, CommandSender banned_by){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("JailRemove Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!Jail.contains(player.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため削除できません。");
			return false;
		}
		Jail.remove(player.getUniqueId().toString());
		block.remove(player.getUniqueId().toString());
		area.remove(player.getUniqueId().toString());

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "じゃあな…！");
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を牢獄リストから削除しました。");
		DiscordSend("223582668132974594", "***Jail[削除]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() + "」によってJailリストから削除されました");
		JailBackupSaveTxt(player.getName(), JailType.REMOVE, banned_by.getName(), "");
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
	public static boolean JailRemove(Command cmd, OfflinePlayer offplayer, CommandSender banned_by){
		if(offplayer == null){
			try{
				throw new java.lang.NullPointerException("JailRemove Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!Jail.contains(offplayer.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため削除できません。");
			return false;
		}
		Jail.remove(offplayer.getUniqueId().toString());
		block.remove(offplayer.getUniqueId().toString());
		area.remove(offplayer.getUniqueId().toString());

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + offplayer.getName() + "」を牢獄リストから削除しました。");
		DiscordSend("223582668132974594", "***Jail[削除]***: プレイヤー「" + offplayer.getName() +"」が「" + banned_by.getName() + "」によってJailリストから削除されました");
		JailBackupSaveTxt(offplayer.getName(), JailType.REMOVE, banned_by.getName(), "");
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
	public static boolean JailRemove(OfflinePlayer offplayer, OfflinePlayer banned_by){
		if(offplayer == null){
			try{
				throw new java.lang.NullPointerException("JailRemove OfflinePlayer(offplayer) is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(banned_by == null){
			try{
				throw new java.lang.NullPointerException("JailRemove OfflinePlayer(banned_by) is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(!Jail.contains(offplayer.getUniqueId().toString())){
			// 既に牢獄にいないので無理
			return false;
		}
		Jail.remove(offplayer.getUniqueId().toString());
		block.remove(offplayer.getUniqueId().toString());
		area.remove(offplayer.getUniqueId().toString());

		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + offplayer.getName() + "」を牢獄リストから削除しました。");
		DiscordSend("223582668132974594", "***Jail[削除]***: プレイヤー「" + offplayer.getName() +"」が「" + banned_by.getName() + "」によってJailリストから削除されました");
		JailBackupSaveTxt(offplayer.getName(), JailType.REMOVE, banned_by.getName(), "");
		return true;
	}

	/**
	 * Jailリストにいるかどうか調べる
	 * @param player プレイヤー
	 * @return 居たらtrue、居なかったらfalse
	 * @author mine_book000
	*/
	public static boolean isJail(Player player){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("isJail Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		return Jail.contains(player.getUniqueId().toString());
	}

	/**
	 * Jailリストにいるかどうか調べる
	 * @param offplayer プレイヤー
	 * @return 居たらtrue、居なかったらfalse
	 * @author mine_book000
	*/
	public static boolean isJail(OfflinePlayer offplayer){
		if(offplayer == null){
			try{
				throw new java.lang.NullPointerException("isJail OfflinePlayer(offplayer) is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		return Jail.contains(offplayer.getUniqueId().toString());
	}

	/**
	 * 南の楽園外に出れるかどうか調べる
	 * @param player プレイヤー
	 * @return 出れるならtrue、出れなければfalse
	 * @author mine_book000
	*/
	public static boolean isJailArea(Player player){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("isJailArea Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(area.containsKey(player.getUniqueId().toString())){
			if(area.get(player.getUniqueId().toString())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * ブロックを設置破壊できるかどうか調べる
	 * @param player プレイヤー
	 * @return できるならtrue、できないならfalse
	 * @author mine_book000
	*/
	public static boolean isJailBlock(Player player){
		if(player == null){
			try{
				throw new java.lang.NullPointerException("isJailBlock Player is null...!");
			}catch(java.lang.NullPointerException e){
				BugReporter(e);
			}
			return false;
		}
		if(block.containsKey(player.getUniqueId().toString())){
			if(block.get(player.getUniqueId().toString())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	/**
	 * Jail情報をセーブする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception IOExceptionの発生時に発生
	*/
	@SuppressWarnings("unchecked")
	public static boolean SaveJailData() throws Exception{
		JSONArray JailJSON = new JSONArray();
		for(String player : Jail){
			JailJSON.add(player);
		}

		JSONObject BlockJSON = new JSONObject();
		for(Entry<String, Boolean> one : block.entrySet()) {
			BlockJSON.put(one.getKey(), one.getValue());
		}

		JSONObject AreaJSON = new JSONObject();
		for(Entry<String, Boolean> one : area.entrySet()) {
			AreaJSON.put(one.getKey(), one.getValue());
		}

		JSONObject LastTextJSON = new JSONObject();
		for(Entry<String, Boolean> one : lasttext.entrySet()) {
			LastTextJSON.put(one.getKey(), one.getValue());
		}

		JSONObject ALLJSON = new JSONObject();
		ALLJSON.put("Jail", JailJSON);
		ALLJSON.put("Block", BlockJSON);
		ALLJSON.put("Area", AreaJSON);
		ALLJSON.put("LastText", LastTextJSON);

		try{
			JavaPlugin plugin = JavaPlugin();
	    	File file = new File(plugin.getDataFolder(), "jail.json");
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
	 * Jail情報をロードする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception 何かしらのExceptionが発生したときに発生(FileNotFoundException, IOException)
	*/
	@SuppressWarnings("unchecked")
	public static boolean LoadJailData() throws Exception{
		JSONParser parser = new JSONParser();
		String json = "";
		try{
			JavaPlugin plugin = JavaPlugin();
	    	File file = new File(plugin.getDataFolder(), "jail.json");
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
		JSONArray JailJSON;
		if(obj.containsKey("Jail")){
			JailJSON = (JSONArray) obj.get("Jail");
			for (int i = 0; i < JailJSON.size(); i++){
				String player = (String) JailJSON.get(i);
				Jail.add(player);
			}
		}
		if(obj.containsKey("Block")){
			for(Entry<String, Boolean> one : (Set<Map.Entry<String, Boolean>>) ((JSONObject) obj.get("Block")).entrySet()){
				block.put(one.getKey(), one.getValue());
			}
		}
		if(obj.containsKey("Area")){
			for(Entry<String, Boolean> one : (Set<Map.Entry<String, Boolean>>) ((JSONObject) obj.get("Area")).entrySet()){
				area.put(one.getKey(), one.getValue());
			}
		}
		if(obj.containsKey("LastText")){
			for(Entry<String, Boolean> one : (Set<Map.Entry<String, Boolean>>) ((JSONObject) obj.get("LastText")).entrySet()){
				lasttext.put(one.getKey(), one.getValue());
			}
		}
		return true;
	}

	public enum JailType {
		ADD("追加"),
		REMOVE("削除"),
		LASTTEXT("遺言"),
		AREATRUE("移動可"),
		AREAFALSE("移動不可"),
		BLOCKTRUE("ブロック設置破壊可"),
		BLOCKFALSE("ブロック設置破壊不可");

		private String name;
	    JailType(String name) {
	        this.name = name;
	    }
	}
	public static void JailBackupSaveTxt(String player, JailType type, String by, String reason){
		try{
			File file = new File(JavaPlugin().getDataFolder(), "jaillog.txt");

			if(file.exists()){
				file.createNewFile();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String text = "["+ sdf.format(new Date()) + "|" + type.name + "] ";
			StringBuffer TextBuf = new StringBuffer();
			TextBuf.append(text);

			if(type == JailType.ADD){
				TextBuf.append(player + "が" + by + "によって追加されました。(理由: " + reason + ")");
			}else if(type == JailType.REMOVE){
				TextBuf.append(player + "が" + by + "によって解除されました。");
			}else if(type == JailType.LASTTEXT){
				TextBuf.append(player + "が遺言を記載しました。(" + reason +")");
			}else if(type == JailType.AREATRUE){
				TextBuf.append(by + "が" + player + "の牢獄外移動を許可しました。");
			}else if(type == JailType.AREAFALSE){
				TextBuf.append(by + "が" + player + "の牢獄外移動を禁止しました。");
			}else if(type == JailType.BLOCKTRUE){
				TextBuf.append(by + "が" + player + "のブロック設置破壊を許可しました。");
			}else if(type == JailType.BLOCKFALSE){
				TextBuf.append(by + "が" + player + "のブロック設置破壊を禁止しました。");
			}

			text = TextBuf.toString();

			FileWriter filewriter = new FileWriter(file, true);

			filewriter.write(text + System.getProperty("line.separator"));

			filewriter.close();
		}catch(IOException e){
			BugReporter(e);
		}
	}
}
