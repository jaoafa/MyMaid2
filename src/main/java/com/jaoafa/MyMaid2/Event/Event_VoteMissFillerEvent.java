package com.jaoafa.MyMaid2.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MinecraftJPVoteMissFiller.CustomEvent.VoteMissFillerEvent;
import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Event_VoteMissFillerEvent extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_VoteMissFillerEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onVoteMissFillerEvent(VoteMissFillerEvent event){
		String player = event.getStringPlayer();
		DiscordSend("499922840871632896", ":mailbox_with_mail: **投票自動補填通知**: " + player + "の投票が受信されていなかったため、自動補填を行います。");
		Event_VoteReceived.VoteReceive(player);
	}
}
