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
		if(!status){
			plugin.getLogger().warning("Reputationチェックが正常に完了しませんでした。");
			return;
		}

		String reputation = (String)json.get("reputation");

		if(reputation.equalsIgnoreCase("10")){
			plugin.getLogger().warning("Reputation: " + reputation + " -> UP to QPPE");
			PermissionsManager.setPermissionsGroup(player, "QPPE");
		}else{
			plugin.getLogger().warning("Reputation: " + reputation + " -> NOT UP");
		}
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
			Object obj = parser.parse(builder.toString());
			JSONObject json = (JSONObject) obj;
			return json;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
