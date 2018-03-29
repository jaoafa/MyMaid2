package com.jaoafa.MyMaid2.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.ParseSelector;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_Kill_kill extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_Kill_kill(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onEvent_Kill_kill(PlayerCommandPreprocessEvent event){
		String command = event.getMessage();
		Player player = event.getPlayer();
		String[] args = command.split(" ", 0);
		if(args.length >= 2){
			List<String> LeastOne = new ArrayList<String>();
			LeastOne.add("r");
			LeastOne.add("type");
			LeastOne.add("team");
			LeastOne.add("name");

			if(args[0].equalsIgnoreCase("/kill")){
				boolean killflag = false;
				String group = PermissionsManager.getPermissionMainGroup(player);
				if(group.equalsIgnoreCase("Limited")){
					killflag = true;
				}else if(group.equalsIgnoreCase("QPPE")){
					killflag = true;
				}else if(group.equalsIgnoreCase("Default")){
					killflag = true;
				}
				if(args[1].equalsIgnoreCase("@e")){
					// Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "コマンド「/kill @e」の実行", false);
					event.setCancelled(true);
					return;
				}
				if(killflag){
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(new Date()) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: " + player.getName() + "さんが" + args[1] + "を殺すとか調子に乗ってると思うので" + player.getName() + "さんを殺しておきますね^^");
					DiscordSend("**jaotan**: " + player.getName() + "さんが" + args[1] + "を殺すとか調子に乗ってると思うので" + player.getName() + "さんを殺しておきますね^^");
					player.setHealth(0);
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@e")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@a")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].startsWith("@e")){
					if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@e")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							List<String> unvalids = parser.getUnValidValues();
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "不適切だったセレクター引数: " + implode(unvalids, ", "));
							event.setCancelled(true);
							return;
						}
						if(!parser.getArgs().containsKey("r")){
							Boolean exist = false;
							for(String one : LeastOne){
								if(parser.getArgs().containsKey(one)){
									Bukkit.broadcastMessage(one + ": true");
									exist = true;
								}
							}
							if(!exist){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」、「type」、「team」、「name」のいずれかを指定せずに実行することはできません。");
								event.setCancelled(true);
								return;
							}
						}
						if(parser.getArgs().containsKey("r")){
							if(Integer.parseInt(parser.getArgs().get("r")) >= 300){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」に300以上の値を指定することはできません。");
								event.setCancelled(true);
								return;
							}
						}else{
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」を指定せずに実行することはできません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@a")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
			}
			if(args[0].equalsIgnoreCase("/minecraft:kill")){
				boolean killflag = false;
				String group = PermissionsManager.getPermissionMainGroup(player);
				if(group.equalsIgnoreCase("Limited")){
					killflag = true;
				}else if(group.equalsIgnoreCase("QPPE")){
					killflag = true;
				}else if(group.equalsIgnoreCase("Default")){
					killflag = true;
				}
				if(killflag){
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(new Date()) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: " + player.getName() + "ごときが" + args[1] + "を殺そうだなんて図が高いわ！ " + player.getName() + "が死ね！");
					DiscordSend("**jaotan**: " + player.getName() + "ごときが" + args[1] + "を殺そうだなんて図が高いわ！ " + player.getName() + "が死ね！");
					player.setHealth(0);
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@e")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@a")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].startsWith("@e")){
					if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
						event.setCancelled(true);
						return;
					}
				}

				if(args[1].startsWith("@e")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
						if(!parser.getArgs().containsKey("r")){
							Boolean exist = false;
							for(String one : LeastOne){
								if(parser.getArgs().containsKey(one)){
									Bukkit.broadcastMessage(one + ": true");
									exist = true;
								}
							}
							if(!exist){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」、「type」、「team」、「name」のいずれかを指定せずに実行することはできません。");
								event.setCancelled(true);
								return;
							}
						}
						if(parser.getArgs().containsKey("r")){
							if(Integer.parseInt(parser.getArgs().get("r")) >= 300){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」に300以上の値を指定することはできません。");
								event.setCancelled(true);
								return;
							}
						}else{
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」を指定せずに実行することはできません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@a")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
