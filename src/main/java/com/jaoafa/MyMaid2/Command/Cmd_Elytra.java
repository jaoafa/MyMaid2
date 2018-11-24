package com.jaoafa.MyMaid2.Command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Elytra extends MyMaid2Premise implements CommandExecutor {
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
		ItemStack elytra = new ItemStack(Material.ELYTRA);
		ItemStack fireworks = new ItemStack(Material.FIREWORK, 64);

		PlayerInventory inv = player.getInventory();
		ItemStack offhand = inv.getItemInOffHand();

		inv.setItemInOffHand(fireworks);
		SendMessage(sender, cmd, "花火をオフハンドのアイテムと置きかえました。");

		if(offhand != null && offhand.getType() != Material.AIR){
			if(player.getInventory().firstEmpty() == -1){
				player.getLocation().getWorld().dropItem(player.getLocation(), offhand);
				SendMessage(sender, cmd, "インベントリがいっぱいだったため、既にオフハンドに持っていたアイテムはあなたの足元にドロップしました。");
			}else{
				inv.addItem(offhand);
			}
		}

		ItemStack chestplate = inv.getChestplate();

		inv.setChestplate(elytra);
		SendMessage(sender, cmd, "エリトラを装備しました。");

		if(chestplate != null && chestplate.getType() != Material.AIR){
			if(player.getInventory().firstEmpty() == -1){
				player.getLocation().getWorld().dropItem(player.getLocation(), chestplate);
				SendMessage(sender, cmd, "インベントリがいっぱいだったため、既に胴体につけていたアイテムはあなたの足元にドロップしました。");
			}else{
				inv.addItem(chestplate);
			}
		}
		return true;
	}
}
