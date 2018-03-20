package com.jaoafa.MyMaid2.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_DiscordLink extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_DiscordLink(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		String uuid = player.getUniqueId().toString();

		if(args.length != 1){
			SendMessage(sender, cmd, "引数は1つのみにしてください。(/discordlink <AuthID>)");
			return true;
		}
		String permission = PermissionsManager.getPermissionMainGroup(player);
		String AuthKey = args[0];

		// AuthKeyは「半角英数字」で構成されているか？
		if(!AuthKey.matches("[0-9a-zA-Z]+")){
			SendMessage(sender, cmd, "AuthKeyは英数字のみ受け付けています。");
			return true;
		}

		// 指定されたAuthKeyは存在するか？
		int id;
		String name, disid, discriminator;
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM discordlink_waiting WHERE authkey = ?");
			statement.setString(1, AuthKey);
			ResultSet res = statement.executeQuery();

			if(!res.next()){
				SendMessage(sender, cmd, "指定されたAuthIDは見つかりませんでした。");
				return true;
			}

			id = res.getInt("id");
			name = res.getString("name");
			disid = res.getString("disid");
			discriminator = res.getString("discriminator");
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}

		// すでにリンク要求されたMinecraftアカウントと紐づいているか？
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM discordlink WHERE uuid = ? AND disid = ? AND disabled = ?");
			statement.setString(1, uuid);
			statement.setString(2, disid);
			statement.setInt(3, 0);
			ResultSet res = statement.executeQuery();

			if(res.next()){
				SendMessage(sender, cmd, "すでにあなたのMinecraftアカウントと接続されています。");
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}

		// リンク要求されたMinecraftアカウントが別のDiscordアカウントと紐づいていないか？
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM discordlink WHERE uuid = ? AND disabled = ?");
			statement.setString(1, uuid);
			statement.setInt(2, 0);
			ResultSet res = statement.executeQuery();

			if(res.next()){
				SendMessage(sender, cmd, "すでにあなたのMinecraftアカウントは別のDiscordアカウントに接続されています。");
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}

		// Discordアカウントが別のMinecraftアカウントと紐づいていないか？
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM discordlink WHERE disid = ? AND disabled = ?");
			statement.setString(1, disid);
			statement.setInt(2, 0);
			ResultSet res = statement.executeQuery();

			if(res.next()){
				SendMessage(sender, cmd, "アカウントリンク要求をしたDiscordアカウントは既に他のMinecraftアカウントと接続されています。");
				return true;
			}
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}

		// DiscordアカウントがDiscordチャンネルから退出していないかどうか
		if(!DiscordAccountExist(disid)){
			SendMessage(sender, cmd, "アカウントリンク要求をしたDiscordアカウントは既に当サーバのDiscordチャンネルから退出しています。");
			return true;
		}

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("DELETE FROM discordlink_waiting WHERE id = ?");
			statement.setInt(1, id);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO discordlink (player, uuid, name, disid, discriminator, pex) VALUES (?, ?, ?, ?, ?, ?);");
			statement.setString(1, player.getName());
			statement.setString(2, uuid);
			statement.setString(3, name);
			statement.setString(4, disid);
			statement.setString(5, discriminator);
			statement.setString(6, permission);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}

		SendMessage(sender, cmd, "アカウントのリンクが完了しました。");
		DiscordSend("189377932429492224", ":loudspeaker:<@" + disid + ">さんのMinecraftアカウント連携を完了しました！ MinecraftID: " + player.getName());
		return true;
	}
}
