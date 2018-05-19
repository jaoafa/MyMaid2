package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Lib.Jail;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_Jail implements Listener {
	JavaPlugin plugin;
	public Event_Jail(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){ // 南の楽園外に出られるかどうか
		Location to = event.getTo();
		Player player = event.getPlayer();
		if(!Jail.isJail(player)){ // jailにいる
			return;
		}
		if(Jail.isJailArea(player)){ // 外に出られない
			return;
		}
		World World = Bukkit.getServer().getWorld("Jao_Afa");
		Location prison = new Location(World, 2856, 69, 2888);
		try{
			if(prison.distance(to) >= 30){
				player.sendMessage("[PRISON] " + ChatColor.GREEN + "あなたは南の楽園から出られません！");
				event.setCancelled(true);

				if(prison.distance(to) >= 50){
					player.teleport(prison);
				}

			}
		}catch(java.lang.IllegalArgumentException ex){
			player.teleport(prison);
		}
	}
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("Jao_Afa")){
			return;
		}
		World World = Bukkit.getServer().getWorld("Jao_Afa");
		Location prison = new Location(World, 2856, 69, 2888);
		if(prison.distance(event.getBlock().getLocation()) <= 50){
			if(event.getBlock().getType() == Material.COMMAND){
				player.sendMessage("[JAIL] " + ChatColor.GREEN + "楽園にコマンドブロックを設置できません。");
				event.setCancelled(true);
				return;
			}
			String group = PermissionsManager.getPermissionMainGroup(player);
			if(!group.equalsIgnoreCase("Admin")){
				if(!group.equalsIgnoreCase("Moderator")){
					player.sendMessage("[JAIL] " + ChatColor.GREEN + "楽園にブロックを設置できません。");
					event.setCancelled(true);
					return;
				}
			}
		}
		if(!Jail.isJail(player)){ // jailにいる
			return;
		}
		if(Jail.isJailBlock(player)){ // ブロックを設置破壊できるか
			return;
		}
		event.setCancelled(true);
		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを置けません。");
		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを置けません。");
	}
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("QPPE")){
			if(event.getBlock().getType() == Material.COMMAND){
				player.sendMessage("[COMMANDCHECK] " + ChatColor.GREEN + "QPPE権限ではコマンドブロックを破壊することができません。");
				event.setCancelled(true);
			}
		}
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("Jao_Afa")){
			return;
		}
		World World = Bukkit.getServer().getWorld("Jao_Afa");
		Location prison = new Location(World, 2856, 69, 2888);
		if(prison.distance(event.getBlock().getLocation()) <= 50){
			if(!group.equalsIgnoreCase("Admin")){
				if(!group.equalsIgnoreCase("Moderator")){
					player.sendMessage("[JAIL] " + ChatColor.GREEN + "楽園でブロックを破壊できません。");
					event.setCancelled(true);
					return;
				}
			}
		}
		if(!Jail.isJail(player)){ // jailにいる
			return;
		}
		if(Jail.isJailBlock(player)){ // ブロックを設置破壊できるか
			return;
		}
		event.setCancelled(true);
		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを壊せません。");
		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを壊せません。");
	}
	@EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!Jail.isJail(player)){ // jailにいる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを着火できません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを着火できません。");
    }
	@EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!Jail.isJail(player)){ // jailにいる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたは水や溶岩を撒けません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたは水や溶岩を撒けません。");
    }
	@EventHandler
	public void onPlayerPickupItemEvent(EntityPickupItemEvent event){
		if(!(event.getEntity() instanceof Player)){
			return;
		}
    	Player player = (Player) event.getEntity();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!Jail.isJail(player)){ // jailにいる
			return;
		}
  		event.setCancelled(true);
    }
	@EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!Jail.isJail(player)){ // jailにいる
			return;
		}
  		String command = event.getMessage();
    	String[] args = command.split(" ", 0);
    	if(args.length >= 2){
    		if(args[0].equalsIgnoreCase("/testment")){
    			return;
    		}
		}
  		event.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはコマンドを実行できません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはコマンドを実行できません。");
    }
	@EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!Jail.isJail(player)){
			return;
		}
		event.setCancelled(true);
	}
	@EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!Jail.isJail(player)){
			return;
		}
		event.setCancelled(true);
	}
}

