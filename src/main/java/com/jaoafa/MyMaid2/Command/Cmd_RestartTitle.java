package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.connorlinfoot.titleapi.TitleAPI;
import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_RestartTitle extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0 || args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(sender instanceof Player){
			SendMessage(sender, cmd, "このコマンドはプレイヤーからは使用できません。");
			return true;
		}else if(sender instanceof BlockCommandSender){
			SendMessage(sender, cmd, "このコマンドはコマンドブロックからは使用できません。");
			return true;
		}

		String i = args[0];

		if(i.equalsIgnoreCase("reset")){
			for(Player player : Bukkit.getOnlinePlayers()){
				TitleAPI.clearTitle(player);
			}
			return true;
		}

		for(Player player : Bukkit.getOnlinePlayers()){
			TitleAPI.sendTitle(player, 0, 99999999, 0, ChatColor.RED + "----- 警告 -----", ChatColor.RESET + "サーバの再起動が" + i + "秒後に実施されます。");
		}
		return true;
	}
}
