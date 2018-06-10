package com.jaoafa.MyMaid2;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Command.Cmd_AFK;
import com.jaoafa.MyMaid2.Command.Cmd_Account;
import com.jaoafa.MyMaid2.Command.Cmd_Body;
import com.jaoafa.MyMaid2.Command.Cmd_Book;
import com.jaoafa.MyMaid2.Command.Cmd_Boots;
import com.jaoafa.MyMaid2.Command.Cmd_Chat;
import com.jaoafa.MyMaid2.Command.Cmd_City;
import com.jaoafa.MyMaid2.Command.Cmd_Ck;
import com.jaoafa.MyMaid2.Command.Cmd_Cmdb;
import com.jaoafa.MyMaid2.Command.Cmd_Color;
import com.jaoafa.MyMaid2.Command.Cmd_DT;
import com.jaoafa.MyMaid2.Command.Cmd_DedRain;
import com.jaoafa.MyMaid2.Command.Cmd_DelHome;
import com.jaoafa.MyMaid2.Command.Cmd_DiscordLink;
import com.jaoafa.MyMaid2.Command.Cmd_EBan;
import com.jaoafa.MyMaid2.Command.Cmd_Elytra;
import com.jaoafa.MyMaid2.Command.Cmd_EnderChest;
import com.jaoafa.MyMaid2.Command.Cmd_G;
import com.jaoafa.MyMaid2.Command.Cmd_Hat;
import com.jaoafa.MyMaid2.Command.Cmd_Head;
import com.jaoafa.MyMaid2.Command.Cmd_Home;
import com.jaoafa.MyMaid2.Command.Cmd_InvEdit;
import com.jaoafa.MyMaid2.Command.Cmd_InvLoad;
import com.jaoafa.MyMaid2.Command.Cmd_InvSave;
import com.jaoafa.MyMaid2.Command.Cmd_InvShow;
import com.jaoafa.MyMaid2.Command.Cmd_Jail;
import com.jaoafa.MyMaid2.Command.Cmd_Leg;
import com.jaoafa.MyMaid2.Command.Cmd_Messenger;
import com.jaoafa.MyMaid2.Command.Cmd_Pin;
import com.jaoafa.MyMaid2.Command.Cmd_Player;
import com.jaoafa.MyMaid2.Command.Cmd_Report;
import com.jaoafa.MyMaid2.Command.Cmd_Respawn;
import com.jaoafa.MyMaid2.Command.Cmd_Selector;
import com.jaoafa.MyMaid2.Command.Cmd_SetHome;
import com.jaoafa.MyMaid2.Command.Cmd_Sign;
import com.jaoafa.MyMaid2.Command.Cmd_Spawn;
import com.jaoafa.MyMaid2.Command.Cmd_Test;
import com.jaoafa.MyMaid2.Command.Cmd_Testment;
import com.jaoafa.MyMaid2.Command.Cmd_Var;
import com.jaoafa.MyMaid2.Command.Cmd_WT;
import com.jaoafa.MyMaid2.Command.Cmd_Wtp;
import com.jaoafa.MyMaid2.Command.Cmd_jao;
import com.jaoafa.MyMaid2.Event.Event_AFK;
import com.jaoafa.MyMaid2.Event.Event_AntiProblemCommand;
import com.jaoafa.MyMaid2.Event.Event_AntiTNTMinecart;
import com.jaoafa.MyMaid2.Event.Event_AntiWither;
import com.jaoafa.MyMaid2.Event.Event_Antijaoium;
import com.jaoafa.MyMaid2.Event.Event_BanLogger;
import com.jaoafa.MyMaid2.Event.Event_ChatSpamKickDisable;
import com.jaoafa.MyMaid2.Event.Event_CommandBlockLogger;
import com.jaoafa.MyMaid2.Event.Event_CommandBlockVariable;
import com.jaoafa.MyMaid2.Event.Event_DedRain;
import com.jaoafa.MyMaid2.Event.Event_EBan;
import com.jaoafa.MyMaid2.Event.Event_FarmNOBreak;
import com.jaoafa.MyMaid2.Event.Event_Jail;
import com.jaoafa.MyMaid2.Event.Event_JoinAutoQPPE;
import com.jaoafa.MyMaid2.Event.Event_JoinHeaderFooterChange;
import com.jaoafa.MyMaid2.Event.Event_JoinjaoPoint;
import com.jaoafa.MyMaid2.Event.Event_LoginLeftPlayerCountNotice;
import com.jaoafa.MyMaid2.Event.Event_LoginSuccessCheck;
import com.jaoafa.MyMaid2.Event.Event_LoginVoteCheck;
import com.jaoafa.MyMaid2.Event.Event_MoveToChunkActionbar;
import com.jaoafa.MyMaid2.Event.Event_NOConcretePowderToConcrete;
import com.jaoafa.MyMaid2.Event.Event_PlayerCheckPreLogin;
import com.jaoafa.MyMaid2.Event.Event_PlayerCommandSendAdmin;
import com.jaoafa.MyMaid2.Event.Event_PlayerQuit;
import com.jaoafa.MyMaid2.Event.Event_QD_NOTSpectator;
import com.jaoafa.MyMaid2.Event.Event_QuitHeaderFooterChange;
import com.jaoafa.MyMaid2.Event.Event_SKKColor;
import com.jaoafa.MyMaid2.Event.Event_SignClick;
import com.jaoafa.MyMaid2.Event.Event_VoteMissFillerEvent;
import com.jaoafa.MyMaid2.Event.Event_VoteReceived;
import com.jaoafa.MyMaid2.Lib.InventoryManager;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Lib.SKKColors;
import com.jaoafa.MyMaid2.Lib.TPSChecker;
import com.jaoafa.MyMaid2.Task.AutoMessenger;
import com.jaoafa.MyMaid2.Task.TPSChange;
import com.jaoafa.MyMaid2.Task.Task_AFK.AFKChecker;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class MyMaid2 extends JavaPlugin implements Listener {
	public static String discordtoken = null;
	public static String serverchat_id = null;
	public static FileConfiguration conf;
	public static String sqlserver = "jaoafa.com";
	public static String sqluser;
	public static String sqlpassword;
	public static Connection c = null;
	public static long ConnectionCreate = 0;
	public static String MCBansRepAPI = null;

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
		Load_Plugin("Votifier");
		Load_Plugin("jaoSuperAchievement");
		Load_Plugin("MinecraftJPVoteMissFiller");
		Load_Plugin("MCBans");
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

		SKKColors.first(this);
		TPSChecker.OnEnable_TPSSetting();
		InventoryManager.start(this);
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
		getCommand("discordlink").setExecutor(new Cmd_DiscordLink(this)); // 2018/03/21
		getCommand("spawn").setExecutor(new Cmd_Spawn(this)); // 2018/03/25
		getCommand("jao").setExecutor(new Cmd_jao(this)); // 2018/03/25
		getCommand("head").setExecutor(new Cmd_Head()); // 2018/03/25
		getCommand("test").setExecutor(new Cmd_Test(this)); // 2018/03/25
		getCommand("color").setExecutor(new Cmd_Color()); // 2018/03/26
		getCommand("elytra").setExecutor(new Cmd_Elytra(this)); // 2018/03/27
		getCommand("messenger").setExecutor(new Cmd_Messenger(this)); // 2018/03/29
		getCommand("selector").setExecutor(new Cmd_Selector(this)); // 2018/03/29
		getCommand("chat").setExecutor(new Cmd_Chat(this)); // 2018/03/29
		getCommand("player").setExecutor(new Cmd_Player(this)); // 2018/03/31
		getCommand("eban").setExecutor(new Cmd_EBan(this)); // 2018/04/01
		getCommand("jail").setExecutor(new Cmd_Jail(this)); // 2018/04/01
		getCommand("hat").setExecutor(new Cmd_Hat(this)); // 2018/04/01
		getCommand("body").setExecutor(new Cmd_Body(this)); // 2018/04/01
		getCommand("leg").setExecutor(new Cmd_Leg(this)); // 2018/04/01
		getCommand("boots").setExecutor(new Cmd_Boots(this)); // 2018/04/01
		getCommand("ck").setExecutor(new Cmd_Ck(this)); // 2018/04/05
		getCommand("sign").setExecutor(new Cmd_Sign(this)); // 2018/04/09
		getCommand("account").setExecutor(new Cmd_Account(this)); // 2018/05/01
		getCommand("pin").setExecutor(new Cmd_Pin(this)); // 2018/05/02
		getCommand("respawn").setExecutor(new Cmd_Respawn(this)); // 2018/05/06
		getCommand("testment").setExecutor(new Cmd_Testment(this)); // 2018/05/09
		getCommand("book").setExecutor(new Cmd_Book(this)); // 2018/05/12
		getCommand("report").setExecutor(new Cmd_Report(this)); // 2018/05/19
		getCommand("wtp").setExecutor(new Cmd_Wtp(this)); // 2018/05/19
		getCommand("var").setExecutor(new Cmd_Var(this)); // 2018/06/03
		getCommand("invload").setExecutor(new Cmd_InvLoad(this)); // 2018/06/06
		getCommand("invsave").setExecutor(new Cmd_InvSave(this)); // 2018/06/06
		getCommand("invedit").setExecutor(new Cmd_InvEdit(this)); // 2018/06/08
		getCommand("invshow").setExecutor(new Cmd_InvShow(this)); // 2018/06/08
		getCommand("enderchest").setExecutor(new Cmd_EnderChest(this)); // 2018/06/08
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
		getCommand("jail").setTabCompleter(new Cmd_Jail(this)); // 2018/04/01
	}

	/**
	 * スケジューリング
	 * @author mine_book000
	 */
	private void Import_Task(){
		new AFKChecker().runTaskTimer(this, 0L, 1200L);
		new TPSChange().runTaskTimer(this, 0L, 1200L);
		new AutoMessenger().runTaskTimer(this, 0L, 12000L);
	}

	/**
	 * リスナー設定
	 * @author mine_book000
	 */
	private void Import_Listener(){
		// 日付は制作完了(登録)の日付
		registEvent(this);
		registEvent(new Event_PlayerCheckPreLogin(this));// 2018/03/20
		registEvent(new Event_CommandBlockLogger(this));// 2018/03/20
		registEvent(new Event_LoginSuccessCheck(this));// 2018/03/20
		registEvent(new Event_PlayerQuit(this));// 2018/03/20
		registEvent(new Event_DedRain(this));// 2018/03/21
		registEvent(new Event_AFK());// 2018/03/21
		registEvent(new Event_QD_NOTSpectator());// 2018/03/21
		registEvent(new Event_MoveToChunkActionbar());// 2018/03/21
		registEvent(new Event_FarmNOBreak());// 2018/03/21
		registEvent(new Event_NOConcretePowderToConcrete());// 2018/03/21
		registEvent(new Event_VoteReceived(this));// 2018/03/24
		registEvent(new Event_PlayerCommandSendAdmin(this));// 2018/03/25
		registEvent(new Event_JoinjaoPoint(this));// 2018/03/25
		registEvent(new Event_Antijaoium(this));// 2018/03/25
		registEvent(new Event_SKKColor(this));// 2018/03/26
		registEvent(new Event_JoinHeaderFooterChange(this));// 2018/03/26
		registEvent(new Event_QuitHeaderFooterChange(this));// 2018/03/26
		registEvent(new Event_VoteMissFillerEvent(this));// 2018/03/27
		registEvent(new Event_AntiProblemCommand(this));// 2018/03/29
		registEvent(new Event_ChatSpamKickDisable(this));// 2018/04/01
		registEvent(new Event_EBan(this));// 2018/04/01
		registEvent(new Event_Jail(this));// 2018/04/01
		registEvent(new Event_LoginLeftPlayerCountNotice(this));// 2018/04/01
		registEvent(new Event_JoinAutoQPPE(this));// 2018/04/07
		registEvent(new Event_SignClick(this));// 2018/04/09
		registEvent(new Event_AntiTNTMinecart(this));// 2018/04/22
		registEvent(new Event_AntiWither(this));// 2018/05/27
		registEvent(new Event_LoginVoteCheck(this));// 2018/05/31
		registEvent(new Event_CommandBlockVariable(this));// 2018/06/03
		registEvent(new Event_BanLogger(this));// 2018/06/10
	}

	/**
	 * リスナー設定の簡略化用
	 * @param listener Listener
	 */
	private void registEvent(Listener l) {
		getServer().getPluginManager().registerEvents(l, this);
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
			getLogger().info("Disable MyMaid2...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}


		if(conf.contains("sqlserver")){
			sqlserver = (String) conf.get("sqlserver");
		}

		MySQL MySQL = new MySQL(sqlserver, "3306", "jaoafa", sqluser, sqlpassword);

		try {
			c = MySQL.openConnection();
			ConnectionCreate = System.currentTimeMillis() / 1000L;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [ClassNotFoundException]");
			getLogger().info("Disable MyMaid2...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [SQLException: " + e.getSQLState() + "]");
			getLogger().info("Disable MyMaid2...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("MySQL Connect successful.");

		if(conf.contains("MCBansRepAPI")){
			MCBansRepAPI = (String) conf.get("MCBansRepAPI");
		}else{
			getLogger().warning("コンフィグにMCBansのReputationを取得するためのAPIが記載されていなかったため、Reputationチェックは動作しません。");
		}

		if(conf.contains("jaoAccountAPI")){
			Cmd_Account.jaoAccountAPI = (String) conf.get("jaoAccountAPI");
		}
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
			getLogger().info("Disable MyMaid2...");
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
		SKKColors.Save();
	}

	@Nullable
	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldGuardPlugin) plugin;
	}

	@Nullable
	public static WorldEditPlugin getWorldEdit() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null; // Maybe you want throw an exception instead
		}

		return (WorldEditPlugin) plugin;
	}
}
