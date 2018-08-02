package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_GLookup extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_GLookup(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 1){
			Player player = Bukkit.getPlayer(args[0]);
			if(player == null){
				SendMessage(sender, cmd, "指定されたプレイヤーは見つかりませんでした。");
				return true;
			}
			SendMessage(sender, cmd, player.getName() + " GameMode: " + player.getGameMode().name());
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
