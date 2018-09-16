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
import com.jaoafa.MyMaid2.Lib.ChatJail;

public class Cmd_ChatJail extends MyMaid2Premise implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Cmd_ChatJail(JavaPlugin plugin) {
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
				String playername = args[1];
				Player player = Bukkit.getPlayerExact(playername);
				if(player == null){
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
					if(offplayer == null){
						SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

						Player any_chance_player = Bukkit.getPlayer(playername);
						if(any_chance_player != null){
							SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
						}
						return true;
					}
					ChatJail.Remove(cmd, offplayer, sender);
					return true;
				}
				ChatJail.Remove(cmd, player, sender);
				return true;
			}
		}else if(args.length >= 3){
			if(args[0].equalsIgnoreCase("add")){
				String playername = args[1];
				Player player = Bukkit.getPlayerExact(playername);
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
					SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

					Player any_chance_player = Bukkit.getPlayer(playername);
					if(any_chance_player != null){
						SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
					}
					return true;
				}

				try {
					ChatJail.Add(player, sender, text);
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
				ChatJail.SendList(sender, cmd);
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
				return Arrays.asList("add", "remove", "list");
			} else {
				//入力されている文字列と先頭一致
				if ("add".startsWith(args[0])) {
					return Collections.singletonList("add");
				} else if ("remove".startsWith(args[0])) {
					return Collections.singletonList("remove");
				} else if ("list".startsWith(args[0])) {
					return Collections.singletonList("list");
				}
			}
		}
		//JavaPlugin#onTabComplete()を呼び出す
		return plugin.onTabComplete(sender, command, alias, args);
	}
}
