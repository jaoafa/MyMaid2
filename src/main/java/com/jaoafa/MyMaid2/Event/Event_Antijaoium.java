package com.jaoafa.MyMaid2.Event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Jail;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.jaoSuperAchievement.AchievementAPI.AchievementAPI;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.AchievementType;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.Achievementjao;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class Event_Antijaoium extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	List<Integer> HEAL_jaoium = new ArrayList<Integer>();
	List<Integer> HEALTH_BOOST_jaoium = new ArrayList<Integer>();
	public Event_Antijaoium(JavaPlugin plugin) {
		this.plugin = plugin;

		HEAL_jaoium.add(-3);
		HEAL_jaoium.add(29);
		HEAL_jaoium.add(125);
		HEAL_jaoium.add(253);

		HEALTH_BOOST_jaoium.add(-7);
	}
	/**
	 * jaoiumと判定されるアイテムかどうか
	 * @param list PotionEffectのList
	 * @return jaoiumかどうか
	 * @author mine_book000
	 */
	private boolean isjaoium(List<PotionEffect> list){
		Boolean jaoium = false;
		for (PotionEffect po : list) {
			if(po.getType().equals(PotionEffectType.HEAL)){
				if(HEAL_jaoium.contains(po.getAmplifier())){
					// アウト
					jaoium = true;
				}
			}
			if(po.getType().equals(PotionEffectType.HEALTH_BOOST)){
				if(HEALTH_BOOST_jaoium.contains(po.getAmplifier())){
					// アウト
					jaoium = true;
				}
			}
		}
		return jaoium;
	}

	// -------------- ↓jaoium取得方法判定↓ -------------- //
	Map<String, String> Reason = new HashMap<>(); // プレイヤー : 理由

	@EventHandler(priority = EventPriority.MONITOR)
	public void ByPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String command = event.getMessage();

		if(!command.startsWith("/give")){
			return;
		}
		if(command.equalsIgnoreCase("/give")){
			return;
		}
		String[] commands = command.split(" ", 0);
		if(commands.length < 3){
			return;
		}

		String item = commands[2];
		if(!item.equalsIgnoreCase("splash_potion") && !item.equalsIgnoreCase("minecraft:splash_potion")){
			return;
		}

		String selector = commands[1];
		boolean SelectorToMe = false;
		boolean ALLPlayer = false;
		String ToPlayer = "";
		if(selector.equalsIgnoreCase("@p")){
			// 自分
			SelectorToMe = true;
		}else if(selector.equalsIgnoreCase(player.getName())){
			// 自分
			SelectorToMe = true;
		}else if(selector.equalsIgnoreCase("@a")){
			// 自分(プレイヤーすべて)
			SelectorToMe = true;
			ALLPlayer = true;
		}else if(selector.equalsIgnoreCase("@e")){
			// 自分(エンティティすべて)
			SelectorToMe = true;
			ALLPlayer = true;
		}else if(selector.equalsIgnoreCase("@s")){
			// 自分(実行者)
			SelectorToMe = true;
		}else{
			Player p = Bukkit.getPlayer(selector);
			if(p != null){
				ToPlayer = selector;
			}
		}
		if(SelectorToMe){
			Reason.put(player.getName(), player.getName() + "の実行したコマンド : " + command);
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			if(ToPlayer.equalsIgnoreCase(p.getName())){
				Reason.put(p.getName(), player.getName() + "の実行したコマンド : " + command);
				continue;
			}
			if(ALLPlayer){
				Reason.put(p.getName(), player.getName() + "の実行したコマンド : " + command);
				continue;
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void ByCommandBlock(ServerCommandEvent event) {
		if (!(event.getSender() instanceof BlockCommandSender)) return;
		BlockCommandSender sender = (BlockCommandSender) event.getSender();

		if (sender.getBlock() == null || !(sender.getBlock().getState() instanceof CommandBlock)) return;
		CommandBlock cmdb = (CommandBlock) sender.getBlock().getState();

		String command = cmdb.getCommand();
		if(!command.startsWith("/give") && !command.startsWith("give")){
			return;
		}
		if(command.equalsIgnoreCase("/give") || command.equalsIgnoreCase("give")){
			return;
		}
		String[] commands = command.split(" ", 0);
		if(commands.length < 3){
			return;
		}

		String item = commands[2];
		if(!item.equalsIgnoreCase("splash_potion") && !item.equalsIgnoreCase("minecraft:splash_potion")){
			return;
		}

		String selector = commands[1];
		boolean ALLPlayer = false;
		String ToPlayer = null;
		if(selector.equalsIgnoreCase("@p")){
			// 一番近い
			Player p = getNearestPlayer(cmdb.getLocation());
			if(p == null){
				return;
			}
			ToPlayer = p.getName();
		}else if(selector.equalsIgnoreCase("@a")){
			// プレイヤーすべて
			ALLPlayer = true;
		}else if(selector.equalsIgnoreCase("@e")){
			// エンティティすべて
			ALLPlayer = true;
		}else{
			Player p = Bukkit.getPlayer(selector);
			if(p != null){
				ToPlayer = selector;
			}
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			if(ToPlayer.equalsIgnoreCase(p.getName())){
				Reason.put(p.getName(), "コマンドブロック(" + cmdb.getLocation().getWorld().getName() + " " + cmdb.getLocation().getBlockX() + " " + cmdb.getLocation().getBlockY() + " " + cmdb.getLocation().getBlockZ() + ")の実行したコマンド : " + command);
				continue;
			}
			if(ALLPlayer){
				Reason.put(p.getName(), "コマンドブロック(" + cmdb.getLocation().getWorld().getName() + " " + cmdb.getLocation().getBlockX() + " " + cmdb.getLocation().getBlockY() + " " + cmdb.getLocation().getBlockZ() + ")の実行したコマンド : " + command);
				continue;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void ByItemPickup(EntityPickupItemEvent event) {
		if(!(event.getEntity() instanceof Player)){
			return;
		}
		Player player = (Player) event.getEntity();
		Item item = event.getItem();
		ItemStack hand = item.getItemStack();
		if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
			PotionMeta potion = (PotionMeta) hand.getItemMeta();
			if(isjaoium(potion.getCustomEffects())){
				Reason.put(player.getName(), player.getLocation().getWorld().getName() + " " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ() + "にて拾ったアイテム");
			}
		}
	}


	/**
	 * 指定されたLocationに一番近いプレイヤーを取得します。
	 * @param loc Location
	 * @return 一番近いプレイヤー
	 */
	public Player getNearestPlayer(Location loc){
		double closest = Double.MAX_VALUE;
		Player closestp = null;
		for(Player i : loc.getWorld().getPlayers()){
			double dist = i.getLocation().distance(loc);
			if (closest == Double.MAX_VALUE || dist < closest){
				closest = dist;
				closestp = i;
			}
		}
		if (closestp == null){
			return null;
		}
		return closestp;
	}

	// -------------- ↑jaoium取得方法判定↑ -------------- //

	@EventHandler(priority = EventPriority.HIGHEST)
	public void ItemPickup(EntityPickupItemEvent event) {
		if(!(event.getEntity() instanceof Player)){
			return;
		}
		Player player = (Player) event.getEntity();
		Item item = event.getItem();
		ItemStack hand = item.getItemStack();
		if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
			PotionMeta potion = (PotionMeta) hand.getItemMeta();
			if(isjaoium(potion.getCustomEffects())){
				player.sendMessage("[jaoium_Checker] " + ChatColor.GREEN + "あなたはjaoiumを拾いました。何か行動をする前に/clearをしないと、自動的に投獄されてしまうかもしれません！");
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void InvClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		Inventory clickedinventory = event.getClickedInventory();
		ItemStack[] is = inventory.getContents();
		if(Jail.isJail(player)){
			return;
		}
		Boolean jaoium = false;
		for(int n=0; n < is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					setjaoiumItemData(player, hand);
					if(inventory.getItem(n) != null) inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(clickedinventory != null) {
			is = clickedinventory.getContents();
			for(int n=0; n < is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						setjaoiumItemData(player, hand);
						clickedinventory.clear(n);
					}
				}
			}
			if(jaoium){
				clickedinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			if(!Achievementjao.getAchievement(player, new AchievementType(13))){
				player.sendMessage(AchievementAPI.getPrefix()
						+ "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			checkjaoiumLocation(player);
			try {
				Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			} catch (ClassNotFoundException | NullPointerException | SQLException e) {
				BugReporter(e);
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		if(Jail.isJail(player)){
			return;
		}
		Boolean jaoium = false;
		for(int n=0; n < is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					setjaoiumItemData(player, hand);
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}

		Boolean enderjaoium = false;
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n < is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					enderjaoium = isjaoium(potion.getCustomEffects());
					if(enderjaoium){
						setjaoiumItemData(player, hand);
						enderchestinventory.clear(n);
					}
				}
			}
			if(enderjaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium || enderjaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			if(!Achievementjao.getAchievement(player, new AchievementType(13))){
				player.sendMessage(AchievementAPI.getPrefix()
						+ "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			checkjaoiumLocation(player);
			try {
				Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			} catch (ClassNotFoundException | NullPointerException | SQLException e) {
				BugReporter(e);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		if(Jail.isJail(player)){
			return;
		}
		Boolean jaoium = false;
		for(int n=0; n < is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					setjaoiumItemData(player, hand);
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		Boolean enderjaoium = false;
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n < is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					enderjaoium = isjaoium(potion.getCustomEffects());
					if(enderjaoium){
						setjaoiumItemData(player, hand);
						enderchestinventory.clear(n);
					}
				}
			}
			if(jaoium || enderjaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			checkjaoiumLocation(player);
			try {
				Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			} catch (ClassNotFoundException | NullPointerException | SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileLaunchEvent(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		if(Jail.isJail(player)){
			return;
		}
		Boolean jaoium = false;
		for(int n=0; n < is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					setjaoiumItemData(player, hand);
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		Boolean enderjaoium = false;
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n < is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					enderjaoium = isjaoium(potion.getCustomEffects());
					if(enderjaoium){
						setjaoiumItemData(player, hand);
						enderchestinventory.clear(n);
					}
				}
			}
			if(enderjaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium || enderjaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			if(!Achievementjao.getAchievement(player, new AchievementType(13))){
				player.sendMessage(AchievementAPI.getPrefix() + "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			checkjaoiumLocation(player);
			try {
				Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			} catch (ClassNotFoundException | NullPointerException | SQLException e) {
				BugReporter(e);
			}
			event.setCancelled(true);
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPotionSplashEvent(PotionSplashEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		if(Jail.isJail(player)){
			return;
		}
		Boolean jaoium = false;
		for(int n=0; n < is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					setjaoiumItemData(player, hand);
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		Boolean ender_jaoium = false;
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n < is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					ender_jaoium = isjaoium(potion.getCustomEffects());
					if(ender_jaoium){
						setjaoiumItemData(player, hand);
						enderchestinventory.clear(n);
					}
				}
			}
			if(ender_jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium || ender_jaoium){
			if(!Achievementjao.getAchievement(player, new AchievementType(13))){
				player.sendMessage(AchievementAPI.getPrefix() + "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			checkjaoiumLocation(player);
			try {
				Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			} catch (ClassNotFoundException | NullPointerException | SQLException e) {
				BugReporter(e);
			}
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void OnBlockDispenseEvent(BlockDispenseEvent event){
		Boolean jaoium = false;
		ItemStack is = event.getItem();
		if(is.getType() == Material.SPLASH_POTION || is.getType() == Material.LINGERING_POTION){
			PotionMeta potion = (PotionMeta) is.getItemMeta();
			jaoium = isjaoium(potion.getCustomEffects());
		}
		if(jaoium){
			event.setCancelled(true);
		}
	}
	private void checkjaoiumLocation(Player player){
		Location loc = player.getLocation();
		String reason = "null";
		if(Reason.containsKey(player.getName())){
			reason = Reason.get(player.getName());
			Reason.remove(player.getName());
		}
		String ItemDataUrl = "null";
		if(ItemData.containsKey(player.getName())){
			ItemDataUrl = ItemData.get(player.getName());
			ItemData.remove(player.getName());
		}
		DiscordSend("223582668132974594", "**jaoium Location & Reason Notice**\n"
				+ "Player: " + player.getName() + "\n"
				+ "Location: " + loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "\n"
				+ "Reason: ``" + reason + "``\n"
				+ "ItemData: " + ItemDataUrl);
	}
	Map<String, String> ItemData = new HashMap<>();
	private void setjaoiumItemData(Player player, ItemStack is){

		if(ItemData.containsKey(player.getName())) return;
		YamlConfiguration yaml = new YamlConfiguration();

		yaml.set("data", is);

		StringBuilder builder = new StringBuilder();
		builder.append("/give @p splash_potion "); // "/give @p <アイテム> "
		builder.append(is.getAmount()); // "/give @p <アイテム> [個数]"
		builder.append(" "); // "/give @p <アイテム> [個数] "
		builder.append(is.getDurability()); // "/give @p <アイテム> [個数] [データ]"
		net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
		NBTTagCompound nbttag = nmsItem.getTag();
		if(nbttag != null){
			builder.append(" "); // "/give @p <アイテム> [個数] [データ] "
			builder.append(nbttag.toString()); // "/give @p <アイテム> [個数] [データ] [データタグ]"
		}

		String command = builder.toString();
		yaml.set("command", command);

		String code = yaml.saveToString();
		String name = "MyMaid2 Antijaoium jaoium ItemData & Command";
		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO cmd (player, uuid, title, command) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, player.getName());
			statement.setString(2, player.getUniqueId().toString());
			statement.setString(3, name);
			statement.setString(4, code);
			statement.executeUpdate();
			ResultSet res = statement.getGeneratedKeys();
			if(res == null || !res.next()){
				throw new IllegalStateException();
			}
			int id = res.getInt(1);
			ItemData.put(player.getName(), "https://jaoafa.com/cmd/" + id);
		}catch(SQLException | ClassNotFoundException e){
			ItemData.put(player.getName(), "null");
		}
	}
}
