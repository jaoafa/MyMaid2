package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_AntiWither extends MyMaid2Premise implements Listener {
	@EventHandler
	public void CreatureSpawn(CreatureSpawnEvent event) {
		// 召喚されたMOBのエンティティを取得する
		LivingEntity ent = event.getEntity();
		// ウィザーの召喚操作によるものか？
		if ((ent.getType() == EntityType.WITHER) &&
				(event.getSpawnReason() == SpawnReason.BUILD_WITHER)){
			Location location = event.getLocation();
			double min = 1.79769313486231570E+308;
			Player min_player = null;
			for(Player player: Bukkit.getServer().getOnlinePlayers()){
				org.bukkit.Location location_p = player.getLocation();
				if(location.getWorld().getName().equals(location_p.getWorld().getName())){
					double distance = location.distance(location_p);
					if(distance < min){
						min = distance;
						min_player = player;
					}
				}
			}
			if(min_player == null){
				event.setCancelled(true);
				return;
			}
			event.setCancelled(true);
			min_player.sendMessage("[AntiWither] " + ChatColor.GREEN + "負荷対策の為にウィザーの召喚を禁止しています。ご協力をお願いします。");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
					p.sendMessage("[" + ChatColor.RED + "NoWither" + ChatColor.WHITE + "] " + ChatColor.GREEN + min_player.getName() + "の近くでウィザーが発生しましたが、発生を規制されました。");
				}
			}
		}
	}
}
