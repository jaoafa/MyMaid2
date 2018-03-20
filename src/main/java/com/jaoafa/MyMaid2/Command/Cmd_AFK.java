package com.jaoafa.MyMaid2.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_AFK extends MyMaid2Premise implements CommandExecutor {
	public Cmd_AFK() {}

	private static Map<String, BukkitTask> afking = new HashMap<String, BukkitTask>();
	private static Map<String, ItemStack> head = new HashMap<String, ItemStack>();
	private static Map<String, Location> loc = new HashMap<String, Location>();
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
			ItemStack[] is = player.getInventory().getArmorContents();
			head.put(player.getName(), is[3]);
			ItemStack[] headice = {
					new ItemStack(Material.ICE),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(is[3])
			};
			player.getInventory().setArmorContents(headice);
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
			BukkitTask task = new Cmd_AFK.afking(JavaPlugin(), player).runTaskTimer(JavaPlugin(), 0L, 5L);
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
		if(head.containsKey(player.getName()) && head.get(player.getName()) != null){
			ItemStack[] is = player.getInventory().getArmorContents();

			ItemStack olditem = head.get(player.getName());
			if(olditem.getType() == Material.ICE){
				olditem = new ItemStack(Material.AIR);
			}

			setArmorContents(player, olditem, is[1], is[2], is[3]);
		}else{
			ItemStack[] is = player.getInventory().getArmorContents();
			setArmorContents(player, new ItemStack(Material.AIR), is[1], is[2], is[3]);
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

	static public class afking extends BukkitRunnable{
		JavaPlugin plugin;
		Player player;
    	public afking(JavaPlugin plugin, Player player) {
    		this.plugin = plugin;
    		this.player = player;
    	}
		@Override
		public void run() {

			if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
				player.sendMessage("[Summer2017] " + ChatColor.GREEN + "ワールド「Summer2017」ではAFK状態になることができません。");
				player.sendMessage("[Summer2017] " + ChatColor.GREEN + "Jao_Afaワールドに移動します…");
				World world = Bukkit.getServer().getWorld("Jao_Afa");
				if(world == null){
					player.sendMessage("[Summer2017] " + ChatColor.GREEN + "「Jao_Afa」ワールドの取得に失敗しました。");
					return;
				}
				Cmd_AFK.loc.put(player.getName(), player.getLocation());
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				player.teleport(loc);
				return;
			}

			//player.getWorld().playSound(player.getLocation(),Sound.EXPLODE,1,1);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			String listname = player.getPlayerListName();
			if(!listname.contains(ChatColor.DARK_GRAY + player.getName())){
				listname = listname.replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
				player.setPlayerListName(listname);
			}
		}
	}
}
