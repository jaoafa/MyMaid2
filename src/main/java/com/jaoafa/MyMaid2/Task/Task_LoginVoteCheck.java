package com.jaoafa.MyMaid2.Task;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Lib.PlayerVoteData;

public class Task_LoginVoteCheck extends BukkitRunnable {
	private Player player;
	public Task_LoginVoteCheck(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		if(!player.isOnline()){
			return;
		}
		PlayerVoteData pvd = new PlayerVoteData(player);
		if(pvd.isVoted()){
			return;
		}
		player.sendMessage("[Vote] " + ChatColor.GREEN + "まだこのサーバに投票していないみたいです！");
		player.sendMessage("[Vote] " + ChatColor.GREEN + "よろしければ投票をお願いします！ https://jaoafa.com/vote");
		player.sendMessage("[Vote] " + ChatColor.GREEN + "投票したのに反映されていない！という場合はDiscordの#supportチャンネルにてお問い合わせください。");
	}
}
