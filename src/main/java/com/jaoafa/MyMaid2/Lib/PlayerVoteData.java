package com.jaoafa.MyMaid2.Lib;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerVoteData {
	OfflinePlayer offplayer;
	/**
	 * 指定したプレイヤーの投票データを取得します。
	 * @param player プレイヤー
	 * @author mine_book000
	 */
	public PlayerVoteData(Player player) {
		if(player == null) throw new NullPointerException("We could not get the player.");
		this.offplayer = player;
	}
	/**
	 * 指定したオフラインプレイヤーの投票データを取得します。
	 * @param offplayer オフラインプレイヤー
	 * @author mine_book000
	 */
	public PlayerVoteData(OfflinePlayer offplayer) {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		this.offplayer = offplayer;
	}
	/**
	 * 指定したプレイヤーネームの投票データを取得します。
	 * @param name プレイヤーネーム
	 * @deprecated プレイヤー名で検索するため、思い通りのプレイヤーを取得できない場合があります。
	 * @exception NullPointerException プレイヤーが取得できなかったとき
	 * @author mine_book000
	 */
	@Deprecated
	public PlayerVoteData(String name) throws NullPointerException {
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(name);
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		this.offplayer = offplayer;
	}

	/**
	 * プレイヤーの投票数を取得します。
	 * @return プレイヤーの投票数
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws UnsupportedOperationException 投票数が取得できなかったとき
	 * @throws NullPointerException プレイヤーが取得できなかったとき
	 */
	public int get() throws ClassNotFoundException, SQLException, UnsupportedOperationException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		if(!exists()) return 0;
		PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM vote WHERE id = ?");
		statement.setInt(1, getID());
		ResultSet res = statement.executeQuery();
		if(res.next()){
			return res.getInt("count");
		}else{
			throw new UnsupportedOperationException("Could not get VoteCount.");
		}
	}

	/**
	 * プレイヤーの投票数データを作成する<br>
	 * ※初めての投票時に作成すること！
	 * @return 作成できたかどうか
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 */
	public boolean create() throws ClassNotFoundException, SQLException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		if(exists()) return false;
		PreparedStatement statement = MySQL.getNewPreparedStatement("INSERT INTO vote (player, uuid, count, first, lasttime, last) VALUES (?, ?, ?, ?, ?, ?);");
		statement.setString(1, offplayer.getName()); // player
		statement.setString(2, offplayer.getUniqueId().toString()); // uuid
		statement.setInt(3, 1);

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String date = format.format(new Date());
		statement.setString(4, date);
		statement.setString(5, date);
		statement.setString(6, date);
		int count = statement.executeUpdate();
		if(count != 0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * プレイヤーの投票数データが存在するかどうかを確認します。
	 * @return 存在するかどうか
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws NullPointerException 内部でNullPointerExceptionが発生した場合
	 * @throws UnsupportedOperationException 内部でUnsupportedOperationExceptionが発生した場合
	 * @author mine_book000
	 */
	public boolean exists() throws SQLException, ClassNotFoundException, NullPointerException, UnsupportedOperationException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM vote WHERE uuid = ? ORDER BY id DESC");
		statement.setString(1, offplayer.getUniqueId().toString()); // uuid
		ResultSet res = statement.executeQuery();
		if(res.next()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * プレイヤーの投票数に1つ追加します。
	 * @return 実行できたかどうか
	 * @throws ClassNotFoundException 内部でClassNotFoundExceptionが発生した場合
	 * @throws SQLException 内部でSQLExceptionが発生した場合
	 * @throws NullPointerException プレイヤーが取得できなかったとき
	 */
	public boolean add() throws ClassNotFoundException, SQLException, NullPointerException {
		if(offplayer == null) throw new NullPointerException("We could not get the player.");
		if(!exists()){
			create();
			return true;
		}
		int next = get() + 1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE vote SET count = ?, lasttime = ?, last = ? WHERE id = ?");
		statement.setInt(1, next);
		statement.setInt(2, (int) (System.currentTimeMillis() / 1000L));
		statement.setString(3, sdf.format(new Date()));
		statement.setInt(4, getID());
		int upcount = statement.executeUpdate();
		if(upcount != 0){
			return true;
		}else{
			return false;
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
		PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM vote WHERE uuid = ? ORDER BY id DESC");
		statement.setString(1, offplayer.getUniqueId().toString()); // uuid
		ResultSet res = statement.executeQuery();
		if(res.next()){
			return res.getInt("id");
		}else{
			throw new UnsupportedOperationException("Could not get ID.");
		}
	}
}
