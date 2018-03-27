package com.jaoafa.MyMaid2.Lib;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Command.Cmd_Color;

public class SKKColors extends MyMaid2Premise {
	public static Map<String, Integer> votecount = new HashMap<String, Integer>();
	public static Map<String, String> LastText = new HashMap<String, String>();
	static List<String> MessageList = new ArrayList<String>();
	static JavaPlugin plugin;
	static File file;

	/**
	 * デフォルトのログインメッセージリストを返します。
	 * @return デフォルトのログインメッセージリスト
	 * @author mine_book000
	 */
	private static List<String> DefaultJoinMessageList(){
		List<String> MessageList = new ArrayList<String>();
		MessageList.add("the New Generation");
		MessageList.add("- Super");
		MessageList.add("Hyper");
		MessageList.add("Ultra");
		MessageList.add("Extreme");
		MessageList.add("Insane");
		MessageList.add("Gigantic");
		MessageList.add("Epic");
		MessageList.add("Amazing");
		MessageList.add("Beautiful");
		MessageList.add("Special");
		MessageList.add("Swag");
		MessageList.add("Lunatic");
		MessageList.add("Exotic");
		MessageList.add("God");
		MessageList.add("Hell");
		MessageList.add("Heaven");
		MessageList.add("Mega");
		MessageList.add("Giga");
		MessageList.add("Tera");
		MessageList.add("Refined");
		MessageList.add("Sharp");
		MessageList.add("Strong");
		MessageList.add("Muscle");
		MessageList.add("Macho");
		MessageList.add("Bomber");
		MessageList.add("Blazing");
		MessageList.add("Frozen");
		MessageList.add("Legendary");
		MessageList.add("Mystical");
		MessageList.add("Tactical");
		MessageList.add("Critical");
		MessageList.add("Overload");
		MessageList.add("Overclock");
		MessageList.add("Fantastic");
		MessageList.add("Criminal");
		MessageList.add("Primordial");
		MessageList.add("Genius");
		MessageList.add("Great");
		MessageList.add("Perfect");
		MessageList.add("Fearless");
		MessageList.add("Ruthless");
		MessageList.add("Bold");
		MessageList.add("Void");
		MessageList.add("Millenium");
		MessageList.add("Exact");
		MessageList.add("Really");
		MessageList.add("Certainty");
		MessageList.add("Infernal");
		MessageList.add("Ender");
		MessageList.add("World");
		MessageList.add("Mad");
		MessageList.add("Crazy");
		MessageList.add("Wrecked");
		MessageList.add("Elegant");
		MessageList.add("Expensive");
		MessageList.add("Rich");
		MessageList.add("Radioactive");
		MessageList.add("Automatic");
		MessageList.add("Honest");
		MessageList.add("Cosmic");
		MessageList.add("Galactic");
		MessageList.add("Dimensional");
		MessageList.add("Sinister");
		MessageList.add("Evil");
		MessageList.add("Abyssal");
		MessageList.add("Hallowed");
		MessageList.add("Holy");
		MessageList.add("Sacred");
		MessageList.add("Omnipotent");

		return MessageList;
	}
	/**
	 * ログインメッセージをロードしたりデータを保存したり初期設定をします。
	 * @param plugin プラグインのJavaPluginを指定
	 * @return 初期設定を完了したかどうか
	 * @author mine_book000
	 */
	public static boolean first(JavaPlugin plugin){
		SKKColors.plugin = plugin;
		// 設定ファイルがなければ作成
		File file = new File(plugin.getDataFolder(), "skkcolors.yml");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				BugReporter(e);
				return false;
			}

			MessageList = DefaultJoinMessageList();
			SKKColors.file = file;
			Save();
		}else{
			SKKColors.file = file;
			Load();
		}
		return true;
	}

	/**
	 * ログインメッセージをセーブします。
	 * @return 完了したかどうか
	 * @author mine_book000
	 */
	public static boolean Save(){
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		data.set("JoinMessageList", MessageList);
		data.set("LastText", LastText);
		data.set("Color", Cmd_Color.color);
		try {
			data.save(file);
			return true;
		} catch (IOException e) {
			BugReporter(e);
			return false;
		}
	}

	/**
	 * ログインメッセージをロードします。
	 * @return 完了したかどうか
	 * @author mine_book000
	 */
	public static boolean Load(){
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		if(data.contains("JoinMessageList")){
			MessageList = data.getStringList("JoinMessageList");
		}else{
			return false;
		}
		if(data.contains("LastText")){
			Map<String, Object> LastText = data.getConfigurationSection("LastText").getValues(true);
			if(LastText.size() != 0){
				for(Entry<String, Object> p: LastText.entrySet()){
					String LastTextStr = (String) p.getValue();
					SKKColors.LastText.put(p.getKey(), LastTextStr);
				}
			}
		}else{
			return false;
		}
		if(data.contains("Color")){
			Map<String, Object> Color = data.getConfigurationSection("Color").getValues(true);
			if(Color.size() != 0){
				for(Entry<String, Object> p: Color.entrySet()){
					ChatColor color = (ChatColor) p.getValue();
					Cmd_Color.color.put(p.getKey(), color);
				}
			}
		}else{
			return false;
		}
		return true;
	}

	/**
	 * プレイヤー投票数を基にChatColorを返却します。
	 * @param player 取得するプレイヤー
	 * @return チャットの四角のChatColor
	 * @author mine_book000
	 */
	public static ChatColor getPlayerSKKChatColor(Player player){
		try{
			String group = PermissionsManager.getPermissionMainGroup(player);
			PlayerVoteData pvd = new PlayerVoteData(player);
			if(group.equalsIgnoreCase("Limited")){
				return ChatColor.BLACK;
				/*}else if(EBan.isEBan(player)){
			return ChatColor.DARK_GRAY;*/
				/*}else if(Color.color.containsKey(player.getName())){
			return Color.color.get(player.getName());*/
			}else if(pvd.exists()){
				int i = pvd.get();
				if(i >= 0 && i <= 5){
					return ChatColor.WHITE;
				}else if(i >= 6 && i <= 19){
					return ChatColor.DARK_BLUE;
				}else if(i >= 20 && i <= 33){
					return ChatColor.BLUE;
				}else if(i >= 34 && i <= 47){
					return ChatColor.AQUA;
				}else if(i >= 48 && i <= 61){
					return ChatColor.DARK_AQUA;
				}else if(i >= 62 && i <= 76){
					return ChatColor.DARK_GREEN;
				}else if(i >= 77 && i <= 89){
					return ChatColor.GREEN;
				}else if(i >= 90 && i <= 103){
					return ChatColor.YELLOW;
				}else if(i >= 104 && i <= 117){
					return ChatColor.GOLD;
				}else if(i >= 118 && i <= 131){
					return ChatColor.RED;
				}else if(i >= 132 && i <= 145){
					return ChatColor.DARK_RED;
				}else if(i >= 146 && i <= 159){
					return ChatColor.DARK_PURPLE;
				}else if(i >= 160){
					return ChatColor.LIGHT_PURPLE;
				}
			}
		}catch(SQLException | ClassNotFoundException e){
			BugReporter(e);
		}
		return ChatColor.GRAY;
	}

	/**
	 * プレイヤー投票数を基にChatColorを出し、Messageの文字列を処理して返却します。
	 * @param player 取得するプレイヤー
	 * @param Message 処理する文字列
	 * @return 処理された文字列
	 * @author mine_book000
	 */
	public static String ReplacePlayerSKKChatColor(Player player, String oldstr, String Message){
		Message = Message.replaceFirst(oldstr, getPlayerSKKChatColor(player) + "■" + ChatColor.WHITE + oldstr);
		return Message;
	}

	/**
	 * プレイヤー投票数を基にTabに表示する文字列を返却します。
	 * @param player 取得するプレイヤー
	 * @return TabListに表示するべき文字列
	 * @author mine_book000
	 */
	@SuppressWarnings("deprecation")
	public static String getPlayerSKKTabListString(Player player){
		Team team = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(player);
		if(team == null){
			return getPlayerSKKChatColor(player) + "■" + ChatColor.RESET + player.getName();
		}else{
			return getPlayerSKKChatColor(player) + "■" + ChatColor.RESET + team.getPrefix() + player.getName();
		}

	}

	/**
	 * プレイヤー投票数を基にTabに表示する処理をします。
	 * @param player 処理するプレイヤー
	 * @author mine_book000
	 */
	public static void setPlayerSKKTabList(Player player){
		player.setPlayerListName(getPlayerSKKTabListString(player));
	}

	/**
	 * プレイヤー投票数を基にログインメッセージを返却します。
	 * @param player 取得するプレイヤー
	 * @param Message 処理する文字列
	 * @return 処理された文字列
	 * @author mine_book000
	 */
	public static String getPlayerSKKJoinMessage(Player player){
		try{
			String group = PermissionsManager.getPermissionMainGroup(player);
			if(group.equalsIgnoreCase("Limited")){
				return ChatColor.RED + player.getName() + ChatColor.YELLOW + " joined the game.";
			}
			PlayerVoteData pvd = new PlayerVoteData(player);
			int i = pvd.get();
			String result = "";
			if(i < 20){
				return null;
			}else if(i < 24){
				result = "VIP";
			}else{
				double vote_double = i / 4;
				vote_double = vote_double - 5;
				int vote = (int) Math.floor(vote_double);
				int o = 0;
				while(vote > 0){
					if(MessageList.size() <= o){
						break;
					}
					if(!result.equalsIgnoreCase("")){
						result += " ";
					}
					result += MessageList.get(o);
					vote--;
					o++;
				}
				if(!getPlayerJoinMsgLastText(player).equals("")){
					result += " " + getPlayerJoinMsgLastText(player);
				}
				result += " VIP (" + i + ")";
			}
			return ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + ", " + ChatColor.YELLOW + result + " joined the game.";
		}catch(ClassNotFoundException | SQLException e){
			return ChatColor.YELLOW + player.getName() + ChatColor.YELLOW + ", " + ChatColor.YELLOW + player.getName() + " joined the game.";
		}
	}


	public static String getPlayerJoinMsgLastText(Player player){
		if(LastText.containsKey(player.getUniqueId().toString())){
			return LastText.get(player.getUniqueId().toString());
		}else{
			return "";
		}
	}

	public static void setPlayerJoinMsgLastMsg(Player player, String next){
		LastText.put(player.getUniqueId().toString(), next);
		Save();
	}
	public static void delPlayerJoinMsgLastMsg(Player player){
		LastText.remove(player.getUniqueId().toString());
		Save();
	}
}
