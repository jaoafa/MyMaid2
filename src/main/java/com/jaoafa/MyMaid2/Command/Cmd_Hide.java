package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_Hide extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Hide(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)){
			SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
			return true;
		}
		Player player = (Player) sender;
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")) {
			SendMessage(sender, cmd, "このコマンドは管理部・モデレータ・常連のみ使用できます。");
			return true;
		}
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			p.hidePlayer(plugin, player);
		}
		SendMessage(sender, cmd, "あなたは他のプレイヤーから見えなくなりました。見えるようにするには/showを実行しましょう。");
		SendMessage(sender, cmd, "なお、プレイヤーリストからも見えなくなりますのでお気をつけて。");
		return true;
	}
}
