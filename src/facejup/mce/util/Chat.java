package facejup.mce.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Chat {
	
	public static void bc(String str)
	{
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', str));
	}

}
