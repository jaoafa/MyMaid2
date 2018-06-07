package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_InvEdit extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_InvEdit(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(!(sender instanceof Player)){
			SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
			return true;
		}
		Player player = (Player) sender;
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
			SendMessage(sender, cmd, "このコマンドは管理部・モデレーターのみ使用可能です。");
			return true;
		}
		if(args.length == 1){
			String playername = args[0];
			Player player_show = Bukkit.getPlayerExact(playername);
			if(player_show == null){
				SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(playername);
				if(any_chance_player != null){
					SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}
			PlayerInventory inv = player_show.getInventory();
			player.openInventory(inv);
			SendMessage(sender, cmd, "インベントリの編集には細心の注意を払ってください。");
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
