package com.jaoafa.MyMaid2.Task;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Task_getUsingMods extends BukkitRunnable {
	private Player player;
	public Task_getUsingMods(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		if(!player.isOnline()){
			return;
		}
		Set<String> mods = player.getListeningPluginChannels();
		StringBuilder builder = new StringBuilder();
		for(String mod : mods){
			builder.append(mod).append(",");
		}
		String strmods;
		if(builder.length() == 0){
			strmods = "Vanilla";
		}else{
			strmods = builder.substring(0, builder.length() - 1);
		}

		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
				p.sendMessage("[MyMaid] " + ChatColor.GREEN + "Mod情報: " + strmods);
			}
}
	}

}
