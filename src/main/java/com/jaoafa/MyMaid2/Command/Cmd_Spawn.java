package com.jaoafa.MyMaid2.Command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Spawn extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Spawn(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		// コマンド実行者がプレイヤーかどうか
		if(!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		Location spawn;
		if(args.length == 1 && args[0].equalsIgnoreCase("true")){
			spawn = player.getBedSpawnLocation();
			if(spawn == null){
				SendMessage(sender, cmd, "スポーン地点が見つかりませんでした。/spawnpointで設定しましょう。");
				return true;
			}
		}else{
			World World = player.getWorld();
			spawn = World.getSpawnLocation();
			spawn.add(0.5f,0f,0.5f);
		}
		player.teleport(spawn);
		SendMessage(sender, cmd, "スポーン地点にテレポートしました。");
		return true;
	}
}
