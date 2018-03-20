package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Command.Cmd_DedRain;

public class Event_DedRain extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_DedRain(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onRainStart(WeatherChangeEvent event) {
		if (!event.isCancelled()) {
			boolean setting = Cmd_DedRain.flag;
			if (event.toWeatherState() && setting) {
				event.setCancelled(true);
			}
		}
	}
}
