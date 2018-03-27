package com.jaoafa.MyMaid2.Task;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Lib.SKKColors;

public class TabListSKKReloader extends BukkitRunnable {
	Player player;
	public TabListSKKReloader(JavaPlugin plugin, Player player) {
		this.player = player;
	}
	@Override
	public void run() {
		SKKColors.setPlayerSKKTabList(player);
	}
}
