package com.jaoafa.MyMaid2.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Command.Cmd_Ded;

public class Event_Ded extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event){
		Player player = event.getEntity();
		Location loc = player.getLocation();


		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2018/08/01 00:00:00");
			Date end = format.parse("2018/08/31 23:59:59");
			if(isPeriod(start, end)){
				if(loc.getWorld().getName().equalsIgnoreCase("Summer2018")){
					return;
				}
			}
		} catch (ParseException e) {
			BugReporter(e);
		}


		Cmd_Ded.ded.put(player.getName(), loc);
		player.sendMessage("[DED] " + ChatColor.GREEN + "死亡した場所に戻るには「/ded」コマンドが使用できます。");
	}
}
