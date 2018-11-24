package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Event_SandBoxBuildProtectionNotice extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location loc = block.getLocation();

		if(!loc.getWorld().getName().equalsIgnoreCase("SandBox")){
			return;
		}

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM sandbox_build WHERE uuid = ?");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			if(res.next()){
				// 既に通知済み
				return;
			}

			player.sendMessage("[SandBox] " + ChatColor.RED + "このワールド(SandBoxワールド)で建築する場合、以下のページから「SandBoxワールドルール」をお読みいただき、必ずお守りください。");
			player.sendMessage("[SandBox] " + ChatColor.RED + "また、基本的に自身の建築物には保護をかけるようお願い申し上げます。");
			player.sendMessage("[SandBox] " + ChatColor.RED + "https://jaoafa.com/community/world/sandbox");

			PreparedStatement statement2 = MySQL.getNewPreparedStatement("INSERT INTO sandbox_build (player, uuid) VALUES (?, ?);");
			statement2.setString(1, player.getName());
			statement2.setString(2, player.getUniqueId().toString());
			statement2.execute();
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
		}

	}
}
