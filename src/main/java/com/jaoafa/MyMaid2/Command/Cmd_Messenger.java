package com.jaoafa.MyMaid2.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Messenger;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_Messenger extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Messenger(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("broadcast")){
				Messenger.RandomBroadcastMessage();
				return true;
			}else if(args[0].equalsIgnoreCase("list")){
				SendMessage(sender, cmd, "----- Message List -----");
				int i = 0;
				for(String message : Messenger.getMessages()) {
					SendMessage(sender, cmd, "[" + i + "] " + message);
					i++;
				}
				return true;
			}
		}else if(args.length >= 2){
			if(args[0].equalsIgnoreCase("add")){
				if (sender instanceof Player) {
					Player p = (Player) sender;
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
						SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
						return true;
					}
				}else if(!(sender instanceof ConsoleCommandSender)){
					SendMessage(sender, cmd, "このコマンドはサーバ内もしくはコンソールから実行可能です。");
					return true;
				}

				String message = "";
				int c = 1;
				while(args.length > c){
					message += args[c];
					if(args.length != (c+1)){
						message += " ";
					}
					c++;
				}

				boolean result = Messenger.Add(message);
				if(result){
					SendMessage(sender, cmd, "メッセージ「" + message + "」の追加処理に成功しました。");
				}else{
					SendMessage(sender, cmd, "メッセージ「" + message + "」の追加処理に失敗しました。");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("setspeaker")){
				if (sender instanceof Player) {
					Player p = (Player) sender;
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
						SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
						return true;
					}
				}else if(!(sender instanceof ConsoleCommandSender)){
					SendMessage(sender, cmd, "このコマンドはサーバ内もしくはコンソールから実行可能です。");
					return true;
				}

				String speaker = "";
				int c = 1;
				while(args.length > c){
					speaker += args[c];
					if(args.length != (c+1)){
						speaker += " ";
					}
					c++;
				}

				Messenger.setSpeaker(speaker);
				SendMessage(sender, cmd, "スピーカーを「" + speaker + "」に変更しました。");
				return true;
			}else if(args[0].equalsIgnoreCase("del")){
				if (sender instanceof Player) {
					Player p = (Player) sender;
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
						SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
						return true;
					}
				}else if(!(sender instanceof ConsoleCommandSender)){
					SendMessage(sender, cmd, "このコマンドはサーバ内もしくはコンソールから実行可能です。");
					return true;
				}

				int i;
				try{
					i = Integer.parseInt(args[1]);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "数字を指定してください！");
					return true;
				}
				if(!Messenger.Contains(i)){
					SendMessage(sender, cmd, "指定されたメッセージID「" + args[1] + "」は存在しません。");
				}
				String message = Messenger.Get(i);
				boolean result = Messenger.Del(i);
				if(result){
					SendMessage(sender, cmd, "メッセージ「" + message + "」の削除に成功しました。");
				}else{
					SendMessage(sender, cmd, "メッセージ「" + message + "」の削除に失敗しました。");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("broadcast")){
				int i;
				try{
					i = Integer.parseInt(args[1]);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "数字を指定してください！");
					return true;
				}
				Messenger.BroadcastMessage(i);
				return true;
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
