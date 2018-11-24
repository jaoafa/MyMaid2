package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_InvShow extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(!(sender instanceof Player)){
			SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
			return true;
		}
		Player player = (Player) sender;
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
			SendMessage(sender, cmd, "このコマンドは管理部・モデレーター・常連のみ使用可能です。");
			return true;
		}
		if(args.length == 1){
			String playername = args[0];
			Player player_show = Bukkit.getPlayerExact(playername);
			if(player_show == null){
				SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(playername);
				if(any_chance_player != null){
					SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}
			PlayerInventory inv = player_show.getInventory();
			Inventory inventory = Bukkit.getServer().createInventory(player, 5 * 9, player_show.getName() + "のインベントリ");
			/*
			ItemStack[] armordata = inv.getArmorContents();
			for(int n=0; n != armordata.length; n++){
				inventory.setItem(n, armordata[n]);
			}

			inventory.setItem(8, inv.getItemInOffHand());*/

			ItemStack[] invdata = inv.getContents();
			for(int n=0; n != invdata.length; n++){
				inventory.setItem(n, invdata[n]);
			}
			player.openInventory(inventory);
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
