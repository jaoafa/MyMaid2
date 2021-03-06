package com.jaoafa.MyMaid2.Command;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Wtp extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// /wtp <World> <X> <Y> <Z> : 4
		// /wtp <Player> <World> <X> <Y> <Z> : 5
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 4){
			if (!(sender instanceof Player)) {
				SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				return true;
			}
			Player player = (Player) sender;
			// /wtp <World> <X> <Y> <Z> : 4
			String worldname = args[0];
			World world = Bukkit.getServer().getWorld(worldname);
			if(world == null){
				SendMessage(sender, cmd, "指定されたワールドは見つかりません。");
				return true;
			}

			if(world.getName().equalsIgnoreCase("Summer2018")){
				SendMessage(sender, cmd, "ワールド「Summer2018」へはテレポートできません。");
				return true;
			}

			int x;
			try{
				x = Integer.parseInt(args[1]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "x座標には数値を指定してください。");
				return true;
			}

			int y;
			try{
				y = Integer.parseInt(args[2]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "y座標には数値を指定してください。");
				return true;
			}

			int z;
			try{
				z = Integer.parseInt(args[3]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "z座標には数値を指定してください。");
				return true;
			}

			Location location = new Location(world, x, y, z);
			player.teleport(location);
			String locstr = x + ", " + y + ", " + z;
			DiscordSend("*[" + player.getName() + ": " + player.getName() + " to " + locstr + "]*");
			Bukkit.broadcastMessage(ChatColor.GRAY + "[" + player.getName() + ": " + player.getName() + " は " + locstr + " にワープしました]");
			return true;
		}else if(args.length == 5){
			// /wtp <Player> <World> <X> <Y> <Z> : 5
			Player tpplayer = Bukkit.getServer().getPlayer(args[0]);
			if(tpplayer == null){
				SendMessage(sender, cmd, "指定されたプレイヤーはオフラインか見つかりませんでした。");
				return true;
			}
			String worldname = args[1];
			World world = Bukkit.getServer().getWorld(worldname);
			if(world == null){
				SendMessage(sender, cmd, "指定されたワールドは見つかりません。");
				return true;
			}

			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date start = format.parse("2018/08/01 00:00:00");
				Date end = format.parse("2018/08/31 23:59:59");
				if(isPeriod(start, end)){
					if(world.getName().equalsIgnoreCase("Summer2018")){
						SendMessage(sender, cmd, "ワールド「Summer2018」へはテレポートできません。");
						return true;
					}
				}
			} catch (ParseException e) {
				BugReporter(e);
			}


			int x;
			try{
				x = Integer.parseInt(args[2]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "x座標には数値を指定してください。");
				return true;
			}

			int y;
			try{
				y = Integer.parseInt(args[3]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "y座標には数値を指定してください。");
				return true;
			}

			int z;
			try{
				z = Integer.parseInt(args[4]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "z座標には数値を指定してください。");
				return true;
			}

			Location location = new Location(world, x, y, z);
			tpplayer.teleport(location);
			String locstr = x + ", " + y + ", " + z;
			DiscordSend("*[" + sender.getName() + ": " + tpplayer.getName() + " to " + locstr + "]*");
			Bukkit.broadcastMessage(ChatColor.GRAY + "[" + sender.getName() + ": " + tpplayer.getName() + " は " + locstr + " にワープしました]");
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
