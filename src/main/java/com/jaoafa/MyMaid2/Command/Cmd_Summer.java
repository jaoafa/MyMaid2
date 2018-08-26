package com.jaoafa.MyMaid2.Command;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Pointjao;

public class Cmd_Summer extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Summer(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(MyMaid2.econ == null){
			SendMessage(sender, cmd, "このコマンドは現在使用できません。");
			return true;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2018/08/01 00:00:00");
			Date end = format.parse("2018/08/31 23:59:59");
			if(!isPeriod(start, end)){
				SendMessage(sender, cmd, "イベントが終了したため、コマンドは使用できません。");
				return true;
			}
		} catch (ParseException e) {
			BugReporter(e);
		}
		if(args.length == 3){
			// /summer exchange [jP/jSP] jao
			if(args[0].equalsIgnoreCase("exchange")){
				// jP: jao Point(クリエイティブワールドで使用できるポイント)
				// jSP: (Summer2017ワールドで使用できるポイント)
				if(args[1].equalsIgnoreCase("jSP")){
					// jP -< jSP

					if (!(sender instanceof Player)) {
						SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
						return true;
					}
					Player player = (Player) sender;
					int i;
					try {
						i = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						SendMessage(sender, cmd, "「交換ポイント」には数値を入力してください。");
						return true;
					}
					try{
						Pointjao jaoP = new Pointjao(player);
						if(!jaoP.has(i)){
							SendMessage(sender, cmd, "指定された交換ポイントのjaoPointをあなたは持っていません。");
							return true;
						}
						jaoP.use(i, "jao Survival Pointへのポイント交換");
					}catch(NullPointerException | ClassNotFoundException | SQLException e){
						SendMessage(sender, cmd, "jaoポイントデータが取得できませんでした。");
						BugReporter(e);
						return true;
					}

					MyMaid2.econ.depositPlayer(player, i);
					SendMessage(sender, cmd, "あなたは現在jao Survival Pointを " + MyMaid2.econ.format(MyMaid2.econ.getBalance(player)) + "持っています。");
					return true;
					//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "money give " + player.getName() + " " + i);
				}else if(args[1].equalsIgnoreCase("jP")){
					// jSP -> jP
					if (!(sender instanceof Player)) {
						SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
						return true;
					}
					Player player = (Player) sender;
					int i;
					try {
						i = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						SendMessage(sender, cmd, "「交換ポイント」には数値を入力してください。");
						return true;
					}
					if(!MyMaid2.econ.has(player, i)){
						SendMessage(sender, cmd, "指定された交換ポイントのjaoSurvivalPointをあなたは持っていません。");
						return true;
					}
					try{
						Pointjao jaoP = new Pointjao(player);
						jaoP.add(i, "jao Survival Pointからのポイント交換");
					}catch(NullPointerException | ClassNotFoundException | SQLException e){
						SendMessage(sender, cmd, "jaoポイントデータが取得できませんでした。");
						BugReporter(e);
						return true;
					}
					MyMaid2.econ.withdrawPlayer(player, i);
					SendMessage(sender, cmd, "あなたは現在jao Survival Pointを " + MyMaid2.econ.format(MyMaid2.econ.getBalance(player)) + "持っています。");
					return true;
				}
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
