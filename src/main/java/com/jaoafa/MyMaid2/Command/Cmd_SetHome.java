package com.jaoafa.MyMaid2.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Cmd_SetHome extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_SetHome(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;

		if(args.length == 0){
			Location loc = player.getLocation();
			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ? AND name = ?");
				statement.setString(1, player.getUniqueId().toString());
				statement.setString(2, "default");

				ResultSet res = statement.executeQuery();
				if(res.next()){
					SendMessage(sender, cmd, "「default」というホームは存在しています。「/home remove default」と打つことでホームを削除できます。");
					return true;
				}
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("INSERT INTO home (player, uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
				statement2.setString(1, player.getName()); // player
				statement2.setString(2, player.getUniqueId().toString()); // uuid
				statement2.setString(3, "default"); // name
				statement2.setString(4, loc.getWorld().toString()); // world
				statement2.setDouble(5, loc.getX()); // x
				statement2.setDouble(6, loc.getY()); // y
				statement2.setDouble(7, loc.getZ()); // z
				statement2.setFloat(8, loc.getYaw()); // yaw
				statement2.setFloat(9, loc.getPitch()); // pitch

				statement.executeUpdate();
				SendMessage(sender, cmd, "「default」としてホームを設定しました。「/home」と打つことでテレポートできます。");
				return true;
			} catch (SQLException | ClassNotFoundException e) {
				BugReporter(e);
				SendMessage(sender, cmd, "操作に失敗しました。");
				SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}else if(args.length == 1){
			String name = args[0];
			Location loc = player.getLocation();
			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ? AND name = ?");
				statement.setString(1, player.getUniqueId().toString());
				statement.setString(2, name);

				ResultSet res = statement.executeQuery();
				if(res.next()){
					SendMessage(sender, cmd, "「" + name + "」というホームは存在しています。「/home remove " + name + "」と打つことでホームを削除できます。");
					return true;
				}
				PreparedStatement statement2 = MySQL.getNewPreparedStatement("INSERT INTO home (player, uuid, name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
				statement2.setString(1, player.getName()); // player
				statement2.setString(2, player.getUniqueId().toString()); // uuid
				statement2.setString(3, name); // name
				statement2.setString(4, loc.getWorld().toString()); // world
				statement2.setDouble(5, loc.getX()); // x
				statement2.setDouble(6, loc.getY()); // y
				statement2.setDouble(7, loc.getZ()); // z
				statement2.setFloat(8, loc.getYaw()); // yaw
				statement2.setFloat(9, loc.getPitch()); // pitch

				statement.executeUpdate();
				SendMessage(sender, cmd, "「" + name + "」としてホームを設定しました。「/home " + name + "」と打つことでテレポートできます。");
				return true;
			} catch (SQLException | ClassNotFoundException e) {
				BugReporter(e);
				SendMessage(sender, cmd, "操作に失敗しました。");
				SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
}
