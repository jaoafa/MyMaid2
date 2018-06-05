package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.InventoryManager;

public class Cmd_InvSave extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_InvSave(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 0){
			if(sender instanceof Player){
				SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
				return true;
			}
			Player player = (Player) sender;
			String saveName = "default"; // 普通はdefault
			boolean bool = InventoryManager.saveInventory(player, saveName);
			if(bool){
				SendMessage(sender, cmd, "インベントリを「" + saveName + "」という名前で保存することに成功しました。");
			}else{
				SendMessage(sender, cmd, "インベントリを「" + saveName + "」という名前で保存することに失敗しました。");
			}
			return true;
		}else if(args.length == 1){
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null){
				// 実行者を名前つけて保存
				if(sender instanceof Player){
					SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
					return true;
				}
				Player run_player = (Player) sender;
				String saveName = args[0]; // 引数あるのでそれ
				boolean bool = InventoryManager.saveInventory(run_player, saveName);
				if(bool){
					SendMessage(sender, cmd, "インベントリを「" + saveName + "」という名前で保存することに成功しました。");
				}else{
					SendMessage(sender, cmd, "インベントリを「" + saveName + "」という名前で保存することに失敗しました。");
				}
				return true;
			}else{
				String saveName = "default";
				boolean bool = InventoryManager.saveInventory(player, saveName);
				if(bool){
					SendMessage(sender, cmd, player.getName() + "のインベントリを「" + saveName + "」という名前で保存することに成功しました。");
				}else{
					SendMessage(sender, cmd, player.getName() + "のインベントリを「" + saveName + "」という名前で保存することに失敗しました。");
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
			String saveName = args[1];
			boolean bool = InventoryManager.saveInventory(player, saveName);
			if(bool){
				SendMessage(sender, cmd, player.getName() + "のインベントリを「" + saveName + "」という名前で保存することに成功しました。");
			}else{
				SendMessage(sender, cmd, player.getName() + "のインベントリを「" + saveName + "」という名前で保存することに失敗しました。");
			}
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
