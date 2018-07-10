package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.EBan;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_EBan extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_EBan(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		/*
		 * commander: コマンド実行者
		 * player: 問題プレイヤー(EBanされるプレイヤー)
		 *
		 * /eban add <Player> <Reason>
		 * /eban remove <Player>
		 * /eban status <Player>
		 */
		if(sender instanceof Player){
			// プレイヤー
			Player commander = (Player) sender;
			// Moderator, Admin以外も可
			if(args.length == 1 && args[0].equalsIgnoreCase("status")){ // statusだけ見られる
				EBan.Status(sender);
				return true;
			}else if(args.length == 2 && args[0].equalsIgnoreCase("status")){ // statusだけ見られる
				String playername = args[1];
				Player player = Bukkit.getPlayerExact(playername);
				if(player != null){
					// プレイヤーがオンライン
					EBan.Status(player, sender);
					return true;
				}else{
					// プレイヤーがオフライン
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
					if(offplayer == null){
						SendMessage(sender, cmd, ChatColor.RED + "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

						Player any_chance_player = Bukkit.getPlayer(playername);
						if(any_chance_player != null){
							SendMessage(sender, cmd, ChatColor.RED + "もしかして: " + any_chance_player.getName());
						}
						return true;
					}
					EBan.Status(offplayer, sender);
				}
				return true;
			}
			String group = PermissionsManager.getPermissionMainGroup(commander);
			if(group.equalsIgnoreCase("Moderator") || group.equalsIgnoreCase("Admin")){
				// Moderator, Admin
				if(args.length == 2 && args[0].equalsIgnoreCase("remove")){
					// /eban remove mine_book000
					String playername = args[1];
					Player player = Bukkit.getPlayerExact(playername);
					if(player != null){
						// プレイヤーがオンライン
						if(EBan.Remove(player, sender)){
							SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
						}else{
							SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
						}
					}else{
						// プレイヤーがオフライン
						OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
						if(offplayer == null){
							SendMessage(sender, cmd, ChatColor.RED + "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

							Player any_chance_player = Bukkit.getPlayer(playername);
							if(any_chance_player != null){
								SendMessage(sender, cmd, ChatColor.RED + "もしかして: " + any_chance_player.getName());
							}
							return true;
						}
						if(EBan.Remove(offplayer, sender)){
							SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
						}else{
							SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
						}
					}
					return true;
				}else if(args.length >= 3 && args[0].equalsIgnoreCase("add")){
					// /eban add mine_book000 test eban reason message
					String reason = "";
					int c = 2;
					while(args.length > c){
						reason += args[c];
						if(args.length != (c+1)){
							reason += " ";
						}
						c++;
					}
					String playername = args[1];
					Player player = Bukkit.getPlayerExact(playername);
					if(player != null){
						// プレイヤーがオンライン
						if(EBan.Add(player, sender, reason)){
							SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
						}else{
							SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
						}
					}else{
						// プレイヤーがオフライン
						OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
						if(offplayer == null){
							SendMessage(sender, cmd, ChatColor.RED + "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

							Player any_chance_player = Bukkit.getPlayer(playername);
							if(any_chance_player != null){
								SendMessage(sender, cmd, ChatColor.RED + "もしかして: " + any_chance_player.getName());
							}
							return true;
						}
						if(EBan.Add(offplayer, sender, reason)){
							SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
						}else{
							SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
						}
					}
					return true;
				}
			}else{
				SendMessage(sender, cmd, ChatColor.RED + "このコマンドは、あなたの権限では使用できません。");
				return true;
			}
		}else if(sender instanceof ConsoleCommandSender){
			// コンソール
			if(args.length == 1 && args[0].equalsIgnoreCase("status")){ // statusだけ見られる
				EBan.Status(sender);
				return true;
			}else if(args.length == 2 && args[0].equalsIgnoreCase("status")){ // statusだけ見られる
				String playername = args[1];
				Player player = Bukkit.getPlayerExact(playername);
				if(player != null){
					// プレイヤーがオンライン
					EBan.Status(player, sender);
					return true;
				}else{
					// プレイヤーがオフライン
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
					if(offplayer == null){
						SendMessage(sender, cmd, ChatColor.RED + "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

						Player any_chance_player = Bukkit.getPlayer(playername);
						if(any_chance_player != null){
							SendMessage(sender, cmd, ChatColor.RED + "もしかして: " + any_chance_player.getName());
						}
						return true;
					}
					EBan.Status(offplayer, sender);
				}
				return true;
			}else if(args.length == 2 && args[0].equalsIgnoreCase("remove")){
				// /eban remove mine_book000
				String playername = args[1];
				Player player = Bukkit.getPlayerExact(playername);
				if(player != null){
					// プレイヤーがオンライン
					if(EBan.Remove(player, sender)){
						SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
					}else{
						SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
					}
				}else{
					// プレイヤーがオフライン
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
					if(offplayer == null){
						SendMessage(sender, cmd, ChatColor.RED + "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

						Player any_chance_player = Bukkit.getPlayer(playername);
						if(any_chance_player != null){
							SendMessage(sender, cmd, ChatColor.RED + "もしかして: " + any_chance_player.getName());
						}
						return true;
					}
					if(EBan.Remove(offplayer, sender)){
						SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
					}else{
						SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
					}
				}
				return true;
			}else if(args.length >= 3 && args[0].equalsIgnoreCase("add")){
				// /eban add mine_book000 test eban reason message
				String reason = "";
				int c = 2;
				while(args.length > c){
					reason += args[c];
					if(args.length != (c+1)){
						reason += " ";
					}
					c++;
				}
				String playername = args[1];
				Player player = Bukkit.getPlayerExact(playername);
				if(player != null){
					// プレイヤーがオンライン
					if(EBan.Add(player, sender, reason)){
						SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
					}else{
						SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
					}
				}else{
					// プレイヤーがオフライン
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(playername);
					if(offplayer == null){
						SendMessage(sender, cmd, ChatColor.RED + "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

						Player any_chance_player = Bukkit.getPlayer(playername);
						if(any_chance_player != null){
							SendMessage(sender, cmd, ChatColor.RED + "もしかして: " + any_chance_player.getName());
						}
						return true;
					}
					if(EBan.Add(offplayer, sender, reason)){
						SendMessage(sender, cmd, ChatColor.RED + "実行に成功しました。");
					}else{
						SendMessage(sender, cmd, ChatColor.RED + "実行に失敗しました。");
					}
				}
				return true;
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
