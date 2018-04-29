package facejup.mce.commands;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import facejup.mce.util.Chat;
import facejup.mce.util.FancyTextUtil;
import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;
import io.netty.util.internal.StringUtil;

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
				if((args.length == 1 || !Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) && !args[0].equalsIgnoreCase("info"))
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

		if(args.length == 0)
		{
			if(!main.getMatchManager().matchtype.selectkits || main.getMatchManager().getPlayerKit(player) == Kit.BOSS)
			{
				sender.sendMessage(Lang.Tag + Chat.translate("&cYou can't select a kit in this mode"));
				return true;
			}
			player.openInventory(InventoryBuilder.createKitInventory(player));
			return true;
		}
		if(args[0].equalsIgnoreCase("info"))
		{
			if(args.length == 1)
			{
				player.sendMessage(Chat.translate("&2Specify a kit with &7/kits info (kitname)"));
				
				return true;
			}
			else if(args.length == 2)
			{
				if(Kit.getKitByName(args[1]) != null)
				{
					Kit kit = Kit.getKitByName(args[1]);
					if(kit == Kit.NONE || kit == Kit.RANDOM || kit == Kit.BOSS)
					{
						player.sendMessage(Chat.translate(Lang.Tag + "&cYou can not view those kits"));
					}else{
						int armorpoints = 0;
						armorpoints += ItemCreator.getArmorPoints(kit.helmet);
						armorpoints += ItemCreator.getArmorPoints(kit.chestplate);
						armorpoints += ItemCreator.getArmorPoints(kit.leggings);
						armorpoints += ItemCreator.getArmorPoints(kit.boots);
						player.sendMessage(Chat.translate("&b&lKit " + Chat.formatName(kit.name())));
						for(int i = 0; i < kit.description.size(); i++)
						{
							player.sendMessage(Chat.translate(kit.description.get(i)));
						}
						player.sendMessage(Chat.translate("&2Potion Effect: &a" + (kit.pot!=null?Chat.formatName(kit.pot.getType().getName()) + " " + (kit.pot.getAmplifier() + 1):"None")));
						player.sendMessage(Chat.translate("&2Armor Points: &a" + armorpoints));	
						if(kit.offhand != null)
						FancyTextUtil.sendItemTooltipMessage(player, kit.offhand.getAmount() + "x " + (kit.offhand.getItemMeta().hasDisplayName()?kit.offhand.getItemMeta().getDisplayName():Chat.formatName(kit.offhand.getType().toString())), kit.offhand);
						for(ItemStack item : kit.storage)
						{
							if(item != null)
								FancyTextUtil.sendItemTooltipMessage(player, item.getAmount() + "x " + (item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():Chat.formatName(item.getType().toString())), item);
						}
					}
				}
			}
		}
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
