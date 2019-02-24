package com.jaoafa.MyMaid2.Command;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.jaoafa.MyMaid2.MyMaid2Premise;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class Cmd_Test extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			return true;
		}
		Player player = (Player) sender;

		if(args.length != 1){
			SendMessage(sender, cmd, "arg 1 only");
			return true;
		}

		if(args[0].equalsIgnoreCase("get")){
			ItemStack item = new ItemStack(Material.STONE);
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
				e.printStackTrace();
				SendMessage(sender, cmd, "NoSuchAlgorithmException error");
				return true;
			}
	        byte[] digest = md.digest(uuid.toString().getBytes());
	        String id = DatatypeConverter.printHexBinary(digest);
	        if(id == null){
				SendMessage(sender, cmd, "id null");
				return true;
			}
	        nbttag.setString("MyMaid_testID", id);
	        nms.setTag(nbttag);

	        item = CraftItemStack.asBukkitCopy(nms);
	        player.getInventory().addItem(item);
	        SendMessage(sender, cmd, "added");
	        return true;
		}else if(args[0].equalsIgnoreCase("check")){
			ItemStack item = player.getInventory().getItemInMainHand();
			if(item == null){
				SendMessage(sender, cmd, "item null");
				return true;
			}
			net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
			NBTTagCompound nbttag = nms.getTag();
			if(nbttag == null){
				SendMessage(sender, cmd, "nbttag null");
				return true;
			}
			String id = nbttag.getString("MyMaid_testID");
			if(id == null){
				SendMessage(sender, cmd, "ID: null*");
				return true;
			}else if(id.equals("")){
				SendMessage(sender, cmd, "ID: notfound"); // <-
				return true;
			}
			SendMessage(sender, cmd, "ID: " + id);
			return true;
		}
		SendMessage(sender, cmd, "/test <get|check>");
		return true;
	}
}
