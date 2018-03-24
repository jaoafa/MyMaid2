package com.jaoafa.MyMaid2.Lib;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;

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

}
