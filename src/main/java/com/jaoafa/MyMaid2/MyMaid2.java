package com.jaoafa.MyMaid2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
import com.jaoafa.MyMaid2.Command.Cmd_ChatJail;
import com.jaoafa.MyMaid2.Command.Cmd_ChuoCity;
import com.jaoafa.MyMaid2.Command.Cmd_City;
import com.jaoafa.MyMaid2.Command.Cmd_Ck;
import com.jaoafa.MyMaid2.Command.Cmd_Cmdb;
import com.jaoafa.MyMaid2.Command.Cmd_Color;
import com.jaoafa.MyMaid2.Command.Cmd_DT;
import com.jaoafa.MyMaid2.Command.Cmd_Ded;
import com.jaoafa.MyMaid2.Command.Cmd_DedRain;
import com.jaoafa.MyMaid2.Command.Cmd_DelHome;
import com.jaoafa.MyMaid2.Command.Cmd_DiscordLink;
import com.jaoafa.MyMaid2.Command.Cmd_EBan;
import com.jaoafa.MyMaid2.Command.Cmd_Elytra;
import com.jaoafa.MyMaid2.Command.Cmd_EnderChest;
import com.jaoafa.MyMaid2.Command.Cmd_G;
import com.jaoafa.MyMaid2.Command.Cmd_GLookup;
import com.jaoafa.MyMaid2.Command.Cmd_Hat;
import com.jaoafa.MyMaid2.Command.Cmd_Head;
import com.jaoafa.MyMaid2.Command.Cmd_Hide;
import com.jaoafa.MyMaid2.Command.Cmd_Home;
import com.jaoafa.MyMaid2.Command.Cmd_InvEdit;
import com.jaoafa.MyMaid2.Command.Cmd_InvLoad;
import com.jaoafa.MyMaid2.Command.Cmd_InvSave;
import com.jaoafa.MyMaid2.Command.Cmd_InvShow;
import com.jaoafa.MyMaid2.Command.Cmd_ItemEdit;
import com.jaoafa.MyMaid2.Command.Cmd_Jail;
import com.jaoafa.MyMaid2.Command.Cmd_Lead;
import com.jaoafa.MyMaid2.Command.Cmd_Leg;
import com.jaoafa.MyMaid2.Command.Cmd_Messenger;
import com.jaoafa.MyMaid2.Command.Cmd_Pin;
import com.jaoafa.MyMaid2.Command.Cmd_Player;
import com.jaoafa.MyMaid2.Command.Cmd_Protector;
import com.jaoafa.MyMaid2.Command.Cmd_Report;
import com.jaoafa.MyMaid2.Command.Cmd_Respawn;
import com.jaoafa.MyMaid2.Command.Cmd_RestartTitle;
import com.jaoafa.MyMaid2.Command.Cmd_Rider;
import com.jaoafa.MyMaid2.Command.Cmd_Selector;
import com.jaoafa.MyMaid2.Command.Cmd_SetHome;
import com.jaoafa.MyMaid2.Command.Cmd_Show;
import com.jaoafa.MyMaid2.Command.Cmd_Sign;
import com.jaoafa.MyMaid2.Command.Cmd_Spawn;
import com.jaoafa.MyMaid2.Command.Cmd_Summer;
import com.jaoafa.MyMaid2.Command.Cmd_Test;
import com.jaoafa.MyMaid2.Command.Cmd_Testment;
import com.jaoafa.MyMaid2.Command.Cmd_Var;
import com.jaoafa.MyMaid2.Command.Cmd_VoteFill;
import com.jaoafa.MyMaid2.Command.Cmd_WT;
import com.jaoafa.MyMaid2.Command.Cmd_Wtp;
import com.jaoafa.MyMaid2.Command.Cmd_jao;
import com.jaoafa.MyMaid2.Event.Event_AFK;
import com.jaoafa.MyMaid2.Event.Event_AntiNetherPortal;
import com.jaoafa.MyMaid2.Event.Event_AntiPotion;
import com.jaoafa.MyMaid2.Event.Event_AntiProblemCommand;
import com.jaoafa.MyMaid2.Event.Event_AntiQDTeleport;
import com.jaoafa.MyMaid2.Event.Event_AntiTNTMinecart;
import com.jaoafa.MyMaid2.Event.Event_AntiWither;
import com.jaoafa.MyMaid2.Event.Event_Antijaoium;
import com.jaoafa.MyMaid2.Event.Event_BanChecker;
import com.jaoafa.MyMaid2.Event.Event_BanLogger;
import com.jaoafa.MyMaid2.Event.Event_ChatJail;
import com.jaoafa.MyMaid2.Event.Event_ChatSpamKickDisable;
import com.jaoafa.MyMaid2.Event.Event_CheckChatOPME;
import com.jaoafa.MyMaid2.Event.Event_CommandBlockLogger;
import com.jaoafa.MyMaid2.Event.Event_CommandBlockVariable;
import com.jaoafa.MyMaid2.Event.Event_DeathToDeath;
import com.jaoafa.MyMaid2.Event.Event_Ded;
import com.jaoafa.MyMaid2.Event.Event_DedRain;
import com.jaoafa.MyMaid2.Event.Event_EBan;
import com.jaoafa.MyMaid2.Event.Event_FarmNOBreak;
import com.jaoafa.MyMaid2.Event.Event_Jail;
import com.jaoafa.MyMaid2.Event.Event_JoinAutoQPPE;
import com.jaoafa.MyMaid2.Event.Event_JoinHeaderFooterChange;
import com.jaoafa.MyMaid2.Event.Event_JoinjaoPoint;
import com.jaoafa.MyMaid2.Event.Event_LoginLeftPlayerCountNotice;
import com.jaoafa.MyMaid2.Event.Event_LoginSuccessCheck;
import com.jaoafa.MyMaid2.Event.Event_LoginTutorialSpawn;
import com.jaoafa.MyMaid2.Event.Event_LoginVoteCheck;
import com.jaoafa.MyMaid2.Event.Event_LongTimeNoSee;
import com.jaoafa.MyMaid2.Event.Event_MoveToChunkActionbar;
import com.jaoafa.MyMaid2.Event.Event_NOConcretePowderToConcrete;
import com.jaoafa.MyMaid2.Event.Event_OnlineTime;
import com.jaoafa.MyMaid2.Event.Event_PlayerCheckPreLogin;
import com.jaoafa.MyMaid2.Event.Event_PlayerCommandSendAdmin;
import com.jaoafa.MyMaid2.Event.Event_PlayerCommandSendRegular;
import com.jaoafa.MyMaid2.Event.Event_PlayerQuit;
import com.jaoafa.MyMaid2.Event.Event_QD_NOTSpectator;
import com.jaoafa.MyMaid2.Event.Event_QuitHeaderFooterChange;
import com.jaoafa.MyMaid2.Event.Event_SKKColor;
import com.jaoafa.MyMaid2.Event.Event_SandBoxBuildProtectionNotice;
import com.jaoafa.MyMaid2.Event.Event_SandBoxRoad;
import com.jaoafa.MyMaid2.Event.Event_SignClick;
import com.jaoafa.MyMaid2.Event.Event_Summer2018;
import com.jaoafa.MyMaid2.Event.Event_Tips;
import com.jaoafa.MyMaid2.Event.Event_VeryVeryStrongAntiLQD;
import com.jaoafa.MyMaid2.Event.Event_VoteMissFillerEvent;
import com.jaoafa.MyMaid2.Event.Event_VoteReceived;
import com.jaoafa.MyMaid2.Lib.InventoryManager;
import com.jaoafa.MyMaid2.Lib.Jail;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;
import com.jaoafa.MyMaid2.Lib.SKKColors;
import com.jaoafa.MyMaid2.Lib.TPSChecker;
import com.jaoafa.MyMaid2.Task.AutoMessenger;
import com.jaoafa.MyMaid2.Task.TPSChange;
import com.jaoafa.MyMaid2.Task.Task_AFK;
import com.jaoafa.MyMaid2.Task.Task_WorldSave;
import com.jaoafa.MyMaid2.Task.Team1000Observer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.milkbowl.vault.economy.Economy;

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
	public static String pastebin_devkey = null;
	public static List<String> pastebin_devkeyList = null;

	public static Economy econ = null;

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
		try {
			Jail.LoadJailData();
		} catch (Exception e) {
			MyMaid2Premise.BugReporter(e);
		}
		/*RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
        	getLogger().info("RegisteredServiceProvider<Economy> is null.");
        	getLogger().info("関連機能は使用できません。");
			getServer().getPluginManager().disablePlugin(this);
			return;
        }
        econ = rsp.getProvider();
        if (econ == null) {
        	getLogger().info("rsp.getProvider() is null.");
        	getLogger().info("関連機能は使用できません。");
			return;
        }*/
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
		getCommand("sethome").setExecutor(new Cmd_SetHome()); // 2018/03/21
		getCommand("delhome").setExecutor(new Cmd_DelHome(this)); // 2018/03/21
		getCommand("g").setExecutor(new Cmd_G()); // 2018/03/21
		getCommand("dedrain").setExecutor(new Cmd_DedRain()); // 2018/03/21
		getCommand("wt").setExecutor(new Cmd_WT()); // 2018/03/21
		getCommand("discordlink").setExecutor(new Cmd_DiscordLink()); // 2018/03/21
		getCommand("spawn").setExecutor(new Cmd_Spawn()); // 2018/03/25
		getCommand("jao").setExecutor(new Cmd_jao()); // 2018/03/25
		getCommand("head").setExecutor(new Cmd_Head()); // 2018/03/25
		getCommand("test").setExecutor(new Cmd_Test()); // 2018/03/25
		getCommand("color").setExecutor(new Cmd_Color()); // 2018/03/26
		getCommand("elytra").setExecutor(new Cmd_Elytra()); // 2018/03/27
		getCommand("messenger").setExecutor(new Cmd_Messenger()); // 2018/03/29
		getCommand("selector").setExecutor(new Cmd_Selector()); // 2018/03/29
		getCommand("chat").setExecutor(new Cmd_Chat()); // 2018/03/29
		getCommand("player").setExecutor(new Cmd_Player()); // 2018/03/31
		getCommand("eban").setExecutor(new Cmd_EBan()); // 2018/04/01
		getCommand("jail").setExecutor(new Cmd_Jail(this)); // 2018/04/01
		getCommand("hat").setExecutor(new Cmd_Hat()); // 2018/04/01
		getCommand("body").setExecutor(new Cmd_Body()); // 2018/04/01
		getCommand("leg").setExecutor(new Cmd_Leg()); // 2018/04/01
		getCommand("boots").setExecutor(new Cmd_Boots()); // 2018/04/01
		getCommand("ck").setExecutor(new Cmd_Ck()); // 2018/04/05
		getCommand("sign").setExecutor(new Cmd_Sign()); // 2018/04/09
		getCommand("account").setExecutor(new Cmd_Account()); // 2018/05/01
		getCommand("pin").setExecutor(new Cmd_Pin()); // 2018/05/02
		getCommand("respawn").setExecutor(new Cmd_Respawn()); // 2018/05/06
		getCommand("testment").setExecutor(new Cmd_Testment()); // 2018/05/09
		getCommand("book").setExecutor(new Cmd_Book()); // 2018/05/12
		getCommand("report").setExecutor(new Cmd_Report()); // 2018/05/19
		getCommand("wtp").setExecutor(new Cmd_Wtp()); // 2018/05/19
		getCommand("var").setExecutor(new Cmd_Var()); // 2018/06/03
		getCommand("invload").setExecutor(new Cmd_InvLoad()); // 2018/06/06
		getCommand("invsave").setExecutor(new Cmd_InvSave()); // 2018/06/06
		getCommand("invedit").setExecutor(new Cmd_InvEdit()); // 2018/06/08
		getCommand("invshow").setExecutor(new Cmd_InvShow()); // 2018/06/08
		getCommand("enderchest").setExecutor(new Cmd_EnderChest()); // 2018/06/08
		getCommand("protector").setExecutor(new Cmd_Protector()); // 2018/06/30
		getCommand("chuocity").setExecutor(new Cmd_ChuoCity()); // 2018/06/30
		getCommand("show").setExecutor(new Cmd_Show()); // 2018/07/19
		getCommand("hide").setExecutor(new Cmd_Hide()); // 2018/07/19
		getCommand("ded").setExecutor(new Cmd_Ded()); // 2018/07/22
		getCommand("restarttitle").setExecutor(new Cmd_RestartTitle()); // 2018/07/25
		getCommand("summer").setExecutor(new Cmd_Summer()); // 2018/08/01
		getCommand("glookup").setExecutor(new Cmd_GLookup()); // 2018/08/02
		getCommand("chatjail").setExecutor(new Cmd_ChatJail(this)); // 2018/09/09
		getCommand("rider").setExecutor(new Cmd_Rider()); // 2018/09/16
		getCommand("itemedit").setExecutor(new Cmd_ItemEdit()); // 2018/10/06
		getCommand("lead").setExecutor(new Cmd_Lead()); // 2018/10/06
		getCommand("votefill").setExecutor(new Cmd_VoteFill()); // 2018/12/02
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
		getCommand("chatjail").setTabCompleter(new Cmd_ChatJail(this)); // 2018/09/09
	}

	/**
	 * スケジューリング
	 * @author mine_book000
	 */
	private void Import_Task(){
		new Task_AFK().runTaskTimerAsynchronously(this, 0L, 1200L);
		new TPSChange().runTaskTimerAsynchronously(this, 0L, 1200L);
		new AutoMessenger().runTaskTimerAsynchronously(this, 0L, 12000L);
		new Team1000Observer().runTaskTimerAsynchronously(this, 0L, 1200L);
		new Task_WorldSave(this).runTaskTimerAsynchronously(this, 0L, 1200L);
	}

	/**
	 * リスナー設定
	 * @author mine_book000
	 */
	private void Import_Listener(){
		// 日付は制作完了(登録)の日付
		registEvent(this);
		registEvent(new Event_PlayerCheckPreLogin());// 2018/03/20
		registEvent(new Event_CommandBlockLogger());// 2018/03/20
		registEvent(new Event_LoginSuccessCheck());// 2018/03/20
		registEvent(new Event_PlayerQuit());// 2018/03/20
		registEvent(new Event_DedRain());// 2018/03/21
		registEvent(new Event_AFK());// 2018/03/21
		registEvent(new Event_QD_NOTSpectator());// 2018/03/21
		registEvent(new Event_MoveToChunkActionbar());// 2018/03/21
		registEvent(new Event_FarmNOBreak());// 2018/03/21
		registEvent(new Event_NOConcretePowderToConcrete());// 2018/03/21
		registEvent(new Event_VoteReceived());// 2018/03/24
		registEvent(new Event_PlayerCommandSendAdmin());// 2018/03/25
		registEvent(new Event_JoinjaoPoint());// 2018/03/25
		registEvent(new Event_Antijaoium());// 2018/03/25
		registEvent(new Event_SKKColor(this));// 2018/03/26
		registEvent(new Event_JoinHeaderFooterChange(this));// 2018/03/26
		registEvent(new Event_QuitHeaderFooterChange(this));// 2018/03/26
		registEvent(new Event_VoteMissFillerEvent());// 2018/03/27
		registEvent(new Event_AntiProblemCommand());// 2018/03/29
		registEvent(new Event_ChatSpamKickDisable());// 2018/04/01
		registEvent(new Event_EBan());// 2018/04/01
		registEvent(new Event_Jail());// 2018/04/01
		registEvent(new Event_LoginLeftPlayerCountNotice());// 2018/04/01
		registEvent(new Event_JoinAutoQPPE());// 2018/04/07
		registEvent(new Event_SignClick());// 2018/04/09
		registEvent(new Event_AntiTNTMinecart());// 2018/04/22
		registEvent(new Event_AntiWither());// 2018/05/27
		registEvent(new Event_LoginVoteCheck());// 2018/05/31
		registEvent(new Event_CommandBlockVariable());// 2018/06/03
		registEvent(new Event_BanLogger());// 2018/06/10
		registEvent(new Event_OnlineTime());// 2018/07/12
		registEvent(new Event_AntiNetherPortal()); // 2018/07/14
		registEvent(new Event_BanChecker()); // 2018/07/15
		registEvent(new Event_AntiQDTeleport()); // 2018/07/18
		registEvent(new Event_AntiPotion()); // 2018/07/19
		registEvent(new Event_Ded()); // 2018/07/22
		registEvent(new Event_Summer2018()); // 2018/07/30
		registEvent(new Event_PlayerCommandSendRegular()); // 2018/08/07
		registEvent(new Event_DeathToDeath()); // 2018/08/07
		registEvent(new Event_LongTimeNoSee()); // 2018/08/18
		registEvent(new Event_CheckChatOPME()); // 2018/09/02
		registEvent(new Event_VeryVeryStrongAntiLQD()); // 2018/09/08
		registEvent(new Event_ChatJail()); // 2018/09/09
		registEvent(new Event_SandBoxRoad()); // 2018/10/08
		registEvent(new Event_SandBoxBuildProtectionNotice()); // 2018/10/19
		registEvent(new Event_LoginTutorialSpawn()); // 2018/10/29
		registEvent(new Event_Tips()); // 2018/11/13
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

		if(conf.contains("pastebin_devkey")){
			if(conf.isList("pastebin_devkey")){
				pastebin_devkeyList = conf.getStringList("pastebin_devkey");
				pastebin_devkey = pastebin_devkeyList.get(0);
			}else{
				pastebin_devkey = conf.getString("pastebin_devkey");
			}
		}else{
			getLogger().info("pastebinのdevKeyが取得できません。");
			getLogger().info("Disable MyMaid2...");
			getServer().getPluginManager().disablePlugin(this);
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
		try {
			Jail.SaveJailData();
		} catch (Exception e) {
			MyMaid2Premise.BugReporter(e);
		}
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
