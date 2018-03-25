package com.jaoafa.MyMaid2.Lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;

/**
 * ポイントシステム
 * @author mine_book000
*/
public class Pointjao extends MyMaid2Premise {
	OfflinePlayer offplayer;
	/**
	 * 指定したプレイヤーからjaoポイントデータを取得します。
	 * @param player プレイヤー
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @author mine_book000
	 */
	public Pointjao(Player player) throws ClassNotFoundException, NullPointerException, SQLException {
		if(player == null) throw new NullPointerException("We could not get the player.");
		this.offplayer = player;

		create();
	}
	/**
	 * 指定したオフラインプレイヤーからjaoポイントデータを取得します。
	 * @param offplayer オフラインプレイヤー
	 * @deprecated プレイヤー名で検索するため、思い通りのプレイヤーを取得できない場合があります。
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @author mine_book000
	 */
	@Deprecated
	public Pointjao(OfflinePlayer offplayer) throws ClassNotFoundException, NullPointerException, SQLException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		this.offplayer = offplayer;

		if(offplayer.hasPlayedBefore()){
			create();
		}else{
			try{
				getID();
			}catch(UnsupportedOperationException e){
				throw new UnsupportedOperationException("jao Point Data Not Found");
			}
		}
	}
	/**
	 * 指定したプレイヤーネームからjaoポイントデータを取得します。
	 * @param name プレイヤーネーム
	 * @deprecated プレイヤー名で検索するため、思い通りのプレイヤーを取得できない場合があります。
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @throws UnsupportedOperationException jaoポイントデータが存在しない場合
	 * @author mine_book000
	 */
	@Deprecated
	public Pointjao(String name) throws NullPointerException, ClassNotFoundException, SQLException, UnsupportedOperationException {
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(name);
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		this.offplayer = offplayer;

		if(offplayer.hasPlayedBefore()){
			create();
		}else{
			try{
				getID();
			}catch(UnsupportedOperationException e){
				throw new UnsupportedOperationException("jao Point Data Not Found");
			}
		}
	}
	/**
	 * 指定したプレイヤーUUIDからjaoポイントデータを取得します。
	 * @param uuid プレイヤーのUUID
	 * @deprecated プレイヤー名で検索するため、思い通りのプレイヤーを取得できない場合があります。
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @author mine_book000
	 */
	public Pointjao(UUID uuid) throws NullPointerException, ClassNotFoundException, SQLException {
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(uuid);
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		this.offplayer = offplayer;

		if(offplayer.hasPlayedBefore()){
			create();
		}else{
			try{
				getID();
			}catch(UnsupportedOperationException e){
				throw new UnsupportedOperationException("jao Point Data Not Found");
			}
		}
	}

	/**
	 * プレイヤーのIDを取得します。
	 * @return 所持しているjaoポイント
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @throws UnsupportedOperationException 内部でUnsupportedOperationExceptionが発生した場合
	 * @author mine_book000
	 */
	public int getID() throws SQLException, ClassNotFoundException, NullPointerException, UnsupportedOperationException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jaopoint WHERE uuid = ? ORDER BY id DESC");
		statement.setString(1, offplayer.getUniqueId().toString()); // uuid
		ResultSet res = statement.executeQuery();
		if(res.next()){
			return res.getInt("id");
		}else{
			throw new UnsupportedOperationException("Could not get ID.");
		}
	}
	/**
	 * jaoポイントデータを作成する
	 * @return 作成できたかどうか
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 */
	public boolean create() throws ClassNotFoundException, SQLException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		if(exists()) return false;
		PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jaopoint (player, uuid, jao) VALUES (?, ?, ?);");
		statement.setString(1, offplayer.getName()); // player
		statement.setString(2, offplayer.getUniqueId().toString()); // uuid
		statement.setInt(3, 0);
		int count = statement.executeUpdate();
		if(count != 0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * jaoポイントデータが存在するかどうかを確認します。
	 * @return 存在するかどうか
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @throws UnsupportedOperationException 内部でUnsupportedOperationExceptionが発生した場合
	 * @author mine_book000
	 */
	public boolean exists() throws SQLException, ClassNotFoundException, NullPointerException, UnsupportedOperationException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jaopoint WHERE uuid = ? ORDER BY id DESC");
		statement.setString(1, offplayer.getUniqueId().toString()); // uuid
		ResultSet res = statement.executeQuery();
		if(res.next()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 所持しているjaoポイントを取得します。
	 * @return 所持しているjaoポイント
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @author mine_book000
	 */
	public int get() throws SQLException, ClassNotFoundException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM jaopoint WHERE uuid = ? ORDER BY id DESC");
		statement.setString(1, offplayer.getUniqueId().toString()); // uuid
		ResultSet res = statement.executeQuery();
		if(res.next()){
			return res.getInt("jao");
		}else{
			return 0;
		}
	}
	/**
	 * 指定したjaoポイントを所持しているかどうか確認
	 * @param i 確認するjaoポイント数
	 * @return 所持していたらtrue、していなかったらfalse
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @author mine_book000
	 */
	public boolean has(int i) throws ClassNotFoundException, SQLException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		int now = get();
		if(now >= i){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 指定したjaoポイントを追加します。
	 * @param add jaoポイント数
	 * @param reason 追加理由
	 * @return jaoポイントを追加したかどうか
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @author mine_book000
	 */
	public boolean add(int add, String reason) throws ClassNotFoundException, SQLException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		int now = get();
		int newjao = now + add;
		int id;
		try {
			id = getID();
		}catch(UnsupportedOperationException e){
			return false;
		}
		PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaopoint SET jao = ? WHERE id = ? ORDER BY id DESC");
		statement.setInt(1, newjao);
		statement.setInt(2, id);
		int count = statement.executeUpdate();
		if(count != 0){
			NoticeAndHistoryAdd(add, newjao, NoticeType.Add, reason);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 指定したjaoポイントを使用します。
	 * @param use jaoポイント数
	 * @param reason 使用理由
	 * @return jaoポイントを使用したかどうか
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @author mine_book000
	 */
	public boolean use(int use, String reason) throws ClassNotFoundException, SQLException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		int now = get();
		if(!has(use)){
			return false;
		}
		int id;
		try {
			id = getID();
		}catch(UnsupportedOperationException e){
			return false;
		}
		int newjao = now - use;
		PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE jaopoint SET jao = ? WHERE id = ? ORDER BY id DESC");
		statement.setInt(1, newjao);
		statement.setInt(2, id);
		int count =  statement.executeUpdate();
		if(count != 0){
			NoticeAndHistoryAdd(use, newjao, NoticeType.Use, reason);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 通知とログ(ヒストリー)に情報を登録する
	 * @param jao 変更ポイント
	 * @param next 変更後のポイント
	 * @param type 加算が減算
	 * @param reason 理由
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 */
	private void NoticeAndHistoryAdd(int jao, int next, NoticeType type, String reason) throws ClassNotFoundException, SQLException {
		SendPlayerNotice(jao, next, type, reason);
		DiscordNotice(jao, type, reason);
		HistoryAdd(jao, next, type, reason);
	}

	/**
	 * プレイヤーに対してjaoポイントの変化を通知する。
	 * @param jao
	 * @param next
	 * @param type
	 * @param reason
	 */
	private void SendPlayerNotice(int jao, int next, NoticeType type, String reason){
		Player player = offplayer.getPlayer();
		if(player == null){
			return;
		}
		player.sendMessage("[POINT] " + ChatColor.GREEN + jao + "ポイントを" + type.getName() + "しました。現在" + next + "ポイント持っています。");
		player.sendMessage("[POINT] " + ChatColor.GREEN + "理由: " + reason);
	}

	/**
	 * Discord#toma_labに通知を出す
	 * @param offplayer OfflinePlayer
	 * @param jao 変更ポイント
	 * @param type 加算か減算
	 * @param reason 理由
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	private void DiscordNotice(int jao, NoticeType type, String reason){
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		if(offplayer.getUniqueId() == null){
			DiscordSend("293856671799967744", ":scroll:**jaoPoint Logger**: " + offplayer.getName() + type.getConjunction() + jao + "ポイントを" + type.getName() + "しました。\n" + "理由: " + reason + "\n" + "***警告: UUIDがnullを返しました。***");
		}else{
			DiscordSend("293856671799967744", ":scroll:**jaoPoint Logger**: " + offplayer.getName() + type.getConjunction() + jao + "ポイントを" + type.getName() + "しました。\n" + "理由: " + reason);
		}
	}

	/**
	 * ログ(ヒストリー)データベースに情報を登録する
	 * @param jao 変更ポイント
	 * @param next 変更後のポイント
	 * @param type 加算が減算
	 * @param reason 理由
	 * @return データベースへの登録に成功したらtrue, 失敗したらfalse
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 */
	private boolean HistoryAdd(int jao, int next, NoticeType type, String reason) throws ClassNotFoundException, SQLException {
		PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO jaopoint_history (player, uuid, type, point, reason, nowpoint, description) VALUES (?, ?, ?, ?, ?, ?, ?);");
		statement.setString(1, offplayer.getName());
		statement.setString(2, offplayer.getUniqueId().toString());
		statement.setString(3, type.name());
		statement.setInt(4, jao);
		statement.setString(5, reason);
		statement.setInt(6, next);
		statement.setString(7, "プラグイン");
		int count =  statement.executeUpdate();
		if(count != 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 通知のタイプ
	 * @author mine_book000
	 */
	public enum NoticeType {
		Add("加算", "に"),
		Use("減算", "から");

		private String name;
		private String conjunction;
		NoticeType(String name, String conjunction) {
			this.name = name;
			this.conjunction = conjunction;
		}

		public String getName(){
			return name;
		}
		public String getConjunction(){
			return conjunction;
		}
	}
}
