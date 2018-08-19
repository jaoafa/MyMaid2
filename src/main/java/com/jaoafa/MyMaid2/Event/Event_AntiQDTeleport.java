package com.jaoafa.MyMaid2.Event;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_AntiQDTeleport extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_AntiQDTeleport(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	Boolean DEBUG = true;
	@EventHandler
	public void onPlayerTeleportEvent(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		String command = event.getMessage();
		Logger LOGGER = Bukkit.getLogger();
		if(!command.contains(" ")){
			return;
		}
		String[] args = command.split(" ", 0);
		if(args[0].equalsIgnoreCase("/tp")){
			if(args.length == 2){ // /tp <Player>
				String to = args[1];
				Player to_player = Bukkit.getPlayer(to);
				if(to_player == null){
					return;
				}
				if(DEBUG) LOGGER.info("<" + player.getName() + "> " + player.getName() + " ==> " + to_player.getName());
				if(to_player.getGameMode() == GameMode.SPECTATOR){ // テレポート先がスペクテイター
					if(DEBUG) LOGGER.info("NG スペクテイタープレイヤー");
					if(DEBUG) LOGGER.info(player.getGameMode().name() + " => " + to_player.getGameMode().name());
					if(TeleportFlag(player)){ // 実行者LQD
						if(DEBUG) LOGGER.info("スペクテイタープレイヤーにテレポートしようとしたため規制しました。");
						event.setCancelled(true);
					}else{
						if(player.getGameMode() != GameMode.SPECTATOR){
							if(DEBUG) LOGGER.info("スペクテイタープレイヤーにテレポートしようとしたため規制しました。");
							player.sendMessage("[GAMEMODE] " + ChatColor.GREEN + "プレイヤー「" + to_player.getName() + "」はスペクテイターモードのためテレポートできません。");
							event.setCancelled(true);
						}
					}
				}
			}else if(args.length == 3){ // /tp <Player> <Player>
				String from = args[1];
				String to = args[2];
				Player from_player = Bukkit.getPlayer(from);
				Player to_player = Bukkit.getPlayer(to);
				if(from_player == null || to_player == null){
					return;
				}
				if(DEBUG) LOGGER.info("<" + player.getName() + "> " + from_player.getName() + " ==> " + to_player.getName());
				if(from_player.getUniqueId().equals(player.getUniqueId())){
					if(DEBUG) LOGGER.info("OK 自分をテレポート");
				}else if(!from_player.getUniqueId().equals(player.getUniqueId())){
					if(DEBUG) LOGGER.info("NG 他人をテレポート");
					if(TeleportFlag(player)){ // 実行者LQD
						if(DEBUG) LOGGER.info("他人をテレポートさせようとしたため規制しました。");
						event.setCancelled(true);
					}
				}
				if(to_player.getGameMode() == GameMode.SPECTATOR){
					if(DEBUG) LOGGER.info("NG スペクテイタープレイヤー");
					if(DEBUG) LOGGER.info(from_player.getGameMode().name() + " => " + to_player.getGameMode().name());
					if(TeleportFlag(player)){ // 実行者LQD
						if(DEBUG) LOGGER.info("スペクテイタープレイヤーにテレポートしようとしたため規制しました。");
						event.setCancelled(true);
					}else{
						if(from_player.getGameMode() != GameMode.SPECTATOR){
							if(DEBUG) LOGGER.info("スペクテイタープレイヤーにテレポートしようとしたため規制しました。");
							player.sendMessage("[GAMEMODE] " + ChatColor.GREEN + "プレイヤー「" + to_player.getName() + "」はスペクテイターモードのためテレポートできません。");
							event.setCancelled(true);
						}
					}
				}
			}else if(args.length == 4){ // /tp ~ ~ ~
				String x = args[1];
				String y = args[2];
				String z = args[3];
				if(DEBUG) LOGGER.info("<" + player.getName() + "> " + player.getName() + " ==> " + x + " " + y + " " + z);
			}else if(args.length == 5){ // /tp <Player> ~ ~ ~
				String from = args[1];
				String x = args[2];
				String y = args[3];
				String z = args[4];
				Player from_player = Bukkit.getPlayer(from);
				if(from_player == null){
					return;
				}
				if(DEBUG) LOGGER.info("<" + player.getName() + "> " + from_player.getName() + " ==> " + x + " " + y + " " + z);
				if(!from_player.getUniqueId().equals(player.getUniqueId())){
					if(DEBUG) LOGGER.info("NG 他人をテレポート");
					if(TeleportFlag(player)){ // 実行者LQD
						if(DEBUG) LOGGER.info("他人をテレポートさせようとしたため規制しました。");
						event.setCancelled(true);
					}
				}
			}else if(args.length == 6){ // /tp ~ ~ ~ 0 0
				String x = args[1];
				String y = args[2];
				String z = args[3];
				String yaw = args[4];
				String pitch = args[5];
				if(DEBUG) LOGGER.info("<" + player.getName() + "> " + player.getName() + " ==> " + x + " " + y + " " + z + " " + yaw + " " + pitch);
			}else if(args.length == 7){ // /tp <Player> ~ ~ ~ 0 0
				String from = args[1];
				String x = args[2];
				String y = args[3];
				String z = args[4];
				String yaw = args[5];
				String pitch = args[6];
				Player from_player = Bukkit.getPlayer(from);
				if(from_player == null){
					return;
				}
				if(DEBUG) LOGGER.info("<" + player.getName() + "> " + from_player.getName() + " ==> " + x + " " + y + " " + z + " " + yaw + " " + pitch);
				if(!from_player.getUniqueId().equals(player.getUniqueId())){
					if(DEBUG) LOGGER.info("NG 他人をテレポート");
					if(TeleportFlag(player)){ // 実行者LQD
						if(DEBUG) LOGGER.info("他人をテレポートさせようとしたため規制しました。");
						event.setCancelled(true);
					}
				}
			}
		}
	}
	boolean TeleportFlag(Player player){
		String group = PermissionsManager.getPermissionMainGroup(player);
		return group.equalsIgnoreCase("Limited") ||
				group.equalsIgnoreCase("QPPE") ||
				group.equalsIgnoreCase("Default");
	}
	String toString(Location loc){
		return loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
	}
}
