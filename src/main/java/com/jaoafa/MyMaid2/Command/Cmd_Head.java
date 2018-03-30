package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Head extends MyMaid2Premise implements CommandExecutor {
	public Cmd_Head() {}
	@SuppressWarnings("deprecation")
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
			Player player = (Player) sender; //コマンド実行者を代入
			OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[0]);
			String name = offplayer.getName();
			ItemStack skull = new ItemStack(Material.SKULL_ITEM);
			SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
			skull.setDurability((short) 3);
			skullMeta.setOwningPlayer(offplayer);
			skull.setItemMeta(skullMeta);
			PlayerInventory inv = player.getInventory();
			ItemStack main = inv.getItemInMainHand();

			inv.setItemInMainHand(skull);
			SendMessage(sender, cmd, "「" + name + "の頭」をメインハンドのアイテムと置きかえました。");

			if(main != null && main.getType() != Material.AIR){
				if(player.getInventory().firstEmpty() == -1){
					player.getLocation().getWorld().dropItem(player.getLocation(), main);
					SendMessage(sender, cmd, "インベントリがいっぱいだったため、既に持っていたアイテムはあなたの足元にドロップしました。");
				}else{
					inv.addItem(main);
				}
			}
			return true;
		}
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender; //コマンド実行者を代入
		String name = player.getName();

		ItemStack skull = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skull.setDurability((short) 3);
		skullMeta.setOwningPlayer(player);
		skull.setItemMeta(skullMeta);
		PlayerInventory inv = player.getInventory();
		ItemStack main = inv.getItemInMainHand();

		inv.setItemInMainHand(skull);
		SendMessage(sender, cmd, "「" + name + "の頭」をメインハンドのアイテムと置きかえました。");

		if(main != null && main.getType() != Material.AIR){
			if(player.getInventory().firstEmpty() == -1){
				player.getLocation().getWorld().dropItem(player.getLocation(), main);
				SendMessage(sender, cmd, "インベントリがいっぱいだったため、既に持っていたアイテムはあなたの足元にドロップしました。");
			}else{
				inv.addItem(main);
			}
		}
		return true;
	}
}
