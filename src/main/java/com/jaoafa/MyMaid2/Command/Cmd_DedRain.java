package com.jaoafa.MyMaid2.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_DedRain extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_DedRain(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static boolean flag = true;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			String old = Boolean.toString(flag);
			if(args[0].equalsIgnoreCase("true")){
				SendMessage(sender, cmd, "降水禁止設定を" + old + "から" + "true" + "に変更しました。");
				flag = true;
				return true;
			}else if(args[0].equalsIgnoreCase("false")){
				SendMessage(sender, cmd, "降水禁止設定を" + old + "から" + "false" + "に変更しました。");
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
