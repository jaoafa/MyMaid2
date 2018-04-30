package com.jaoafa.MyMaid2.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.ParseSelector;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_AntiProblemCommand extends MyMaid2Premise implements Listener {
	JavaPlugin plugin;
	public Event_AntiProblemCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onEvent_AntiProblemCommand(PlayerCommandPreprocessEvent event){
		String command = event.getMessage();
		Player player = event.getPlayer();
		String[] args = command.split(" ", 0);
		if(args.length == 1){

		}else if(args.length >= 1){
			if(args[0].equalsIgnoreCase("/god") || args[0].equalsIgnoreCase("/worldguard:god")){
				// /godコマンド規制
				player.chat("オ、オオwwwwwwwwオレアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww");
				event.setCancelled(true);
				return;
			}else if(args[0].equalsIgnoreCase("/pl") || args[0].equalsIgnoreCase("/bukkit:pl")){
				String group = PermissionsManager.getPermissionMainGroup(player);
				if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
					return;
				}
				// /plコマンド規制
				player.chat("なんか静かですね。街の中にはギャラルホルンもいないし本部とはえらい違いだ。");
				player.chat("ああ。火星の戦力は軒並み向こうに回してんのかもな。");
				player.chat("まっそんなのもう関係ないですけどね！");
				player.chat("上機嫌だな。");
				player.chat("そりゃそうですよ！みんな助かるし、タカキも頑張ってたし、俺も頑張らないと！");
				player.chat("ああ。（そうだ。俺たちが今まで積み上げてきたもんは全部無駄じゃなかった。これからも俺たちが立ち止まらないかぎり道は続く）");
				player.chat("ぐわっ！");
				player.chat("団長？何やってんだよ？団長！");
				player.chat("ぐっ！うおぉ～～！");
				player.chat("うおっ！あっ！");
				player.chat("はぁはぁはぁ・・・。なんだよ、結構当たんじゃねぇか。ふっ・・・。");
				player.chat("だ・・・団長・・・。あっ・・・あぁ・・・。");
				player.chat("なんて声出してやがる・・・ライド。");
				player.chat("だって・・・だって・・・。");
				player.chat("俺は鉄華団団長オルガ・イツカだぞ。こんくれぇなんてこたぁねぇ。");
				player.chat("そんな・・・俺なんかのために・・・。");
				player.chat("団員を守んのは俺の仕事だ。");
				player.chat("でも！");
				player.chat("いいから行くぞ。皆が待ってんだ。それに・・・。");

				player.chat("俺は止まんねぇからよ、お前らが止まんねぇかぎり、その先に俺はいるぞ！");
				player.chat("だからよ、止まるんじゃねぇぞ・・・。");
				event.setCancelled(true);
				return;
			}
		}else if(args.length >= 2){
			List<String> LeastOne = new ArrayList<String>();
			LeastOne.add("r");
			LeastOne.add("type");
			LeastOne.add("team");
			LeastOne.add("name");

			if(args[0].equalsIgnoreCase("/kill")){
				// /killコマンド規制
				boolean killflag = false;
				String group = PermissionsManager.getPermissionMainGroup(player);
				if(group.equalsIgnoreCase("Limited")){
					killflag = true;
				}else if(group.equalsIgnoreCase("QPPE")){
					killflag = true;
				}else if(group.equalsIgnoreCase("Default")){
					killflag = true;
				}
				if(args[1].equalsIgnoreCase("@e")){
					// Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "コマンド「/kill @e」の実行", false);
					player.chat("キリトかなーやっぱりww");
					player.chat("自分は思わないんだけど周りにキリトに似てるってよく言われるwww");
					player.chat("こないだDQNに絡まれた時も気が付いたら意識無くて周りに人が血だらけで倒れてたしなwww");
					player.chat("ちなみに彼女もアスナに似てる(聞いてないw)");
					event.setCancelled(true);
					return;
				}
				if(killflag){
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(new Date()) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: " + player.getName() + "さんが" + args[1] + "を殺すとか調子に乗ってると思うので" + player.getName() + "さんを殺しておきますね^^");
					DiscordSend("**jaotan**: " + player.getName() + "さんが" + args[1] + "を殺すとか調子に乗ってると思うので" + player.getName() + "さんを殺しておきますね^^");
					player.setHealth(0);
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@a")){
					player.chat("キリトかなーやっぱw");
					player.chat("一応オタクだけど彼女いるし、俺って退けない性格だしそこら辺とかめっちゃ似てるって言われる()");
					player.chat("握力も31キロあってクラスの女子にたかられる←彼女いるからやめろ！笑");
					player.chat("俺、これでも中1ですよ？");
					event.setCancelled(true);
					return;
				}
				if(args[1].startsWith("@e")){
					if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
						player.chat("最後にキレたのは高2のころかな。オタクだからってウェイ系に絡まれたときw");
						player.chat("最初は微笑してたんだけど、推しを貶されて気づいたらウェイ系は意識無くなってて、25人くらいに取り押さえられてたw記憶無いけど、ひたすら笑顔で殴ってたらしいw俺ってサイコパスなのかもなww");
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@e")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							Set<String> unvalids = parser.getUnValidValues();
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "不適切だったセレクター引数: " + implode(unvalids, ", "));
							event.setCancelled(true);
							return;
						}
						if(!parser.getArgs().containsKey("r")){
							Boolean exist = false;
							for(String one : LeastOne){
								if(parser.getArgs().containsKey(one)){
									Bukkit.broadcastMessage(one + ": true");
									exist = true;
								}
							}
							if(!exist){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」、「type」、「team」、「name」のいずれかを指定せずに実行することはできません。");
								event.setCancelled(true);
								return;
							}
						}
						if(parser.getArgs().containsKey("r")){
							if(Integer.parseInt(parser.getArgs().get("r")) >= 300){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」に300以上の値を指定することはできません。");
								event.setCancelled(true);
								return;
							}
						}else{
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」を指定せずに実行することはできません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@a")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
			}
			if(args[0].equalsIgnoreCase("/minecraft:kill")){
				// /minecraft:killコマンド規制
				boolean killflag = false;
				String group = PermissionsManager.getPermissionMainGroup(player);
				if(group.equalsIgnoreCase("Limited")){
					killflag = true;
				}else if(group.equalsIgnoreCase("QPPE")){
					killflag = true;
				}else if(group.equalsIgnoreCase("Default")){
					killflag = true;
				}
				if(args[1].equalsIgnoreCase("@e")){
					// Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "コマンド「/kill @e」の実行", false);
					player.chat("キリトかなーやっぱりww");
					player.chat("自分は思わないんだけど周りにキリトに似てるってよく言われるwww");
					player.chat("こないだDQNに絡まれた時も気が付いたら意識無くて周りに人が血だらけで倒れてたしなwww");
					player.chat("ちなみに彼女もアスナに似てる(聞いてないw)");
					event.setCancelled(true);
					return;
				}
				if(killflag){
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(new Date()) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: " + player.getName() + "ごときが" + args[1] + "を殺そうだなんて図が高いわ！ " + player.getName() + "が死ね！");
					DiscordSend("**jaotan**: " + player.getName() + "ごときが" + args[1] + "を殺そうだなんて図が高いわ！ " + player.getName() + "が死ね！");
					player.setHealth(0);
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@a")){
					player.chat("キリトかなーやっぱw");
					player.chat("一応オタクだけど彼女いるし、俺って退けない性格だしそこら辺とかめっちゃ似てるって言われる()");
					player.chat("握力も31キロあってクラスの女子にたかられる←彼女いるからやめろ！笑");
					player.chat("俺、これでも中1ですよ？");
					event.setCancelled(true);
					return;
				}
				if(args[1].startsWith("@e")){
					if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
						player.chat("最後にキレたのは高2のころかな。オタクだからってウェイ系に絡まれたときw");
						player.chat("最初は微笑してたんだけど、推しを貶されて気づいたらウェイ系は意識無くなってて、25人くらいに取り押さえられてたw記憶無いけど、ひたすら笑顔で殴ってたらしいw俺ってサイコパスなのかもなww");
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@e")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
						if(!parser.getArgs().containsKey("r")){
							Boolean exist = false;
							for(String one : LeastOne){
								if(parser.getArgs().containsKey(one)){
									Bukkit.broadcastMessage(one + ": true");
									exist = true;
								}
							}
							if(!exist){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」、「type」、「team」、「name」のいずれかを指定せずに実行することはできません。");
								event.setCancelled(true);
								return;
							}
						}
						if(parser.getArgs().containsKey("r")){
							if(Integer.parseInt(parser.getArgs().get("r")) >= 300){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」に300以上の値を指定することはできません。");
								event.setCancelled(true);
								return;
							}
						}else{
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」を指定せずに実行することはできません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@a")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
			}
			if(args[0].equalsIgnoreCase("/pex")){
				if(args[1].equalsIgnoreCase("promote")){
					// /pex promoteコマンド規制
					player.chat("(◞‸◟) ｻﾊﾞｵﾁﾅｲｰﾅ? ﾎﾜｯｳｳﾞｼﾞｸｼﾞｸﾞｨﾝﾉﾝﾞﾝﾞﾝﾞﾝﾞﾍﾟﾗﾚｸﾞｼﾞｭﾁﾞ…ﾇﾇﾉｮｩﾂﾋﾞｮﾝﾇｽﾞｨｺｹｰｯﾝｦｯ…ｶﾅｼﾐ…");
					event.setCancelled(true);
					return;
				}
			}
			if(args[0].equalsIgnoreCase("//calc") || args[0].equalsIgnoreCase("/worldedit:/calc") || args[0].equalsIgnoreCase("//eval") || args[0].equalsIgnoreCase("/worldedit:/eval")){
				// /calcコマンド規制
				player.chat("オ、オオwwwwwwwwオレアタマ良いwwwwwwww最近めっちょ成績あがってんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソハゲアタマを見下しながら食べるフライドチキンは一段とウメェなァァァァwwwwwwww");
				event.setCancelled(true);
				return;
			}
		}
		for(String arg : args){
			// @eをふくむもの
			if(arg.equalsIgnoreCase("@e")){
				player.chat("開けてみたいでしょ～？");
				player.chat("うん、みたーい！");
				player.chat("行きますよー！");
				player.chat("はい！");
				player.chat("せーのっ！");
				player.chat("あぁ～！水素の音ォ〜！！");
				event.setCancelled(true);
				return;
			}
		}
	}
}
