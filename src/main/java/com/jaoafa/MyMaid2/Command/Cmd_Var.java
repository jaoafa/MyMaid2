package com.jaoafa.MyMaid2.Command;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.MyMaidVariable;
public class Cmd_Var extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			SendUsageMessage(sender, cmd);
			return true;
		}else if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(args[0].equalsIgnoreCase("text")){
			//Text(/var text var text)
			if(args.length != 3){
				SendMessage(sender, cmd, "引数が適切ではありません。");
				return true;
			}
			Pattern p = Pattern.compile("^[a-zA-Z0-9_]+$");
			Matcher m = p.matcher(args[1]);
			if(!m.find()){
				SendMessage(sender, cmd, "変数は英数字記号のみ許可しています。");
				return true;
			}
			String value = "";
			int c = 2;
			while(args.length > c){
				value += args[c];
				if(args.length != (c+1)){
					value += " ";
				}
				c++;
			}

			boolean bool = MyMaidVariable.set(args[1], value);
			if(bool){
				SendMessage(sender, cmd, "変数「" + args[1] + "」に「" + args[2] + "」を入力しました。");
			}else{
				SendMessage(sender, cmd, "変数「" + args[1] + "」に「" + args[2] + "」を入力することに失敗しました。");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("plus")){
			//Plus(/var plus var var)
			if(args.length < 3){
				SendMessage(sender, cmd, "引数が適切ではありません。");
			}
			if(!MyMaidVariable.exist(args[1])){
				SendMessage(sender, cmd, "変数「" + args[1] + "」は定義されていません。");
				return true;
			}
			if(!MyMaidVariable.exist(args[2])){
				SendMessage(sender, cmd, "変数「" + args[2] + "」は定義されていません。");
				return true;
			}
			int var1;
			try{
				var1 = Integer.parseInt(MyMaidVariable.getString(args[1]));
			} catch (NumberFormatException nfe) {
				SendMessage(sender, cmd, "変数「" + args[1] + "」は数字ではないため加算先にできません。");
				return true;
			}
			int var2;
			try{
				var2 = Integer.parseInt(MyMaidVariable.getString(args[2]));
			} catch (NumberFormatException nfe) {
				SendMessage(sender, cmd, "変数「" + args[2] + "」は数字ではないため加算元にできません。");
				return true;
			}
			int newvar = var1 + var2;
			MyMaidVariable.set(args[1], String.valueOf(newvar));
			SendMessage(sender, cmd, "変数「" + args[1] + "」と変数「" + args[2] + "」を加算し、変数「" + args[1] + "」に入力しました。(回答:" + newvar +")");
			return true;
		}else if(args[0].equalsIgnoreCase("minus")){
			//minus(/var minus var var)
			if(args.length < 3){
				SendMessage(sender, cmd, "引数が適切ではありません。");
				return true;
			}
			if(!MyMaidVariable.exist(args[1])){
				SendMessage(sender, cmd, "変数「" + args[1] + "」は定義されていません。");
				return true;
			}
			if(!MyMaidVariable.exist(args[2])){
				SendMessage(sender, cmd, "変数「" + args[2] + "」は定義されていません。");
				return true;
			}
			int var1;
			try{
				var1 = Integer.parseInt(MyMaidVariable.getString(args[1]));
			} catch (NumberFormatException nfe) {
				SendMessage(sender, cmd, "変数「" + args[1] + "」は数字ではないため減算元にできません。");
				return true;
			}
			int var2;
			try{
				var2 = Integer.parseInt(MyMaidVariable.getString(args[2]));
			} catch (NumberFormatException nfe) {
				SendMessage(sender, cmd, "変数「" + args[2] + "」は数字ではないため減算値にできません。");
				return true;
			}
			int newvar = var1 - var2;
			MyMaidVariable.set(args[1], String.valueOf(newvar));
			SendMessage(sender, cmd, "変数「" + args[1] + "」から変数「" + args[2] + "」を減算し、変数「" + args[1] + "」に入力しました。(回答:" + newvar +")");
			return true;
		}else if(args[0].equalsIgnoreCase("block")){
			//Block(/var block var x y z)
			SendMessage(sender, cmd, "未実装");
			/*
			if(args.length != 5){
				SendMessage(sender, cmd, "引数が適切ではありません。");
			}
			int x;
			try{
				x = Integer.parseInt(args[2]);
			} catch (NumberFormatException nfe) {
				SendMessage(sender, cmd, "数値を指定してください。(X)");
				return true;
			}

			int y;
			try{
				y = Integer.parseInt(args[3]);
			} catch (NumberFormatException nfe) {
				SendMessage(sender, cmd, "数値を指定してください。(X)");
				return true;
			}

			int z;
			try{
				z = Integer.parseInt(args[4]);
			} catch (NumberFormatException nfe) {
				SendMessage(sender, cmd, "数値を指定してください。(X)");
				return true;
			}
			World world;
			if (sender instanceof Player) {
				world = ((Player) sender).getWorld();
			}else if (sender instanceof BlockCommandSender) {
				CommandBlock cb = (CommandBlock) ((BlockCommandSender)sender).getBlock().getState();
				world = cb.getWorld();
			}else{
				SendMessage(sender, cmd, "プレイヤーかコマンドブロックから実行してください。");
				return true;
			}
			Location cmdb_loc = new Location(world, x, y, z);
			if(cmdb_loc.getBlock().getType() != Material.COMMAND){
				SendMessage(sender, cmd, "指定するブロックはコマンドブロックにしてください。");
				return true;
			}
			CommandBlock cmdb = (CommandBlock) cmdb_loc.getBlock().getState();
			// Todo コマンドブロックの実行結果取得方法
			List<MetadataValue> meta = cmdb.getMetadata("LastOutput");
			SendMessage(sender, cmd, ""+meta);
			 */
		}else if(args[0].equalsIgnoreCase("equal")){
			//Equal(/var equal var var)
			if(args.length < 3){
				SendMessage(sender, cmd, "引数が適切ではありません。");
			}
			if(!MyMaidVariable.exist(args[1])){
				SendMessage(sender, cmd, "変数「" + args[1] + "」は定義されていません。");
				return true;
			}
			String var1 = MyMaidVariable.getString(args[1]);
			if(!MyMaidVariable.exist(args[2])){
				SendMessage(sender, cmd, "変数「" + args[2] + "」は定義されていません。");
				return true;
			}
			String var2 = MyMaidVariable.getString(args[2]);
			if(var1.equals(var2)){
				SendMessage(sender, cmd, "結果は「True」です。");
				return true;
			}else{
				SendMessage(sender, cmd, "結果は「False」です。");
				return true;
			}
		}else if(args[0].equalsIgnoreCase("list")){
			int count = MyMaidVariable.getCount();
			int page = 1;
			if(args.length == 1){
				int i = 0;
				try {
					i = Integer.parseInt(args[1]);
				}catch(NumberFormatException e){
					SendMessage(sender, cmd, "ページ名には数字を指定してください。");
					return true;
				}
				if(i <= 0){
					SendMessage(sender, cmd, "0以下のページ名を指定することはできません。");
					return true;
				}
			}
			int maxpage = count / 10;
			SendMessage(sender, cmd, "Variable List: " + page + "page / " + maxpage + "page");
			SendMessage(sender, cmd, "-------------------------");
			Map<String, String> map = MyMaidVariable.list(page);
			for(Map.Entry<String, String> e : map.entrySet()) {
				SendMessage(sender, cmd, "$" + e.getKey() + "$ => " + e.getValue());
			}
			SendMessage(sender, cmd, "-------------------------");
			if(page != maxpage){
				SendMessage(sender, cmd, "次のページを見るには「/var list " + (page + 1) + "」を実行します。");
			}
			return true;
		}else if(args[0].equalsIgnoreCase("clear")){
			if(!MyMaidVariable.exist(args[1])){
				SendMessage(sender, cmd, "変数「" + args[1] + "」は定義されていません。");
				return true;
			}
			MyMaidVariable.remove(args[1]);
			SendMessage(sender, cmd, "変数「" + args[1] + "」を削除しました。");
			return true;
		}
		SendUsageMessage(sender, cmd);
		return true;

	}
}
