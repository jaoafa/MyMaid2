package com.jaoafa.MyMaid2.Command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Ck extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Ck(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// 処理方法等判定

		// Mob(not Player)乗車時にそのMobもキルするかどうか - 引数2番目
		boolean MobKillFlag = true; // デフォルトtrue(キルする)
		if(args.length >= 2){
			try {
				MobKillFlag = Boolean.valueOf(args[1]);
			}catch (NumberFormatException e){
				SendMessage(sender, cmd, "Mobキルフラグの指定が誤っています。(boolean)");
				return true;
			}
		}

		// トロッコ距離 - 引数3番目
		double MinecartDistance = 10; // デフォルト10、最大値100
		if(args.length >= 3){
			try {
				MinecartDistance = Double.valueOf(args[2]);
			}catch (NumberFormatException e){
				SendMessage(sender, cmd, "トロッコ距離の指定が誤っています。(double)");
				return true;
			}
			if(MinecartDistance > 100){
				SendMessage(sender, cmd, "トロッコ距離は100以下を指定してください。");
				return true;
			}
		}

		/*
		 * 処理方法判定 - 引数1番目
		 *
		 * false : 一番近いプレイヤーが、一番近いトロッコに乗っていなかったら削除。それ以上は処理しない(デフォルト)
		 * nearcheck : 近く(※1)のトロッコ全てに対し、そのトロッコに一番近いプレイヤーが乗っているかを判定して乗っていなければ削除。
		 * nearall : 近く(※1)のトロッコに全てに対し、トロッコに誰も(Mob含む)乗っていなかったら削除。近くとか関係なし
		 * all : 誰か乗っている・乗っていないに関わらず近く(※1)のトロッコを削除。危険
		 * true : nearcheck同等。互換性を維持するため
		 */
		String type = "false";
		if(args.length >= 1) type = args[0];
		if(type.equalsIgnoreCase("false")){
			RUNTypeFalse(sender, cmd, commandLabel, args, MinecartDistance, MobKillFlag);
			return true;
		}else if(type.equalsIgnoreCase("nearcheck")){
			RUNTypeNearCheck(sender, cmd, commandLabel, args, MinecartDistance, MobKillFlag);
			return true;
		}else if(type.equalsIgnoreCase("nearall")){
			RUNTypeNearALL(sender, cmd, commandLabel, args, MinecartDistance, MobKillFlag);
			return true;
		}else if(type.equalsIgnoreCase("all")){
			RUNTypeALL(sender, cmd, commandLabel, args, MinecartDistance, MobKillFlag);
			return true;
		}else if(type.equalsIgnoreCase("true")){
			RUNTypeNearCheck(sender, cmd, commandLabel, args, MinecartDistance, MobKillFlag);
			return true;
		}else{
			SendMessage(sender, cmd, "指定されたタイプ「" + type + "」に適合する処理方法は見つかりませんでした。");
			return true;
		}


		/*
		if ((sender instanceof BlockCommandSender)) {
			BlockCommandSender cmdb = (BlockCommandSender) sender;
			List<Entity> entitys = cmdb.getBlock().getWorld().getEntities();
			double min = 1.79769313486231570E+308;
			Player player = null;
			for(Player p: Bukkit.getServer().getOnlinePlayers()){
				Location location_p = p.getLocation();
				double distance;
				try{
					distance = cmdb.getBlock().getLocation().distance(location_p);
				}catch(java.lang.IllegalArgumentException e){
					distance = -1;
				}

				if(distance < min){
					if(!p.getWorld().getName().equals(cmdb.getBlock().getWorld().getName())){
						continue;
					}
					min = distance;
					player = p;
				}
			}
			Minecart minecart = null;
			min = 1.79769313486231570E+308;
			for (Iterator<Entity> i = entitys.iterator(); i.hasNext();) {
				Entity entity = i.next();
				if ( !(entity instanceof RideableMinecart) ) {
					minecart = null;
					continue;
				}
				Location location_p = entity.getLocation();
				double distance;
				try{
					distance = cmdb.getBlock().getLocation().distance(location_p);
				}catch(java.lang.IllegalArgumentException e){
					continue;
				}

				if(distance < min){
					if(!entity.getWorld().getName().equals(cmdb.getBlock().getWorld().getName())){
						minecart = null;
						continue;
					}
					min = distance;
					minecart = (Minecart) entity;
				}
				if(player == null){
					SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}
				if(minecart != null && player.isInsideVehicle() && player.getVehicle().getUniqueId().equals(minecart.getUniqueId())){
					minecart = null;
					continue;
				}
				if(args.length == 1){
					try{
						if(min > Integer.parseInt(args[0])){
							minecart = null;
							continue;
						}
					}catch(NumberFormatException e){
						// なにもしない
					}
				}
				if(minecart != null){
					if(args.length == 1 && args[0].equalsIgnoreCase("true")){
						for(Entity e : minecart.getPassengers()){
							if(e.getType() != EntityType.PLAYER) e.remove();
						}
					}
					minecart.remove();
					SendMessage(sender, cmd, "トロッコ「" + minecart.getName() + "(UUID:" + minecart.getUniqueId() +")」を削除しました。");
				}
			}

			if(minecart == null){
				SendMessage(sender, cmd, "削除できるトロッコはありませんでした。");
			}
			return true;

		}
		SendMessage(sender, cmd, "コマンドブロックのみ実行可能です。");
		return true;
		 */
	}

	/**
	 * 指定されたCommandSenderの座標を取得
	 * @param sender CommandSender
	 * @return 座標
	 */
	@Nullable
	private Location getSenderLocation(CommandSender sender){
		if(sender instanceof Player){
			Player player = (Player) sender;
			return player.getLocation();
		}else if(sender instanceof BlockCommandSender){
			BlockCommandSender cmdb = (BlockCommandSender) sender;
			return cmdb.getBlock().getLocation();
		}else{
			return null;
		}
	}

	/**
	 * 指定されたCommandSenderのワールドにいるエンティティを取得
	 * @param sender CommandSender
	 * @return 座標
	 */
	@Nullable
	private List<Entity> getSenderWorldEntity(CommandSender sender){
		if(sender instanceof Player){
			Player player = (Player) sender;
			return player.getWorld().getEntities();
		}else if(sender instanceof BlockCommandSender){
			BlockCommandSender cmdb = (BlockCommandSender) sender;
			return cmdb.getBlock().getWorld().getEntities();
		}else{
			return null;
		}
	}

	/**
	 * 一番近いプレイヤーが、一番近いトロッコに乗っていなかったら削除。(デフォルト)
	 */
	private void RUNTypeFalse(CommandSender sender, Command cmd, String commandLabel, String[] args, double MinecartDistance, boolean MobKillFlag){
		// 実行者の座標
		Location loc = getSenderLocation(sender);
		if(loc == null){
			SendMessage(sender, cmd, "現在地座標が取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者のワールドのエンティティを取得
		List<Entity> entitys = getSenderWorldEntity(sender);
		if(entitys == null) {
			SendMessage(sender, cmd, "ワールドのエンティティが取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者に一番近いトロッコを取得
		Minecart minecart = null; // トロッコ
		double cart_closest = Double.MAX_VALUE; // 距離
		for(Iterator<Entity> i = entitys.iterator(); i.hasNext();){
			Entity entity = i.next();
			if(!(entity instanceof RideableMinecart)){ // Minecartじゃなかったら次へ
				continue;
			}
			Minecart cart = (Minecart) entity;
			Location entityloc = entity.getLocation();
			if(!entityloc.getWorld().getUID().equals(loc.getWorld().getUID())){
				continue; // ありえないと思う(取得時にワールド指定してるから)けど、ワールド違ったら次へ
			}

			double dist = loc.distance(loc); // 距離を計算

			// Doubleの最大値か、すでに入力されている距離よりも近ければ代入
			if((cart_closest == Double.MAX_VALUE || dist < cart_closest)){
				cart_closest = dist;
				minecart = cart;
			}
		} // トロッコ取得終了

		if(cart_closest >= MinecartDistance){
			// トロッコへの距離がMinecartDistanceブロックよりも遠かったら処理を終了
			SendMessage(sender, cmd, "トロッコへの距離が" + MinecartDistance + "ブロック以上あった為処理を終了します。(INFO: " + cart_closest + "Block)");
			return;
		}

		if(minecart == null){
			SendMessage(sender, cmd, "トロッコが見つかりませんでした。");
			return;
		}

		// トロッコに一番近いプレイヤーを取得
		double player_closest = Double.MAX_VALUE;
		Player player = null;
		for(Player i : Bukkit.getOnlinePlayers()){
			if(!i.getWorld().getUID().equals(loc.getWorld().getUID())){
				continue;
			}
			double dist = i.getLocation().distance(minecart.getLocation()); // トロッコとプレイヤーの距離
			if (player_closest == Double.MAX_VALUE || dist < player_closest){
				player_closest = dist;
				player = i;
			}
		}

		if(player == null){
			// プレイヤーが取得できなかった
			SendMessage(sender, cmd, "近くのプレイヤーが取得できませんでした。");
			return;
		}

		// トロッコ判定
		if(minecart.getPassengers().isEmpty()){
			// 誰も乗ってない
			minecart.remove();
			SendMessage(sender, cmd, "トロッコ「" + minecart.getName() + "(UUID:" + minecart.getUniqueId() +")」を削除しました。(DEBUG: 1)");
			return;
		}
		boolean minecartPlayerFlag = false; // トロッコに乗っているのは一番近いプレイヤーか？
		for(Entity passenger : minecart.getPassengers()){
			if(!passenger.getUniqueId().equals(player.getUniqueId())){
				continue;
			}
			minecartPlayerFlag = true;
			break;
		}
		if(!minecartPlayerFlag){
			// 消すべきトロッコがない
			SendMessage(sender, cmd, "削除できるトロッコはありませんでした。");
			return;
		}

		// Mobをキルする必要があるならプレイヤー以外をキルする
		if(MobKillFlag){
			for(Entity passenger : minecart.getPassengers()){
				if(passenger.getType() == EntityType.PLAYER) continue;
				passenger.remove(); // キル(削除)
			}
		}

		minecart.remove();
		SendMessage(sender, cmd, "トロッコ「" + minecart.getName() + "(UUID:" + minecart.getUniqueId() +")」を削除しました。(DEBUG: 2)");
		return;
	}
	/**
	 * 近く(※1)のトロッコ全てに対し、そのトロッコに一番近いプレイヤーが乗っているかを判定して乗っていなければ削除。
	 */
	private void RUNTypeNearCheck(CommandSender sender, Command cmd, String commandLabel, String[] args, double MinecartDistance, boolean MobKillFlag){
		// 実行者の座標
		Location loc = getSenderLocation(sender);
		if(loc == null){
			SendMessage(sender, cmd, "現在地座標が取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者のワールドのエンティティを取得
		List<Entity> entitys = getSenderWorldEntity(sender);
		if(entitys == null) {
			SendMessage(sender, cmd, "ワールドのエンティティが取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者に近いトロッコすべてを取得
		List<Minecart> minecarts = new ArrayList<>();
		for(Iterator<Entity> i = entitys.iterator(); i.hasNext();){
			Entity entity = i.next();
			if(!(entity instanceof RideableMinecart)){ // Minecartじゃなかったら次へ
				continue;
			}
			Minecart cart = (Minecart) entity;
			Location entityloc = entity.getLocation();
			if(!entityloc.getWorld().getUID().equals(loc.getWorld().getUID())){
				continue; // ありえないと思う(取得時にワールド指定してるから)けど、ワールド違ったら次へ
			}

			double dist = loc.distance(loc); // 距離を計算

			if(dist >= MinecartDistance){
				// トロッコへの距離がMinecartDistanceブロックよりも遠かったら次へ
				continue;
			}
			minecarts.add(cart);
		} // トロッコ取得終了

		int removeCartCount = 0; // 削除したトロッコ数
		int checkCartCount = 0; // チェックしたトロッコ数

		// トロッコチェック
		for(Minecart cart : minecarts){
			checkCartCount++; // チェックするのでチェックトロッコ数増加

			// トロッコに一番近いプレイヤーを取得
			double player_closest = Double.MAX_VALUE;
			Player player = null;
			for(Player i : Bukkit.getOnlinePlayers()){
				if(!i.getWorld().getUID().equals(loc.getWorld().getUID())){
					continue;
				}
				double dist = i.getLocation().distance(cart.getLocation()); // トロッコとプレイヤーの距離
				if (player_closest == Double.MAX_VALUE || dist < player_closest){
					player_closest = dist;
					player = i;
				}
			}
			if(player == null) continue; // 近くのプレイヤーが居なかったら次へ

			boolean minecartPlayerFlag = false; // トロッコに乗っているのは一番近いプレイヤーか？
			for(Entity passenger : cart.getPassengers()){
				if(!passenger.getUniqueId().equals(player.getUniqueId())){
					continue;
				}
				minecartPlayerFlag = true;
				break;
			}
			if(!minecartPlayerFlag){
				// 消すべきトロッコがない
				SendMessage(sender, cmd, "削除できるトロッコはありませんでした。");
				return;
			}

			// Mobをキルする必要があるならプレイヤー以外をキルする
			if(MobKillFlag){
				for(Entity passenger : cart.getPassengers()){
					if(passenger.getType() == EntityType.PLAYER) continue;
					passenger.remove(); // キル(削除)
				}
			}

			cart.remove();
			removeCartCount++; // 削除したので削除トロッコ数増加
			return;
		}
		SendMessage(sender, cmd, "トロッコを" + removeCartCount + " / " + checkCartCount + "個削除しました。");
		return;
	}

	/**
	 * 近く(※1)のトロッコに全てに対し、トロッコに誰も(Mob含む)乗っていなかったら削除。
	 */
	private void RUNTypeNearALL(CommandSender sender, Command cmd, String commandLabel, String[] args, double MinecartDistance, boolean MobKillFlag){
		// 実行者の座標
		Location loc = getSenderLocation(sender);
		if(loc == null){
			SendMessage(sender, cmd, "現在地座標が取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者のワールドのエンティティを取得
		List<Entity> entitys = getSenderWorldEntity(sender);
		if(entitys == null) {
			SendMessage(sender, cmd, "ワールドのエンティティが取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者に近いトロッコすべてを取得
		List<Minecart> minecarts = new ArrayList<>();
		for(Iterator<Entity> i = entitys.iterator(); i.hasNext();){
			Entity entity = i.next();
			if(!(entity instanceof RideableMinecart)){ // Minecartじゃなかったら次へ
				continue;
			}
			Minecart cart = (Minecart) entity;
			Location entityloc = entity.getLocation();
			if(!entityloc.getWorld().getUID().equals(loc.getWorld().getUID())){
				continue; // ありえないと思う(取得時にワールド指定してるから)けど、ワールド違ったら次へ
			}

			double dist = loc.distance(loc); // 距離を計算

			if(dist >= MinecartDistance){
				// トロッコへの距離がMinecartDistanceブロックよりも遠かったら次へ
				continue;
			}
			minecarts.add(cart);
		} // トロッコ取得終了

		int removeCartCount = 0; // 削除したトロッコ数
		int checkCartCount = 0; // チェックしたトロッコ数

		// トロッコチェック
		for(Minecart cart : minecarts){
			checkCartCount++; // チェックするのでチェックトロッコ数増加

			// トロッコに一番近いプレイヤーを取得
			double player_closest = Double.MAX_VALUE;
			Player player = null;
			for(Player i : Bukkit.getOnlinePlayers()){
				if(!i.getWorld().getUID().equals(loc.getWorld().getUID())){
					continue;
				}
				double dist = i.getLocation().distance(cart.getLocation()); // トロッコとプレイヤーの距離
				if (player_closest == Double.MAX_VALUE || dist < player_closest){
					player_closest = dist;
					player = i;
				}
			}
			if(player == null) continue; // 近くのプレイヤーが居なかったら次へ

			if(!cart.getPassengers().isEmpty()) continue; // 誰か乗っていたら次へ

			// Mobが乗っていてもスキップするので、Mobの削除は無視

			cart.remove();
			removeCartCount++; // 削除したので削除トロッコ数増加
			return;
		}
		SendMessage(sender, cmd, "トロッコを" + removeCartCount + " / " + checkCartCount + "個削除しました。");
		return;
	}

	/**
	 *  誰か乗っている・乗っていないに関わらず近く(※1)のトロッコを削除。危険
	 */
	private void RUNTypeALL(CommandSender sender, Command cmd, String commandLabel, String[] args, double MinecartDistance, boolean MobKillFlag){
		// 実行者の座標
		Location loc = getSenderLocation(sender);
		if(loc == null){
			SendMessage(sender, cmd, "現在地座標が取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者のワールドのエンティティを取得
		List<Entity> entitys = getSenderWorldEntity(sender);
		if(entitys == null) {
			SendMessage(sender, cmd, "ワールドのエンティティが取得できませんでした。許可されていない実行者である場合があります。");
			return;
		}

		// 実行者に近いトロッコすべてを取得
		List<Minecart> minecarts = new ArrayList<>();
		for(Iterator<Entity> i = entitys.iterator(); i.hasNext();){
			Entity entity = i.next();
			if(!(entity instanceof RideableMinecart)){ // Minecartじゃなかったら次へ
				continue;
			}
			Minecart cart = (Minecart) entity;
			Location entityloc = entity.getLocation();
			if(!entityloc.getWorld().getUID().equals(loc.getWorld().getUID())){
				continue; // ありえないと思う(取得時にワールド指定してるから)けど、ワールド違ったら次へ
			}

			double dist = loc.distance(loc); // 距離を計算

			if(dist >= MinecartDistance){
				// トロッコへの距離がMinecartDistanceブロックよりも遠かったら次へ
				continue;
			}
			minecarts.add(cart);
		} // トロッコ取得終了

		int removeCartCount = 0; // 削除したトロッコ数

		// トロッコチェック
		for(Minecart cart : minecarts){
			// Mobをキルする必要があるならプレイヤー以外をキルする
			if(MobKillFlag){
				for(Entity passenger : cart.getPassengers()){
					if(passenger.getType() == EntityType.PLAYER) continue;
					passenger.remove(); // キル(削除)
				}
			}

			cart.remove();
			removeCartCount++; // 削除したので削除トロッコ数増加
			return;
		}
		SendMessage(sender, cmd, "トロッコを" + removeCartCount + "個削除しました。");
		return;
	}
}
