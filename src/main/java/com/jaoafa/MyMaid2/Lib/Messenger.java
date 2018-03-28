package com.jaoafa.MyMaid2.Lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jaoafa.MyMaid2.MyMaid2Premise;

public class Messenger extends MyMaid2Premise {
	JavaPlugin plugin;
	public Messenger(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	//メッセージリスト
	private static List<String> Messages = new ArrayList<String>();
	private static String speaker = "└( ・з・)┘";

	/**
	 * メッセージを追加
	 * @param message 追加するメッセージ
	 * @author mine_book000
	 * @return 追加できたかどうか
	 */
	public static boolean Add(String message){
		boolean result = Messages.add(message);
		try {
			SaveMessenger();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return result;
		}
		return result;
	}

	/**
	 * メッセージが存在するか調べる
	 * @param index 調べるメッセージの番号
	 * @author mine_book000
	 * @return 存在したかどうか
	 */
	public static boolean Contains(int index){
		try{
			Messages.get(index);
		}catch(IndexOutOfBoundsException e){
			return false;
		}
		return true;
	}

	/**
	 * メッセージを取得
	 * @param index 取得するメッセージの番号
	 * @author mine_book000
	 * @return 取得したメッセージ
	 */
	public static String Get(int index){
		return Messages.get(index);
	}

	/**
	 * メッセージを削除
	 * @param index 削除するメッセージの番号
	 * @author mine_book000
	 * @return 削除できたかどうか
	 */
	public static boolean Del(int index){
		try{
			Messages.remove(index);
		}catch(IndexOutOfBoundsException e){
			return false;
		}
		try {
			SaveMessenger();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return true;
		}
		return true;
	}

	/**
	 * メッセージリストを取得
	 * @author mine_book000
	 * @return メッセージリスト
	 */
	public static List<String> getMessages(){
		return Messages;
	}

	/**
	 * スピーカーを取得
	 * @return 取得したスピーカー
	 */
	public static String getSpeaker(){
		return speaker;
	}

	/**
	 * スピーカーを設定する
	 * @param speaker 設定するスピーカー
	 */
	public static void setSpeaker(String speaker){
		Messenger.speaker = speaker;
	}

	/**
	 * メッセージをランダム配信する
	 * @author mine_book000
	 */
	public static void RandomBroadcastMessage(){
		try {
			LoadMessenger();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}
		List<String> list = new ArrayList<String>(Messages);
		Collections.shuffle(list);
		String message = list.get(0);
		String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
		for(Player play: Bukkit.getServer().getOnlinePlayers()) {
			String msg = message.replaceAll("%player%", play.getName());
			play.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + speaker + ChatColor.WHITE +  ": " + msg);
		}
	}

	/**
	 * メッセージをランダム配信する
	 * @author mine_book000
	 */
	public static void BroadcastMessage(int messageid){
		try {
			LoadMessenger();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}
		String message = Messages.get(messageid);
		String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
		for(Player play: Bukkit.getServer().getOnlinePlayers()) {
			String msg = message.replaceAll("%player%", play.getName());
			play.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "└( ・з・)┘" + ChatColor.WHITE +  ": " + msg);
		}
	}

	/**
	 * メッセージをランダムに取得する
	 * @author mine_book000
	 * @return メッセージ
	 */
	public static String GetRandomMessage(){
		try {
			LoadMessenger();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return "";
		}
		List<String> list = new ArrayList<String>(Messages);
		Collections.shuffle(list);
		String message = list.get(0);
		return message;
	}

	/**
	 * メッセンジャーをセーブする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception IOExceptionの発生時に発生
	 */
	@SuppressWarnings("unchecked")
	public static boolean SaveMessenger() throws Exception{
		JSONArray array = new JSONArray();
		for(String message : Messages) {
			array.add(message);
		}
		try{
			JavaPlugin plugin = JavaPlugin();
			File file = new File(plugin.getDataFolder() + File.separator + "messenger.json");
			FileWriter filewriter = new FileWriter(file);

			filewriter.write(array.toJSONString());

			filewriter.close();
		}catch(IOException e){
			e.printStackTrace();
			throw new Exception("IOException");
		}
		return true;
	}

	/**
	 * メッセンジャーをロードする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception 何かしらのExceptionが発生したときに発生(FileNotFoundException, IOException)
	 */
	public static boolean LoadMessenger() throws Exception{
		Messages.clear();

		JSONParser parser = new JSONParser();
		String json = "";
		try{
			JavaPlugin plugin = JavaPlugin();
			File file = new File(plugin.getDataFolder() + File.separator + "messenger.json");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String separator = System.getProperty("line.separator");

			String str;
			while((str = br.readLine()) != null){
				json += str + separator;
			}
			br.close();
		}catch(FileNotFoundException e1){
			e1.printStackTrace();
			throw new FileNotFoundException(e1.getMessage());
		}catch(IOException e1){
			e1.printStackTrace();
			throw new IOException(e1.getMessage());
		}
		JSONArray array;
		try {
			array = (JSONArray) parser.parse(json);
		} catch (ParseException e1) {
			array = new JSONArray();
		}
		for(Object obj : array) {
			String message = obj.toString();
			Messages.add(message);
		}
		return true;
	}
}
