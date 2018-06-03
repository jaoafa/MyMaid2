package com.jaoafa.MyMaid2.Command;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Pointjao;
import com.jaoafa.MyMaid2.Task.Task_DedRain_Stop;

public class Cmd_DedRain extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_DedRain(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static boolean flag = true;
	BukkitTask task = null;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			String old = Boolean.toString(flag);
			if(args[0].equalsIgnoreCase("true")){
				if(flag){
					SendMessage(sender, cmd, "既に降水禁止設定はオフです。");
					return true;
				}
				SendMessage(sender, cmd, "降水禁止設定を" + old + "から" + "true(オン)" + "に変更しました。");
				flag = true;
				if(task != null) task.cancel();
				return true;
			}else if(args[0].equalsIgnoreCase("false")){
				if(!flag){
					SendMessage(sender, cmd, "既に降水禁止設定はオフです。");
					return true;
				}
				if(Bukkit.getOfflinePlayers().length != 1){
					if(sender instanceof Player){
						Player player = (Player) sender;
						try {
							Pointjao jaoP = new Pointjao(player);
							if(!jaoP.has(10)){
								// 所持していない
								SendMessage(sender, cmd, "降水禁止設定を変更するためのjaoポイントが足りません。");
								return true;
							}
							jaoP.use(10, "降水禁止設定をオフにしたため");
						} catch (ClassNotFoundException | NullPointerException | SQLException e) {
							BugReporter(e);
						}
						task = new Task_DedRain_Stop(plugin, player).runTaskLater(plugin, 12000L);
						SendMessage(sender, cmd, "10分後に降水禁止設定が戻されます。");
					}
				}

				SendMessage(sender, cmd, "降水禁止設定を" + old + "から" + "false(オフ)" + "に変更しました。");
				flag = false;
				return true;
			}
		}else if(args.length == 0){
			String now = Boolean.toString(flag);
			SendMessage(sender, cmd, "現在の降水禁止設定は" + now + "です。");
			SendMessage(sender, cmd, "/dedrain <true/false>で変更することができます。");
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
