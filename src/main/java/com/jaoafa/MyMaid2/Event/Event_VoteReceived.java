package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PlayerVoteData;
import com.jaoafa.MyMaid2.Lib.Pointjao;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.AchievementType;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.Achievementjao;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class Event_VoteReceived extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onVotifierEvent(VotifierEvent event) {
		Vote vote = event.getVote();
		String name = vote.getUsername();
		VoteReceive(name);
	}


	public static void VoteReceive(String name){
		final int VOTEPOINT = 20;

		String oldVote = "取得できませんでした";
		String newVote = "取得できませんでした";

		String oldjao = "取得できませんでした";
		String newjao = "取得できませんでした";

		/* ------------- 投票イベント関連開始 ------------- */

		/*
		String plusargs = "";
		// ハロウィンイベント Issue #18
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2017/10/15 09:00:00");
			Date end = format.parse("2017/11/01 08:59:59");
			if(isPeriod(start, end)){
				plusargs = "&pluscount=2"; // 2倍
			}
		} catch (ParseException e) {
			BugReporter(e);
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2017/07/01 00:00:00");
			Date end = format.parse("2017/07/14 23:59:59");
			if(Method.isPeriod(start, end)){
				VOTECOUNT += 20;
				Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイントポイント追加しました。");
    			DiscordSend(player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイント追加しました。");
			}
		} catch (ParseException e) {
			BugReporter(e);
		}
		 */

		/* ------------- 投票イベント関連終了 ------------- */

		UUID uuid = null;
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM login WHERE player = ? ORDER BY id DESC");
			statement.setString(1, name);

			ResultSet res = statement.executeQuery();
			if(res.next()){
				uuid = UUID.fromString(res.getString("uuid"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			DiscordSend("499922840871632896", ":outbox_tray:**投票受信エラー**: " + name + "のプレイヤーデータをデータベースから取得している最中にClassNotFoundExceptionもしくはSQLExceptionが発生したため、投票処理が正常に行われませんでした。");
			BugReporter(e);
			return;
		}

		if(uuid == null){
			DiscordSend("499922840871632896", ":outbox_tray:**投票受信エラー**: " + name + "のプレイヤーデータがデータベースから取得できなかったため、投票処理が正常に行われませんでした。");
			return;
		}

		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(uuid);

		if(offplayer == null){
			DiscordSend("499922840871632896", ":outbox_tray:**投票受信エラー**: " + name + "のOfflinePlayerを取得できなかったため、投票処理が正常に行われませんでした。");
			return;
		}

		if(!offplayer.getName().equals(name)){
			name += "(" + offplayer.getName() + ")";
		}

		boolean first = PlayerVoteData.TodayFirstVote();

		String i;
		try{
			PlayerVoteData pvd = new PlayerVoteData(offplayer);
			oldVote = String.valueOf(pvd.get());

			pvd.add();

			newVote = String.valueOf(pvd.get());
			i = String.valueOf(pvd.get());
		}catch(ClassNotFoundException | SQLException e){
			DiscordSend("499922840871632896", ":outbox_tray:**投票受信エラー**: " + name + "の投票処理時にClassNotFoundExceptionもしくはSQLExceptionが発生したため、投票処理(投票数追加)が正常に行われませんでした。");
			BugReporter(e);
			return;
		}catch(NullPointerException e){
			DiscordSend("499922840871632896", ":outbox_tray:**投票受信エラー**: " + name + "のプレイヤー投票データが取得できなかったため、投票処理(投票数追加)が正常に行われませんでした。");
			return;
		}

		try{
			Pointjao pointjao = new Pointjao(offplayer);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			oldjao = "" + pointjao.get();
			pointjao.add(VOTEPOINT, sdf.format(new Date()) + "の投票ボーナス");
			newjao = "" + pointjao.get();


			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date start = format.parse("2018/08/12 00:00:00");
				Date end = format.parse("2018/08/31 23:59:59");
				if(isPeriod(start, end)){
					Random rnd = new Random();
	    			int random = rnd.nextInt(40)+11;
					pointjao.add(random, sdf.format(new Date()) + "の投票ボーナス (サバイバルイベント分)");

					Bukkit.broadcastMessage("[jaoPoint] " + ChatColor.GREEN + name + "さんがサバイバルイベント投票ボーナスによってjaoポイントを追加で" + random + "ポイント追加しました。");
	    			DiscordSend(name + "さんがサバイバルイベント投票ボーナスによってjaoポイントを追加で" + random + "ポイント追加しました。");
				}
			} catch (ParseException e) {
				BugReporter(e);
			}

		}catch(NullPointerException e){
			DiscordSend("499922840871632896", ":outbox_tray:**投票受信エラー**: " + name + "のプレイヤーデータが取得できなかったため、投票処理(ポイント追加)が正常に行われませんでした。");
			return;
		}catch(ClassNotFoundException | SQLException e){
			DiscordSend("499922840871632896", ":outbox_tray:**投票受信エラー**: " + name + "の投票処理時にClassNotFoundExceptionもしくはSQLExceptionが発生したため、投票処理(投票数追加)が正常に行われませんでした。");
			BugReporter(e);
			return;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "投票をよろしくお願いします！ https://jaoafa.com/vote");
		DiscordSend("プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
		DiscordSend("投票をよろしくお願いします！ https://jaoafa.com/vote");
		DiscordSend("499922840871632896", ":inbox_tray:**投票を受信しました。(" + format.format(new Date()) + ")**\n"
				+ "プレイヤー: `"  + name + "`\n"
				+ "投票前カウント: " + oldVote + "\n"
				+ "投票後カウント: " + newVote + "\n"
				+ "投票前jaoポイント: " + oldjao + "\n"
				+ "投票後jaoポイント: " + newjao);

		if(first){ // 初めての投票だったら、実績獲得 (No.21 / 筆頭株主)
			if(offplayer != null){
				Achievementjao.getAchievement(offplayer, new AchievementType(21));
			}
		}

		/*
        	try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date start = format.parse("2017/07/01 00:00:00");
				Date end = format.parse("2017/07/14 23:59:59");
				if(Method.isPeriod(start, end)){
					Pointjao.addjao(player, 10, "七夕投票イベント");
					Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイントポイント追加しました。");
	    			DiscordSend(player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイント追加しました。");
				}
			} catch (ParseException e) {
				BugReporter(e);
			}
		 */

		//SimpleDateFormat date = new SimpleDateFormat("yyyy-MM");
		/*
    		if(date.format(Date).equalsIgnoreCase("2017-02")){
    			Random rnd = new Random();
    			int random = rnd.nextInt(50)+1;

    			Pointjao.addjao(player, random, sdf.format(Date) + "の投票ボーナス(2月ポイント補填ボーナス)");
    			Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが投票し、2月ポイント補填ボーナスを" + random + "ポイント追加しました。");
    			DiscordSend(player.getName() + "さんが投票し、2月ポイント補填ボーナスを" + random + "ポイント追加しました。");
    		}
		 */
	}
}
