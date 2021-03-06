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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jaoafa.MyMaid2.MyMaid2;
import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Lib.Discord.DiscordEmbed;

public class Event_JoinAutoQPPE extends MyMaid2Premise implements Listener {
	@EventHandler
	public void OnEvent_JoinjaoPoint(PlayerJoinEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		String url = MyMaid2.MCBansRepAPI;
		if(url == null){
			Bukkit.getLogger().warning("コンフィグにMCBansのReputationを取得するためのAPIが記載されていなかったため、Reputationチェックは動作しません。");
			return;
		}

		String pex = PermissionsManager.getPermissionMainGroup(player);
		if(!pex.equalsIgnoreCase("Limited")){
			if(pex.equalsIgnoreCase("QPPE")){
				JSONObject json = getHttpJson(url + "?u=" + uuid.toString());
				if(!json.containsKey("status")){
					return;
				}
				Boolean status = (Boolean) json.get("status");
				if(!status){
					Bukkit.getLogger().warning("Reputationチェックが正常に完了しませんでした。");
					return;
				}
				String reputation = (String) json.get("reputation");
				if(reputation.equalsIgnoreCase("10")){
					return;
				}
				Bukkit.getLogger().warning("Reputation: " + reputation + " -> QPPE -> Limited DOWN");
				PermissionsManager.setPermissionsGroup(player, "Limited");
				DiscordSend("223582668132974594", player.getName() + ": QPPEでしたが、Reputationが10ではなかったため(" + reputation + ")、Limitedに変更しました。");
			}
			return;
		}

		JSONObject json = getHttpJson(url + "?u=" + uuid.toString());
		if(!json.containsKey("status")){
			return;
		}

		Boolean status = (Boolean) json.get("status");
		String reputation = "取得失敗";
		if(!status){
			Bukkit.getLogger().warning("Reputationチェックが正常に完了しませんでした。");
			// return;
		}else{
			reputation = (String) json.get("reputation");
		}

		boolean jaotanAutoUp;
		if(reputation.equalsIgnoreCase("10")){
			Bukkit.getLogger().warning("Reputation: " + reputation + " -> UP to QPPE");
			PermissionsManager.setPermissionsGroup(player, "QPPE");
			jaotanAutoUp = true;
		}else{
			Bukkit.getLogger().warning("Reputation: " + reputation + " -> NOT UP");
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

		if(jaotanAutoUp){
			embed.addFields("ブロック編集等のデータ", "https://jaoafa.com/tomachi/co.php?player=" + player.getName(), false);
		}

		embed.setThumbnail(
				"https://crafatar.com/renders/body/" + uuid.toString() + ".png?overlay=true&scale=10",
				"https://crafatar.com/renders/body/" + uuid.toString() + ".png?overlay=true&scale=10",
				451,
				200
		);

		DiscordSend("223582668132974594", "", embed);

		if(!reputation.equalsIgnoreCase("10")){
			sendMCBansData(url, player.getName(), player.getUniqueId());
		}
	}

	private static void sendMCBansData(String url, String player, UUID uuid){
		JSONObject json = getHttpJson(url + "?u=" + uuid.toString() + "&data");
		if(!json.containsKey("status")){
			return;
		}

		Boolean status = (Boolean) json.get("status");
		if(!status){
			return;
		}
		String count = (String) json.get("datacount");
		String data = (String) json.get("data");

		DiscordSend("223582668132974594", "**-----: MCBans Ban DATA / `" + player + "` :-----**\n"
				+ "Ban: " + count + "\n"
				+ "```" + data + "```");
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
