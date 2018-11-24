package com.jaoafa.MyMaid2.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_DiscordLink extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		try {
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
			PreparedStatement statement1 = MySQL.getNewPreparedStatement("SELECT * FROM discordlink_waiting WHERE authkey = ?");
			statement1.setString(1, AuthKey);
			ResultSet res1 = statement1.executeQuery();

			if(!res1.next()){
				SendMessage(sender, cmd, "指定されたAuthIDは見つかりませんでした。");
				return true;
			}

			id = res1.getInt("id");
			name = res1.getString("name");
			disid = res1.getString("disid");
			discriminator = res1.getString("discriminator");


			// すでにリンク要求されたMinecraftアカウントと紐づいているか？
			PreparedStatement statement2 = MySQL.getNewPreparedStatement("SELECT * FROM discordlink WHERE uuid = ? AND disid = ? AND disabled = ?");
			statement2.setString(1, uuid);
			statement2.setString(2, disid);
			statement2.setInt(3, 0);
			ResultSet res2 = statement2.executeQuery();

			if(res2.next()){
				SendMessage(sender, cmd, "すでにあなたのMinecraftアカウントと接続されています。");
				return true;
			}

			// リンク要求されたMinecraftアカウントが別のDiscordアカウントと紐づいていないか？
			PreparedStatement statement3 = MySQL.getNewPreparedStatement("SELECT * FROM discordlink WHERE uuid = ? AND disabled = ?");
			statement3.setString(1, uuid);
			statement3.setInt(2, 0);
			ResultSet res3 = statement3.executeQuery();

			if(res3.next()){
				SendMessage(sender, cmd, "すでにあなたのMinecraftアカウントは別のDiscordアカウントに接続されています。");
				return true;
			}

			// Discordアカウントが別のMinecraftアカウントと紐づいていないか？
			PreparedStatement statement4 = MySQL.getNewPreparedStatement("SELECT * FROM discordlink WHERE disid = ? AND disabled = ?");
			statement4.setString(1, disid);
			statement4.setInt(2, 0);
			ResultSet res4 = statement4.executeQuery();

			if(res4.next()){
				SendMessage(sender, cmd, "アカウントリンク要求をしたDiscordアカウントは既に他のMinecraftアカウントと接続されています。");
				return true;
			}

			// DiscordアカウントがDiscordチャンネルから退出していないかどうか
			if(!DiscordAccountExist(disid)){
				SendMessage(sender, cmd, "アカウントリンク要求をしたDiscordアカウントは既に当サーバのDiscordチャンネルから退出しています。");
				return true;
			}


			PreparedStatement statement5 = MySQL.getNewPreparedStatement("DELETE FROM discordlink_waiting WHERE id = ?");
			statement5.setInt(1, id);
			statement5.executeUpdate();

			PreparedStatement statement6 = MySQL.getNewPreparedStatement("INSERT INTO discordlink (player, uuid, name, disid, discriminator, pex) VALUES (?, ?, ?, ?, ?, ?);");
			statement6.setString(1, player.getName());
			statement6.setString(2, uuid);
			statement6.setString(3, name);
			statement6.setString(4, disid);
			statement6.setString(5, discriminator);
			statement6.setString(6, permission);
			statement6.executeUpdate();

			SendMessage(sender, cmd, "アカウントのリンクが完了しました。");
			DiscordSend("512242412635029514", ":loudspeaker:<@" + disid + ">さんのMinecraftアカウント連携を完了しました！ MinecraftID: " + player.getName());
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "操作に失敗しました。");
			SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
			return true;
		}
	}
}
