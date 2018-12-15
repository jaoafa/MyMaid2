package com.jaoafa.MyMaid2.Event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_Anti4BYTES extends MyMaid2Premise implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	public void OnPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();

		if(!check4bytechars(message)){
			return;
		}
		player.sendMessage("[4BYTESChecker] " + ChatColor.GREEN + "メッセージ内に絵文字などの4バイト文字が含まれています。Minecraftの仕様上、4バイト文字は表示されません。注意してください。");

		DiscordSend("293856671799967744", "__**[4BYTESChecker]**__ " + "プレイヤー「" + player.getName() + "」が投稿したメッセージ内に4バイト文字が含まれていました。\nメッセージ: ```" + message + "```\n判定された対象文字列: ```" + check4bytechars_MatchText(message) + "```");
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage();

		if(!check4bytechars(message)){
			return;
		}
		player.sendMessage("[4BYTESChecker] " + ChatColor.GREEN + "コマンド内に絵文字などの4バイト文字が含まれています。Minecraftの仕様上、4バイト文字は表示されません。注意してください。");
		player.sendMessage("[4BYTESChecker] " + ChatColor.GREEN + "また、アイテム名などに4バイト文字列を含まれているとチャンク破損等を起こす可能性があります。出来る限り使用は避けてください。");

		DiscordSend("293856671799967744", "__**[4BYTESChecker]**__ " + "プレイヤー「" + player.getName() + "」が実行したコマンド内に4バイト文字が含まれていました。\nコマンド: ```" + message + "```\n判定された対象文字列: ```" + check4bytechars_MatchText(message) + "```");
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void OnSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location loc = block.getLocation();
		String world = loc.getWorld().getName();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		String[] messages = event.getLines();

		for(String message : messages){
			if(!check4bytechars(message)){
				continue;
			}
			player.sendMessage("[4BYTESChecker] " + ChatColor.GREEN + "看板内に絵文字などの4バイト文字が含まれています。Minecraftの仕様上、4バイト文字は表示されません。注意してください。");
			player.sendMessage("[4BYTESChecker] " + ChatColor.GREEN + "また、アイテム名などに4バイト文字列を含まれているとチャンク破損等を起こす可能性があります。出来る限り使用は避けてください。");

			DiscordSend("293856671799967744", "__**[4BYTESChecker]**__ " + "プレイヤー「" + player.getName() + "」が設置した看板(" + world + " " +  + x + " " + y + " " + z + ")内に4バイト文字が含まれていました。\nコマンド: ```" + message + "```\n判定された対象文字列: ```" + check4bytechars_MatchText(message) + "```");
		}
	}
	List<String> sendedCmdb = new ArrayList<>();
	@EventHandler
	public void onCommandBlockCall(ServerCommandEvent event) {
		if (!(event.getSender() instanceof BlockCommandSender)) return;
		BlockCommandSender sender = (BlockCommandSender) event.getSender();

		if (sender.getBlock() == null || !(sender.getBlock().getState() instanceof CommandBlock)) return;
		CommandBlock cmdb = (CommandBlock) sender.getBlock().getState();

		String command = cmdb.getCommand();

		String world = cmdb.getWorld().getName();
		int x = cmdb.getX();
		int y = cmdb.getY();
		int z = cmdb.getZ();

		if(!check4bytechars(command)){
			return;
		}

		if(sendedCmdb.contains(command)){
			return;
		}

		Player nearestPlayer = getNearestPlayer(cmdb.getLocation());
		if(nearestPlayer == null){
			return;
		}
		nearestPlayer.sendMessage("[4BYTESChecker] " + ChatColor.GREEN + "あなたの近くで実行されたコマンドブロック(" + world + " " + x + " " + y + " " + z + ")のコマンド内に絵文字などの4バイト文字が含まれています。Minecraftの仕様上、4バイト文字は表示されません。注意してください。");
		nearestPlayer.sendMessage("[4BYTESChecker] " + ChatColor.GREEN + "また、アイテム名やコマンドなどに4バイト文字列を含まれているとチャンク破損等を起こす可能性があります。出来る限り使用は避けてください。");

		DiscordSend("293856671799967744", "__**[4BYTESChecker]**__ " + "プレイヤー「" + nearestPlayer.getName() + "」の近くで実行されたコマンドブロック(" + world + " " + x + " " + y + " " + z + ")のコマンド内に4バイト文字が含まれていました。\nコマンド: ```" + command + "```\n判定された対象文字列: ```" + check4bytechars_MatchText(command) + "```");

		sendedCmdb.add(command);
	}
}
