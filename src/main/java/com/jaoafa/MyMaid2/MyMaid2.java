package com.jaoafa.MyMaid2;

import org.bukkit.plugin.java.JavaPlugin;

public class MyMaid2 extends JavaPlugin {
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
