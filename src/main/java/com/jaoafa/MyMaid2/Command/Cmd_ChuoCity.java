package com.jaoafa.MyMaid2.Command;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Cmd_ChuoCity extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_ChuoCity(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)){
			SendMessage(sender, cmd, "このコマンドはプレイヤーからのみ実行できます。");
			return true;
		}
		/*
		 * /chuocity: 今いる場所の土地情報を取得します。
		 * /chuocity new: 土地を登録します。(管理部・モデレータのみ)
		 * /chuocity get <LandID>: 土地IDの土地を購入します。
		 */

		Command_Land(sender, cmd, commandLabel, args);
		return true;
	}

	/*
	 * コマンド実行時メソッド
	 */

	private void Command_Land(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = (Player) sender;
		WorldGuardPlugin wg = MyMaid2.getWorldGuard();
		Location loc = player.getLocation();
		Vector v = new Vector(loc.getX(), loc.getY(), loc.getZ());

		RegionManager rm = wg.getRegionManager(player.getWorld());

		List<String> regionlist = rm.getApplicableRegionsIDs(v);

		if(regionlist.size() == 0){
			SendMessage(sender, cmd, "この場所は登録されていません。");
			return;
		}

		List<ProtectedRegion> inheritance = new LinkedList<ProtectedRegion>();
		ProtectedRegion lastregion = null;
		for(String region : regionlist){
			ProtectedRegion r = rm.getRegion(region);
			if(lastregion == null){
				lastregion = r;
			}
			inheritance.add(r);
		}
		Collections.reverse(inheritance);

		ListIterator<ProtectedRegion> it = inheritance.listIterator(
				inheritance.size());
		int indent = 0;
		while (it.hasPrevious()) {
			ProtectedRegion cur = it.previous();

			String msg = "";

			// Put symbol for child
			if (indent != 0) {
				for (int i = 0; i < indent; i++) {
					msg += "  ";
				}
				msg += "\u2517";
			}

			// Put name
			SendMessage(sender, cmd, msg + cur.getId());
			indent++;
		}

		SendMessage(sender, cmd, "Owners: " + String.join(", ", lastregion.getOwners().getPlayers()));
		SendMessage(sender, cmd, "Members: " + String.join(", ", lastregion.getMembers().getPlayers()));
	}
}
