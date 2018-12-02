package com.jaoafa.MyMaid2.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Event.Event_VoteReceived;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Cmd_VoteFill extends MyMaid2Premise implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
			SendUsageMessage(sender, cmd);
			return true;
		}
		if(!(sender instanceof Player)){
			SendMessage(sender, cmd, "このコマンドはプレイヤーから実行してください。");
			return true;
		}
		Player player = (Player) sender;
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
			SendMessage(sender, cmd, "このコマンドは管理部・モデレーターのみ使用可能です。");
			return true;
		}
		if(args.length != 1){
			SendMessage(sender, cmd, "引数には投票を補填するプレイヤー名を入力してください。");
			return true;
		}
		String name = args[1];
		DiscordSend("499922840871632896", ":mailbox_with_mail: **投票手動補填通知**: " + player.getName() + "によって" + name + "の補填処理を開始しました。");
		boolean res = Event_VoteReceived.VoteReceive(name);
		if(res){
			SendMessage(sender, cmd, "補填が完了しました。");
			return true;
		}else{
			SendMessage(sender, cmd, "補填に失敗しました。");
			return false;
		}
	}
}
