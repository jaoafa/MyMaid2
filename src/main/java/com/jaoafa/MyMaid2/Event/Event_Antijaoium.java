package com.jaoafa.MyMaid2.Event;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.AchievementType;
import com.jaoafa.jaoSuperAchievement.jaoAchievement.Achievementjao;

public class Event_Antijaoium extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_Antijaoium(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	List<Integer> HEAL_jaoium = new ArrayList<Integer>();
	List<Integer> HEALTH_BOOST_jaoium = new ArrayList<Integer>();
	/**
	 * jaoiumと判定されるアイテムかどうか
	 * @param list PotionEffectのList
	 * @return jaoiumかどうか
	 * @author mine_book000
	 */
	private boolean isjaoium(List<PotionEffect> list){

		HEAL_jaoium.add(-3);
		HEAL_jaoium.add(29);
		HEAL_jaoium.add(125);
		HEAL_jaoium.add(253);

		HEALTH_BOOST_jaoium.add(-7);

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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void InvClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		Inventory clickedinventory = event.getClickedInventory();
		ItemStack[] is = inventory.getContents();
		/*if(Jail.isJail(player)){
			return;
		}*/
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(clickedinventory != null) {
			is = clickedinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
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
				player.sendMessage("[" + ChatColor.RED + "j" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "S" + ChatColor.AQUA + "u" + ChatColor.BLUE + "p" + ChatColor.DARK_BLUE + "e" + ChatColor.RED + "r" + ChatColor.GOLD + "A" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + ChatColor.AQUA + "i" + ChatColor.BLUE + "e" + ChatColor.DARK_BLUE + "v" + ChatColor.RED + "e" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.AQUA + "t" + ChatColor.RESET + "] "
						+ "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			//Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
		}
	}
	@EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			if(!Achievementjao.getAchievement(player, new AchievementType(13))){
				player.sendMessage("[" + ChatColor.RED + "j" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "S" + ChatColor.AQUA + "u" + ChatColor.BLUE + "p" + ChatColor.DARK_BLUE + "e" + ChatColor.RED + "r" + ChatColor.GOLD + "A" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + ChatColor.AQUA + "i" + ChatColor.BLUE + "e" + ChatColor.DARK_BLUE + "v" + ChatColor.RED + "e" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.AQUA + "t" + ChatColor.RESET + "] "
						+ "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			//Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
		}
	}

    @EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			//Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
		}
	}
	@EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			if(!Achievementjao.getAchievement(player, new AchievementType(13))){
				player.sendMessage("[" + ChatColor.RED + "j" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "S" + ChatColor.AQUA + "u" + ChatColor.BLUE + "p" + ChatColor.DARK_BLUE + "e" + ChatColor.RED + "r" + ChatColor.GOLD + "A" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + ChatColor.AQUA + "i" + ChatColor.BLUE + "e" + ChatColor.DARK_BLUE + "v" + ChatColor.RED + "e" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.AQUA + "t" + ChatColor.RESET + "] "
						+ "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			//Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			event.setCancelled(true);
		}
	}
	@EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.SPLASH_POTION || hand.getType() == Material.LINGERING_POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			if(!Achievementjao.getAchievement(player, new AchievementType(13))){
				player.sendMessage("[" + ChatColor.RED + "j" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "S" + ChatColor.AQUA + "u" + ChatColor.BLUE + "p" + ChatColor.DARK_BLUE + "e" + ChatColor.RED + "r" + ChatColor.GOLD + "A" + ChatColor.YELLOW + "c" + ChatColor.GREEN + "h" + ChatColor.AQUA + "i" + ChatColor.BLUE + "e" + ChatColor.DARK_BLUE + "v" + ChatColor.RED + "e" + ChatColor.GOLD + "m" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "n" + ChatColor.AQUA + "t" + ChatColor.RESET + "] "
						+ "実績の解除中に問題が発生しました。もう一度お試しください。");
				return;
			}
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			//Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
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
}
