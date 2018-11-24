package com.jaoafa.MyMaid2.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Jail;

public class Cmd_Testment extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length >= 1){
			if(!(sender instanceof Player)){
				SendMessage(sender, cmd, "サーバ内で実行してください。");
				return true;
			}
			Player player = (Player) sender;
			// 遺言
			String lasttext = "";
			int c = 0;
			while(args.length > c){
				lasttext += args[c];
				if(args.length != (c+1)){
					lasttext += " ";
				}
				c++;
			}
			Jail.JailLastText(cmd, player, lasttext);
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}

}
