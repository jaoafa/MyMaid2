package com.jaoafa.MyMaid2.Event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.jaoafa.MyMaid2.MyMaid2Premise;
import com.jaoafa.MyMaid2.Lib.PermissionsManager;

public class Event_PlayerCommandSendAdmin extends MyMaid2Premise implements Listener {
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		String command = e.getMessage();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("Default") || group.equalsIgnoreCase("Regular") || group.equalsIgnoreCase("Moderator") || group.equalsIgnoreCase("Admin")){
			// Default以上は実行試行したコマンドを返す
			player.sendMessage(ChatColor.DARK_GRAY + "Cmd: " + command); // 仮
		}
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String p_group = PermissionsManager.getPermissionMainGroup(p);
			if((p_group.equalsIgnoreCase("Admin") || p_group.equalsIgnoreCase("Moderator")) && (!player.getName().equals(p.getName()))){
				//if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator"))){
				p.sendMessage(ChatColor.GRAY + "(" + group +") " + player.getName() + ": " + ChatColor.YELLOW + command);
			}
		}
		/*
		if(command.contains(" ")){
			String[] commands = command.split(" ", 0);
			List<String> tells = new ArrayList<String>();
			tells.add("/tell");
			tells.add("/msg");
			tells.add("/message");
			tells.add("/m");
			tells.add("/t");
			tells.add("/w");

			if(!Bukkit.getServer().getOnlinePlayers().contains(commands[1])){
				return;
			}
			 */
			/*
			if(tells.contains(commands[0])){
				if(commands.length <= 2){
					return;
				}
				String text = "";
				int c = 2;
				while(commands.length > c){
					text += commands[c];
					if(commands.length != (c+1)){
						text += " ";
					}
					c++;
				}
				if(MyMaid.lunachatapi.isPlayerJapanize(player.getName())){
					String jp = MyMaid.lunachatapi.japanize(text, JapanizeType.GOOGLE_IME);
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						String p_group = PermissionsManager.getPermissionMainGroup(p);
						if((p_group.equalsIgnoreCase("Admin") || p_group.equalsIgnoreCase("Moderator")) && (!player.getName().equals(p.getName()))){
							//if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator"))){
							p.sendMessage(ChatColor.GRAY + "(" + ChatColor.YELLOW + jp + ChatColor.GRAY + ")");
						}
					}
				}

			}else if(commands[0].equalsIgnoreCase("/r")){
				if(commands.length <= 1){
					return;
				}
				String text = "";
				int c = 1;
				while(commands.length > c){
					text += commands[c];
					if(commands.length != (c+1)){
						text += " ";
					}
					c++;
				}
				if(MyMaid.lunachatapi.isPlayerJapanize(player.getName())){
					String jp = MyMaid.lunachatapi.japanize(text, JapanizeType.GOOGLE_IME);
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						String p_group = PermissionsManager.getPermissionMainGroup(p);
						if((p_group.equalsIgnoreCase("Admin") || p_group.equalsIgnoreCase("Moderator")) && (!player.getName().equals(p.getName()))){
							//if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator"))){
							p.sendMessage(ChatColor.GRAY + "(" + ChatColor.YELLOW + jp + ChatColor.GRAY + ")");
						}
					}
				}
			}
		}
			*/

	}
}
