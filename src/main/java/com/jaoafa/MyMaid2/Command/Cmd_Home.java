package com.jaoafa.MyMaid2.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Cmd_Home extends MyMaid2Premise implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Cmd_Home(JavaPlugin plugin) {
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
			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ? AND name = ?");
				statement.setString(1, player.getUniqueId().toString());
				statement.setString(2, "default");

				ResultSet res = statement.executeQuery();
				if(res.next()){
					World world = Bukkit.getWorld(res.getString("world"));
					if(world == null){
						SendMessage(sender, cmd, "指定されたホームのワールドが取得できませんでした。");
						return true;
					}
					Location loc = new Location(world, res.getDouble("x"), res.getDouble("y"), res.getDouble("z"), res.getFloat("pitch"), res.getFloat("yaw"));
					player.teleport(loc);
					SendMessage(sender, cmd, "ホーム「default」にテレポートしました。");
					return true;
				}else{
					SendMessage(sender, cmd, "ホーム「default」は見つかりませんでした。");
					return true;
				}

			} catch (SQLException | ClassNotFoundException e) {
				BugReporter(e);
				SendMessage(sender, cmd, "操作に失敗しました。");
				SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				try {
					PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ?");
					statement.setString(1, player.getUniqueId().toString());

					ResultSet res = statement.executeQuery();
					SendMessage(sender, cmd, "----- " + player.getName() + "さんのホームリスト -----");
					int i = 0;
					while(res.next()){
						String name = res.getString("name");
						String world = res.getString("world");
						double x = res.getDouble("x");
						double y = res.getDouble("y");
						double z = res.getDouble("z");
						float yaw = res.getFloat("yaw");
						float pitch = res.getFloat("pitch");
						SendMessage(sender, cmd, name + ": " + world + " " + x + " " + y + " " + z + "(" + yaw + " " + pitch + ")");
						i++;
					}
					if(i == 0){
						SendMessage(sender, cmd, "見つかりませんでした。");
						return true;
					}
				} catch (SQLException | ClassNotFoundException e) {
					BugReporter(e);
					SendMessage(sender, cmd, "操作に失敗しました。");
					SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return true;
				}
				return true;
			}

			String name = args[0];
			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ? AND name = ?");
				statement.setString(1, player.getUniqueId().toString());
				statement.setString(2, name);

				ResultSet res = statement.executeQuery();
				if(res.next()){
					World world = Bukkit.getWorld(res.getString("world"));
					if(world == null){
						SendMessage(sender, cmd, "指定されたホームのワールドが取得できませんでした。");
						return true;
					}
					Location loc = new Location(world, res.getDouble("x"), res.getDouble("y"), res.getDouble("z"), res.getFloat("yaw"), res.getFloat("pitch"));
					player.teleport(loc);
					SendMessage(sender, cmd, "ホーム「" + name + "」にテレポートしました。");
					return true;
				}else{
					SendMessage(sender, cmd, "ホーム「" + name + "」は見つかりませんでした。");
					return true;
				}

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
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return plugin.onTabComplete(sender, cmd, alias, args);
		}
		Player player = (Player) sender;


		if (args.length == 1) {
			if(args[0].length() == 0){
				try {
					PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ?");
					statement.setString(1, player.getUniqueId().toString());
					ResultSet res = statement.executeQuery();
					List<String> returndata = new ArrayList<>();
					while(res.next()){
						returndata.add(res.getString("name"));
					}
					return returndata;
				} catch (SQLException | ClassNotFoundException e) {
					BugReporter(e);
					SendMessage(sender, cmd, "操作に失敗しました。");
					SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return plugin.onTabComplete(sender, cmd, alias, args);
				}
			}else{
				try {
					String name = args[0];
					PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ? AND name LIKE ?");
					statement.setString(1, player.getUniqueId().toString());
					statement.setString(2, name + "%");

					ResultSet res = statement.executeQuery();
					List<String> returndata = new ArrayList<>();
					while(res.next()){
						returndata.add(res.getString("name"));
					}
					return returndata;
				} catch (SQLException | ClassNotFoundException e) {
					BugReporter(e);
					SendMessage(sender, cmd, "操作に失敗しました。");
					SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return plugin.onTabComplete(sender, cmd, alias, args);
				}
			}
		}
		return plugin.onTabComplete(sender, cmd, alias, args);
	}
}
