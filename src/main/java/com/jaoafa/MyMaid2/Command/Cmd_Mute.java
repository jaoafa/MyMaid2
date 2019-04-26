package com.jaoafa.MyMaid2.Command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MuteManager;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_Mute extends MyMaid2Premise implements CommandExecutor {
	@SuppressWarnings("deprecation")
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
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
			SendMessage(sender, cmd, "このコマンドは管理部・モデレーターのみ使用可能です。");
			return true;
		}

		if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")){
				// /mute add <Player|UUID>
				String player_or_uuid = args[1];
				OfflinePlayer target = Bukkit.getPlayerExact(player_or_uuid);
				if(target == null){
					// UUID?
					try{
						// UUID
						UUID target_uuid = UUID.fromString(player_or_uuid);
						target = Bukkit.getOfflinePlayer(target_uuid);
						if(target == null){
							SendMessage(sender, cmd, "指定されたUUIDのプレイヤーは見つかりませんでした。");
							return true;
						}
					}catch(IllegalArgumentException e){
						// OfflinePlayer
						target = Bukkit.getOfflinePlayer(player_or_uuid);
						if(target == null){
							SendMessage(sender, cmd, "指定されたプレイヤー「" + player_or_uuid + "」は見つかりませんでした。");

							Player any_chance_player = Bukkit.getPlayer(player_or_uuid);
							if(any_chance_player != null){
								SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
							}
							return true;
						}
					}
				}
				boolean bool = MuteManager.Add(target);
				if(bool){
					// Successful
					SendMessage(sender, cmd, "プレイヤー「" + target.getName() + "」をミュートリストに追加しました。");
				}else{
					// Error
					SendMessage(sender, cmd, "プレイヤー「" + target.getName() + "」をミュートリストに追加できませんでした…。");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("remove")){
				// /mute remove <Player|UUID>
				String player_or_uuid = args[1];
				OfflinePlayer target = Bukkit.getPlayerExact(player_or_uuid);
				if(target == null){
					// UUID?
					try{
						// UUID
						UUID target_uuid = UUID.fromString(player_or_uuid);
						target = Bukkit.getOfflinePlayer(target_uuid);
						if(target == null){
							SendMessage(sender, cmd, "指定されたUUIDのプレイヤーは見つかりませんでした。");
							return true;
						}
					}catch(IllegalArgumentException e){
						// OfflinePlayer
						target = Bukkit.getOfflinePlayer(player_or_uuid);
						if(target == null){
							SendMessage(sender, cmd, "指定されたプレイヤー「" + player_or_uuid + "」は見つかりませんでした。");

							Player any_chance_player = Bukkit.getPlayer(player_or_uuid);
							if(any_chance_player != null){
								SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
							}
							return true;
						}
					}
				}
				boolean bool = MuteManager.Remove(target);
				if(bool){
					// Successful
					SendMessage(sender, cmd, "プレイヤー「" + target.getName() + "」をミュートリストから削除しました。");
				}else{
					// Error
					SendMessage(sender, cmd, "プレイヤー「" + target.getName() + "」をミュートリストから削除できませんでした…。");
				}
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				SendMessage(sender, cmd, "現在、以下のプレイヤーがミュートされています。");
				Set<String> mute_players = new HashSet<>();
				List<String> mutes = MuteManager.loadMutes();
				for(String mute : mutes){
					UUID uuid = UUID.fromString(mute);
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(uuid);
					String name;
					if(offplayer != null){
						name = offplayer.getName();
					}else{
						name = mute;
					}
					mute_players.add(name);
				}
				String players = implode(mute_players, ",");
				SendMessage(sender, cmd, players);
				return true;
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
