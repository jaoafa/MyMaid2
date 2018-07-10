package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Lib.EBan;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_EBan implements Listener {
	JavaPlugin plugin;
	public Event_EBan(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void OnEvent_LoginEBanCheck(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(!EBan.isEBan(player)){
			return;
		}
		String reason = EBan.getLastEBanReason(player);
		if(reason == null){
			return;
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator") && !group.equalsIgnoreCase("Regular")){
				continue;
			}
			p.sendMessage("[EBan] " + ChatColor.RED + "プレイヤー「" + player.getName() + "」は、「" + reason + "」という理由でEBanされています。");
			p.sendMessage("[EBan] " + ChatColor.RED + "詳しい情報は /eban status " + player.getName() + " でご確認ください。");
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){ // 南の楽園外に出られるかどうか
		Location to = event.getTo();
		Player player = event.getPlayer();
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		World World = Bukkit.getServer().getWorld("Jao_Afa");
		Location prison = new Location(World, 2856, 69, 2888);
		try{
			if(prison.distance(to) >= 30){
				player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたは南の楽園から出られません！");
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
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		event.setCancelled(true);
		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはブロックを置けません。");
		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはブロックを置けません。");
	}
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		event.setCancelled(true);
		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはブロックを壊せません。");
		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはブロックを壊せません。");
	}
	@EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはブロックを着火できません。");
  		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはブロックを着火できません。");
    }
	@EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたは水や溶岩を撒けません。");
  		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたは水や溶岩を撒けません。");
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
    	if(!EBan.isEBan(player)){ // EBanされてる
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
    	if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはコマンドを実行できません。");
  		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはコマンドを実行できません。");
    }
	@EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!EBan.isEBan(player)){ // EBanされてる
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
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		event.setCancelled(true);
	}
}

