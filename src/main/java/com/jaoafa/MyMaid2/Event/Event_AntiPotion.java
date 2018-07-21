package com.jaoafa.MyMaid2.Event;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.jaoafa.MyMaid2.Lib.PermissionsManager;

/*
 * ポーション系は、いろいろちょっとめんどくさいので、以下のように権限によって使用の可否を決める
 * Limited: 所持を含む全部の動作を禁止
 * QPPE: Limited同様、所持を含む全部の動作を禁止。付与も禁止
 * Default: 所持、飲むことのみ許可、ただし透明化・スピードなどサーバに負荷がかかったり、他のプレイヤーに迷惑がかかる可能性のあるポーションは禁止
 * Regular以降: 全許可
 */
public class Event_AntiPotion implements Listener {
	JavaPlugin plugin;
	public Event_AntiPotion(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/**
	 * 指定されたポーションエフェクトが使用できるものかどうか調べます。
	 * @param effects ポーションエフェクトのリスト
	 * @return trueで許可、falseで不許可
	 */
	public static boolean ApplyCustomEffects(Collection<PotionEffect> effects){
		Set<PotionEffectType> effectTypes = new HashSet<PotionEffectType>();
		for(PotionEffect effect : effects){
			PotionEffectType type = effect.getType();
			effectTypes.add(type);
		}
		if(effectTypes.contains(PotionEffectType.INVISIBILITY)) return false; // 透明化
		if(effectTypes.contains(PotionEffectType.SPEED)) return false; // スピード

		//その他、jaoium関連
		if(effectTypes.contains(PotionEffectType.HEAL)) return false;
		if(effectTypes.contains(PotionEffectType.HEALTH_BOOST)) return false;
		return true;
	}

	@EventHandler
	public void PotionDrink(PlayerItemConsumeEvent event){ // ポーションを飲む
		Player player = event.getPlayer();
	    if(event.getItem() == null) return;
	    String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Limited")){
			// 所持を含む全部の動作を禁止
			event.setCancelled(true);
			return;
		}
		if(group.equalsIgnoreCase("QPPE")){
			// Limited同様、所持を含む全部の動作を禁止。付与も禁止
			ItemStack item = event.getItem();
			if(item.getType() != Material.POTION){
				return;
			}
			event.setCancelled(true);
		}else if(group.equalsIgnoreCase("Default")){
			// 所持、飲むことのみ許可、ただし透明化・スピードなどサーバに負荷がかかったり、他のプレイヤーに迷惑がかかる可能性のあるポーションは禁止
			ItemStack item = event.getItem();
			if(item.getType() != Material.POTION){
				return;
			}
			PotionMeta meta = (PotionMeta) item.getItemMeta();
			List<PotionEffect> effects = meta.getCustomEffects();
			if(!ApplyCustomEffects(effects)){
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void PotionSplash(PotionSplashEvent event){ // ポーションが割れる
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getPotion().getShooter();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Limited")){
			// 所持を含む全部の動作を禁止
			event.setCancelled(true);
			return;
		}
		if(group.equalsIgnoreCase("QPPE")){
			// Limited同様、所持を含む全部の動作を禁止。付与も禁止
			ItemStack item = event.getPotion().getItem();
			if(item.getType() != Material.POTION){
				return;
			}
			event.setCancelled(true);
		}else if(group.equalsIgnoreCase("Default")){
			// 所持、飲むことのみ許可、ただし透明化・スピードなどサーバに負荷がかかったり、他のプレイヤーに迷惑がかかる可能性のあるポーションは禁止
			ItemStack item = event.getPotion().getItem();
			if(item.getType() != Material.POTION){
				return;
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void PotionInteract(PlayerInteractEvent event){ // クリックするときとか
		Player player = event.getPlayer();
	    ItemStack item = event.getItem();
	    if(item == null) return;
		if(item.getType() != Material.POTION){
			return;
		}
		String group = PermissionsManager.getPermissionMainGroup(player);
	    if(group.equalsIgnoreCase("Limited")){
			// 所持を含む全部の動作を禁止
			event.setCancelled(true);
			return;
		}
		if(group.equalsIgnoreCase("QPPE")){
			// Limited同様、所持を含む全部の動作を禁止。付与も禁止
			event.setCancelled(true);
		}else if(group.equalsIgnoreCase("Default")){
			// 所持、飲むことのみ許可、ただし透明化・スピードなどサーバに負荷がかかったり、他のプレイヤーに迷惑がかかる可能性のあるポーションは禁止
			event.setCancelled(true);
		}
	}

	@EventHandler
    public void PlayerPickupItem(EntityPickupItemEvent event){
		if(!(event.getEntity() instanceof Player)){
			return;
		}
		Player player = (Player) event.getEntity();
		ItemStack item = event.getItem().getItemStack();
	    if(item == null) return;
	    String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Limited")){
			// 所持を含む全部の動作を禁止
			if(item.getType() != Material.POTION){
				return;
			}
			event.setCancelled(true);
			return;
		}
		if(group.equalsIgnoreCase("QPPE")){
			// Limited同様、所持を含む全部の動作を禁止。付与も禁止
		    if(item.getType() != Material.POTION){
				return;
			}
			event.setCancelled(true);
		}else if(group.equalsIgnoreCase("Default")){
			// 所持、飲むことのみ許可、ただし透明化・スピードなどサーバに負荷がかかったり、他のプレイヤーに迷惑がかかる可能性のあるポーションは禁止
		    if(item.getType() != Material.POTION){
				return;
			}
			event.setCancelled(true);
		}
	}
}
