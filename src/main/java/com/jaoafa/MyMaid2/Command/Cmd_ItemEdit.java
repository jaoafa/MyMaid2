package com.jaoafa.MyMaid2.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_ItemEdit extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_ItemEdit(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		/*
		  * /itemedit name <Name>
		  * /itemedit lore <Lore>
		 */
		ItemStack is = player.getInventory().getItemInMainHand();
		if(is.getType() == Material.AIR){
			SendMessage(sender, cmd, "何かブロックを持ってください。");
			return true;
		}
		if(args.length >= 2){
			if(args[0].equalsIgnoreCase("name")){
				ItemMeta meta = is.getItemMeta();
				String text = "";
				int c = 1;
				while(args.length > c){
					text += args[c];
					if(args.length != (c+1)){
						text += " ";
					}
					c++;
				}
				text = ChatColor.translateAlternateColorCodes('&', text);
				meta.setDisplayName(text);
				is.setItemMeta(meta);
				player.getInventory().setItemInMainHand(is);
				player.updateInventory();
				SendMessage(sender, cmd, "Nameを更新しました。");
				return true;
			}else if(args[0].equalsIgnoreCase("lore")){
				ItemMeta meta = is.getItemMeta();
				List<String> lore = new ArrayList<String>();
				int c = 1;
				while(args.length > c){
					String text = ChatColor.translateAlternateColorCodes('&', args[c]);
					lore.add(text);
					c++;
				}

				meta.setLore(lore);
				is.setItemMeta(meta);
				player.getInventory().setItemInMainHand(is);
				player.updateInventory();
				SendMessage(sender, cmd, "Loreを更新しました。");
				return true;
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
