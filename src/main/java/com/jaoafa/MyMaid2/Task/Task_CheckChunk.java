package com.jaoafa.MyMaid2.Task;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Task_CheckChunk extends BukkitRunnable {
	private Chunk chunk;
	public Task_CheckChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	@Override
	public void run() {
		String world = chunk.getWorld().getName();
		for (int y = 0; y < 128; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Block block = chunk.getBlock(x, y, z);
					if(block == null){
						continue;
					}
					Material type = block.getType();
					if(type == Material.SIGN || type == Material.SIGN_POST || type == Material.WALL_SIGN){
						// 看板
						Sign sign = (Sign) block.getState();
						String[] lines = sign.getLines();
						for(String message : lines){
							if(!MyMaid2Premise.check4bytechars(message)){
								continue;
							}
							MyMaid2Premise.DiscordSend("293856671799967744", "__**[4BYTESChecker_ChunkSearch]**__ " + "看板(" + world + " " + x + " " + y + " " + z + ")内に4バイト文字が含まれていました。\nメッセージ: ```" + message + "```\n判定された対象文字列: ```" + MyMaid2Premise.check4bytechars_MatchText(message) + "```");
						}
					}else if(type == Material.COMMAND || type == Material.COMMAND_CHAIN || type == Material.COMMAND_REPEATING){
						// コマンドブロック
						CommandBlock cmdb = (CommandBlock) block.getState();
						String message = cmdb.getCommand();
						if(!MyMaid2Premise.check4bytechars(message)){
							continue;
						}
						MyMaid2Premise.DiscordSend("293856671799967744", "__**[4BYTESChecker_ChunkSearch]**__ " + "コマンドブロック(" + world + " " + x + " " + y + " " + z + ")内に4バイト文字が含まれていました。\nメッセージ: ```" + message + "```\n判定された対象文字列: ```" + MyMaid2Premise.check4bytechars_MatchText(message) + "```");
					}
				}
			}
		}
	}
}
