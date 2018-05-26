package com.jaoafa.MyMaid2.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Command.MyMaidBookHistory.MyMaidBookHistoryType;
import com.jaoafa.MyMaid2.Lib.MySQL;
import com.jaoafa.MyMaid2.Lib.Pointjao;

public class Cmd_Book extends MyMaid2Premise implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Book(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				// /book list : 本のリストを表示
				try {
					PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT COUNT(*) FROM book WHERE status = ?");
					statement.setString(1, "now");
					ResultSet rescount = statement.executeQuery();
					rescount.next();
					int size = rescount.getInt("COUNT(*)");

					statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE status = ?");
					statement.setString(1, "now");
					ResultSet res = statement.executeQuery();

					int count = 0;
					int page = 1;
					int startcount = (page - 1) * 10;
					int endcount = page * 10;
					int maxpage = size / 10;
					if(size % 10 != 0) maxpage += 1;

					SendMessage(sender, cmd, "Book List: " + page + "page / " + maxpage + "page");
					SendMessage(sender, cmd, "-------------------------");

					while(res.next()){
						if(count < startcount){
							count++;
							continue;
						}
						if(count > endcount){
							break;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						MyMaidBookData bookdata;
						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
						SendMessage(sender, cmd, "[" + bookdata.getID() + "] " + bookdata.getTitle() + ChatColor.GREEN + " - " + bookdata.getAuthorName() + "(" + bookdata.getRequiredjao() + "jao)");
						count++;
					}

					SendMessage(sender, cmd, "-------------------------");
					SendMessage(sender, cmd, startcount + " - " + endcount + " / " + size);
					if(page != maxpage){
						SendMessage(sender, cmd, "次のページを見るには「/book list " + (page + 1) + "」を実行します。");
					}
					return true;

				} catch (ClassNotFoundException | SQLException e) {
					BugReporter(e);
				}
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("get")){
				// /book get <Name|ID>
				if(!(sender instanceof Player)){
					SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				Player player = (Player) sender;
				try {
					MyMaidBookData bookdata = null;
					if(isNumber(args[1])){
						// ID
						PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE id = ?");
						statement.setInt(1, Integer.parseInt(args[1]));
						ResultSet res = statement.executeQuery();

						if(!res.next()){
							SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						if(status.equalsIgnoreCase("end")){
							// 販売終了
							SendMessage(sender, cmd, "指定された本はすでに販売を終了しています。");
							return true;
						}else if(!status.equalsIgnoreCase("now")){
							SendMessage(sender, cmd, "指定された本は未発売か、なにかしらの理由で販売できません。(Status: " + status + ")");
							return true;
						}

						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}else{
						PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE title = ?");
						statement.setString(1, args[1]);
						ResultSet res = statement.executeQuery();

						if(!res.next()){
							SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}

					Pointjao Pjao = new Pointjao(player);

					if(!Pjao.has(bookdata.getRequiredjao())){
						// ポイントが足りない
						SendMessage(sender, cmd, "指定された本を購入するには、" + bookdata.getRequiredjao() + "jaoが必要です。");
						return true;
					}
					Pjao.use(bookdata.getRequiredjao(), "本「" + bookdata.getTitle() + "」の購入のため");
					if(bookdata.getAuthor() != null){
						try{
							@SuppressWarnings("deprecation")
							Pointjao Pjao_wasbought = new Pointjao(bookdata.getAuthor());
							Pjao_wasbought.add(bookdata.getRequiredjao(), "本「" + bookdata.getTitle() + "」を" + player.getName() + "が購入したため");
						}catch (UnsupportedOperationException e){
							// jaotanなど指定した場合アカウントがないといわれるので回避
						}
					}

					ItemStack is = bookdata.getBook();
					if(player.getInventory().firstEmpty() == -1){
						player.getLocation().getWorld().dropItem(player.getLocation(), is);
						SendMessage(sender, cmd, "購入された本をインベントリに追加しようとしましたが、インベントリが一杯だったのであなたの足元にドロップしました。");
					}else{
						player.getInventory().addItem(is);
						SendMessage(sender, cmd, "購入された本をインベントリに追加しました。");
					}

					SendMessage(sender, cmd, "必ずお読みください: https://jaoafa.com/blog/tomachi/book_command_item_duplication");

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String date = sdf.format(new Date());
					bookdata.addHistory(player, MyMaidBookHistoryType.BUY, date);
					PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE book SET history = ?, count = count + 1 WHERE id = ?");
					statement.setString(1, bookdata.getRawHistory());
					statement.setInt(2, bookdata.getID());
					statement.executeUpdate();
					return true;
				} catch (SQLException | ClassNotFoundException | NullPointerException e) {
					// TODO 自動生成された catch ブロック
					BugReporter(e);
					return true;
				}
			}else if(args[0].equalsIgnoreCase("delete")){
				// /book delete <Name|ID>
				if(!(sender instanceof Player)){
					SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				Player player = (Player) sender;
				try{
					MyMaidBookData bookdata = null;
					if(isNumber(args[1])){
						// ID
						PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE id = ?");
						statement.setInt(1, Integer.parseInt(args[1]));
						ResultSet res = statement.executeQuery();

						if(!res.next()){
							SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");


						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, status, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}else{
						PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE title = ?");
						statement.setString(1, args[1]);
						ResultSet res = statement.executeQuery();

						if(!res.next()){
							SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, status, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}

					if(bookdata.getAuthor() == null){
						SendMessage(sender, cmd, "この本は販売終了できません。(プレイヤーデータ取得失敗)");
						return true;
					}
					OfflinePlayer offplayer = bookdata.getAuthor();
					if(!offplayer.getUniqueId().toString().equalsIgnoreCase(player.getUniqueId().toString())){ // 割とクソ実装感ある
						// UUIDが違ったら(プレイヤーが違ったら)
						SendMessage(sender, cmd, "この本は" + offplayer.getName() + "の販売物のため、販売を終了できません。");
						return true;
					}
					if(!bookdata.getStatus().equalsIgnoreCase("now")){
						SendMessage(sender, cmd, "この本は現在販売中ではないため、販売を終了できません。");
						return true;
					}

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String date = sdf.format(new Date());
					bookdata.addHistory(player, MyMaidBookHistoryType.END, date);
					PreparedStatement statement = MySQL.getNewPreparedStatement("UPDATE book SET history = ?, status = ? WHERE id = ?");
					statement.setString(1, bookdata.getRawHistory());
					statement.setString(2, "end");
					statement.setInt(3, bookdata.getID());
					statement.executeUpdate();
					SendMessage(sender, cmd, "販売を終了しました。");
					return true;
				} catch (SQLException | ClassNotFoundException e) {
					// TODO 自動生成された catch ブロック
					BugReporter(e);
					return true;
				}
			}else if(args[0].equalsIgnoreCase("history")){
				// /book history <Name|ID>
				try{
					MyMaidBookData bookdata = null;
					if(isNumber(args[1])){
						// ID
						PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE id = ?");
						statement.setInt(1, Integer.parseInt(args[1]));
						ResultSet res = statement.executeQuery();

						if(!res.next()){
							SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}


						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");


						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, status, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}else{
						PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE title = ?");
						statement.setString(1, args[1]);
						ResultSet res = statement.executeQuery();

						if(!res.next()){
							SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, status, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}
					SendMessage(sender, cmd, "----- 本「" + bookdata.getTitle() + "」のログデータ -----");
					List<MyMaidBookHistory> historys = bookdata.getHistory();
					for(MyMaidBookHistory history : historys){
						SendMessage(sender, cmd, history.getName() + "が" + history.getDate() + "に" + history.getType().getName() + "しました。");
					}
					return true;
				} catch (SQLException | ClassNotFoundException e) {
					// TODO 自動生成された catch ブロック
					BugReporter(e);
					return true;
				}
			}else if(args[0].equalsIgnoreCase("sell")){
				// /book save <Point>
				if(!(sender instanceof Player)){
					SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				Player player = (Player) sender;
				if(!isNumber(args[1])){ // 販売に必要なjaoポイント
					SendMessage(sender, cmd, "必要jaoポイント数には数値を指定してください。");
					return true;
				}
				int requiredjao = Integer.parseInt(args[1]);
				try {
					if(player.getInventory().getItemInMainHand().getType() == Material.AIR){
						SendMessage(sender, cmd, "アイテムを持っていません。");
						return true;
					}
					Material handtype = player.getInventory().getItemInMainHand().getType();
					if(handtype != Material.WRITTEN_BOOK){
						SendMessage(sender, cmd, "このコマンドを使用するには、販売する本を手に持ってください。");
						return true;
					}
					BookMeta book = (BookMeta) player.getInventory().getItemInMainHand().getItemMeta();

					String title = book.getTitle();

					PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE title = ? AND status = ?");
					statement.setString(1, title);
					statement.setString(2, "now");
					ResultSet res = statement.executeQuery();

					if(res.next()){
						// 被りを防ぐため、同名の本は販売できない
						SendMessage(sender, cmd, "指定された本の題名と同じ題名の本が発売されています。");
						SendMessage(sender, cmd, "システムの障害を引き起こしたり、無駄な発売のし過ぎを未然に防ぐため、同名の本を複数販売することはできません。");
						return true;
					}

					String pages_data = implode(book.getPages(), "§j");

					String author = player.getName();
					String author_uuid = player.getUniqueId().toString();
					long unixtime = System.currentTimeMillis() / 1000L;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String date = sdf.format(new Date());
					statement.execute("INSERT INTO book (title, author, author_uuid, data, requiredjao, count, history, status, createdate) VALUES ('" + title + "', '" + author + "', '" + author_uuid + "', '" + pages_data + "', '" + requiredjao + "', '0', '" + author + ",create," + date + "', 'now', '" + unixtime + "');");
					SendMessage(sender, cmd, "指定された本の販売を開始しました。詳しくは/book listをお使いください。");
					return true;
				} catch (SQLException | ClassNotFoundException e) {
					// TODO 自動生成された catch ブロック
					BugReporter(e);
					return true;
				}
			}else if(args[0].equalsIgnoreCase("list")){
				// /book list <Page> : 本のリストを表示
				try {
					PreparedStatement statement = MySQL.getNewPreparedStatement("SELECT COUNT(*) FROM book WHERE status = ?");
					statement.setString(1, "now");
					ResultSet rescount = statement.executeQuery();
					rescount.next();
					int size = rescount.getInt("COUNT(*)");

					statement = MySQL.getNewPreparedStatement("SELECT * FROM book WHERE status = ?");
					statement.setString(1, "now");
					ResultSet res = statement.executeQuery();
					if(!isNumber(args[1])){
						SendMessage(sender, cmd, "指定されたページは数値ではありません。");
						return true;
					}

					int count = 0;
					int page = Integer.parseInt(args[1]);
					int startcount = (page - 1) * 10;
					int endcount = page * 10;
					int maxpage = size / 10;
					if(size % 10 != 0) maxpage += 1;

					SendMessage(sender, cmd, "Book List: " + page + "page / " + maxpage + "page");
					SendMessage(sender, cmd, "-------------------------");

					while(res.next()){
						if(count < startcount){
							count++;
							continue;
						}
						if(count > endcount){
							break;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						MyMaidBookData bookdata;
						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReporter(e);
								SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
						SendMessage(sender, cmd, "[" + bookdata.getID() + "] " + bookdata.getTitle() + " - " + bookdata.getAuthorName() + "(" + bookdata.getRequiredjao() + "jao)");
						count++;
					}

					SendMessage(sender, cmd, "-------------------------");
					SendMessage(sender, cmd, startcount + " - " + endcount + " / " + size);
					if(page != maxpage){
						SendMessage(sender, cmd, "次のページを見るには「/book list " + (page + 1) + "」を実行します。");
					}
					return true;
				} catch (SQLException | ClassNotFoundException e) {
					// TODO 自動生成された catch ブロック
					BugReporter(e);
					return true;
				}
			}
		}
		SendUsageMessage(sender, cmd);
		return true;
	}
	boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	<T> String implode(List<T> list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (T e : list) {
			sb.append(glue).append(e);
		}
		return sb.substring(glue.length());
	}
}

class MyMaidBookData extends MyMaid2Premise {
	private int id; // 本のID
	private String title; // 本のタイトル
	private OfflinePlayer author = null; // 著者。存在しない場合nullもしくは空文字になるケース
	private String author_name; // 著者名
	private UUID author_uuid = null; // 著者UUID。UUIDが存在しない場合nullになるケースあり
	private List<String> pages = new ArrayList<String>(); // ページデータ。
	private int requiredjao; // 必要jaoポイント数
	private int count; // 購入数
	private String history; // 本のログデータ(購入など)
	private String status; // 本のステータス。販売中/販売終了など
	private Date createdate;
	private int createdate_unixtime;

	MyMaidBookData(int id, String title, String author_name, String pages_str, int requiredjao, int count, String history, String status, int createdate_unixtime){
		this.id = id;
		this.title = title;
		this.author_name = author_name;
		@SuppressWarnings("deprecation")
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(author_name);
		if(offplayer != null){
			author_uuid = offplayer.getUniqueId();
			this.author = offplayer;
		}
		pages = Arrays.asList(pages_str.split("§j"));
		this.requiredjao = requiredjao;
		this.history = history;
		this.status = status;
		createdate = new Date(createdate_unixtime);
		this.createdate_unixtime = createdate_unixtime;
	}

	MyMaidBookData(int id, String title, UUID author_uuid, String pages_str, int requiredjao, int count, String history, String status, int createdate_unixtime) throws IllegalArgumentException{
		this.id = id;
		this.title = title;
		this.author_uuid = author_uuid;
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(author_uuid);
		if(offplayer != null){
			author_name = offplayer.getName();
			this.author = offplayer;
		}else{
			throw new IllegalArgumentException("データの解析に失敗しました。(This UUID Player is not found. / UUID: " + author_uuid.toString() + ")");
		}
		pages = Arrays.asList(pages_str.split("§j"));
		this.requiredjao = requiredjao;
		this.history = history;
		this.status = status;
		createdate = new Date(createdate_unixtime);
		this.createdate_unixtime = createdate_unixtime;
	}

	int getID(){
		return id;
	}

	String getTitle(){
		return title;
	}

	OfflinePlayer getAuthor(){
		return author;
	}

	String getAuthorName(){
		return author_name;
	}

	UUID getAuthorUUID(){
		return author_uuid;
	}

	List<String> getPages(){
		return pages;
	}

	String getPagesToString(){
		return implode(getPages(), "§j");
	}

	int getRequiredjao(){
		return requiredjao;
	}

	int getCount(){
		return count;
	}

	List<MyMaidBookHistory> getHistory(){
		// 一行ごとに「mine_book000|create|1514732400」など。
		List<String> lines = Arrays.asList(getRawHistory().replaceAll("\r\n", "\n").split("\n"));
		List<MyMaidBookHistory> historyList = new ArrayList<MyMaidBookHistory>();
		for(String line : lines){
			String[] line_data = line.split(",");
			if(line_data.length == 3){
				MyMaidBookHistory mbh = new MyMaidBookHistory(line_data[0], line_data[1], line_data[2]);
				historyList.add(mbh);
			}else{
				// データ形式が不適合
				DiscordSend("293856671799967744", ":octagonal_sign:MyMaidのBookコマンドにてMyMaidBookHistoryのデータ形式不適合が発生しました。\nデータ: ```" + line + "```\n行数: " + lines.size() + "行\n列数: " + line_data.length + "列");
				//Bukkit.getLogger().warning("MyMaidのBookコマンドにてMyMaidBookHistoryのデータ形式不適合が発生しました。\nデータ: ```" + line + "```\n行数: " + lines.size() + "行\n列数: " + line_data.length + "列");
			}
		}
		return historyList;
	}

	String getRawHistory(){
		return history;
	}

	void addHistory(Player player, MyMaidBookHistoryType type, String date){
		String rawhistory = getRawHistory();
		history = rawhistory + "\n" + player.getName() + "," + type.getRawName() + "," + date;
	}

	String getStatus(){
		return status;
	}

	Date getCreateDate(){
		return createdate;
	}

	int getCreateDateUnixTime(){
		return createdate_unixtime;
	}

	ItemStack getBook(){
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bm = (BookMeta) item.getItemMeta();
		bm.setAuthor(getAuthorName());
		bm.setTitle(getTitle());
		bm.setPages(getPages());
		List<String> lore = new ArrayList<String>();
		lore.add("BookID: " + getID());
		lore.add("必要jaoポイント数: " + getRequiredjao() + "jao");
		bm.setLore(lore);
		item.setItemMeta(bm);
		return item;
	}

	<T> String implode(List<T> list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (T e : list) {
			sb.append(glue).append(e);
		}
		return sb.substring(glue.length());
	}
}
class MyMaidBookHistory {
	private String name;
	private String type;
	private String date;

	MyMaidBookHistory(String name, String type, String date){
		this.name = name;
		this.type = type;
		this.date = date;
	}

	String getName(){
		return name;
	}

	MyMaidBookHistoryType getType(){
		if(type.equalsIgnoreCase("create")){
			return MyMaidBookHistoryType.CREATE;
		}else if(type.equalsIgnoreCase("buy")){
			return MyMaidBookHistoryType.BUY;
		}else if(type.equalsIgnoreCase("end")){
			return MyMaidBookHistoryType.END;
		}else{
			return MyMaidBookHistoryType.UNKNOWN;
		}
	}

	String getRawType(){
		return type;
	}

	String getDate(){
		return date;
	}

	public enum MyMaidBookHistoryType {
		CREATE("発売開始", "create"),
		BUY("購入", "buy"),
		END("発売終了", "end"),
		UNKNOWN("未定義", "unknown");

		private String name;
		private String rawname;
		MyMaidBookHistoryType(String name, String rawname) {
			this.name = name;
			this.rawname = rawname;
		}

		String getName(){
			return name;
		}

		String getRawName(){
			return rawname;
		}
	}
}