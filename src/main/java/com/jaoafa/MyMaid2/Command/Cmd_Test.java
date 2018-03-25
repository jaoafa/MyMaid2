package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Test extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Test(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;
		PlayerInventory inv = player.getInventory();
		ItemStack is = inv.getItemInMainHand();
		Bukkit.broadcastMessage(is.getType().name());
		return true;
	}
}
