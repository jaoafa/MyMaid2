package com.jaoafa.MyMaid2.Command;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Jail;

public class Cmd_Jail extends MyMaid2Premise implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Cmd_Jail(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		if(args.length == 2){
			if(args[0].equalsIgnoreCase("remove")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
					if(offplayer == null){
						SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
						return true;
					}
					Jail.JailRemove(cmd, offplayer, sender);
					return true;
				}
				Jail.JailRemove(cmd, player, sender);
				return true;
			}
		}else if(args.length >= 3){
			if(args[0].equalsIgnoreCase("area")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
					return true;
				}
				boolean after = Boolean.valueOf(args[2]);
				Jail.JailArea(cmd, player, sender, after);
				return true;
			}else if(args[0].equalsIgnoreCase("block")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
					return true;
				}
				boolean after = Boolean.valueOf(args[2]);
				Jail.JailBlock(cmd, player, sender, after);
				return true;
			}else if(args[0].equalsIgnoreCase("add")){
				Player player = Bukkit.getPlayer(args[1]);
				String text = "";
				int c = 2;
				while(args.length > c){
					text += args[c];
					if(args.length != (c+1)){
						text += " ";
					}
					c++;
				}
				if(player == null){
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
					if(offplayer == null){
						SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
						return true;
					}
					try {
						Jail.JailAdd(offplayer, sender, text);
					} catch (ClassNotFoundException | NullPointerException | SQLException e) {
						BugReporter(e);
						SendMessage(sender, cmd, "操作に失敗しました。");
						SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
						SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
						return true;
					}
					return true;
				}

				try {
					Jail.JailAdd(player, sender, text, false);
				} catch (ClassNotFoundException | NullPointerException | SQLException e) {
					BugReporter(e);
					SendMessage(sender, cmd, "操作に失敗しました。");
					SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return true;
				}
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				Jail.SendList(sender, cmd);
				return true;
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			if (args[0].length() == 0) {
				return Arrays.asList("add", "remove", "list", "area", "block", "lasttext");
			} else {
				//入力されている文字列と先頭一致
				if ("add".startsWith(args[0])) {
					return Collections.singletonList("add");
				} else if ("remove".startsWith(args[0])) {
					return Collections.singletonList("remove");
				} else if ("list".startsWith(args[0])) {
					return Collections.singletonList("list");
				} else if ("area".startsWith(args[0])) {
					return Collections.singletonList("area");
				} else if ("block".startsWith(args[0])) {
					return Collections.singletonList("block");
				} else if ("lasttext".startsWith(args[0])) {
					return Collections.singletonList("lasttext");
				}
			}
		}
		//JavaPlugin#onTabComplete()を呼び出す
		return plugin.onTabComplete(sender, command, alias, args);
	}

}