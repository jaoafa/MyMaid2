package com.jaoafa.MyMaid2.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Task.Task_AFKING;

public class Cmd_AFK extends MyMaid2Premise implements CommandExecutor {
	public Cmd_AFK() {}

	private static Map<String, BukkitTask> afking = new HashMap<>();
	private static Map<String, ItemStack> head = new HashMap<>();
	private static Map<String, Location> loc = new HashMap<>();
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

		if(!getAFKing(player)){
			// NOT AFKなら NOT AFK→AFK
			setAFK_True(player);
		}else{
			// AFKなら AFK→NOT AFK
			setAFK_False(player);
		}
		return true;
	}

	/**
	 * プレイヤーをAFKにする
	 *
	 * @param player 設定するプレイヤー
	 * @author mine_book000
	 */
	public static void setAFK_True(Player player){
		if(getHeadICE(player)){
			PlayerInventory playerinv = player.getInventory();
			head.put(player.getName(), playerinv.getHelmet());
			player.getInventory().setHelmet(new ItemStack(Material.ICE));
			player.updateInventory();
		}

		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is afk!");
		DiscordSend(player.getName() + " is afk!");

		String listname = player.getPlayerListName().replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
		player.setPlayerListName(listname);

		TitleSender().setTime_tick(player, 0, 99999999, 0);
		TitleSender().sendTitle(player, ChatColor.RED + "AFK NOW!", ChatColor.BLUE + "" + ChatColor.BOLD + "When you are back, please enter the command '/afk'.");
		TitleSender().setTime_tick(player, 0, 99999999, 0);

		try{
			BukkitTask task = new Task_AFKING(JavaPlugin(), player).runTaskTimer(JavaPlugin(), 0L, 5L);
			Cmd_AFK.afking.put(player.getName(), task);
		}catch(java.lang.NoClassDefFoundError e){
			BugReporter(e);
			Cmd_AFK.afking.put(player.getName(), null);
		}
	}

	/**
	 * プレイヤーのAFKを解除する
	 *
	 * @param player 解除するプレイヤー
	 * @author mine_book000
	 */
	public static void setAFK_False(Player player){
		PlayerInventory playerinv = player.getInventory();
		if(head.containsKey(player.getName()) && head.get(player.getName()) != null){
			ItemStack olditem = head.get(player.getName());
			if(olditem.getType() == Material.ICE){
				olditem = new ItemStack(Material.AIR);
			}

			playerinv.setHelmet(olditem);
		}else{
			playerinv.setHelmet(new ItemStack(Material.AIR));
		}

		if(afking.get(player.getName()) != null){
			afking.get(player.getName()).cancel();
		}
		afking.remove(player.getName());

		DiscordSend(player.getName() + " is now online!");
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is now online!");

		String listname = player.getPlayerListName().replaceAll(player.getName(), ChatColor.WHITE + player.getName());
		player.setPlayerListName(listname);

		TitleSender().resetTitle(player);

		if(loc.containsKey(player.getName())){
			player.teleport(loc.get(player.getName()));
			loc.remove(player.getName());
		}
	}

	/**
	 * プレイヤーがAFKかどうか調べる
	 *
	 * @param player 調べるプレイヤー
	 * @return AFKかどうか
	 * @author mine_book000
	 */
	public static boolean getAFKing(Player player){
		if(afking.containsKey(player.getName())){
			if(afking.get(player.getName()) != null){
				return true;
			}
		}
		return false;
	}

	/**
	 * ICEを頭にかぶせてもよいか調べる
	 *
	 * @param player 調べるプレイヤー
	 * @return かぶせてよいか
	 * @author mine_book000
	 */
	static boolean getHeadICE(Player player){
		return true;
	}
}
