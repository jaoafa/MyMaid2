package com.jaoafa.MyMaid2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Lib.Discord.DiscordEmbed;

public abstract class MyMaid2Premise {
	/**
	 * ヘルプメッセージを設定・取得します。
	 * @return ヘルプメッセージ
	 */
	/*public abstract String getHelpMessage();

	public abstract MyMaid2Premise_Usage[] getUsage();*/

	public static JavaPlugin JavaPlugin(){
		if(MyMaid2.javaplugin == null){
			throw new NullPointerException("getJavaPlugin()が呼び出されましたが、MyMaid2.javapluginはnullでした。");
		}
		return MyMaid2.javaplugin;
	}

	/**
	 * CommandSenderに対してメッセージを送信します。
	 * @param sender CommandSender
	 * @param cmd Commandデータ
	 * @param message メッセージ
	 */
	public static void SendMessage(CommandSender sender, Command cmd, String message) {
		sender.sendMessage("[" + cmd.getName().toUpperCase() +"] " + ChatColor.GREEN + message);
	}

	/**
	 * CommandSenderに対してヘルプメッセージと使い方を送信します。
	 * @param sender
	 * @param cmd
	 */
	public void SendUsageMessage(CommandSender sender, Command cmd){
		SendMessage(sender, cmd, "------- " + cmd.getName() + " --------");
		SendMessage(sender, cmd, cmd.getDescription());
		String CMDusage = cmd.getUsage();

		CMDusage = CMDusage.replaceAll("<command>", cmd.getName());

		if(CMDusage.contains("\n")){
			String[] usages = CMDusage.split("\n");
			for(String usage : usages){
				SendMessage(sender, cmd, usage);
			}
		}else{
			SendMessage(sender, cmd, CMDusage);
		}

	}

	/**
	 * 管理部とモデレータにメッセージを送信します。
	 * @param message 送信するメッセージ
	 */
	public void SendMessageToAdminModerator(String message){
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
				p.sendMessage("[MyMaid2] " + ChatColor.GREEN + message);
			}
		}
	}

	/**
	 * 指定されたLocationに一番近いプレイヤーを取得します。
	 * @param loc Location
	 * @return 一番近いプレイヤー
	 */
	public Player getNearestPlayer(Location loc){
		double closest = Double.MAX_VALUE;
		Player closestp = null;
		for(Player i : loc.getWorld().getPlayers()){
			double dist = i.getLocation().distance(loc);
			if (closest == Double.MAX_VALUE || dist < closest){
				closest = dist;
				closestp = i;
			}
		}
		if (closestp == null){
			return null;
		}
		return closestp;
	}

	/**
	 * 指定されたLocationに一番近いプレイヤーの距離を取得します。
	 * @param loc Location
	 * @return 一番近いプレイヤーの距離
	 */
	public double getNearestPlayerDistance(Location loc){
		double closest = Double.MAX_VALUE;
		Player closestp = null;
		for(Player i : loc.getWorld().getPlayers()){
			double dist = i.getLocation().distance(loc);
			if (closest == Double.MAX_VALUE || dist < closest){
				closest = dist;
				closestp = i;
			}
		}
		if (closestp == null){
			return -1;
		}
		return closest;
	}

	/**
	 * Discordへメッセージを送信します。(#server-chat)
	 * @param message 送信するメッセージ
	 * @return 送信できたかどうか
	 */
	public static boolean DiscordSend(String message){
		if(MyMaid2.discordtoken == null){
			throw new NullPointerException("DiscordSendが呼び出されましたが、discordtokenが登録されていませんでした。");
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bot " + MyMaid2.discordtoken);
		headers.put("User-Agent", "DiscordBot (https://jaoafa.com, v0.0.1)");

		Map<String, String> contents = new HashMap<>();
		contents.put("content", message);
		return postHttpsJsonByJson("https://discordapp.com/api/channels/" + MyMaid2.serverchat_id + "/messages", headers, contents);
	}

	/**
	 * Discordへチャンネルを指定してメッセージを送信します。
	 * @param channel 送信先のチャンネルID
	 * @param message 送信するメッセージ
	 * @return 送信できたかどうか
	 */
	public static boolean DiscordSend(String channel, String message){
		if(MyMaid2.discordtoken == null){
			throw new NullPointerException("DiscordSendが呼び出されましたが、discordtokenが登録されていませんでした。");
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bot " + MyMaid2.discordtoken);
		headers.put("User-Agent", "DiscordBot (https://jaoafa.com, v0.0.1)");

		Map<String, String> contents = new HashMap<>();
		contents.put("content", message);
		return postHttpsJsonByJson("https://discordapp.com/api/channels/" + channel + "/messages", headers, contents);
	}

	/**
	 * Discordへチャンネルを指定してメッセージを送信します。
	 * @param channel 送信先のチャンネルID
	 * @param message 送信するメッセージ
	 * @param embed 送信するEmbed
	 * @return 送信できたかどうか
	 */
	@SuppressWarnings("unchecked")
	public static boolean DiscordSend(String channel, String message, DiscordEmbed embed){
		if(MyMaid2.discordtoken == null){
			throw new NullPointerException("DiscordSendが呼び出されましたが、discordtokenが登録されていませんでした。");
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bot " + MyMaid2.discordtoken);
		headers.put("User-Agent", "DiscordBot (https://jaoafa.com, v0.0.1)");

		JSONObject paramobj = new JSONObject();
		paramobj.put("content", message);
		paramobj.put("embed", embed.buildJSON());
		return postHttpsJsonByJsonObj("https://discordapp.com/api/channels/" + channel + "/messages", headers, paramobj);
	}

	public static JSONArray DiscordGuildList(){
		if(MyMaid2.discordtoken == null){
			throw new NullPointerException("DiscordGuildListが呼び出されましたが、discordtokenが登録されていませんでした。");
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bot " + MyMaid2.discordtoken);
		headers.put("User-Agent", "DiscordBot (https://jaoafa.com, v0.0.1)");

		return getHttpJson("https://discordapp.com/api/guilds/189377932429492224/members?limit=1000", headers);
	}

	public static boolean DiscordAccountExist(String disid){
		JSONArray List = DiscordGuildList();
		for(int i = 0; i < List.size(); i++){
			JSONObject user = (JSONObject) List.get(i);
			JSONObject userdata = (JSONObject) user.get("user");
			String id = (String) userdata.get("id");
			if(id.equalsIgnoreCase(disid)){
				return true;
			}
		}
		return false;
	}

	private static JSONArray getHttpJson(String address, Map<String, String> headers){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpURLConnection connect = (HttpURLConnection) url.openConnection();
			connect.setRequestMethod("GET");
			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()) {
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				System.out.println("DiscordWARN: " + connect.getResponseMessage());
				BugReporter(new IOException(builder.toString()));
				return null;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(builder.toString());
			JSONArray json = (JSONArray) obj;
			return json;
		}catch(Exception e){
			BugReporter(e);
			return null;
		}
	}

	protected static JSONObject getHttpsJson(String address, Map<String, String> headers){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpsURLConnection connect = (HttpsURLConnection) url.openConnection();
			connect.setRequestMethod("GET");
			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()) {
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				System.out.println("ConnectWARN: " + connect.getResponseMessage());
				BugReporter(new IOException(builder.toString()));
				return null;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(builder.toString());
			JSONObject json = (JSONObject) obj;
			return json;
		}catch(Exception e){
			BugReporter(e);
			return null;
		}
	}

	protected static String getHttpsString(String address){
		StringBuilder builder = new StringBuilder();
		System.out.println("getHttpsJsonURL: " + address);
		try{
			URL url = new URL(address);

			HttpsURLConnection connect = (HttpsURLConnection) url.openConnection();
			connect.setRequestMethod("GET");

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				System.out.println("getHttpsJsonWARN: " + connect.getResponseMessage());
				BugReporter(new IOException(builder.toString()));
				return null;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			return builder.toString();
		}catch(Exception e){
			BugReporter(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static boolean postHttpsJsonByJson(String address, Map<String, String> headers, Map<String, String> contents){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
			connect.setRequestMethod("POST");

			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()){
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connect.getOutputStream());
			JSONObject paramobj = new JSONObject();
			for(Map.Entry<String, String> content : contents.entrySet()){
				paramobj.put(content.getKey(), content.getValue());
			}
			out.write(paramobj.toJSONString());
			out.close();

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				Bukkit.getLogger().warning("DiscordWARN: " + builder.toString());
				return false;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	private static boolean postHttpsJsonByJsonObj(String address, Map<String, String> headers, JSONObject paramobj){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
			connect.setRequestMethod("POST");

			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()){
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connect.getOutputStream());

			out.write(paramobj.toJSONString());
			out.close();

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				Bukkit.getLogger().warning("DiscordWARN: " + builder.toString());
				return false;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	static String bugreport_folder = null;
	/**
	 * バグリポーター<br>
	 * Discordにメッセージ送ったり、管理部・モデレーターに通知したりする。
	 * @param exception
	 */
	public static void BugReporter(Throwable exception){
		// フォルダ作成
		if(bugreport_folder == null){
			String Path = JavaPlugin().getDataFolder() + File.separator + "bugreport" + File.separator;
			File folder = new File(Path);
			if(folder.exists()){
				bugreport_folder = Path;
			}else{
				if(folder.mkdir()){
					JavaPlugin().getLogger().info("BugReportのリポートディレクトリの作成に成功しました。");
					bugreport_folder = Path;
				}else{
					JavaPlugin().getLogger().info("BugReportのリポートディレクトリの作成に失敗しました。");
				}
			}
		}

		// ID作成
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		String id = sdf.format(new Date());

		String filepath = bugreport_folder + id + ".json";
		File file = new File(filepath);

		// バグ記録ファイル作成・記録
		try {
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			exception.printStackTrace(pw);
			fw.write(pw.toString());
			fw.close();
			JavaPlugin().getLogger().info("Bugreport: ファイル書き込みに成功");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			JavaPlugin().getLogger().info("Bugreport: ファイル書き込みに失敗");
			e.printStackTrace();
		}

		// 管理部・モデレーターにメッセージを送信
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
				p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。");
				p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + exception.getMessage());
			}
		}

		// Discordへ送信
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		boolean res = DiscordSend("293856671799967744", "MyMaidでエラーが発生しました。" + "\n"
				+ "```" + sw.toString() + "```\n"
				+ "Cause: `" + exception.getCause() + "`\n"
				+ "報告ID: `" + id + "`");
		if(res){
			JavaPlugin().getLogger().info("Bugreport: Discord送信に成功");
		}else{
			JavaPlugin().getLogger().info("Bugreport: Discord送信に失敗");
		}

		// スタックトレース出力とか
		JavaPlugin().getLogger().info("Bugreport: エラー発生。報告ID: 「" + id + "」");
		exception.printStackTrace();
	}

	/**
	 * 指定した地点の地面の高さを返す
	 *
	 * @param loc
	 *            地面を探したい場所の座標
	 * @return 地面の高さ（Y座標）
	 *
	 * http://www.jias.jp/blog/?57
	 */
	public static int getGroundPos(Location loc) {

		// 最も高い位置にある非空気ブロックを取得
		loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

		// 最後に見つかった地上の高さ
		int ground = loc.getBlockY();

		// 下に向かって探索
		for (int y = loc.getBlockY(); y != 0; y--) {
			// 座標をセット
			loc.setY(y);

			// そこは太陽光が一定以上届く場所で、非固体ブロックで、ひとつ上も非固体ブロックか
			if (loc.getBlock().getLightFromSky() >= 8
					&& !loc.getBlock().getType().isSolid()
					&& !loc.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
				// 地上の高さとして記憶しておく
				ground = y;
			}
		}

		// 地上の高さを返す
		return ground;
	}
	/**
	 * 指定された期間内かどうか
	 * @param start 期間の開始
	 * @param end 期間の終了
	 * @return 期間内ならtrue、期間外ならfalse
	 * @see http://www.yukun.info/blog/2009/02/java-jsp-gregoriancalendar-period.html
	 */
	public static boolean isPeriod(Date start, Date end){
		Date now = new Date();
		if(now.after(start)){
			if(now.before(end)){
				return true;
			}
		}

		return false;
	}

	/**
	 * リストの内容をStringにし、その間にglueを挟んで返す(PHPのimplodeと同等)
	 * @see https://qiita.com/rkowase/items/7e73468d421c16add76d
	 * @param list リスト
	 * @param glue 挟むテキスト
	 * @return 処理したテキスト
	 */
	public static <T> String implode(Set<T> list, String glue) {
	    StringBuilder sb = new StringBuilder();
	    for (T e : list) {
	        sb.append(glue).append(e);
	    }
	    return sb.substring(glue.length());
	}
	public static <T> String implode(Collection<? extends Player> list, String glue) {
	    StringBuilder sb = new StringBuilder();
	    for (Player e : list) {
	        sb.append(glue).append(e.getName());
	    }
	    return sb.substring(glue.length());
	}
}