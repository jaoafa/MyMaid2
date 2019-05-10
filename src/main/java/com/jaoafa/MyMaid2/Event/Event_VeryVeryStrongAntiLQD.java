package com.jaoafa.MyMaid2.Event;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_VeryVeryStrongAntiLQD extends MyMaid2Premise implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void ItemInteract(PlayerInteractEvent event){ // クリックするときとか
		Player player = event.getPlayer();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("QPPE") && !group.equalsIgnoreCase("Default")){
			return; // QD以外はおわり
		}
		ItemStack item = event.getItem();

		if(item != null && item.getEnchantments() != null){
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
				Enchantment encha = e.getKey();
				int level = e.getValue();
				int maxlevel = encha.getMaxLevel();

				if(level <= maxlevel){
					continue;
				}
				// 本来の最大値より超えている場合
				DiscordSend("576549889715208192", "__**[VeryVeryStrongAntiLQD]**__ 本来の最大値を超えたアイテムが" + player.getName() + "から見つかりました。\n"
						+ "アイテム情報: " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")\n"
						+ "エンチャント/レベル/最大レベル: " + encha.getName() + " - " + level + " / " + maxlevel + "\n"
						+ "イベント: PlayerInteractEvent");
				item.removeEnchantment(encha);
				player.updateInventory();
			}
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void PlayerMove(PlayerMoveEvent event){ // 移動時
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();
		if(from.distance(to) == 0){
			// そこから動いていなかったらreturn
			return;
		}
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("QPPE") && !group.equalsIgnoreCase("Default")){
			return; // QD以外はおわり
		}
		ItemStack item = player.getInventory().getItemInMainHand();

		if(item != null && item.getEnchantments() != null){
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
				Enchantment encha = e.getKey();
				int level = e.getValue();
				int maxlevel = encha.getMaxLevel();

				if(level <= maxlevel){
					continue;
				}
				// 本来の最大値より超えている場合
				DiscordSend("576549889715208192", "__**[VeryVeryStrongAntiLQD]**__ 本来の最大値を超えたアイテムが" + player.getName() + "から見つかりました。\n"
						+ "アイテム情報: " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")\n"
						+ "エンチャント/レベル/最大レベル: " + encha.getName() + " - " + level + " / " + maxlevel + "\n"
						+ "イベント: PlayerMoveEvent (ItemInMainHand)");
				item.removeEnchantment(encha);
				player.updateInventory();
			}
		}

		item = player.getInventory().getItemInOffHand();

		if(item != null && item.getEnchantments() != null){
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
				Enchantment encha = e.getKey();
				int level = e.getValue();
				int maxlevel = encha.getMaxLevel();

				if(level <= maxlevel){
					continue;
				}
				// 本来の最大値より超えている場合
				DiscordSend("576549889715208192", "__**[VeryVeryStrongAntiLQD]**__ 本来の最大値を超えたアイテムが" + player.getName() + "から見つかりました。\n"
						+ "アイテム情報: " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")\n"
						+ "エンチャント/レベル/最大レベル: " + encha.getName() + " - " + level + " / " + maxlevel + "\n"
						+ "イベント: PlayerMoveEvent (ItemInOffHand)");
				item.removeEnchantment(encha);
				player.updateInventory();
			}
		}

		item = player.getInventory().getHelmet();
		if(item != null && item.getEnchantments() != null){
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
				Enchantment encha = e.getKey();
				int level = e.getValue();
				int maxlevel = encha.getMaxLevel();

				if(level <= maxlevel){
					continue;
				}
				// 本来の最大値より超えている場合
				DiscordSend("576549889715208192", "__**[VeryVeryStrongAntiLQD]**__ 本来の最大値を超えたアイテムが" + player.getName() + "から見つかりました。\n"
						+ "アイテム情報: " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")\n"
						+ "エンチャント/レベル/最大レベル: " + encha.getName() + " - " + level + " / " + maxlevel + "\n"
						+ "イベント: PlayerMoveEvent (Helmet)");
				item.removeEnchantment(encha);
				player.updateInventory();
			}
		}

		item = player.getInventory().getChestplate();

		if(item != null && item.getEnchantments() != null){
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
				Enchantment encha = e.getKey();
				int level = e.getValue();
				int maxlevel = encha.getMaxLevel();

				if(level <= maxlevel){
					continue;
				}
				// 本来の最大値より超えている場合
				DiscordSend("576549889715208192", "__**[VeryVeryStrongAntiLQD]**__ 本来の最大値を超えたアイテムが" + player.getName() + "から見つかりました。\n"
						+ "アイテム情報: " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")\n"
						+ "エンチャント/レベル/最大レベル: " + encha.getName() + " - " + level + " / " + maxlevel + "\n"
						+ "イベント: PlayerMoveEvent (Chestplate)");
				item.removeEnchantment(encha);
				player.updateInventory();
			}
		}

		item = player.getInventory().getLeggings();

		if(item != null && item.getEnchantments() != null){
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
				Enchantment encha = e.getKey();
				int level = e.getValue();
				int maxlevel = encha.getMaxLevel();

				if(level <= maxlevel){
					continue;
				}
				// 本来の最大値より超えている場合
				DiscordSend("576549889715208192", "__**[VeryVeryStrongAntiLQD]**__ 本来の最大値を超えたアイテムが" + player.getName() + "から見つかりました。\n"
						+ "アイテム情報: " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")\n"
						+ "エンチャント/レベル/最大レベル: " + encha.getName() + " - " + level + " / " + maxlevel + "\n"
						+ "イベント: PlayerMoveEvent (Leggings)");
				item.removeEnchantment(encha);
				player.updateInventory();
			}
		}

		item = player.getInventory().getBoots();

		if(item != null && item.getEnchantments() != null){
			for(Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
				Enchantment encha = e.getKey();
				int level = e.getValue();
				int maxlevel = encha.getMaxLevel();

				if(level <= maxlevel){
					continue;
				}
				// 本来の最大値より超えている場合
				DiscordSend("576549889715208192", "__**[VeryVeryStrongAntiLQD]**__ 本来の最大値を超えたアイテムが" + player.getName() + "から見つかりました。\n"
						+ "アイテム情報: " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")\n"
						+ "エンチャント/レベル/最大レベル: " + encha.getName() + " - " + level + " / " + maxlevel + "\n"
						+ "イベント: PlayerMoveEvent (Boots)");
				item.removeEnchantment(encha);
				player.updateInventory();
			}
		}
	}
}
