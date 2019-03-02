package com.jaoafa.MyMaid2.Command;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Lib.Pointjao;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class Cmd_GetEsc extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;

		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular") && !group.equalsIgnoreCase("Default")){
			SendMessage(sender, cmd, "このコマンドは管理部・モデレーター・常連・デフォルト権限のプレイヤーのみ使用可能です。");
			return true;
		}

		int REQUIRED_jao = 1000;

		try {
			Pointjao pointjao = new Pointjao(player);
			if(!pointjao.has(REQUIRED_jao)){
				// 所持していない
				SendMessage(sender, cmd, "新しいEscapeJailアイテムの精製に失敗しました。精製に必要なjaoポイントが足りません！");
				return true;
			}
		} catch (ClassNotFoundException | NullPointerException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "新しいEscapeJailアイテムの精製に失敗しました。時間をおいてまたお試しください！(1)");
			return true;
		}

		ItemStack item = new ItemStack(Material.POTATO_ITEM);
		net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound nbttag = nms.getTag();
		if(nbttag == null){
			nbttag = new NBTTagCompound();
		}
		UUID uuid = UUID.randomUUID();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "新しいEscapeJailアイテムの精製に失敗しました。時間をおいてまたお試しください！(2)");
			return true;
		}
        byte[] digest = md.digest(uuid.toString().getBytes());
        String id = DatatypeConverter.printHexBinary(digest);
        if(id == null){
			SendMessage(sender, cmd, "新しいEscapeJailアイテムの精製に失敗しました。時間をおいてまたお試しください！(3)");
			return true;
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
			SendMessage(sender, cmd, "新しいEscapeJailアイテムの精製に失敗しました。時間をおいてまたお試しください！(3)");
			return true;
		}

        try {
			Pointjao pointjao = new Pointjao(player);
			pointjao.use(REQUIRED_jao, "EscapeJailアイテムを精製したため");
		} catch (ClassNotFoundException | NullPointerException | SQLException e) {
			BugReporter(e);
			SendMessage(sender, cmd, "新しいEscapeJailアイテムの精製に失敗しました。時間をおいてまたお試しください！(4)");
			return true;
		}

        player.getWorld().createExplosion(
        		player.getLocation().getX(),
        		player.getLocation().getY(),
        		player.getLocation().getZ(),
        		5,
        		false,
        		false
        );
        SendMessage(sender, cmd, "新しいEscapeJailアイテムの精製に成功しました。");
        SendMessage(sender, cmd, "このアイテムをインベントリの中に配置しておくと、Jail(EBan除く)をアイテム個数分だけ無効化できます！");

        if(player.getInventory().firstEmpty() == -1){
        	player.getLocation().getWorld().dropItem(player.getLocation(), item);
            SendMessage(sender, cmd, "インベントリがいっぱいだったため、あなたの足元にアイテムをドロップしました。");
        	Bukkit.getLogger().info("[EscapeJail] dropped to " + player.getName());
		}else{
			player.getInventory().addItem(item);
        	Bukkit.getLogger().info("[EscapeJail] gived to " + player.getName());
		}
        return true;
	}
}
