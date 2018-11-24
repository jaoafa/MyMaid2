package com.jaoafa.MyMaid2.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_Pin extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;

		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Limited") && !group.equalsIgnoreCase("QPPE")){
			SendMessage(sender, cmd, "このコマンドを使用できる権限を持っていません。");
			return true;
		}

		if(args.length == 0){
			SendMessage(sender, cmd, "PINコードを入力してください。");
			return true;
		}
		String pin = args[0];
		String regex = "^[0-9][0-9][0-9][0-9]$"; //正規表現
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(pin);
		if (!m.find()){
			SendMessage(sender, cmd, "4桁の半角数字を入力してください。");
			return true;
		}

		JSONObject json = getHttpsJson("https://jaoafa.com/pin/code.json", null);

		String code = (String) json.get("code");
		Long time = (Long) json.get("time");
		String status = (String) json.get("status");

		long utc = System.currentTimeMillis();
		String utcs = String.valueOf(utc);
		utcs = utcs.substring(0, utcs.length()-3);
		utc = Long.parseLong(utcs);
		//long times = Long.parseLong(time);

		if(utc > time){
			SendMessage(sender, cmd, "PINコードの有効期限が切れています。再度取得してください。(ErrorCode:1)");
			Bukkit.getLogger().info("PINERROR: 有効期間が切れ、無効のPINコードが入力されました。Now:\"" + utc + "\" PINClose:\"" + time + "\"");
			return true;
		}

		if(status.trim().equals("used")){
			SendMessage(sender, cmd, "PINコードが既に使用されています。再度取得してください。(ErrorCode:2)");
			Bukkit.getLogger().info("PINERROR: 既に使用されているコードが入力されました。 PINStatus:\"" + status + "\"");
			return true;
		}

		if(!code.trim().equals(pin.trim())){
			SendMessage(sender, cmd, "PINコードが誤っています。再度確認し正しいコードを入力してください。(ErrorCode:3)");
			Bukkit.getLogger().info("PINERROR: コードが誤っています。Input PIN:\"" + pin.trim() + "\" NetWork PIN:\"" + code.trim() + "\"");
			return true;
		}

		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが認証を通過しました。");
		PermissionsManager.setPermissionsGroup(player, "default1");
		SendMessage(sender, cmd, "登録しました。当鯖にお越しいただきありがとうございます。");
		//SendMessage(sender, cmd, "是非当鯖の宣伝をよろしくおねがいします！");
		SendMessage(sender, cmd, "minecraft.jpで投票する: https://minecraft.jp/servers/jaoafa.com");
		Bukkit.getLogger().info("PIN: \"" + player.getName() + "\"さんが登録されました。Input PIN:\"" + pin.trim() + "\" NetWork PIN:\"" + code.trim() + "\" PINStatus:\"" + status + "\" Now:\"" + utc + "\" PINClose:\"" + time + "\"");

		DiscordSend("223582668132974594", "__**[PIN]**__ " + player.getName() + "がアカウントアクティベーションを完了しました。");

		getHttpsString("https://jaoafa.com/pin/close.php?code=" + code);
		return true;
	}
}
