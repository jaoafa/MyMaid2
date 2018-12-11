package com.jaoafa.MyMaid2.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Cmd_Chat implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
            return true;
        }
        if(args.length >= 2){
            if(Bukkit.getPlayerExact(args[0]) != null){
                return true;
            }
            ChatColor color = ChatColor.GRAY;
            boolean chatcolor = true;
            String coloring = args[args.length-1].toLowerCase();
            switch (coloring) {
                case "color:AQUA":
                    color = ChatColor.AQUA;
                    break;
                case "color:BLACK":
                    color = ChatColor.BLACK;
                    break;
                case "color:BLUE":
                    color = ChatColor.BLUE;
                    break;
                case "color:DARK_AQUA":
                    color = ChatColor.DARK_AQUA;
                    break;
                case "color:DARK_BLUE":
                    color = ChatColor.DARK_BLUE;
                    break;
                case "color:DARK_GRAY":
                    color = ChatColor.DARK_GRAY;
                    break;
                case "color:DARK_GREEN":
                    color = ChatColor.DARK_GREEN;
                    break;
                case "color:DARK_PURPLE":
                    color = ChatColor.DARK_PURPLE;
                    break;
                case "color:DARK_RED":
                    color = ChatColor.DARK_RED;
                    break;
                case "color:GOLD":
                    color = ChatColor.GOLD;
                    break;
                case "color:GREEN":
                    color = ChatColor.GREEN;
                    break;
                case "color:LIGHT_PURPLE":
                    color = ChatColor.LIGHT_PURPLE;
                    break;
                case "color:RED":
                    color = ChatColor.RED;
                    break;
                case "color:WHITE":
                    color = ChatColor.WHITE;
                    break;
                case "color:YELLOW":
                    color = ChatColor.YELLOW;
                    break;
                default:
                    chatcolor = false;
                    break;
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
