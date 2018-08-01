package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_WT extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_WT(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		if(args.length == 1){
			if (!(sender instanceof Player)) {
				SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				return true;
			}
			Player player = (Player) sender;
			if(args[0].equalsIgnoreCase("1")){
				// Jao_Afa
				World world = Bukkit.getServer().getWorld("Jao_Afa");
				if(world == null){
					SendMessage(sender, cmd, "「Jao_Afa」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				player.teleport(loc);
				SendMessage(sender, cmd, "「Jao_Afa」ワールドにテレポートしました。");
				return true;
			}else if(args[0].equalsIgnoreCase("2")){
				// Jao_Afa_Old
				World world = Bukkit.getServer().getWorld("Summer2018");
				if(world == null){
					SendMessage(sender, cmd, "「Summer2018」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				player.teleport(loc);
				SendMessage(sender, cmd, "「Summer2018」ワールドにテレポートしました。");
				return true;
			}else if(args[0].equalsIgnoreCase("3")){
				// ReJao_Afa
				World world = Bukkit.getServer().getWorld("SandBox");
				if(world == null){
					SendMessage(sender, cmd, "「SandBox」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				player.teleport(loc);
				SendMessage(sender, cmd, "「SandBox」ワールドにテレポートしました。");
				return true;
			}else{
				World world = Bukkit.getServer().getWorld(args[0]);
				if(world == null){
					SendMessage(sender, cmd, "指定されたワールドは存在しません。");
					return true;
				}else{
					Location loc = new Location(world, 0, 0, 0, 0, 0);
					int y = getGroundPos(loc);
					loc = new Location(world, 0, y, 0, 0, 0);
					loc.add(0.5f,0f,0.5f);
					player.teleport(loc);
					SendMessage(sender, cmd, "「" + world.getName() + "」ワールドにテレポートしました。");
					return true;
				}
			}
		}else if(args.length == 2){
			Player play = null;
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.getName().equalsIgnoreCase(args[0])){
					play = p;
				}
			}
			if(play == null){
				SendMessage(sender, cmd, "ユーザーが見つかりませんでした。");
				return true;
			}
			if(args[1].equalsIgnoreCase("1")){
				// Jao_Afa
				World world = Bukkit.getServer().getWorld("Jao_Afa");
				if(world == null){
					SendMessage(sender, cmd, "「Jao_Afa」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				play.teleport(loc);
				SendMessage(sender, cmd, play.getName() + "が「Jao_Afa」ワールドにテレポートしました。");
				SendMessage(play, cmd, "「Jao_Afa」ワールドにテレポートしました。");
				return true;
			}else if(args[1].equalsIgnoreCase("2")){
				// Jao_Afa_Old
				World world = Bukkit.getServer().getWorld("Jao_Afa_Old");
				if(world == null){
					SendMessage(sender, cmd, "「Jao_Afa_Old」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				play.teleport(loc);
				SendMessage(sender, cmd, play.getName() + "が「Jao_Afa_Old」ワールドにテレポートしました。");
				SendMessage(play, cmd, "「Jao_Afa_Old」ワールドにテレポートしました。");
				return true;
			}else if(args[1].equalsIgnoreCase("3")){
				// SandBox
				World world = Bukkit.getServer().getWorld("SandBox");
				if(world == null){
					SendMessage(sender, cmd, "「SandBox」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				play.teleport(loc);
				SendMessage(sender, cmd, "「SandBox」ワールドにテレポートしました。");
				return true;
			}else{
				World world = Bukkit.getServer().getWorld(args[1]);
				if(world == null){
					SendMessage(sender, cmd, "指定されたワールドは存在しません。");
					return true;
				}else{
					Location loc = new Location(world, 0, 0, 0, 0, 0);
					int y = getGroundPos(loc);
					loc = new Location(world, 0, y, 0, 0, 0);
					loc.add(0.5f,0f,0.5f);
					play.teleport(loc);
					SendMessage(sender, cmd, play.getName() + "が「" + world.getName() + "」ワールドにテレポートしました。");
					SendMessage(play, cmd, "「" + world.getName() + "」ワールドにテレポートしました。");
					return true;
				}
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
