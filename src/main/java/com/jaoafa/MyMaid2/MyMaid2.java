package com.jaoafa.MyMaid2;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Command.Cmd_AFK;
import com.jaoafa.MyMaid2.Command.Cmd_City;
import com.jaoafa.MyMaid2.Command.Cmd_Cmdb;
import com.jaoafa.MyMaid2.Command.Cmd_DT;
import com.jaoafa.MyMaid2.Command.Cmd_DedRain;
import com.jaoafa.MyMaid2.Command.Cmd_DelHome;
import com.jaoafa.MyMaid2.Command.Cmd_G;
import com.jaoafa.MyMaid2.Command.Cmd_Home;
import com.jaoafa.MyMaid2.Command.Cmd_SetHome;
import com.jaoafa.MyMaid2.Command.Cmd_WT;
import com.jaoafa.MyMaid2.Event.Event_AFK;
import com.jaoafa.MyMaid2.Event.Event_CommandBlockLogger;
import com.jaoafa.MyMaid2.Event.Event_DedRain;
import com.jaoafa.MyMaid2.Event.Event_LoginSuccessCheck;
import com.jaoafa.MyMaid2.Event.Event_PlayerCheckPreLogin;
import com.jaoafa.MyMaid2.Event.Event_PlayerQuit;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Task.Task_AFK;

public class MyMaid2 extends JavaPlugin implements Listener {
	public static String discordtoken = null;
	public static String serverchat_id = null;
	public static FileConfiguration conf;
	public static String sqlserver = "jaoafa.com";
	public static String sqluser;
	public static String sqlpassword;
	public static Connection c = null;

	public static JavaPlugin javaplugin = null;
	public static MyMaid2 mymaid2 = null;

	/**
	 * プラグインが起動したときに呼び出し
	 * @author mine_book000
	 * @since 2018/03/07
	 */
	@Override
	public void onEnable() {
		getLogger().info("--------------------------------------------------");
		// クレジット
		getLogger().info("(c) jao Minecraft Server MyMaid2 Project.");
		getLogger().info("Product by tomachi.");

		javaplugin = this;
		mymaid2 = this;

		// 連携プラグインの確認
		Load_Plugin("GeoipAPI");
		Load_Plugin("dynmap");
		if(!this.isEnabled()) return;

		// PermissionsManager初期設定
		PermissionsManager.first();
		if(!this.isEnabled()) return;

		// コンフィグ読み込み
		Load_Config();
		if(!this.isEnabled()) return;

		// リスナーを設定
		Import_Listener();
		// スケジュールタスクをスケジュ―リング
		Import_Task();
		// コマンドを設定
		Import_Command_Executor();
		// Tabコンプリーターを設定
		Import_Command_TabCompleter();
		getLogger().info("--------------------------------------------------");
	}

	/**
	 * コマンドの設定
	 * @author mine_book000
	 */
	private void Import_Command_Executor(){
		// 日付は制作完了(登録)の日付
		getCommand("afk").setExecutor(new Cmd_AFK()); // 2018/03/18
		getCommand("dt").setExecutor(new Cmd_DT(this)); // 2018/03/20
		getCommand("city").setExecutor(new Cmd_City(this)); // 2018/03/20
		getCommand("cmdb").setExecutor(new Cmd_Cmdb()); // 2018/03/21
		getCommand("home").setExecutor(new Cmd_Home(this)); // 2018/03/21
		getCommand("sethome").setExecutor(new Cmd_SetHome(this)); // 2018/03/21
		getCommand("delhome").setExecutor(new Cmd_DelHome(this)); // 2018/03/21
		getCommand("g").setExecutor(new Cmd_G(this)); // 2018/03/21
		getCommand("dedrain").setExecutor(new Cmd_DedRain(this)); // 2018/03/21
		getCommand("wt").setExecutor(new Cmd_WT(this)); // 2018/03/21
	}

	/**
	 * Tabコンプリーターの設定
	 * @author mine_book000
	 */
	private void Import_Command_TabCompleter(){
		// 日付は制作完了(登録)の日付
		getCommand("dt").setTabCompleter(new Cmd_DT(this)); // 2018/03/20
		getCommand("home").setTabCompleter(new Cmd_Home(this)); // 2018/03/21
		getCommand("delhome").setTabCompleter(new Cmd_DelHome(this)); // 2018/03/21
	}

	/**
	 * スケジューリング
	 * @author mine_book000
	 */
	private void Import_Task(){
		new Task_AFK.AFKChecker().runTaskTimer(this, 0L, 1200L);
	}

	/**
	 * リスナー設定
	 * @author mine_book000
	 */
	private void Import_Listener(){
		// 日付は制作完了(登録)の日付
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new Event_PlayerCheckPreLogin(this), this); // 2018/03/20
		getServer().getPluginManager().registerEvents(new Event_CommandBlockLogger(this), this); // 2018/03/20
		getServer().getPluginManager().registerEvents(new Event_LoginSuccessCheck(this), this); // 2018/03/20
		getServer().getPluginManager().registerEvents(new Event_PlayerQuit(this), this); // 2018/03/20
		getServer().getPluginManager().registerEvents(new Event_DedRain(this), this); // 2018/03/21
		getServer().getPluginManager().registerEvents(new Event_AFK(), this); // 2018/03/21
	}

	/**
	 * コンフィグ読み込み
	 * @author mine_book000
	 */
	private void Load_Config(){
		conf = getConfig();
		if(conf.contains("discordtoken")){
			discordtoken = (String) conf.get("discordtoken");
		}else{
			getLogger().info("Discordへの接続に失敗しました。 [conf NotFound]");
			getLogger().info("Disable MyMaid2...");
			getServer().getPluginManager().disablePlugin(this);
		}
		if(conf.contains("serverchat_id")){
			serverchat_id = (String) conf.get("serverchat_id");
		}else{
			serverchat_id = "250613942106193921"; // #server-chat
		}

		if(conf.contains("sqluser") && conf.contains("sqlpassword")){
			sqluser = conf.getString("sqluser");
			sqlpassword = conf.getString("sqlpassword");
		}else{
			getLogger().info("MySQL Connect err. [conf NotFound]");
			getLogger().info("Disable AntiAlts2...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}


		if(conf.contains("sqlserver")){
			sqlserver = (String) conf.get("sqlserver");
		}

		MySQL MySQL = new MySQL(sqlserver, "3306", "jaoafa", sqluser, sqlpassword);

		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [ClassNotFoundException]");
			getLogger().info("Disable AntiAlts2...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [SQLException: " + e.getSQLState() + "]");
			getLogger().info("Disable AntiAlts2...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("MySQL Connect successful.");
	}



	/**
	 * 連携プラグイン確認
	 * @author mine_book000
	 */
	private void Load_Plugin(String PluginName){
		if(getServer().getPluginManager().isPluginEnabled(PluginName)){
			getLogger().info("MyMaid2 Success(LOADED: " + PluginName + ")");
			getLogger().info("Using " + PluginName);
		}else{
			getLogger().warning("MyMaid2 ERR(NOTLOADED: " + PluginName + ")");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	/**
	 * プラグインが停止したときに呼び出し
	 * @author mine_book000
	 * @since 2018/03/07
	 */
	@Override
	public void onDisable() {

	}
}
