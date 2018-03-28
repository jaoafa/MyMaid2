package com.jaoafa.MyMaid2.Task;

import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.Lib.Messenger;

public class AutoMessenger extends BukkitRunnable {
	/**
	 * 定期メッセージ(10分毎)
	 * @author mine_book000
	 */
	@Override
	public void run() {
		Messenger.RandomBroadcastMessage();
	}
}
