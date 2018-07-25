package com.jaoafa.MyMaid2.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Ded extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Ded(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Map<String, Location> ded = new HashMap<String, Location>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof Player)){
			SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(!ded.containsKey(player.getName())){
			SendMessage(sender, cmd, "死亡した情報が存在しません。");
			return true;
		}
		Location loc = ded.get(player.getName());
		player.teleport(loc);
		SendMessage(sender, cmd, loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "にテレポートしました。");
		SendMessage(sender, cmd, "警告!! PvP等での「/ded」コマンドの利用は原則禁止です！多く使用すると迷惑行為として認識される場合もあります！");
		return true;
	}
}
