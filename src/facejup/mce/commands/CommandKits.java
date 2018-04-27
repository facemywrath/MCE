package facejup.mce.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.enums.MatchType;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import facejup.mce.util.Chat;
import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.Lang;

public class CommandKits implements CommandExecutor{

	public String name = "kits";
	public Main main;
	
	public CommandKits(Main main)
	{
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			if(args.length < 2 || getKitByName(args[1]) == null)
			{
				if(args.length == 1 && !Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore())
				{
					sender.sendMessage(Lang.NullPlayer);
					return true;
				}
				sender.sendMessage(Lang.ConsoleUse);
				return true;
			}
			if(args.length > 1 && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && getKitByName(args[1]) != null)
			{
				Kit kit = getKitByName(args[1]);
				if(Bukkit.getOfflinePlayer(args[0]).isOnline())
					Bukkit.getPlayer(args[0]).sendMessage(Chat.translate(Lang.Tag + "You have been given the kit &b" + StringUtils.capitalize(kit.name().toLowerCase())));
				sender.sendMessage(Chat.translate(Lang.Tag + "&aYou've given &b" + Bukkit.getOfflinePlayer(args[0]).getName() + " &athe kit &b" + StringUtils.capitalize(kit.name().toLowerCase())));
				User user = main.getUserManager().getUser(Bukkit.getOfflinePlayer(args[0]));
				user.unlockKit(kit);
				user.incScore(Achievement.SPENDER);
			}
		}
		else if(((Player) sender).isOp())
		{
			if(args.length < 2 || getKitByName(args[1]) == null)
			{
				if(args.length == 1 && !Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore())
				{
					sender.sendMessage(Lang.NullPlayer);
					return true;
				}
			}
			if(args.length > 0 && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && getKitByName(args[1]) != null)
			{
				Kit kit = getKitByName(args[1]);
				if(Bukkit.getOfflinePlayer(args[0]).isOnline())
					Bukkit.getPlayer(args[0]).sendMessage(Chat.translate(Lang.Tag + "You have been given the kit &b" + StringUtils.capitalize(kit.name().toLowerCase())));
				sender.sendMessage(Chat.translate(Lang.Tag + "&aYou've given &b" + Bukkit.getOfflinePlayer(args[0]).getName() + " &athe kit &b" + StringUtils.capitalize(kit.name().toLowerCase())));
				User user = main.getUserManager().getUser(Bukkit.getOfflinePlayer(args[0]));
				user.unlockKit(kit);
				return true;
			}
		}
		Player player = (Player) sender;
		if(!main.getMatchManager().matchtype.selectkits || main.getMatchManager().getPlayerKit(player) == Kit.BOSS)
		{
			sender.sendMessage(Lang.Tag + Chat.translate("&cYou can't select a kit in this mode"));
			return true;
		}
		player.openInventory(InventoryBuilder.createKitInventory(player));

		return true;
	}
	
	public Kit getKitByName(String str)
	{
		for(Kit kit : Kit.values())
		{
			if(kit.name().equalsIgnoreCase(str))
				return kit;
		}
		return null;
	}

}
