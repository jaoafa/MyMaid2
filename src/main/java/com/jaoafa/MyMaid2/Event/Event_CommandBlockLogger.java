package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Event_CommandBlockLogger extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onCommandBlockCall(ServerCommandEvent event) {
		if(event.getSender() instanceof ConsoleCommandSender){
			Bukkit.getLogger().info("CONSOLE COMMAND: " + event.getCommand());
		}
		if (!(event.getSender() instanceof BlockCommandSender)) return;
		BlockCommandSender sender = (BlockCommandSender) event.getSender();

		if (sender.getBlock() == null || !(sender.getBlock().getState() instanceof CommandBlock)) return;
		CommandBlock cmdb = (CommandBlock) sender.getBlock().getState();

		String command = cmdb.getCommand();

		if(command.startsWith("/testfor")){
			return;
		}else if(command.startsWith("testfor")){
			return;
		}else if(command.startsWith("/testforblock")){
			return;
		}else if(command.startsWith("testforblock")){
			return;
		}else if(command.startsWith("/testforblocks")){
			return;
		}else if(command.startsWith("testforblocks")){
			return;
		}else if(command.equals("")){
			return;
		}

		if(cmdb.getWorld().getName().equalsIgnoreCase("Jao_Afa_Old")){
			return;
		}

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
			String sql = "INSERT INTO cmdb_log (nearplayer, nearplayer_uuid, distance, command, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement statement = MySQL.getNewPreparedStatement(sql);
			statement.setString(1, name);
			statement.setString(2, uuid);
			statement.setDouble(3, nearestDistance);
			statement.setString(4, command);
			statement.setString(5, world);
			statement.setInt(6, x);
			statement.setInt(7, y);
			statement.setInt(8, z);
			statement.executeUpdate();

		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}
	}
}
