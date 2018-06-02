package com.jaoafa.MyMaid2.Event;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_CommandBlockVariable extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_CommandBlockVariable(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onCommandBlockCall(ServerCommandEvent event) {
		if (!(event.getSender() instanceof BlockCommandSender)) return;
		BlockCommandSender sender = (BlockCommandSender) event.getSender();

		if (sender.getBlock() == null || !(sender.getBlock().getState() instanceof CommandBlock)) return;
		CommandBlock cmdb = (CommandBlock) sender.getBlock().getState();

		String command = cmdb.getCommand();
		if(!command.startsWith("$")) return; // 最初に$が入ってたら変数入りコマンド

		command = StringUtils.stripStart(command, "$"); // $を消す
		command = StringUtils.stripStart(command, "/"); // /を消す


	}
}