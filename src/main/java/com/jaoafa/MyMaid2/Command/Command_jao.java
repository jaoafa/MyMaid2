package com.jaoafa.MyMaid2.Command;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Pointjao;

public class Command_jao extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Command_jao(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		if(args.length == 1){
			OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[0]);
			try{
				Pointjao Pjao = new Pointjao(offplayer);
				int now = Pjao.get();
				SendMessage(sender, cmd, "現在" + offplayer.getName() + "が所持しているポイント数は" + now + "ポイントです。");
				return true;
			}catch(UnsupportedOperationException e){
				SendMessage(sender, cmd, "jaoポイントデータが存在しません。");
				return true;
			}catch(NullPointerException e){
				SendMessage(sender, cmd, "プレイヤーが見つかりません。");
				return true;
			}catch(ClassNotFoundException | SQLException e){
				BugReporter(e);
				SendMessage(sender, cmd, "操作に失敗しました。");
				SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}else if(args.length >= 4){
			// /jao add player point reason
			// /jao use player point reason
			if(args[0].equalsIgnoreCase("add")){
				// /jao add player point reason
				OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
				if(offplayer == null){
					SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}

				int point;
				try{
					point = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "ポイントには数値を指定してください。");
					return true;
				}
				if(point <= 0){
					SendMessage(sender, cmd, "ポイントは1以上を指定してください。");
					return true;
				}

				String reason = "";
				int c = 3;
				while(args.length > c){
					reason += args[c];
					if(args.length != (c+1)){
						reason += " ";
					}
					c++;
				}
				if (sender instanceof Player) {
					reason += " (Player: " + sender.getName() + ")";
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender bcs = (BlockCommandSender) sender;
					Block block = bcs.getBlock();
					reason += " (CmdBlock: " + block.getX() + " " + block.getY() + " " + block.getZ() + ")";
				}else if (sender instanceof CommandMinecart) {
					CommandMinecart cm = (CommandMinecart) sender;
					Location loc = cm.getLocation();
					reason += " (CmdCart: " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + ")";
				}else if (sender instanceof RemoteConsoleCommandSender) {
					reason += " (Rcon)";
				}else if (sender instanceof ConsoleCommandSender) {
					reason += " (Console)";
				}else if (sender instanceof ProxiedCommandSender) {
					ProxiedCommandSender pcs = (ProxiedCommandSender) sender;
					CommandSender Callee_sender = pcs.getCallee(); // コマンドの呼び出しに使用されているCommandSenderを返します。(コマンド実行させられているCommandSender？)
					CommandSender Caller_sender = pcs.getCaller(); // このプロキシされたコマンドをトリガしたCommandSenderを返します。(executeコマンドを実行したCommandSender？)

					reason += " (Execute: " + Callee_sender.getName() + " [" + Callee_sender + "] => " + Caller_sender.getName() + " [" + Caller_sender + "])";
				}else{
					reason += " (実行元特定不能: " + sender.getName() + "|" + sender.toString() + ")";
				}
				try{
					Pointjao Pjao = new Pointjao(offplayer);
					boolean bool = Pjao.add(point, reason);
					if(bool){
						SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」に" + point + "ポイントを追加しました。");
					}else{
						SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」に" + point + "ポイントを追加できませんでした。");
					}
					return true;
				}catch(UnsupportedOperationException e){
					SendMessage(sender, cmd, "jaoポイントデータが存在しません。");
					return true;
				}catch(NullPointerException e){
					SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}catch(ClassNotFoundException | SQLException e){
					BugReporter(e);
					SendMessage(sender, cmd, "操作に失敗しました。");
					SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("use")){
				// /jao use player point reason
				OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
				if(offplayer == null){
					SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}

				int point;
				try{
					point = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "ポイントには数値を指定してください。");
					return true;
				}
				if(point <= 0){
					SendMessage(sender, cmd, "ポイントは1以上を指定してください。");
					return true;
				}

				String reason = "";
				int c = 3;
				while(args.length > c){
					reason += args[c];
					if(args.length != (c+1)){
						reason += " ";
					}
					c++;
				}
				if (sender instanceof Player) {
					reason += " (Player: " + sender.getName() + ")";
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender bcs = (BlockCommandSender) sender;
					Block block = bcs.getBlock();
					reason += " (CmdBlock: " + block.getX() + " " + block.getY() + " " + block.getZ() + ")";
				}else if (sender instanceof CommandMinecart) {
					CommandMinecart cm = (CommandMinecart) sender;
					Location loc = cm.getLocation();
					reason += " (CmdCart: " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + ")";
				}else if (sender instanceof RemoteConsoleCommandSender) {
					reason += " (Rcon)";
				}else if (sender instanceof ConsoleCommandSender) {
					reason += " (Console)";
				}else if (sender instanceof ProxiedCommandSender) {
					ProxiedCommandSender pcs = (ProxiedCommandSender) sender;
					CommandSender Callee_sender = pcs.getCallee(); // コマンドの呼び出しに使用されているCommandSenderを返します。(コマンド実行させられているCommandSender？)
					CommandSender Caller_sender = pcs.getCaller(); // このプロキシされたコマンドをトリガしたCommandSenderを返します。(executeコマンドを実行したCommandSender？)

					reason += " (Execute: " + Callee_sender.getName() + " [" + Callee_sender + "] => " + Caller_sender.getName() + " [" + Caller_sender + "])";
				}else{
					reason += " (実行元特定不能: " + sender.getName() + "|" + sender.toString() + ")";
				}
				try{
					Pointjao Pjao = new Pointjao(offplayer);
					boolean bool = Pjao.use(point, reason);
					if(bool){
						SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」から" + point + "ポイントを減算しました。");
					}else{
						SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」から" + point + "ポイントを減算できませんでした。");
					}
					return true;
				}catch(UnsupportedOperationException e){
					SendMessage(sender, cmd, "jaoポイントデータが存在しません。");
					return true;
				}catch(NullPointerException e){
					SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}catch(ClassNotFoundException | SQLException e){
					BugReporter(e);
					SendMessage(sender, cmd, "操作に失敗しました。");
					SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("pay")){
				// /jao pay player point reason
				if (!(sender instanceof Player)) {
					SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player player = (Player) sender;

				OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
				if(offplayer == null){
					SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}

				int point;
				try{
					point = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "ポイントには数値を指定してください。");
					return true;
				}
				if(point <= 0){
					SendMessage(sender, cmd, "ポイントは1以上を指定してください。");
					return true;
				}

				String reason = "";
				int c = 3;
				while(args.length > c){
					reason += args[c];
					if(args.length != (c+1)){
						reason += " ";
					}
					c++;
				}

				String add_reason = "プレイヤー「" + offplayer.getName() + "」へ「" + reason + "」という理由での支払い。";
				String use_reason = "プレイヤー「" + player.getName() + "」から「" + reason + "」という理由での支払い。";

				try{
					Pointjao playerPjao = new Pointjao(player);
					Pointjao offplayerPjao = new Pointjao(offplayer);
					boolean use_bool = playerPjao.use(point, add_reason);
					if(use_bool){
						boolean add_bool = offplayerPjao.add(point, use_reason);
						if(add_bool){
							SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」に" + point + "ポイントを支払いました。");
						}else{
							SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」に" + point + "ポイントを支払えませんでした。");
							playerPjao.add(point, "『" + add_reason + "』の失敗による返却");
						}
					}else{
						SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」から" + point + "ポイントを減算できませんでした。");
					}
					return true;
				}catch(UnsupportedOperationException e){
					SendMessage(sender, cmd, "jaoポイントデータが存在しません。");
					return true;
				}catch(NullPointerException e){
					SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}catch(ClassNotFoundException | SQLException e){
					BugReporter(e);
					SendMessage(sender, cmd, "操作に失敗しました。");
					SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return true;
				}
			}
		}
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		try{
			Pointjao Pjao = new Pointjao(player);
			int now = Pjao.get();
			SendMessage(sender, cmd, "現在あなたが所持しているポイント数は" + now + "ポイントです。");
			return true;
		}catch(UnsupportedOperationException e){
			SendMessage(sender, cmd, "jaoポイントデータが存在しません。");
			return true;
		}catch(NullPointerException e){
			SendMessage(sender, cmd, "プレイヤーが見つかりません。");
			return true;
		}catch(ClassNotFoundException | SQLException e){
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}
	}
}
