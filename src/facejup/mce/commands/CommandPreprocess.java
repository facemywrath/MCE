package facejup.mce.commands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import facejup.mce.main.Main;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;

public class CommandPreprocess implements Listener {
	
	private Main main;
	
	public CommandPreprocess(Main main)
	{
		this.main = main;
		this.main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void commandPreProcess(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		if(event.getMessage().startsWith("/pl") && !player.isOp())
		{
			player.sendMessage(Lang.NoPerm);
			event.setCancelled(true);
			return;
		}
		if(event.getMessage().equalsIgnoreCase("/help"))
		{
			//TODO: SHOW HELP MESSAGE;
			player.sendMessage(Chat.translate("&a&lPlayer Commands:"));
			player.sendMessage(Chat.translate("   &b&o/spectate &ato toggle spectator mode."));
			player.sendMessage(Chat.translate("   &b&o/stats &ato view your stats."));
			player.sendMessage(Chat.translate("   &b&o/stats (player) &ato view another player's stats."));
			player.sendMessage(Chat.translate("   &b&o/stats top &ato view top stats."));
			player.sendMessage(Chat.translate("   &b&o/achievements &ato open the achievement menu."));
			player.sendMessage(Chat.translate("   &b&o/kits &aor rightclick with the &5&lKit Selector &ato open the kit menu."));
			player.sendMessage(Chat.translate("   &b&o/buy &ato view our buycraft menu."));
			player.sendMessage(" ");
			event.setCancelled(true);
		}
	}

}
