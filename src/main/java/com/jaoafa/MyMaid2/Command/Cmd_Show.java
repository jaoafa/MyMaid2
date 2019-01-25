package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_Show extends MyMaid2Premise implements CommandExecutor {
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
			p.showPlayer(MyMaid2.mymaid2, player);
		}
		if(Cmd_Hide.hided.contains(player.getUniqueId())){
			Cmd_Hide.hided.remove(player.getUniqueId());
		}
		SendMessage(sender, cmd, "あなたは他のプレイヤーから見えるようになりました。見えなくするには/hideを実行しましょう。");
		return true;
	}
}
