package com.jaoafa.MyMaid2.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Report extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Report(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0 || (args.length >= 1 && args[0].equalsIgnoreCase("help"))){
			SendUsageMessage(sender, cmd);
			return true;
		}
		String message = "<@&189381504059572224>\n__**[REPORT]**__ ``" + sender.getName() + "`` reported --> ```";
		int c = 1;
		while(args.length > c){
			message += args[c];
			if(args.length != (c+1)){
				message += " ";
			}else{
				message += "```";
			}
			c++;
		}

		boolean sendflag = DiscordSend("223582668132974594", message);
		if(sendflag){
			SendMessage(sender, cmd, "送信が完了しました。");
		}else{
			SendMessage(sender, cmd, "送信に失敗しました。");
		}
		return true;
	}
}
