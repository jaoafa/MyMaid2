package com.jaoafa.MyMaid2;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.Command.Cmd_AFK;

public class MyMaid2 extends JavaPlugin {
	public static String discordtoken = null;
	public static FileConfiguration conf;

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

		//コンフィグ読み込み
		Load_Config();
		//リスナーを設定
		Import_Listener();
		//スケジュールタスクをスケジュ―リング
		Import_Task();
		//コマンドを設定
		Import_Command_Executor();
		getLogger().info("--------------------------------------------------");
	}

	/**
	 * コマンドの設定
	 * @author mine_book000
	 */
	private void Import_Command_Executor(){
		// 日付は制作完了(登録)の日付
		getCommand("afk").setExecutor(new Cmd_AFK()); // 2018/03/18
	}

	/**
	 * スケジューリング
	 * @author mine_book000
	 */
	private void Import_Task(){

	}

	/**
	 * リスナー設定
	 * @author mine_book000
	 */
	private void Import_Listener(){

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
