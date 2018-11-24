package com.jaoafa.MyMaid2.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Discord.DiscordEmbed;

public class Cmd_Test extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;
		DiscordEmbed embed = new DiscordEmbed();
		embed.setTitle("New Player Join Info");
		embed.setUrl("https://jaoafa.com/user/" + player.getUniqueId().toString());
		embed.setDescription("新規プレイヤーがサーバにログインしました！\n※タイトルをクリックするとユーザページを開きます。");
		embed.setAuthor("jaotan", "https://jaoafa.com/", "https://jaoafa.com/wp-content/uploads/2018/03/IMG_20180326_070515.jpg", "https://jaoafa.com/wp-content/uploads/2018/03/IMG_20180326_070515.jpg");
		embed.addFields("プレイヤーID", player.getName(), false);
		embed.addFields("プレイヤー数", Bukkit.getServer().getOnlinePlayers().size() + "人", false);
		embed.addFields("プレイヤー", implode(Bukkit.getServer().getOnlinePlayers(), ", "), false);

		Bukkit.broadcastMessage(embed.build());

		DiscordSend("293856671799967744", "", embed);
		return true;
	}
}
