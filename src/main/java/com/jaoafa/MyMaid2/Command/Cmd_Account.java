package com.jaoafa.MyMaid2.Command;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Cmd_Account extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Account(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	String url = "https://jaoafa.com/wp-login.php";
	public static String jaoAccountAPI = null;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(jaoAccountAPI == null){
			SendMessage(sender, cmd, "現在jaoアカウントに関する作業を行うことができません。開発者にお問い合わせください。");
			return true;
		}
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if (!(sender instanceof Player)) {
			SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 0){
			JSONObject json = getJson(player, "getaccount");
			if(getStatus(json)){
				SendMessage(sender, cmd, "--- jaoAccount Data ---");
				SendMessage(sender, cmd, "ID: " + json.get("userlogin"));
				SendMessage(sender, cmd, "URL: " + url);
				SendMessage(sender, cmd, "パスワードはセキュリティ保護のため、確認するにはリセットする必要があります。");
				SendMessage(sender, cmd, "/account resetを使ってパスワードをリセットしてください。");
			}else{
				SendMessage(sender, cmd, "jaoアカウントが存在しませんでした。");
				SendMessage(sender, cmd, "/account createを使ってアカウントを作成してください！");
			}

			return true;
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("reset")){
				JSONObject json = getJson(player, "psreset");
				if(getStatus(json)){
					SendMessage(sender, cmd, "jaoアカウントのパスワードリセットに成功しました。");
					SendMessage(sender, cmd, "ID: " + json.get("id"));
					SendMessage(sender, cmd, "Password: " + json.get("password"));
					SendMessage(sender, cmd, "URL: " + url);
					SendMessage(sender, cmd, "パスワードはセキュリティ保護のため、ログイン後変更することをお勧めします。");
				}else{
					SendMessage(sender, cmd, "jaoアカウントのパスワードリセットに失敗しました。");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("create")){
				if(getStatus(getJson(player, "getaccount"))){
					SendMessage(sender, cmd, "jaoアカウントは既に作成されています。");
				}else{
					JSONObject json = getJson(player, "create");
					if(getStatus(json)){
						SendMessage(sender, cmd, "jaoアカウントを作成しました！");
						SendMessage(sender, cmd, "ID: " + json.get("id"));
						SendMessage(sender, cmd, "Password: " + json.get("password"));
						SendMessage(sender, cmd, "URL: " + url);
					}else{
						SendMessage(sender, cmd, "jaoアカウントの作成に失敗しました…。");
						SendMessage(sender, cmd, "ErrorMessage: " + getErrorMessage(json));
					}
				}
				return true;
			}
		}else if(args.length >= 2){
			if(args[0].equalsIgnoreCase("setdesc")){
				String description = "";
				int c = 1;
				while(args.length > c){
					description += args[c];
					if(args.length != (c+1)){
						description += " ";
					}
					c++;
				}
				JSONObject json;
				try {
					json = getJson(player, "setdesc", "desc=" + URLEncoder.encode(description, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					json = getJson(player, "setdesc", "desc=" + description);
				}
				if(getStatus(json)){
					SendMessage(sender, cmd, "自己紹介の変更に成功しました。");
				}else{
					if(getErrorMessage(json).equalsIgnoreCase("User Not Found")){
						SendMessage(sender, cmd, "jaoアカウントが存在しませんでした。");
						SendMessage(sender, cmd, "/account createを使ってアカウントを作成してください！");
					}else{
						SendMessage(sender, cmd, "自己紹介の変更に失敗しました…。");
						SendMessage(sender, cmd, "ErrorMessage: " + getErrorMessage(json));
					}
				}
			}
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;
	}

	public static JSONObject getJson(Player player, String type){
		if(jaoAccountAPI == null){
			return null;
		}
		String data = getHttpsString(jaoAccountAPI + "?type=" + type + "&id=" + player.getName() + "&uuid=" + player.getUniqueId().toString());
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(data);
		} catch (ParseException e2) {
			BugReporter(e2);
			return null;
		}
		return obj;
	}
	public static JSONObject getJson(Player player, String type, String args){
		if(jaoAccountAPI == null){
			return null;
		}
		String data = getHttpsString(jaoAccountAPI + "?type=" + type + "id=" + player.getName() + "&uuid=" + player.getUniqueId() + "&" + args);
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(data);
		} catch (ParseException e2) {
			BugReporter(e2);
			return null;
		}
		return obj;
	}
	public static boolean getStatus(JSONObject json){
		if(json.containsKey("status")){
			return ((boolean) json.get("status"));
		}
		return false;
	}
	public static String getErrorMessage(JSONObject json){
		if(json.containsKey("message")){
			return ((String) json.get("message"));
		}
		return "";
	}
}
