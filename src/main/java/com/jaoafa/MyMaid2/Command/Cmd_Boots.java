package com.jaoafa.MyMaid2.Command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Boots extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Boots(JavaPlugin plugin) {
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
		PlayerInventory inv = player.getInventory();
		ItemStack hand = player.getInventory().getItemInMainHand();
		if(hand.getType() == Material.AIR){
			SendMessage(sender, cmd, "手にブロックを持ってください。");
			return true;
		}
		ItemStack head = inv.getBoots();
		if(head != null){
			if(head.getType() != Material.AIR){
				inv.removeItem(head);
			}
		}
		inv.setBoots(hand);
		player.getInventory().setItemInMainHand(head);
		SendMessage(sender, cmd, "持っていたブロックを足につけました。");
		return true;
	}
}
