package com.jaoafa.MyMaid2.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Command.Cmd_AFK;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_Summer2018 extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_Summer2018(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2018/08/01 00:00:00");
			Date end = format.parse("2018/08/31 23:59:59");
			if(!isPeriod(start, end)){
				return;
			}
		} catch (ParseException e) {
			BugReporter(e);
		}
		if(!player.getWorld().getName().startsWith("Summer2018")){
			return;
		}
		if(event.getNewGameMode() == GameMode.SURVIVAL){
			return;
		}
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")){
			return;
		}
		player.sendMessage("[GAMEMODE] " + ChatColor.GREEN + "処理に失敗しました。");
		event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerTeleportEvent(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2018/08/01 00:00:00");
			Date end = format.parse("2018/08/31 23:59:59");
			if(!isPeriod(start, end)){
				return;
			}
		} catch (ParseException e) {
			BugReporter(e);
		}
		if(event.getCause() == TeleportCause.COMMAND){
			Location To = event.getTo();
			if(!To.getWorld().getName().startsWith("Summer2018")){
				return;
			}
			String group = PermissionsManager.getPermissionMainGroup(player);
			if(event.getFrom().getWorld().getName().startsWith("Summer2018") && (group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator"))){
				return;
			}
			player.sendMessage("[TELEPORT] " + ChatColor.GREEN + "ワールド「Summer2018」にいる人などへはテレポートできません。");
			event.setCancelled(true);
		}else if(event.getCause() == TeleportCause.SPECTATE){
			Location To = event.getTo();
			if(!To.getWorld().getName().startsWith("Summer2018")){
				return;
			}
			String group = PermissionsManager.getPermissionMainGroup(player);
			if(event.getFrom().getWorld().getName().startsWith("Summer2018") && (group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator"))){
				return;
			}
			player.sendMessage("[TELEPORT] " + ChatColor.GREEN + "ワールド「Summer2018」にいる人などへはテレポートできません。");
			event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerChangedWorldEvent(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		String world = player.getWorld().getName();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2018/08/01 00:00:00");
			Date end = format.parse("2018/08/31 23:59:59");
			if(!isPeriod(start, end)){
				return;
			}
		} catch (ParseException e) {
			BugReporter(e);
		}
		if(!world.startsWith("Summer2018")){
			if(event.getFrom().getName().startsWith("Summer2018")){
				player.setGameMode(GameMode.CREATIVE);
			}
			return;
		}
		player.setGameMode(GameMode.SURVIVAL);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerBedEnterEvent(PlayerBedEnterEvent event){
		Player player = event.getPlayer();
		Block block = event.getBed();
		Location loc = block.getLocation();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2018/08/01 00:00:00");
			Date end = format.parse("2018/08/31 23:59:59");
			if(!isPeriod(start, end)){
				return;
			}
		} catch (ParseException e) {
			BugReporter(e);
		}

		if(!loc.getWorld().getName().startsWith("Summer2018")){
			return;
		}

		int Summer2018er = Bukkit.getWorld("Summer2018").getPlayers().size();
		int AFKer = 0;

		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.getWorld().getName().equalsIgnoreCase("Summer2018")){
				continue;
			}
			if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR){
				continue;
			}
			if(Cmd_AFK.getAFKing(p)){
				AFKer++;
			}
		}
		int CREATIVEorSPECTATOR = 0;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.getWorld().getName().equalsIgnoreCase("Summer2018")){
				continue;
			}
			if(Cmd_AFK.getAFKing(p)){
				continue;
			}
			if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR){
				CREATIVEorSPECTATOR++;
			}
		}
		int Need = Summer2018er - (AFKer + CREATIVEorSPECTATOR);
		int NowSleeping = 1;
		if(Cmd_AFK.getAFKing(player)){
			NowSleeping = 0;
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.getWorld().getName().equalsIgnoreCase("Summer2018")){
				continue;
			}
			if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR){
				continue;
			}
			if(Cmd_AFK.getAFKing(p)){
				continue;
			}
			if(p.isSleeping()){
				NowSleeping++;
			}
		}
		int NowNeed = Need - NowSleeping;

		SendSummer2018(ChatColor.GOLD + "[Summer2018]" + " " + ChatColor.RESET + player.getName() + "が就寝しました。夜が明けるにはあと" + NowNeed + "人が就寝しなければなりません。(必要人数: " + Need + "人 | AFK: " + AFKer + "人)");
		if(NowNeed == 0){
			SendSummer2018(ChatColor.GOLD + "[Summer2018]" + " " + ChatColor.RESET + "まもなく朝がやってきます…！");
			Bukkit.getWorld("Summer2018").setTime(0L);
		}else{
			Set<String> notsleeping = new HashSet<>();
			for(Player p : Bukkit.getOnlinePlayers()){
				if(!p.getWorld().getName().equalsIgnoreCase("Summer2018")){
					continue;
				}
				if(p.isSleeping()){
					continue;
				}
				if(Cmd_AFK.getAFKing(p)){
					continue;
				}
				if(p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR){
					continue;
				}
				if(player.getName().equalsIgnoreCase(p.getName())){
					continue;
				}
				notsleeping.add(p.getName());
			}
			SendSummer2018(ChatColor.GOLD + "[Summer2018]" + " " + ChatColor.RESET + "寝ていないプレイヤー: " + implode(notsleeping, ", "));
		}
	}

	private void SendSummer2018(String message){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(!player.getWorld().getName().equalsIgnoreCase("Summer2018")){
				continue;
			}
			player.sendMessage(message);
		}
	}
}
