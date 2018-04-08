package facejup.mce.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.Lang;

public class CommandAchievements implements CommandExecutor{

	public String name = "achievements";
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ConsoleUse);
			return true;
		}
		
		Player player = (Player) sender;
		player.openInventory(InventoryBuilder.createAchievementInventory(player));
		
		return true;
	}

	
	
}
