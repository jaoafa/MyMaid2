package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_AntiNetherPortal extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_AntiNetherPortal(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onEntityCreatePortalEvent(EntityCreatePortalEvent event){
		event.setCancelled(true);
		if(event.getEntityType() != EntityType.PLAYER){
			return;
		}
		Player player = (Player) event.getEntity();
		player.sendMessage("[AntiNetherPortal] " + ChatColor.GREEN + "負荷対策および不必要な破壊を抑制するため、ネザーポータルの生成を禁止しています。ご協力をお願いします。");
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
				p.sendMessage("[" + ChatColor.RED + "NoNetherPortal" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + "の近くでネザーポータルの生成がされましたが、生成を規制されました。");
			}
		}
	}

	@EventHandler
	public void onPortalCreateEvent(PortalCreateEvent event){
		event.setCancelled(true);
	}
}
