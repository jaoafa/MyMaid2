package com.jaoafa.MyMaid2.Command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_City extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_City(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/**
	 * デバックモードか<br>
	 * デバックモードならtrue, そうでなければfalse
	 */
	final boolean DebugMode = true;
	static final int LOCATION_X = 0;
	static final int LOCATION_Z = 1;

	Map<String, Set<Location>> Corner = new HashMap<>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
		if(dynmap == null || !dynmap.isEnabled()){
			SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
			return true;
		}
		if(!(sender instanceof Player)){
			SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
			return true;
		}
		Player player = (Player) sender;
		DynmapAPI dynmapapi = (DynmapAPI) dynmap;
		MarkerAPI markerapi = dynmapapi.getMarkerAPI();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}

		if(markerapi.getMarkerSet("towns") == null){
			Bukkit.getLogger().info("エリア情報を保管するDynmapSets「towns」が存在しないため、作成します。");
			markerapi.createMarkerSet("towns", "Towns", null, true);
		}

		if(args.length == 1){
			if(args[0].equalsIgnoreCase("addcorner")){
				// /city addcorner - コーナーを追加
				Set<Location> corner = new LinkedHashSet<Location>();
				if(Corner.containsKey(player.getName())){
					corner.addAll(Corner.get(player.getName()));
				}
				Location loc = player.getLocation();
				corner.add(loc);
				Corner.put(player.getName(), corner);
				SendMessage(sender, cmd, "次の地点をコーナーキューに追加しました: #" + corner.size() + " X: " + loc.getBlockX() + " Z: " + loc.getBlockZ());
				return true;
			}else if(args[0].equalsIgnoreCase("clearcorner")){
				// /city clearcorner - コーナーを削除
				if(Corner.containsKey(player.getName())){
					Corner.remove(player.getName());
					SendMessage(sender, cmd, "削除に成功しました。");
					return true;
				}else{
					SendMessage(sender, cmd, "削除に失敗しました。");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("undocorner")){
				// /city undocorner - ひとつ前に追加したコーナーを削除
				if(Corner.containsKey(player.getName())){
					Set<Location> corner = new LinkedHashSet<Location>();
					int now = 0;
					Location loc = null;
					for(Location one : Corner.get(player.getName())){
						if(now == (Corner.get(player.getName()).size() - 1)){
							loc = one;
							break;
						}
						corner.add(one);
						now++;
					}
					if(loc == null){
						SendMessage(sender, cmd, "ひとつ前のコーナーの削除に失敗しました。");
						return true;
					}

					Corner.put(player.getName(), corner);

					SendMessage(sender, cmd, "ひとつ前のコーナー(X: " + loc.getBlockX() + " / Z: " + loc.getBlockZ() + ")の削除に成功しました。");
					return true;
				}else{
					SendMessage(sender, cmd, "ひとつ前のコーナーの削除に失敗しました。");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("show")){
				// /city show [エリア名] - エリアの情報を表示。エリア名を設定しないといまいるところのエリア情報を表示(できるかどうか)
				long start = System.currentTimeMillis();
				Location now = player.getLocation();
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				boolean bool = true;
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					List<Location> locs = new ArrayList<>();
					for(int i = 0; i < areamarker.getCornerCount(); i++){
						Location loc = new Location(Bukkit.getWorld(areamarker.getWorld()), areamarker.getCornerX(i), -114514, areamarker.getCornerZ(i));
						locs.add(loc);
					}
					if(inArea(now, locs)){
						SendMessage(sender, cmd, "この場所は「" + areamarker.getLabel() + "」という名前のエリアです。");
						SendMessage(sender, cmd, "説明: " + areamarker.getDescription());
						bool = true;
					}
				}
				if(bool) SendMessage(sender, cmd, "この場所はエリア登録されていません。");

				if(DebugMode){
					long end = System.currentTimeMillis();
					System.out.println("処理時間: " + (end - start)  + "ms");
				}

				/*
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					// @param XorZ Xならtrue, Zならfalse
					// @param isMaxMaxならtrue, Minならfalse
					int maxX = getMaxOrMin(areamarker, true, true);
					int maxZ = getMaxOrMin(areamarker, false, true);
					int minX = getMaxOrMin(areamarker, true, false);
					int minZ = getMaxOrMin(areamarker, false, false);
					if(DebugMode) System.out.println("最大最小" + maxX + ", " + maxZ + ", " + minX + ", " + minZ);

					Location playerloc = player.getLocation();
					if(playerloc.getX() < minX || playerloc.getZ() > maxX){
						double x = playerloc.getX();
						double z = playerloc.getZ();
						int count = getCorners(areamarker, (int) x, (int) z);
						if(DebugMode) System.out.println("Count: " + count);

						if(count % 2 != 0){
							// 範囲内
							SendMessage(sender, cmd, "この場所は「" + areamarker.getLabel() + "」という名前のエリアです。");
							SendMessage(sender, cmd, "説明: " + areamarker.getDescription());

							if(DebugMode){
								long end = System.currentTimeMillis();
								System.out.println("処理時間: " + (end - start)  + "ms");
							}
							return true;
						}
					}
				}

				SendMessage(sender, cmd, "この場所はエリア登録されていません。");

				if(DebugMode){
					long end = System.currentTimeMillis();
					System.out.println("処理時間: " + (end - start)  + "ms");
				}
				return true;
				 */
				/*
				double minX = 0;
				double maxX = 0;
				boolean bool = true;
				int c = 0;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					for(int i = 0; i < areamarker.getCornerCount(); i++){
						double x = areamarker.getCornerX(i);
						if(minX > x){
							minX = x;
						}
						if(maxX < x){
							maxX = x;
						}
						c++;
					}
				}
				SendMessage(sender, cmd, "minX: " + minX);
				SendMessage(sender, cmd, "maxX: " + maxX);
				SendMessage(sender, cmd, "Count: " + c);

				if(minX < player.getLocation().getX() && maxX < player.getLocation().getX()){
					SendMessage(sender, cmd, "この場所はエリア登録されていません。");
				}else if(minX > player.getLocation().getX() && maxX > player.getLocation().getX()){
					SendMessage(sender, cmd, "+1");
				}else if(minX < player.getLocation().getX() && maxX > player.getLocation().getX()){
					SendMessage(sender, cmd, "線分より右にいるのか左にいるのかを判定し、結果次第");
				}
				 */
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("del")){
				String cityName = args[1];
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						areamarker.deleteMarker();
						SendMessage(sender, cmd, "指定されたエリア名のエリアを削除しました。");
						DiscordSend("254166905852657675", "::cityscape:**Cityデータが削除されました。(" + sdf.format(new Date()) + ")**\n"
								+ "プレイヤー: `"  + player.getName() + "`\n"
								+ "市名: `" + areamarker.getLabel() + "`");
						return true;
					}
				}
				SendMessage(sender, cmd, "指定されたエリア名のエリアは見つかりませんでした。");
				return true;
			}else if(args[0].equalsIgnoreCase("show")){
				String cityName = args[1];
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					SendMessage(sender, cmd, "指定されたエリア名のエリアは見つかりませんでした。");
					return true;
				}
				String label = select.getLabel();
				String desc = select.getDescription();
				SendMessage(sender, cmd, "--- " + label + " ---");
				SendMessage(sender, cmd, "説明: " + desc);
				return true;
			}else if(args[0].equalsIgnoreCase("addcorner")){
				String cityName = args[1];
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					SendMessage(sender, cmd, "指定されたエリア名のエリアは見つかりませんでした。");
					return true;
				}
				List<Double> Xs = new LinkedList<>();
				List<Double> Zs = new LinkedList<>();
				for(int i = 0; i < select.getCornerCount(); i++){
					Xs.add(select.getCornerX(i));
					Zs.add(select.getCornerZ(i));
				}
				Location loc = player.getLocation();
				Xs.add(loc.getBlockX() + 0.5);
				Zs.add(loc.getBlockZ() + 0.5);
				double[] ArrXs = Xs.stream().mapToDouble(Double::doubleValue).toArray();
				double[] ArrZs = Zs.stream().mapToDouble(Double::doubleValue).toArray();
				select.setCornerLocations(ArrXs, ArrZs);
				SendMessage(sender, cmd, "エリア「" + select.getLabel() + "」に新しいコーナーを追加しました。");
				SendMessage(sender, cmd, "順番を変更したりする場合は、「/city editcorner <Name>」コマンドをお使いください。");
			}else if(args[0].equalsIgnoreCase("editcorner")){
				SendMessage(sender, cmd, "システム障害が多くみられるため、一時的にeditcornerの使用を停止しています。");
				return true;
				/*
				String cityName = args[1];
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					SendMessage(sender, cmd, "指定されたエリア名のエリアは見つかりませんでした。");
					return true;
				}
				new _OpenGUI(player, select).runTaskLater(plugin, 1);
				return true;
				*/
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("add")){
				// /city add <Name> <Color> - エリアの範囲を設定(Dynmapに表示)
				if(!Corner.containsKey(player.getName())){
					// コーナー未登録
					SendMessage(sender, cmd, "コーナーが未登録です。/city addcornerを使用してコーナーを登録してください。");
					return true;
				}
				Set<Location> corner = Corner.get(player.getName());
				if(corner.size() < 2){
					// コーナー数が2つ未満
					SendMessage(sender, cmd, "コーナー数が足りません。/city addcornerを使用してコーナーを登録してください。");
					return true;
				}
				String cityName = args[1];
				String color = args[2];

				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(AreaMarker areamarker : markerset.getAreaMarkers()){
						if(areamarker.getLabel().equals(cityName)){
							SendMessage(sender, cmd, "登録しようとしたエリア名は既に存在します。再登録する場合は削除してください。");
							return true;
						}
					}
				}

				MarkerSet set = markerapi.getMarkerSet("towns");
				List<Double> Xs = new LinkedList<>();
				List<Double> Zs = new LinkedList<>();
				for(Location loc : corner){
					Xs.add(new Double(loc.getBlockX()) + 0.5);
					Zs.add(new Double(loc.getBlockZ()) + 0.5);
				}
				double[] ArrXs = Xs.stream().mapToDouble(Double::doubleValue).toArray();
				double[] ArrZs = Zs.stream().mapToDouble(Double::doubleValue).toArray();
				AreaMarker area = set.createAreaMarker(null, cityName, false, player.getWorld().getName(), ArrXs, ArrZs, true);
				if(area == null){
					SendMessage(sender, cmd, "登録に失敗しました。");
					return true;
				}
				area.setFillStyle(area.getFillOpacity(), 0x808080);

				int colorint;
				try{
					colorint = Integer.parseInt(color, 16);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "登録に失敗しました。");
					SendMessage(sender, cmd, "色の指定が不正です。");
					return true;
				}
				area.setLineStyle(area.getLineWeight(), area.getLineOpacity(), colorint);
				SendMessage(sender, cmd, "登録に成功しました。");
				Corner.remove(player.getName());
				DiscordSend("254166905852657675", "::cityscape:**Cityデータが追加されました。(" + sdf.format(new Date()) + ")**\n"
						+ "プレイヤー: `"  + player.getName() + "`\n"
						+ "市名: `" + area.getLabel() + "`");
				return true;
			}else if(args[0].equalsIgnoreCase("setcolor")){
				// /city color <Name> <Color> - エリアの色を変更
				String cityName = args[1];
				String color = args[2];
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					SendMessage(sender, cmd, "指定されたエリア名のエリアは見つかりませんでした。");
					return true;
				}
				int newcolorint;
				try{
					newcolorint = Integer.parseInt(color, 16);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "登録に失敗しました。");
					SendMessage(sender, cmd, "色の指定が不正です。");
					return true;
				}
				select.setLineStyle(select.getLineWeight(), select.getLineOpacity(), newcolorint);
				SendMessage(sender, cmd, "エリア色の変更に成功しました。");
				DiscordSend("254166905852657675", "::cityscape:**Cityデータが変更されました。(" + sdf.format(new Date()) + ") - SETCOLOR**\n"
						+ "プレイヤー: `"  + player.getName() + "`\n"
						+ "市名: `" + select.getLabel() + "`\n"
						+ "新市色: " + color + " (16進数: " + newcolorint + ")");
				return true;
			}else if(args[0].equalsIgnoreCase("setdesc")){
				String cityName = args[1];
				String desc = "";
				int c = 2;
				while(args.length > c){
					desc += args[c]+" ";
					c++;

				}
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					SendMessage(sender, cmd, "指定されたエリア名のエリアは見つかりませんでした。");
					return true;
				}
				desc = htmlspecialchars(desc);
				select.setDescription("<b>" + select.getLabel() + "</b><br />" +  desc);
				SendMessage(sender, cmd, "指定されたエリア名のエリアへ説明文を追加しました。");
				DiscordSend("254166905852657675", "::cityscape:**Cityデータが変更されました。(" + sdf.format(new Date()) + ") - SETDESC**\n"
						+ "プレイヤー: `"  + player.getName() + "`\n"
						+ "市名: `" + select.getLabel() + "`\n"
						+ "新説明: " + desc);
				return true;
			}
		}else if(args.length >= 3){
			if(args[0].equalsIgnoreCase("setdesc")){
				String cityName = args[1];
				String desc = "";
				int c = 2;
				while(args.length > c){
					desc += args[c]+" ";
					c++;

				}
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					SendMessage(sender, cmd, "指定されたエリア名のエリアは見つかりませんでした。");
					return true;
				}
				desc = htmlspecialchars(desc);
				select.setDescription("<b>" + select.getLabel() + "</b><br />" +  desc);
				SendMessage(sender, cmd, "指定されたエリア名のエリアへ説明文を追加しました。");
				DiscordSend("254166905852657675", "::cityscape:**Cityデータが変更されました。(" + sdf.format(new Date()) + ") - SETDESC**\n"
						+ "プレイヤー: `"  + player.getName() + "`\n"
						+ "市名: `" + select.getLabel() + "`\n"
						+ "新説明: " + desc);
				return true;
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}

	String htmlspecialchars(String str){

		String[] escape = {"&", "<", ">", "\"", "\'", "\n", "\t"};
		String[] replace = {"&amp;", "&lt;", "&gt;", "&quot;", "&#39;", "<br>", "&#x0009;"};

		for (int i = 0; i < escape.length; i++ ){
			str = str.replace(escape[i], replace[i]);
		}

		return str;
	}

	/**
	 * エリアの中にいるか調べる(ダミーエリア指定無)
	 * @param l 現在地
	 * @param locs エリアコーナー
	 * @return いる場合はtrue, いない場合はfalse
	 */
	public boolean inArea(Location l, List<Location> locs){
		return inArea(l, locs, false);
	}

	/**
	 * エリアの中にいるか調べる(ダミーエリア指定有)
	 * @param l 現在地
	 * @param locs エリアコーナー
	 * @param f ダミーエリアかどうか。trueはダミーエリア、falseはダミーエリアではない(0はダミーエリアでfalse,1はtrue ?)
	 * @return いる場合はtrue, いない場合はfalse
	 */
	public boolean inArea(Location l, List<Location> locs, boolean f) {
		if(isSquare(locs)){
			return getSW(locs).getX() < l.getX() && getSW(locs).getZ() < l.getZ() && getNE(locs).getX() > l.getX() && getNE(locs).getZ() > l.getZ();
		}else if(isTriangle(locs)){
			return ws(locs.get(0), locs.get(1), locs.get(3), l);
		} else { // 多角形
			Location nwst = new Location(Bukkit.getWorlds().get(0), min(locs, LOCATION_X), -114514, min(locs, LOCATION_Z));
			Location nest = new Location(Bukkit.getWorlds().get(0), max(locs, LOCATION_X), -114514, min(locs, LOCATION_Z));
			Location swst = new Location(Bukkit.getWorlds().get(0), min(locs, LOCATION_X), -114514, max(locs, LOCATION_Z));
			Location sest = new Location(Bukkit.getWorlds().get(0), min(locs, LOCATION_X), -114514, min(locs, LOCATION_Z));
			boolean inDummyArea = false;
			//ダミーエリアの生成
			List<Location> dummysNW = new ArrayList<>();
			List<Location> dummysNE = new ArrayList<>();
			List<Location> dummysSW = new ArrayList<>();
			List<Location> dummysSE = new ArrayList<>();
			for (Location lc : locs) {
				if (lc.getX() > nwst.getX() || lc.getZ() > nwst.getZ()) {
					dummysNW.add(lc);
				}

				if (lc.getX() < nest.getX() || lc.getZ() > nwst.getZ()) {
					dummysNE.add(lc);
				}
				if (lc.getX() < sest.getX() || lc.getZ() < sest.getZ()) {
					dummysSE.add(lc);
				}
				if (lc.getX() > swst.getX() || lc.getZ() < swst.getZ()) {
					dummysSW.add(lc);
				}
			}
			//ダミーエリアの中にいるか？
			if (inArea(l, dummysNW, !f) || inArea(l, dummysNE, !f) || inArea(l, dummysSE, !f) || inArea(l, dummysSW, !f)) { // ここでダミーエリアのダミーエリアは判定したいエリア
				inDummyArea = true;
			}

			if (inDummyArea) {
				return f;
			} else {
				return !f;
			}
		}
	}

	public Location getSW(List<Location> locs) throws IllegalArgumentException {
		double x = Double.MIN_VALUE, y, z = Double.MIN_VALUE;
		if (locs.size() == 0){
			throw new IllegalArgumentException();
		}else{
			y = locs.get(0).getY();
		}
		for (Location l : locs) {
			if (x > l.getX()) x = l.getX();
			if (z > l.getX()) z = l.getZ();
		}
		return new Location(Bukkit.getWorlds().get(0), x, y, z);
	}

	public Location getNE(List<Location> locs) throws IllegalArgumentException {
		double x = Double.MAX_VALUE, y, z = Double.MAX_VALUE;
		if (locs.size() == 0){
			throw new IllegalArgumentException();
		}else{
			y = locs.get(0).getY();
		}
		for (Location l : locs) {
			if (x < l.getX()) x = l.getX();
			if (z < l.getX()) z = l.getZ();
		}
		return new Location(Bukkit.getWorlds().get(0), x,y,z);
	}

	/**
	 * 四角形かどうか
	 * @param locs
	 * @return
	 */
	boolean isSquare(List<Location> locs){
		return locs.size() == 4 ? true : false; // 4つの点だったら四角形
	}

	/**
	 * 三角形かどうか
	 * @param locs
	 * @return
	 */
	boolean isTriangle(List<Location> locs){
		return locs.size() == 3 ? true : false; // 3つの点だったら三角形
	}

	/**
	 * 最小値
	 * @param locs
	 * @param flag
	 * @return
	 * @throws IllegalArgumentException
	 */
	public double min(List<Location> locs,int flag) throws IllegalArgumentException {
		switch (flag) {
		case LOCATION_X:
			double x = Double.MAX_VALUE;
			for (Location l : locs) {
				if (x > l.getX()) x = l.getX();
			}
			return x;
		case LOCATION_Z:
			double z = Double.MAX_VALUE;
			for (Location l : locs) {
				if (z > l.getZ()) z = l.getZ();
			}
			return z;
		default:
			throw new IllegalArgumentException();
		}
	}
	/**
	 * 最大値
	 * @param locs
	 * @param flag
	 * @return
	 * @throws IllegalArgumentException
	 */
	public double max(List<Location> locs, int flag) throws IllegalArgumentException {
		switch (flag) {
		case LOCATION_X:
			double x = Double.MIN_VALUE;
			for (Location l : locs) {
				if (x < l.getX()) x = l.getX();
			}
			return x;
		case LOCATION_Z:
			double z = Double.MIN_VALUE;
			for (Location l : locs) {
				if (z < l.getZ()) z = l.getZ();
			}
			return z;
		default:
			throw new IllegalArgumentException();
		}
	}
	/**
	 *
	 * @param locs
	 * @param l
	 * @return
	 */
	public boolean i(ArrayList<Location> locs, Location l) {
		int len = locs.size();
		for (int i=0;i>=len-2;i++) {
			if (ws(locs.get(0), locs.get(i+1), locs.get(i+2), l)) {
				return true;
			}
		}
		return false;
	}
	/**
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return p1,p2,p3を頂点とする三角形の中にp4があるか
	 * @see http://www5d.biglobe.ne.jp/~tomoya03/shtml/algorithm/Hougan.htm
	 */
	public boolean ws(Location p1, Location p2, Location p3, Location p4) {
		//三点が一直線上にあるなら居ないと判定
		if ((p1.getX() - p3.getX()) * (p1.getZ() - p2.getZ()) == (p1.getX() - p2.getX()) * (p1.getZ() - p3.getZ())) {
			return false;
		}
		if (intersectM(p1, p2, p4, p3) < 0) return false;
		if (intersectM(p1, p3, p4, p2) < 0) return false;
		if (intersectM(p2, p3, p4, p1) < 0) return false;
		return true;
	}

	public double intersectM(Location p1, Location p2, Location p3, Location p4) {
		return (
				(p1.getX() - p2.getX()) *
				(p3.getZ() - p1.getZ()) +
				(p1.getZ() - p2.getZ()) *
				(p1.getX() - p3.getX())
				) * (
						(p1.getX() - p2.getX()) *
						(p4.getZ() - p1.getZ()) +
						(p1.getZ() - p2.getZ()) *
						(p1.getX() - p4.getX())
						);
	}
}
