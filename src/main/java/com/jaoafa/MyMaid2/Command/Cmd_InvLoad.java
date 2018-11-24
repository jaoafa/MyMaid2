package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.InventoryManager;

public class Cmd_InvLoad extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 0){
			if(!(sender instanceof Player)){
				SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
				return true;
			}
			Player player = (Player) sender;
			String loadName = "default"; // 普通はdefault
			boolean bool = InventoryManager.loadInventory(player, loadName);
			if(bool){
				SendMessage(sender, cmd, "「" + loadName + "」という名前のインベントリデータから復旧することに成功しました。");
			}else{
				SendMessage(sender, cmd, "「" + loadName + "」という名前のインベントリデータから復旧することに失敗しました。");
			}
			return true;
		}else if(args.length == 1){
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null){
				// 実行者を名前つけて保存
				if(!(sender instanceof Player)){
					SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
					return true;
				}
				Player run_player = (Player) sender;
				String loadName = args[0]; // 引数あるのでそれ
				boolean bool = InventoryManager.loadInventory(run_player, loadName);
				if(bool){
					SendMessage(sender, cmd, "「" + loadName + "」という名前のインベントリデータから復旧することに成功しました。");
				}else{
					SendMessage(sender, cmd, "「" + loadName + "」という名前のインベントリデータから復旧することに失敗しました。");
				}
				return true;
			}else{
				String loadName = "default";
				boolean bool = InventoryManager.loadInventory(player, loadName);
				if(bool){
					SendMessage(sender, cmd, player.getName() + "のインベントリを「" + loadName + "」という名前のインベントリデータから復旧することに成功しました。");
				}else{
					SendMessage(sender, cmd, player.getName() + "のインベントリを「" + loadName + "」という名前のインベントリデータから復旧することに失敗しました。");
				}
				return true;
			}
		}else if(args.length == 2){
			String playername = args[0];
			Player player = Bukkit.getPlayerExact(playername);
			if(player == null){
				SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(playername);
				if(any_chance_player != null){
					SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}
			String loadName = args[1];
			boolean bool = InventoryManager.loadInventory(player, loadName);
			if(bool){
				SendMessage(sender, cmd, player.getName() + "のインベントリを「" + loadName + "」という名前のインベントリデータから復旧することに成功しました。");
			}else{
				SendMessage(sender, cmd, player.getName() + "のインベントリを「" + loadName + "」という名前のインベントリデータから復旧することに失敗しました。");
			}
			return true;
		}else if(args.length == 3){
			String load_playername = args[0];
			Player load_player = Bukkit.getPlayerExact(load_playername);
			if(load_player == null){
				SendMessage(sender, cmd, "指定されたプレイヤー「" + load_playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(load_playername);
				if(any_chance_player != null){
					SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}
			String restore_playername = args[1];
			Player restore_player = Bukkit.getPlayerExact(restore_playername);
			if(restore_player == null){
				SendMessage(sender, cmd, "指定されたプレイヤー「" + restore_playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(restore_playername);
				if(any_chance_player != null){
					SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}
			String loadName = args[2];
			boolean bool = InventoryManager.restoreInventory(load_player, restore_player, loadName);
			if(bool){
				SendMessage(sender, cmd, load_player.getName() + "の「" + loadName + "」という名前のインベントリデータから" + restore_player.getName() + "のインベントリに復旧することに成功しました。");
			}else{
				SendMessage(sender, cmd, load_player.getName() + "の「" + loadName + "」という名前のインベントリデータから" + restore_player.getName() + "のインベントリに復旧することに失敗しました。");
			}
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
