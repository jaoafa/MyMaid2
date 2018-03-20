package com.jaoafa.MyMaid2.Task;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Command.Cmd_AFK;

public class Task_AFK {
	public static Map<String,Long> afktime = new HashMap<String,Long>();
	/**
	 * AFKチェックタスク(1分毎)
	 * @author mine_book000
	 */
	public static class AFKChecker extends BukkitRunnable{
		@Override
		public void run() {
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(Cmd_AFK.getAFKing(player)){
					continue; // AFKかどうかを調べるのでAFKは無視
				}
				if(!afktime.containsKey(player.getName())){
					continue; // AFKタイムが設定されてないと処理しようがないので無視
				}
				if(player.getGameMode() == GameMode.SPECTATOR){
					continue; // スペクテイターモードは誰かにくっついて動いててもMoveイベント発生しないので無視
				}
				long nowtime = System.currentTimeMillis();
				long lastmovetime = afktime.get(player.getName());
				long sa = nowtime - lastmovetime; // 前回移動した時間から現在の時間の差を求めて3分差があったらAFK扱い
				if(sa >= 180000){
					Cmd_AFK.setAFK_True(player);
				}
			}
		}
	}
}
