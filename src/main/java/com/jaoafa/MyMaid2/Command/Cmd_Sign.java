package com.jaoafa.MyMaid2.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Sign extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Sign(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, Sign> signlist = new HashMap<String, Sign>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		if(args.length == 0){
			SendUsageMessage(sender, cmd);
			return true;
		}
		Player player = (Player) sender;

		if(!signlist.containsKey(player.getUniqueId().toString())){
			SendMessage(sender, cmd, "看板が選択されていません。");
			return true;
		}

		Sign sign = signlist.get(player.getUniqueId().toString());
		String regex = "^[1-4]$"; //正規表現
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(args[0]);
		if (!m.find()){
			SendMessage(sender, cmd, "エラーが発生しました。看板の行番号:1～4を入力してください。");
			return true;
		}

		String text = "";
		int c = 1;
		while(args.length > c){
			text += args[c];
			if(args.length != (c+1)){
				text+=" ";
			}
			c++;
		}
        sign.setLine(Integer.parseInt(args[0])-1, text);
        sign.update();
        SendMessage(sender, cmd, "書き換えを行いました。");
		return true;

	}
}
