package com.jaoafa.MyMaid2.Task;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.titleapi.TitleAPI;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PlayerVoteData;
import com.jaoafa.MyMaid2.Lib.TPSChecker;

public class TPSChange extends BukkitRunnable {
	double OldTps1mColor = 20;
	@Override
	public void run() {
		String tps1m = TPSChecker.getTPS1m();
		String tps5m = TPSChecker.getTPS5m();
		String tps15m = TPSChecker.getTPS15m();

		ChatColor tps1mcolor;
		try{
			double tps1m_double = Double.parseDouble(tps1m);
			tps1mcolor = TPSChecker.TPSColor(tps1m_double);

			if(tps1m_double <= 11 && OldTps1mColor > 11){
				// やばいぞ
				Bukkit.broadcastMessage("[TPSChecker] " + ChatColor.RED + "ちょっと待って！TPSがかなり下がっています！(" + tps1m + " / 20)");
				Bukkit.broadcastMessage("[TPSChecker] " + ChatColor.RED + "意図的なサーバへの負荷をかける行為は「サーバルール」の「サーバシステムに危害を加えない。」に違反すると判断される場合があります。あなたの行動が負荷になっていないかぜひ確認してみてください。");
				MyMaid2Premise.DiscordSend("[TPSChecker] ちょっと待って！TPSがかなり下がっています！(" + tps1m + " / 20)");
			}
			OldTps1mColor = tps1m_double;
		}catch(NumberFormatException e){
			tps1mcolor = ChatColor.GREEN;
		}

		ChatColor tps5mcolor;
		try{
			double tps5m_double = Double.parseDouble(tps5m);
			tps5mcolor = TPSChecker.TPSColor(tps5m_double);
		}catch(NumberFormatException e){
			tps5mcolor = ChatColor.GREEN;
		}

		ChatColor tps15mcolor;
		try{
			double tps15m_double = Double.parseDouble(tps15m);
			tps15mcolor = TPSChecker.TPSColor(tps15m_double);
		}catch(NumberFormatException e){
			tps15mcolor = ChatColor.GREEN;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		//String tpsmsg = "[\"\",{\"text\":\"TPS\n\",\"color\":\"gold\"},{\"text\":\"1m: \",\"color\":\"red\"},{\"text\":\"" + tps1m + "\",\"color\":\"" + tps1mcolor + "\"},{\"text\":\"\n\",\"color\":\"\"},{\"text\":\"5m: \",\"color\":\"yellow\"},{\"text\":\"" + tps5m + "\",\"color\":\"" + tps5mcolor + "\"},{\"text\":\"\n\",\"color\":\"none\"},{\"text\":\"15m: \",\"color\":\"green\"},{\"text\":\"" + tps15m + "\",\"color\":\"" + tps15mcolor + "\"}]";
		String tpsmsg = ChatColor.GOLD + "TPS\n" + ChatColor.RED + "1m: " + tps1mcolor + tps1m + "\n" + ChatColor.YELLOW + "5m: " + tps5mcolor +  tps5m + "\n" + ChatColor.GREEN + "15m: " +  tps15mcolor + tps15m;
		String header = ChatColor.GOLD +  "jao " + ChatColor.YELLOW + "Minecraft " + ChatColor.AQUA + "Server\n"
				+ "Date: " + sdf.format(new Date()) + "\n"
				+ ChatColor.RED + "Online: " + Bukkit.getServer().getOnlinePlayers().size() + " / " + Bukkit.getServer().getMaxPlayers() + "\n"
				+ "Vote: " + "{VOTECOUNT}" + "\n"
				+ "OnlineTime: " + "{ONLINETIME}";

		for(Player player: Bukkit.getServer().getOnlinePlayers()) {
			PlayerVoteData pvd = new PlayerVoteData(player);
			String votecount = "取得に失敗しました。";
			try {
				votecount = pvd.get() + "回";
			} catch (ClassNotFoundException | UnsupportedOperationException | NullPointerException | SQLException e) {
				return;
			}
			int onlineTime = player.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
			String time = getStringTime(onlineTime);
			String _header = header.replaceAll("\\{VOTECOUNT\\}", votecount);
			_header = _header.replaceAll("\\{ONLINETIME\\}", time);
			TitleAPI.sendTabTitle(player, _header, tpsmsg);
		}
	}
	public static String getStringTime(int sec){
		if(sec < 60) {
			return sec + "秒";
		}
		int min = sec / 60;
		int s = 60 * min;
		int secLeft = sec - s;
		if(min < 60) {
			if (secLeft > 0) {
				return String.valueOf(min + "分" + secLeft + "秒");
			}
			return String.valueOf(min + "分");
		}
		if(min < 1440) {
			String time = "";
			int hours = min / 60;
			time = hours + "時間";
			int inMins = 60 * hours;
			int leftOver = min - inMins;
			if (leftOver >= 1) {
				time = time + "" + leftOver + "分";
			}
			if (secLeft > 0) {
				time = time + "" + secLeft + "秒";
			}
			return time;
		}
		String time = "";
		int days = min / 1440;
		time = days + "日";
		int inMins = 1440 * days;
		int leftOver = min - inMins;
		if(leftOver >= 1) {
			if (leftOver < 60) {
				time = time + "" + leftOver + "分";
			}else{
				int hours = leftOver / 60;
				time = time + "" + hours + "時間";
				int hoursInMins = 60 * hours;
				int minsLeft = leftOver - hoursInMins;
				if(leftOver >= 1) {
					time = time + "" + minsLeft + "分";
				}
			}
		}
		if(secLeft > 0) {
			time = time + "" + secLeft + "秒";
		}
		return time;
	}
}
