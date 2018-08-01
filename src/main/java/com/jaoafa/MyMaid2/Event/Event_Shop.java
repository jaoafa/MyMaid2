package com.jaoafa.MyMaid2.Event;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.Pointjao;

public class Event_Shop extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_Shop(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	// 1: [jaoShop] <- プレイヤー入力必要
	// 2: PLAYERNAME
	// 3: 売 10 : 5 買  <- プレイヤー入力必要。(販売者から見て売り買いポイント)
	// 4: アイテム名 OR STONEなどのMaterial.name()

	// 1スロット目は売らない

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChangeEvent(SignChangeEvent event){
		Player player = event.getPlayer();
		Block Block = event.getBlock();
		Location Loc = Block.getLocation();

		if(!event.getLine(0).equalsIgnoreCase("[jaoShop]")){
			return; // 1行目が[jaoShop]じゃなかったら終わり
		}

		Location ChestLoc = Loc.add(0, -1, 0);
		Block ChestBlock = ChestLoc.getWorld().getBlockAt(ChestLoc);
		if(ChestBlock.getType() != Material.CHEST){
			event.setLine(0, ChatColor.RED + "[jaoShop-ERROR]");
			event.setLine(1, "");
			event.setLine(2, "");
			event.setLine(3, "");
			player.sendMessage(ChatColor.RED + "[jaoShop-ERROR] " + ChatColor.RESET + "看板下のブロックがチェストではありません。");
			return; // 下のブロックがチェストじゃなかったら看板壊して終わり
		}
		Chest Chest = (Chest) ChestBlock.getState();
		Inventory inv = Chest.getInventory();
		ItemStack is = inv.getItem(0);
		if(is == null || is.getType() == Material.AIR){
			event.setLine(0, ChatColor.RED + "[jaoShop-ERROR]");
			event.setLine(1, "");
			event.setLine(2, "");
			event.setLine(3, "");
			player.sendMessage(ChatColor.RED + "[jaoShop-ERROR] " + ChatColor.RESET + "チェストの一番最初にアイテムがありませんでした。");
			return; // チェストインベントリの一番目にアイテムがなければ看板にエラー吐いて終わり
		}

		String Line3 = event.getLine(2); // 3行目
		String regex = "^([0-9]+) : ([0-9]+)$"; //正規表現
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(Line3);
		if(!m.find()){
			event.setLine(0, ChatColor.RED + "[jaoShop-ERROR]");
			event.setLine(1, "");
			event.setLine(2, "");
			event.setLine(3, "");
			player.sendMessage(ChatColor.RED + "[jaoShop-ERROR] " + ChatColor.RESET + "3行目が不適当です。\"10 : 0\"などと\"買 : 売\"の形式で記載してください。");
			return;
		}
		String BUY_Str = m.group(1);
		if(!isNumber(BUY_Str)){
			event.setLine(0, ChatColor.RED + "[jaoShop-ERROR]");
			event.setLine(1, "");
			event.setLine(2, "");
			event.setLine(3, "");
			player.sendMessage(ChatColor.RED + "[jaoShop-ERROR] " + ChatColor.RESET + "買いポイント数が不適当です。\"10 : 0\"などと\"買 : 売\"の形式で記載してください。");
			return;
		}
		int BUY = Integer.parseInt(BUY_Str);
		boolean BUYFlag = true;
		if(BUY == 0){
			BUYFlag = false;
		}


		String SELL_Str = m.group(0);
		if(!isNumber(SELL_Str)){
			event.setLine(0, ChatColor.RED + "[jaoShop-ERROR]");
			event.setLine(1, "");
			event.setLine(2, "");
			event.setLine(3, "");
			player.sendMessage(ChatColor.RED + "[jaoShop-ERROR] " + ChatColor.RESET + "売りポイント数が不適当です。\"10 : 0\"などと\"買 : 売\"の形式で記載してください。");
			return;
		}
		int SELL = Integer.parseInt(SELL_Str);
		boolean SELLFlag = true;
		if(SELL == 0){
			SELLFlag = false;
		}

		if(!BUYFlag && !SELLFlag){
			event.setLine(0, ChatColor.RED + "[jaoShop-ERROR]");
			event.setLine(1, "");
			event.setLine(2, "");
			event.setLine(3, "");
			player.sendMessage(ChatColor.RED + "[jaoShop-ERROR] " + ChatColor.RESET + "買いと売りの両方を無効化することはできません。");
			return;
		}

		String Name;
		if(is.getItemMeta().hasDisplayName()){
			Name = is.getItemMeta().getDisplayName();
		}else{
			Name = is.getType().name();
		}

		event.setLine(1, player.getName());
		//event.setLine(2, "買" + BUY + " : " + SELL + "売");
		event.setLine(3, Name);

		player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "以下の取引内容にて設定しました。");
		player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "取引運営者: " + player.getName());
		if(BUYFlag){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "買い(利用者が売る): " +  BUY + "jao");
		}else{
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "買い(利用者が売る): 無効");
		}
		if(SELLFlag){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "売り(利用者は買う): " +  SELL + "jao");
		}else{
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "売り(利用者は買う): 無効");
		}
		player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "商品表示名: " + Name);
		return;
	}
	// 左クリック: 売る(取引運営者が買っているものを売る・左側数値)
	// 右クリック: 買う(取引運営者が売っているものを買う・右側数値)
	// SHIFT(スニーク)左クリック: 壊す
	// SHIFT(スニーク)右クリック: アイテムなどの情報表示
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignClick(PlayerInteractEvent event){
		Action action = event.getAction();
		if(action == Action.LEFT_CLICK_AIR || action == Action.PHYSICAL || action == Action.RIGHT_CLICK_AIR){
			return;
		}
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Material material = event.getMaterial();
		if(material != Material.SIGN_POST && material != Material.WALL_SIGN){
			return;
		}
		Sign sign = (Sign) block.getState();
		String Line1 = sign.getLine(0);
		if(!Line1.equalsIgnoreCase("[jaoShop]")){
			return;
		}
		Location loc = block.getLocation();
		Location ChestLoc = loc.add(0, -1, 0);
		Block ChestBlock = ChestLoc.getWorld().getBlockAt(ChestLoc);
		if(ChestBlock.getType() != Material.CHEST){
			return;
		}
		if(action == Action.LEFT_CLICK_BLOCK){ // 左クリック
			if(player.isSneaking()){
				// SHIFT
				// BREAK
			}else{
				// NOT SHIFT
				SELL(event);
			}
		}else if(action == Action.RIGHT_CLICK_BLOCK){ // 右クリック
			if(player.isSneaking()){
				// SHIFT
				SHOW(event);
			}else{
				// NOT SHIFT
				BUY(event);
			}
		}
	}
	@SuppressWarnings("deprecation")
	void BUY(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Sign sign = (Sign) block.getState();
		Location loc = block.getLocation();
		Location ChestLoc = loc.add(0, -1, 0);
		Block ChestBlock = ChestLoc.getWorld().getBlockAt(ChestLoc);
		Chest chest = (Chest) ChestBlock.getState();

		// チェストに関連するアイテムがあるかどうか？
		// 必要なjaoポイントを持っているか？
		// →販売・購入

		String TransManager_Str = sign.getLine(1);
		if(TransManager_Str.equalsIgnoreCase(player.getName())){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "取引運営者が購入することはできません。");
			return;
		}
		OfflinePlayer TransManager = Bukkit.getOfflinePlayer(TransManager_Str);
		if(TransManager == null){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "取引運営者が取得できませんでした。");
			return;
		}
		Pointjao TransManager_jao;
		try{
			TransManager_jao = new Pointjao(TransManager);
		}catch(NullPointerException | ClassNotFoundException | SQLException e){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "取引運営者のjaoポイントデータが取得できませんでした。");
			BugReporter(e);
			return;
		}
		Pointjao Player_jao;
		try{
			Player_jao = new Pointjao(player);
		}catch(NullPointerException | ClassNotFoundException | SQLException e){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "jaoポイントデータが取得できませんでした。");
			BugReporter(e);
			return;
		}

		Inventory inv = chest.getInventory();

		String Line4 = sign.getLine(3);
		ItemStack MainItem = inv.getItem(0);
		if(MainItem == null){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "販売アイテムの情報が取得できませんでした。");
			return;
		}
		ItemStack item = null;
		int slot;
		for(int i = 1; i < inv.getSize(); i++){
			ItemStack is = inv.getItem(i);
			if(is == null){
				continue;
			}
			if(MainItem != is){
				continue;
			}
			item = is;
			slot = i;
			break;
		}
		if(item == null){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "販売できる商品がありませんでした。");
			return;
		}



		try {
			int jao = Player_jao.get();
		} catch (ClassNotFoundException | NullPointerException | SQLException e) {
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "jaoポイントデータが操作できませんでした。");
			BugReporter(e);
			return;
		}
	}
	void SELL(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Sign sign = (Sign) block.getState();
		Location loc = block.getLocation();
		Location ChestLoc = loc.add(0, -1, 0);
		Block ChestBlock = ChestLoc.getWorld().getBlockAt(ChestLoc);
		Chest chest = (Chest) ChestBlock.getState();

	}
	void SHOW(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Sign sign = (Sign) block.getState();
		Location loc = block.getLocation();
		Location ChestLoc = loc.add(0, -1, 0);
		Block ChestBlock = ChestLoc.getWorld().getBlockAt(ChestLoc);
		Chest chest = (Chest) ChestBlock.getState();

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material material = block.getType();
		if(material != Material.SIGN_POST && material != Material.WALL_SIGN){
			return;
		}
		Sign sign = (Sign) block.getState();
		String Line1 = sign.getLine(0);
		if(!Line1.equalsIgnoreCase("[jaoShop]")){
			return;
		}
		String Line2 = sign.getLine(1);
		if(!Line2.equalsIgnoreCase(player.getName())){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "あなたはこの看板を破壊できません。");
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChestBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material material = block.getType();
		if(material != Material.CHEST){
			return;
		}
		Location loc = block.getLocation();
		Location SignLoc = loc.add(0, 1, 0);
		Block Sign = loc.getWorld().getBlockAt(SignLoc);
		Material Signmaterial = Sign.getType();
		if(Signmaterial != Material.SIGN_POST && Signmaterial != Material.WALL_SIGN){
			return;
		}
		Sign sign = (Sign) Sign.getState();
		String Line1 = sign.getLine(0);
		if(!Line1.equalsIgnoreCase("[jaoShop]")){
			return;
		}
		String Line2 = sign.getLine(1);
		if(!Line2.equalsIgnoreCase(player.getName())){
			player.sendMessage(ChatColor.GOLD + "[jaoShop] " + ChatColor.RESET + "あなたはこのチェストを破壊できません。");
			event.setCancelled(true);
			return;
		}
	}
	boolean isNumber(String num) {
		try {
			int i = Integer.parseInt(num);
			if(i < 0){
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
