package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand.EnumClientCommand;

public class Cmd_Respawn extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Respawn(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 1){
			// Respawn
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null){
				SendMessage(sender, cmd, "指定されたプレイヤーは見つかりませんでした。");
				return true;
			}
			if(!player.isOnline()){
				SendMessage(sender, cmd, "指定されたプレイヤーはオンラインではありませんでした。");
				return true;
			}
			SendMessage(sender, cmd, "指定されたプレイヤーをリスポーンさせます…");
			Respawn(player, 1);
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
	public void Respawn(final Player player, int Time){
		Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {

			@Override
			public void run() {
				((CraftPlayer)player).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
			}
		}, Time);
	}
}
