package com.jaoafa.MyMaid2.Event;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Event_CommandBlockLogger extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_CommandBlockLogger(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCommandBlockCall(ServerCommandEvent event) {
		if (!(event.getSender() instanceof BlockCommandSender)) return;
		BlockCommandSender sender = (BlockCommandSender) event.getSender();

		if (sender.getBlock() == null || !(sender.getBlock().getState() instanceof CommandBlock)) return;
		CommandBlock cmdb = (CommandBlock) sender.getBlock().getState();

		String command = cmdb.getCommand();

		Player nearestPlayer = getNearestPlayer(cmdb.getLocation());
		String name, uuid;
		if(nearestPlayer == null){
			name = uuid = "null";
		}else{
			name = nearestPlayer.getName();
			uuid = nearestPlayer.getUniqueId().toString();
		}

		double nearestDistance = getNearestPlayerDistance(cmdb.getLocation());

		String world = cmdb.getWorld().getName();
		int x = cmdb.getX();
		int y = cmdb.getY();
		int z = cmdb.getZ();

		try {
			Statement statement = MySQL.getNewStatement();
			statement.executeUpdate("INSERT INTO cmdb_log (nearplayer, nearplayer_uuid, distance, command, world, x, y, z, date) VALUES ('" + name + "', '" + uuid + "', '" + nearestDistance + "', '" + command + ", '" + world + "', '" + x + "', '" + y + "', '" + z + "', CURRENT_TIMESTAMP);");
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}
}
