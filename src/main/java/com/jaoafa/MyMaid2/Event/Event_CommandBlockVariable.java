package com.jaoafa.MyMaid2.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MyMaidVariable;

public class Event_CommandBlockVariable extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_CommandBlockVariable(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onCommandBlockCall(ServerCommandEvent event) {
		if (!(event.getSender() instanceof BlockCommandSender)) return;
		BlockCommandSender sender = (BlockCommandSender) event.getSender();

		if (sender.getBlock() == null || !(sender.getBlock().getState() instanceof CommandBlock)) return;
		CommandBlock cmdb = (CommandBlock) sender.getBlock().getState();

		String command = cmdb.getCommand();
		if(!command.startsWith("$")) return; // 最初に$が入ってたら変数入りコマンド

		command = StringUtils.stripStart(command, "$"); // $を消す
		command = StringUtils.stripStart(command, "/"); // /を消す

		// 「@p」のみ置き換える動作をする
		if(sender instanceof Player){
			Player player = (Player) sender;
			NearestPlayer npr = new NearestPlayer(player.getLocation());
			if(npr.getStatus()){
				command = command.replaceAll("@" + "p" + "", npr.getPlayer().getName());
			}
		}else if(sender instanceof BlockCommandSender){
			NearestPlayer npr = new NearestPlayer(cmdb.getBlock().getLocation());
			if(npr.getStatus()){
				command = command.replaceAll("@" + "p" + "", npr.getPlayer().getName());
			}
		}

		// ----- 事前定義(予約済み変数) ----- //

		SimpleDateFormat sdf_Year = new SimpleDateFormat("yyyy");
		command = command.replaceAll("\\$" + "DateTime_Year" + "\\$", sdf_Year.format(new Date()));
		SimpleDateFormat sdf_Month = new SimpleDateFormat("MM");
		command = command.replaceAll("\\$" + "DateTime_Month" + "\\$", sdf_Month.format(new Date()));
		SimpleDateFormat sdf_Day = new SimpleDateFormat("dd");
		command = command.replaceAll("\\$" + "DateTime_Day" + "\\$", sdf_Day.format(new Date()));

		SimpleDateFormat sdf_Hour = new SimpleDateFormat("HH");
		command = command.replaceAll("\\$" + "DateTime_Hour" + "\\$", sdf_Hour.format(new Date()));
		SimpleDateFormat sdf_Minute = new SimpleDateFormat("mm");
		command = command.replaceAll("\\$" + "DateTime_Minute" + "\\$", sdf_Minute.format(new Date()));
		SimpleDateFormat sdf_Second = new SimpleDateFormat("ss");
		command = command.replaceAll("\\$" + "DateTime_Second" + "\\$", sdf_Second.format(new Date()));

		command = command.replaceAll("\\$" + "PlayerCount" + "\\$", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));


		for(Player p : Bukkit.getOnlinePlayers()){
			if(!command.contains("$" + "Damager_" + p.getName() + "$")){
				continue;
			}
			EntityDamageEvent ede = p.getLastDamageCause();
			if(ede == null){
				continue;
			}
			Entity e = ede.getEntity();
			if(e == null){
				continue;
			}
			String name = e.getName();
			command = command.replaceAll("\\$" +  "Damager_" + p.getName() + "\\$", name);
		}

		ScoreboardManager sbm = Bukkit.getScoreboardManager();
		Scoreboard sb = sbm.getMainScoreboard();
		for(Objective obj : sb.getObjectives()){
			String regex = "\\$Score_" + obj.getName() + "_(.+?)\\$";
			Pattern p = Pattern.compile(regex);

			Matcher m = p.matcher(command);

			while(m.find()){
				Score i = obj.getScore(m.group(1));
				if(i == null){
					continue;
				}
				command = command.replaceAll("\\$" +  "Score_" + obj.getName() + "_" + m.group(1) + "\\$", ""+i.getScore());
			}
		}

		// ----- 事前定義(予約済み変数) ----- //

		Map<String, String> map = MyMaidVariable.listALL();
		for(Map.Entry<String, String> e : map.entrySet()) {
			command = command.replaceAll("\\$" + e.getKey() + "\\$", e.getValue());
		}

		Bukkit.dispatchCommand(sender, command);
	}
}
class NearestPlayer {
	Boolean status;
	Player player = null;
	double closest = -1;
	public NearestPlayer(Location loc){
		double closest = Double.MAX_VALUE;
		Player closestp = null;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.getWorld().equals(loc.getWorld())){
				continue;
			}
			double dist = p.getLocation().distance(loc);
			if(closest == Double.MAX_VALUE || dist < closest){
				closest = dist;
				closestp = p;
			}
		}
		if(closestp == null){
			this.status = false;
		}else{
			this.status = true;
			this.player = closestp;
			this.closest = closest;
		}
	}
	public Boolean getStatus(){
		return status;
	}
	public Player getPlayer(){
		return player;
	}
	public Double getClosest(){
		return closest;
	}
}