package facejup.mce.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Chat {
	
	public static void bc(String str)
	{
		Bukkit.broadcastMessage(translate(str));
	}

	public static String translate(String str)
	{
		
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String formatName(String str)
	{
		return StringUtils.capitaliseAllWords(str.toLowerCase().replaceAll("_", " "));
	}
	
}
