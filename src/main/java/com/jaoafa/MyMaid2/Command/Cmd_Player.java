package com.jaoafa.MyMaid2.Command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_Player extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Player(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 0){
			if (!(sender instanceof Player)) {
				SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			Player player = (Player) sender;
			String MainGroup = PermissionsManager.getPermissionMainGroup(player);
			List<String> groups = PermissionsManager.getPermissionGroupList(player);

			for(String group : groups){
				if(MainGroup.equalsIgnoreCase(group)){
					SendMessage(player, cmd, "You Permission group \"" + group +"\" (Main)");
				}else{
					SendMessage(player, cmd, "You Permission group \"" + group +"\"");
				}
			}
		}else if(args.length == 1){
			String p = args[0];

			if(p == null){
				SendMessage(sender, cmd, "引数にnullを指定できません。"); // 本来ありえなさそうだけど
				return true;
			}

			try{
				String MainGroup = PermissionsManager.getPermissionMainGroup(p);
				if(MainGroup == null){
					SendMessage(sender, cmd, "メイングループを取得できませんでした。");
					return true;
				}
				List<String> groups = PermissionsManager.getPermissionGroupList(p);
				for(String group : groups){
					if(MainGroup.equalsIgnoreCase(group)){
						SendMessage(sender, cmd, "You Permission group \"" + group +"\" (Main)");
					}else{
						SendMessage(sender, cmd, "You Permission group \"" + group +"\"");
					}
				}
			}catch(IllegalArgumentException e){
				SendMessage(sender, cmd, "プレイヤーを取得できません。");
				return true;
			}
		}else{
			SendMessage(sender, cmd, "引数が適していません。");
		}
		return true;
	}
}
