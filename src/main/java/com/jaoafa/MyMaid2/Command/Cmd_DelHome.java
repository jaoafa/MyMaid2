package com.jaoafa.MyMaid2.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;

public class Cmd_DelHome extends MyMaid2Premise implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Cmd_DelHome(JavaPlugin plugin) {
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

		if(args.length == 1){
			String name = args[0];
			try {
				PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM home WHERE uuid = ? AND name = ?");
				statement.setString(1, player.getUniqueId().toString());
				statement.setString(2, name);

				ResultSet res = statement.executeQuery();
				if(res.next()){
					int id = res.getInt("id");
					PreparedStatement statement2 = MySQL.getNewPreparedStatement("DELETE FROM home WHERE id = ?");
					statement2.setInt(1, id);
					statement.executeUpdate();
					SendMessage(sender, cmd, "ホーム「" + name + "」の削除に成功しました。");
					return true;
				}else{
					SendMessage(sender, cmd, "ホーム「" + name + "」の削除に失敗しました。");
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
		if (!(sender instanceof org.bukkit.entity.Player)) {
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
					List<String> returndata = new ArrayList<String>();
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
					List<String> returndata = new ArrayList<String>();
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
