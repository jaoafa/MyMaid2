package com.jaoafa.MyMaid2.Task;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Command.Cmd_AFK;

public class Task_AFKING extends BukkitRunnable {
	private Player player;
	public Task_AFKING(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		//player.getWorld().playSound(player.getLocation(),Sound.EXPLODE,1,1);
		if(!Cmd_AFK.getAFKing(player)){
			return;
	   	}
		if(!player.isOnline()){
			Cmd_AFK.setAFK_False(player);
		}
		player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		String listname = player.getPlayerListName();
		if(!listname.contains(ChatColor.DARK_GRAY + player.getName())){
			listname = listname.replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
			player.setPlayerListName(listname);
		}
	}
}
