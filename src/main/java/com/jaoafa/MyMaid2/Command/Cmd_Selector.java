package com.jaoafa.MyMaid2.Command;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.ParseSelector;

public class Cmd_Selector extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Selector(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length != 1){
			SendUsageMessage(sender, cmd);
			return true;
		}
		try{
			ParseSelector parser = new ParseSelector(args[0]);
			if(parser.isValidValues()){
				SendMessage(sender, cmd, "指定されたセレクターは適切です。");
				SendMessage(sender, cmd, "セレクター: " + parser.getSelector());
				SendMessage(sender, cmd, "引数: ");
				for(Entry<String, String> one : parser.getArgs().entrySet()){
					String key = one.getKey();
					String value = one.getValue();
					SendMessage(sender, cmd, key + " = " + value);
				}
				return true;
			}else{
				SendMessage(sender, cmd, "指定されたセレクターは適切ではありません。");
				List<String> unvalids = parser.getUnValidValues();
				SendMessage(sender, cmd, "不適切だったセレクター引数: " + implode(unvalids, ", "));
				return true;
			}
		}catch(IllegalArgumentException e){
			SendMessage(sender, cmd, "指定されたセレクターは適切でありません");
			SendMessage(sender, cmd, "理由: " + e.getMessage());
			return true;
		}
	}
}
