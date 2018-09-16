package com.jaoafa.MyMaid2.Command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Rider extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Rider(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0 || args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 1){
			if (!(sender instanceof Player)) {
				SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				return true;
			}
			Player player = (Player) sender;

			if(args[0].equalsIgnoreCase("leave")){
				if(player.getPassengers().size() == 0){
					SendMessage(sender, cmd, "あなたには誰も乗っていません。");
					return true;
				}
				//String rider = implodeEntityName(player.getPassengers(), ",");

				for(Entity entity : player.getPassengers()){
					if(player.removePassenger(entity)){
						SendMessage(sender, cmd, "プレイヤー・エンティティ「" + entity.getName() + "」を下ろしました。");
					}else{
						if(entity.getVehicle().eject()){
							SendMessage(sender, cmd, "プレイヤー・エンティティ「" + entity.getName() + "」を下ろしました。");
						}else{
							SendMessage(sender, cmd, "プレイヤー・エンティティ「" + entity.getName() + "」を下ろせませんでした。");
						}
					}
				}
				return true;
			}

			Player p = Bukkit.getPlayerExact(args[0]);
			if(p != null){
				if(player.getUniqueId().toString().equals(p.getUniqueId().toString())){
					SendMessage(sender, cmd, "処理できません。");
					return true;
				}
				Boolean bool = p.addPassenger(player);
				if(bool){
					SendMessage(sender, cmd, "プレイヤー「" + p.getName() + "」に乗りました。");
				}else{
					SendMessage(sender, cmd, "プレイヤー「" + p.getName() + "」に乗れませんでした。");
				}
			}else{
				Entity e = null;
				List<Entity> NearEntitys = player.getNearbyEntities(15.0, 15.0, 15.0);
				for(Entity near : NearEntitys){
					if(near.getType() == EntityType.PLAYER){
						continue;
					}
					if(near.getName().equalsIgnoreCase(args[0])){
						e = near;
						break;
					}
				}
				if(e == null){
					SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
					return true;
				}
				Boolean bool = e.addPassenger(player);
				if(bool){
					SendMessage(sender, cmd, "エンティティ「" + e.getName() + "」に乗りました。");
				}else{
					SendMessage(sender, cmd, "エンティティ「" + e.getName() + "」に乗れませんでした。");
				}
			}
			return true;
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("leave")){
				Entity rider = null;
				if (sender instanceof Player) {
					Player player = (Player) sender;
					List<Entity> WorldEntitys = player.getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(!e.getName().equalsIgnoreCase(args[1])){
							continue;
						}
						double distance = e.getLocation().distance(player.getLocation());
						if(d > distance){
							rider = e;
							d = distance;
						}
					}
					if(rider == null){
						SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender cmdb = (BlockCommandSender) sender;
					List<Entity> WorldEntitys = cmdb.getBlock().getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(!e.getName().equalsIgnoreCase(args[1])){
							continue;
						}
						double distance = e.getLocation().distance(cmdb.getBlock().getLocation());
						if(d > distance){
							rider = e;
							d = distance;
						}
					}
					if(rider == null){
						SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else{
					SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				if(rider.getPassengers().size() == 0){
					SendMessage(sender, cmd, "指定されたプレイヤー・エンティティ「" + rider.getName() + "」には誰も乗っていません。");
					return true;
				}
				//String rider = implodeEntityName(player.getPassengers(), ",");

				for(Entity entity : rider.getPassengers()){
					if(rider.removePassenger(entity)){
						SendMessage(sender, cmd, "指定されたプレイヤー・エンティティ「" + rider.getName() + "」からプレイヤー・エンティティ「" + entity.getName() + "」を下ろしました。");
					}else{
						if(entity.getVehicle().eject()){
							SendMessage(sender, cmd, "指定されたプレイヤー・エンティティ「" + rider.getName() + "」からプレイヤー・エンティティ「" + entity.getName() + "」を下ろしました。");
						}else{
							SendMessage(sender, cmd, "指定されたプレイヤー・エンティティ「" + rider.getName() + "」からプレイヤー・エンティティ「" + entity.getName() + "」を下ろせませんでした。");
						}
					}
				}
				return true;
			}
			// 乗せる
			Entity rider = null;
			Entity riding = null;
			Player p_rider = Bukkit.getPlayerExact(args[0]); // 乗る人
			Player p_riding = Bukkit.getPlayerExact(args[1]); // 乗られる人
			if(p_rider != null){
				rider = p_rider;
			}else{
				if (sender instanceof Player) {
					Player player = (Player) sender;
					List<Entity> WorldEntitys = player.getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[0])){
							continue;
						}
						double distance = e.getLocation().distance(player.getLocation());
						if(d > distance){
							rider = e;
							d = distance;
						}
					}
					if(rider == null){
						SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender cmdb = (BlockCommandSender) sender;
					List<Entity> WorldEntitys = cmdb.getBlock().getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[0])){
							continue;
						}
						double distance = e.getLocation().distance(cmdb.getBlock().getLocation());
						if(d > distance){
							rider = e;
							d = distance;
						}
					}
					if(rider == null){
						SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else{
					SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}

			}

			if(p_riding != null){
				riding = p_riding;
			}else{
				if (sender instanceof Player) {
					Player player = (Player) sender;
					List<Entity> WorldEntitys = player.getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[1])){
							continue;
						}
						double distance = e.getLocation().distance(player.getLocation());
						if(d > distance){
							riding = e;
							d = distance;
						}
					}
					if(riding == null){
						SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender cmdb = (BlockCommandSender) sender;
					List<Entity> WorldEntitys = cmdb.getBlock().getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[1])){
							continue;
						}
						double distance = e.getLocation().distance(cmdb.getBlock().getLocation());
						if(d > distance){
							riding = e;
							d = distance;
						}
					}
					if(riding == null){
						SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else{
					SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
			}
			String rider_type = "エンティティ", riding_type = "エンティティ";
			if(rider.getType() == EntityType.PLAYER){
				rider_type = "プレイヤー";
			}
			if(riding.getType() == EntityType.PLAYER){
				riding_type = "プレイヤー";
			}

			if(riding.getUniqueId().toString().equalsIgnoreCase(rider.getUniqueId().toString())){
				SendMessage(sender, cmd, "処理できません。");
				return true;
			}

			Boolean bool = riding.addPassenger(rider);
			if(bool){
				SendMessage(sender, cmd, rider_type + "「" + rider.getName() + "」を" + riding_type + "「" + riding.getName() + "」に乗せました。");
			}else{
				SendMessage(sender, cmd, rider_type + "「" + rider.getName() + "」を" + riding_type + "「" + riding.getName() + "」に乗せられませんでした。");
			}
			return true;
		}


		SendUsageMessage(sender, cmd);
		return true;
	}
}
