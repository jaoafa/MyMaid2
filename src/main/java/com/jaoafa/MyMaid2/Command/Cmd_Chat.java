package com.jaoafa.MyMaid2.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Chat extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Chat(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length >= 2){
			if(Bukkit.getPlayerExact(args[0]) != null){
				SendMessage(sender, cmd, "オンラインユーザーを話者に指定できません。");
				return true;
			}
			ChatColor color = ChatColor.GRAY;
			Boolean chatcolor = true;
			if(args[args.length-1].equalsIgnoreCase("color:" + "AQUA")){
				color = ChatColor.AQUA;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "BLACK")){
				color = ChatColor.BLACK;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "BLUE")){
				color = ChatColor.BLUE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_AQUA")){
				color = ChatColor.DARK_AQUA;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_BLUE")){
				color = ChatColor.DARK_BLUE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_GRAY")){
				color = ChatColor.DARK_GRAY;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_GREEN")){
				color = ChatColor.DARK_GREEN;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_PURPLE")){
				color = ChatColor.DARK_PURPLE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_RED")){
				color = ChatColor.DARK_RED;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "GOLD")){
				color = ChatColor.GOLD;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "GREEN")){
				color = ChatColor.GREEN;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "LIGHT_PURPLE")){
				color = ChatColor.LIGHT_PURPLE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "RED")){
				color = ChatColor.RED;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "WHITE")){
				color = ChatColor.WHITE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "YELLOW")){
				color = ChatColor.YELLOW;
			}else{
				chatcolor = false;
			}
			String text = "";
			int c = 1;
			while(args.length > c){
				if((args.length-1) == c && chatcolor){
					break;
				}
				text += args[c]+" ";
				c++;

			}
			text = ChatColor.translateAlternateColorCodes('&', text);
			if(args[0].equalsIgnoreCase("jaotan")){
				color = ChatColor.GOLD;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + color + "■" + ChatColor.WHITE + args[0] +  ": " + text);
			DiscordSend("**" + args[0] + "**: " + text);
			return true;
		}else{
			SendUsageMessage(sender, cmd);
			return true;
		}
	}
}
