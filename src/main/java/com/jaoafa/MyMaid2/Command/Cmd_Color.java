package com.jaoafa.MyMaid2.Command;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PlayerVoteData;

public class Cmd_Color extends MyMaid2Premise implements CommandExecutor {
	public static Map<String,ChatColor> color = new HashMap<>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("remove")){
				Cmd_Color.color.remove(player.getUniqueId().toString());
				SendMessage(sender, cmd, "四角色をリセットしました。");
				return true;
			}
			PlayerVoteData pvd = new PlayerVoteData(player);
			int i;
			try{
				i = pvd.get();
			}catch(ClassNotFoundException | SQLException e){
				BugReporter(e);
				SendMessage(sender, cmd, "操作に失敗しました。");
				SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
			if(i < 200){
	  			SendMessage(sender, cmd, "投票数が200回を超えていないため、四角色を変更する権限がありません。");
				return true;
	  		}
			ChatColor color;
			if(args[0].equalsIgnoreCase("AQUA")){
				color = ChatColor.AQUA;
			}else if(args[0].equalsIgnoreCase("BLACK")){
				color = ChatColor.BLACK;
			}else if(args[0].equalsIgnoreCase("BLUE")){
				color = ChatColor.BLUE;
			}else if(args[0].equalsIgnoreCase("DARK_AQUA")){
				color = ChatColor.DARK_AQUA;
			}else if(args[0].equalsIgnoreCase("DARK_BLUE")){
				color = ChatColor.DARK_BLUE;
			}else if(args[0].equalsIgnoreCase("DARK_GRAY")){
				color = ChatColor.DARK_GRAY;
			}else if(args[0].equalsIgnoreCase("DARK_GREEN")){
				color = ChatColor.DARK_GREEN;
			}else if(args[0].equalsIgnoreCase("DARK_PURPLE")){
				color = ChatColor.DARK_PURPLE;
			}else if(args[0].equalsIgnoreCase("DARK_RED")){
				color = ChatColor.DARK_RED;
			}else if(args[0].equalsIgnoreCase("GOLD")){
				color = ChatColor.GOLD;
			}else if(args[0].equalsIgnoreCase("GREEN")){
				color = ChatColor.GREEN;
			}else if(args[0].equalsIgnoreCase("LIGHT_PURPLE")){
				color = ChatColor.LIGHT_PURPLE;
			}else if(args[0].equalsIgnoreCase("RED")){
				color = ChatColor.RED;
			}else if(args[0].equalsIgnoreCase("WHITE")){
				color = ChatColor.WHITE;
			}else if(args[0].equalsIgnoreCase("YELLOW")){
				color = ChatColor.YELLOW;
			}else if(args[0].equalsIgnoreCase("GRAY")){
				color = ChatColor.GRAY;
			}else{
				SendMessage(sender, cmd, "指定された色は選択できませんでした。");
				return true;
			}
			Cmd_Color.color.put(player.getUniqueId().toString(), color);
			player.setPlayerListName(Cmd_Color.color.get(player.getUniqueId().toString()) + "■" + ChatColor.WHITE + player.getName());
			SendMessage(sender, cmd, "四角色を「" + color + "■" + ChatColor.GREEN + "」に変更しました。");
			return true;
		}
		PlayerVoteData pvd = new PlayerVoteData(player);
		int i;
		try{
			i = pvd.get();
		}catch(ClassNotFoundException | SQLException e){
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}
		if(i < 200){
  			SendMessage(sender, cmd, "投票数が200回を超えていないため、四角色を閲覧する権限がありません。");
			return true;
  		}
		if(color.containsKey(player.getUniqueId().toString()) && color.get(player.getUniqueId().toString()) != null){
			SendMessage(sender, cmd, "「" + color.get(player.getUniqueId().toString()) + "■" + ChatColor.GREEN + " (" + color.get(player.getUniqueId().toString()).name() +")」に指定されています。");
			return true;
		}else{
			SendMessage(sender, cmd, "デフォルト色のLIGHT_PURPLEに指定されています。");
			return true;
		}
	}
}
