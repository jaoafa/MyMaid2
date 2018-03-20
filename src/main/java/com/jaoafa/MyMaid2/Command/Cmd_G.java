package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_G extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_G(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		if(args.length == 0){
			if (!(sender instanceof Player)) {
				SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				return true;
			}
			Player player = (Player) sender;
			GameMode beforeGameMode = player.getGameMode();

			if(player.getGameMode() == GameMode.SPECTATOR){
				// スペクテイターならクリエイティブにする
				player.setGameMode(GameMode.CREATIVE);
				if(player.getGameMode() != GameMode.CREATIVE){
					SendMessage(sender, cmd, "ゲームモードの変更ができませんでした。");
					return true;
				}
				SendMessage(sender, cmd, beforeGameMode.name() + " -> " + GameMode.CREATIVE.name());
				return true;
			}else if(player.getGameMode() == GameMode.CREATIVE){
				// クリエイティブならスペクテイターにする
				player.setGameMode(GameMode.SPECTATOR);
				if(player.getGameMode() != GameMode.SPECTATOR){
					SendMessage(sender, cmd, "ゲームモードの変更ができませんでした。");
					return true;
				}
				SendMessage(sender, cmd, beforeGameMode.name() + " -> " + GameMode.SPECTATOR.name());
				return true;
			}else{
				// それ以外(サバイバル・アドベンチャー)ならクリエイティブにする
				player.setGameMode(GameMode.CREATIVE);
				if(player.getGameMode() != GameMode.CREATIVE){
					SendMessage(sender, cmd, "ゲームモードの変更ができませんでした。");
					return true;
				}
				SendMessage(sender, cmd, beforeGameMode.name() + " -> " + GameMode.CREATIVE.name());
				return true;
			}
		}else if(args.length == 1){
			if (!(sender instanceof Player)) {
				SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				return true;
			}
			Player player = (Player) sender;
			GameMode beforeGameMode = player.getGameMode();

			int i;
			try{
				i = Integer.parseInt(args[0]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "引数には数値を指定してください。");
				return true;
			}

			GameMode gm = GameMode.getByValue(i);
			if(gm == null){
				SendMessage(sender, cmd, "指定された引数からゲームモードが取得できませんでした。");
				return true;
			}

			player.setGameMode(gm);
			if(player.getGameMode() != gm){
				SendMessage(sender, cmd, "ゲームモードの変更ができませんでした。");
				return true;
			}
			SendMessage(sender, cmd, beforeGameMode.name() + " -> " + gm.name());
			return true;
		}else if(args.length == 2){
			int i;
			try{
				i = Integer.parseInt(args[0]);
			}catch(NumberFormatException e){
				SendMessage(sender, cmd, "引数には数値を指定してください。");
				return true;
			}

			GameMode gm = GameMode.getByValue(i);
			if(gm == null){
				SendMessage(sender, cmd, "指定された引数からゲームモードが取得できませんでした。");
				return true;
			}

			String playername = args[1];
			Player player = Bukkit.getPlayerExact(playername);
			if(player == null){
				SendMessage(sender, cmd, "指定されたプレイヤー「" + playername + "」は見つかりませんでした。");

				Player any_chance_player = Bukkit.getPlayer(playername);
				if(any_chance_player != null){
					SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
				}
				return true;
			}

			GameMode beforeGameMode = player.getGameMode();

			player.setGameMode(gm);
			if(player.getGameMode() != gm){
				SendMessage(sender, cmd, "ゲームモードの変更ができませんでした。");
				return true;
			}
			SendMessage(sender, cmd, player.getName() + ": " + beforeGameMode.name() + " -> " + gm.name());
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}

}
