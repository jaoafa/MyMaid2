package com.jaoafa.MyMaid2.Event;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jaoafa.MyMaid2.MyMaid2;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Lib.Discord.DiscordEmbed;

public class Event_JoinAutoQPPE extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_JoinAutoQPPE(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void OnEvent_JoinjaoPoint(PlayerJoinEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		String pex = PermissionsManager.getPermissionMainGroup(player);
		if(!pex.equalsIgnoreCase("Limited")){
			return;
		}

		String url = MyMaid2.MCBansRepAPI;
		if(url == null){
			plugin.getLogger().warning("コンフィグにMCBansのReputationを取得するためのAPIが記載されていなかったため、Reputationチェックは動作しません。");
			return;
		}

		JSONObject json = getHttpJson(url + "?u=" + uuid.toString());
		if(!json.containsKey("status")){
			return;
		}

		Boolean status = (Boolean) json.get("status");
		String reputation = "取得失敗";
		if(!status){
			plugin.getLogger().warning("Reputationチェックが正常に完了しませんでした。");
			// return;
		}else{
			reputation = (String) json.get("reputation");
		}

		boolean jaotanAutoUp;
		if(reputation.equalsIgnoreCase("10")){
			plugin.getLogger().warning("Reputation: " + reputation + " -> UP to QPPE");
			PermissionsManager.setPermissionsGroup(player, "QPPE");
			jaotanAutoUp = true;
		}else{
			plugin.getLogger().warning("Reputation: " + reputation + " -> NOT UP");
			jaotanAutoUp = false;
		}

		DiscordEmbed embed = new DiscordEmbed();
		embed.setTitle("New Player Join Info");
		embed.setUrl("https://jaoafa.com/user/" + uuid.toString());
		embed.setDescription("新規プレイヤーがサーバにログインしました！\n※タイトルをクリックするとユーザページを開きます。");
		embed.setAuthor("jaotan", "https://jaoafa.com/", "https://jaoafa.com/wp-content/uploads/2018/03/IMG_20180326_070515.jpg", "https://jaoafa.com/wp-content/uploads/2018/03/IMG_20180326_070515.jpg");
		embed.addFields("プレイヤーID", "`" + player.getName() +"`" , false);
		embed.addFields("評価値", reputation + " / 10", false);
		embed.addFields("jaotanによる自動通過", String.valueOf(jaotanAutoUp), false);
		embed.addFields("プレイヤー数", Bukkit.getServer().getOnlinePlayers().size() + "人", false);
		embed.addFields("プレイヤー", "`" + implode(Bukkit.getServer().getOnlinePlayers(), ", ") + "`", false);

		DiscordSend("223582668132974594", "", embed);
	}

	private static JSONObject getHttpJson(String address){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpURLConnection connect = (HttpURLConnection)url.openConnection();
			connect.setRequestMethod("GET");

			connect.setDoOutput(true);

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

				Bukkit.getLogger().warning("MCBansRepAPIWARN: " + builder.toString());
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
			String res = builder.toString();
			Bukkit.getLogger().warning("MCBansRepAPIRES: " + res);
			Object obj = parser.parse(res);
			JSONObject json = (JSONObject) obj;
			return json;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
