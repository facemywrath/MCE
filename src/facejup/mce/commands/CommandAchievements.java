package facejup.mce.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import facejup.mce.enums.Achievement;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import facejup.mce.util.Chat;
import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.Lang;

public class CommandAchievements implements CommandExecutor{

	public String name = "achievements";
	public Main main;
	
	public CommandAchievements(Main main)
	{
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			if(args.length < 2 || getAchievementByName(args[1]) == null)
			{
				if(args.length == 1 && !Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore())
				{
					sender.sendMessage(Lang.NullPlayer);
					return true;
				}
				sender.sendMessage(Lang.ConsoleUse);
				return true;
			}
			if(args.length > 1 && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && getAchievementByName(args[1]) != null)
			{
				Achievement ach = getAchievementByName(args[1]);
				sender.sendMessage(Chat.translate(Lang.Tag + "You've given &b" + Bukkit.getOfflinePlayer(args[0]).getName() + "&a the achievement &b" + StringUtils.capitaliseAllWords(ach.name().toLowerCase().replaceAll("_", " "))));
				User user = main.getUserManager().getUser(Bukkit.getOfflinePlayer(args[0]));
				user.setScore(ach, ach.score);
			}
		}
		else if(((Player) sender).isOp())
		{
			if(args.length < 2 || getAchievementByName(args[1]) == null)
			{
				if(args.length == 1 && !Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore())
				{
					sender.sendMessage(Lang.NullPlayer);
					return true;
				}
				Player player = (Player) sender;
				player.openInventory(InventoryBuilder.createAchievementInventory(player));
				return true;
			}
			if(args.length > 1 && Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore() && getAchievementByName(args[1]) != null)
			{
				Achievement ach = getAchievementByName(args[1]);
				sender.sendMessage(Chat.translate(Lang.Tag + "You've given &b" + Bukkit.getOfflinePlayer(args[0]).getName() + "&a the achievement &b" + StringUtils.capitaliseAllWords(ach.name().toLowerCase().replaceAll("_", " "))));
				User user = main.getUserManager().getUser(Bukkit.getOfflinePlayer(args[0]));
				user.setScore(ach, ach.score);
				return true;
			}
		}

		Player player = (Player) sender;
		player.openInventory(InventoryBuilder.createAchievementInventory(player));


		return true;
	}
	
	public Achievement getAchievementByName(String str)
	{
		for(Achievement ach : Achievement.values())
		{
			if(ach.name().equalsIgnoreCase(str))
				return ach;
		}
		return null;
	}



}
