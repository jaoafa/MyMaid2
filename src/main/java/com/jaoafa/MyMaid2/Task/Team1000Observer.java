package com.jaoafa.MyMaid2.Task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class Team1000Observer extends BukkitRunnable {
	@Override
	public void run() {
		for (Team team : Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
			if (team.getSize() >= 1000) { // そのチームに1000体以上いるなら
				Bukkit.broadcastMessage("[TeamObserver] チーム「" + team.getName() + "」に1000体以上のエンティティが含まれていたため、削除しました。(" + team.getSize() + " エンティティ)");
				team.unregister();
			}
		}
	}
}
