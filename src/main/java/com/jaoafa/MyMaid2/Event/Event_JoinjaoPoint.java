package com.jaoafa.MyMaid2.Event;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Lib.Pointjao;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class Event_JoinjaoPoint extends MyMaid2Premise implements Listener {
	@EventHandler
	public void OnEvent_JoinjaoPoint(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		SimpleDateFormat date_full = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date_full.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		String today = date_full.format(cal.getTime());

		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");

		try {
			PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM login WHERE uuid = ? AND date >= cast(? as datetime) AND login_success = ?");
			statement.setString(1, uuid);
			statement.setString(2, today);
			statement.setBoolean(3, true);
			ResultSet res = statement.executeQuery();
			if(res.next()){
				return;
			}
		} catch (SQLException | ClassNotFoundException e) {
			BugReporter(e);
		}

		try {
			Pointjao Pjao = new Pointjao(player);
			Pjao.add(10, date.format(new Date()) + "のログインボーナス");
		}catch(NullPointerException e){
			BugReporter(e);
			return;
		}catch(ClassNotFoundException | SQLException e){
			BugReporter(e);
			return;
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2019/03/05 00:00:00");
			Date end = format.parse("2019/03/15 23:59:59");
			if(isPeriod(start, end)){
				String group = PermissionsManager.getPermissionMainGroup(player);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular") && !group.equalsIgnoreCase("Default")){
					return;
				}

				ItemStack item = new ItemStack(Material.POTATO_ITEM);
				net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
				NBTTagCompound nbttag = nms.getTag();
				if(nbttag == null){
					nbttag = new NBTTagCompound();
				}
				UUID item_uuid = UUID.randomUUID();
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("SHA-1");
				} catch (NoSuchAlgorithmException e) {
					BugReporter(e);
					player.sendMessage("[EscapeJail] " + ChatColor.RED + "新しいEscapeJailアイテムの精製に失敗しました。(2)");
					return;
				}
		        byte[] digest = md.digest(item_uuid.toString().getBytes());
		        String id = DatatypeConverter.printHexBinary(digest);
		        if(id == null){
		        	player.sendMessage("[EscapeJail] " + ChatColor.RED + "新しいEscapeJailアイテムの精製に失敗しました。(3)");
					return;
				}
		        nbttag.setString("MyMaid_EscapeJailID", id);
		        nms.setTag(nbttag);

		        item = CraftItemStack.asBukkitCopy(nms);

		        ItemMeta meta = item.getItemMeta();
		        meta.setDisplayName("EscapeItem - IMO");
		        List<String> lore = new ArrayList<String>();
		        lore.add("このアイテムをインベントリ内に配置しておくと、Jail(EBan除く)を無効化できます！(アイテム1つにつき1回限り)");
		        lore.add(ChatColor.RED + "NBTデータを削除してしまうと、アイテムは無効となります。また、複製をしても1つしか有効ではありません。");
		        meta.setLore(lore);
		        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 999, true);
		        item.setItemMeta(meta);

		        try {
					PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO uniqueitem (id, type) VALUES (?, ?)");
					statement.setString(1, id);
					statement.setString(2, "MyMaid_EscapeJailID");
					statement.executeUpdate();
				} catch (SQLException | ClassNotFoundException e) {
					BugReporter(e);
					player.sendMessage("[EscapeJail] " + ChatColor.RED + "新しいEscapeJailアイテムの精製に失敗しました。(3)");
					return;
				}

		        player.sendMessage("[EscapeJail] " + ChatColor.RED + "新しいEscapeJailアイテムの精製に成功しました。");
		        player.sendMessage("[EscapeJail] " + ChatColor.RED + "このアイテムをインベントリの中に配置しておくと、Jail(EBan除く)をアイテム個数分だけ無効化できます！");

		        if(player.getInventory().firstEmpty() == -1){
		        	player.getLocation().getWorld().dropItem(player.getLocation(), item);
		        	player.sendMessage("[EscapeJail] " + ChatColor.RED + "インベントリがいっぱいだったため、あなたの足元にアイテムをドロップしました。");
		        	Bukkit.getLogger().info("[EscapeJail] dropped to " + player.getName());
				}else{
					player.getInventory().addItem(item);
		        	Bukkit.getLogger().info("[EscapeJail] gived to " + player.getName());
				}
			}
		} catch (ParseException e) {
			BugReporter(e);
		}
	}
}
