package facejup.mce.commands;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import facejup.mce.arenas.ArenaManager;
import facejup.mce.enums.AddType;
import facejup.mce.enums.Kit;
import facejup.mce.enums.MatchType;
import facejup.mce.players.User;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;
import facejup.mce.util.Numbers;
import net.md_5.bungee.api.ChatColor;

public class CommandArena implements CommandExecutor{

	private CommandManager cm;
	public HashMap<Player, AddType> adding = new HashMap<>();
	public HashMap<Player, String> arenaAdd = new HashMap<>();
	public String name = "arena";

	public CommandArena(CommandManager cm) {
		this.cm = cm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{

		if (!(sender instanceof Player)) 
		{
			if(args.length == 0)
			{
				sender.sendMessage(Lang.ConsoleUse);
				return true;
			}
			if(args[0].equalsIgnoreCase("kick"))
			{
				if(args.length == 1)
				{
					sender.sendMessage(Lang.InvalidSyn);
					return true;
				}
				if(!Bukkit.getOfflinePlayer(args[1]).isOnline())
				{
					sender.sendMessage(Lang.NullPlayer);
					return true;
				}
				if(cm.getMain().getMatchManager().getPlayersAlive().contains(sender))
				{
					cm.getMain().getMatchManager().kickPlayer(Bukkit.getPlayer(args[1]));
				}
			}
			else
			sender.sendMessage(Lang.ConsoleUse);
			return true;
		}

		Player player = (Player) sender;

		if(!player.isOp())
			return true;

		if (args.length == 0) 
		{
			sender.sendMessage(Lang.InvalidSyn);
			return true;
		}
		// Anything
		ArenaManager am = cm.getMain().getMatchManager().getArenaManager();
		if (args[0].equalsIgnoreCase("create")) 
		{
			if (args.length == 1) 
			{
				sender.sendMessage(Lang.InvalidSyn);
				return true;
			}
			String name = args[1];
			am.createArena(name, player.getWorld());
			adding.put(player, AddType.BOUND1);
			arenaAdd.put(player, name);
			player.sendMessage(Chat.translate("&9(&bMCE&9) &aArena " + name + " created. Now you must select the first bounding coordinate."));
			return true;
		}
		if (args[0].equalsIgnoreCase("set")) 
		{
			if (args.length == 1) 
			{
				sender.sendMessage(Lang.InvalidSyn);
				return true;
			}
			if (args[1].equals("1")) 
			{
				if (args.length == 2)
				{
					sender.sendMessage(Lang.InvalidSyn);
					return true;
				}
				String ArenaName = args[2];
				if (am.getArenaSection(ArenaName) == null) {
					sender.sendMessage(Lang.Tag + Chat.translate("&cAn arena by the name of: &6" + ArenaName + " &cdoes not exist!"));
					return true;
				}
				sender.sendMessage(Lang.Tag + Chat.translate("&aYou are now editing bound 1 for arena: &b" + ArenaName));
				arenaAdd.put(player, ArenaName);
				adding.put(player, AddType.BOUND1);
				return true;
			}
			if (args[1].equals("2")) 
			{
				if (args.length == 2)
				{
					sender.sendMessage(Lang.InvalidSyn);
					return true;
				}
				String ArenaName = args[2];
				if (am.getArenaSection(ArenaName) == null) {
					sender.sendMessage(Lang.Tag + Chat.translate("&cAn arena by the name of: &6" + ArenaName + " &cdoes not exist!"));
					return true;
				}
				sender.sendMessage(Lang.Tag + Chat.translate("&aYou are now editing bound 2 for arena: &b" + ArenaName));
				arenaAdd.put(player, ArenaName);
				adding.put(player, AddType.BOUND2);
				return true;
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("list")) 
		{
			sender.sendMessage(ChatColor.AQUA + "----------==={" + ChatColor.GREEN + "Arenas" + ChatColor.AQUA + "}===----------");
			sender.sendMessage(cm.getMain().getMatchManager().getArenaManager().getArenaList().stream().sorted().collect(Collectors.joining(", ")));
		}
		if(args[0].equalsIgnoreCase("setsign"))
		{
			if(args.length == 1)
			{
				sender.sendMessage(Lang.InvalidSyn);
				return true;
			}
			String ArenaName = args[1];
			if(am.getArenaSection(args[1]) == null)
			{
				sender.sendMessage(Lang.Tag + Chat.translate("&cAn arena by the name of &6" + ArenaName + " &cdoes not exist!"));
				return true;
			}
			sender.sendMessage(Lang.Tag + Chat.translate("Rightclick a sign for &6Arena " + ArenaName));
			arenaAdd.put(player, ArenaName);
			adding.put(player, AddType.SIGN);
		}
		if(args[0].equalsIgnoreCase("setcreator"))
		{
			if(args.length == 1)
			{
				sender.sendMessage(Lang.InvalidSyn);
				return true;
			}
			String ArenaName = args[1];
			if(am.getArenaSection(args[1]) == null)
			{
				sender.sendMessage(Lang.Tag + Chat.translate("&cAn arena by the name of &6" + ArenaName + " &cdoes not exist!"));
				return true;
			}
			if(args.length == 2)
			{
				sender.sendMessage(Lang.InvalidSyn);
				return true;
			}
			ConfigurationSection section = am.getArenaSection(ArenaName);
			section.set("Creator", args[2]);
			am.getFileControl().save();
			sender.sendMessage(Lang.Tag + Chat.translate("You set the creator of &b" + ArenaName + " &ato be &b" + args[2]));
		}
		if (args[0].equalsIgnoreCase("spawn")) 
		{
			if (args.length == 1) 
			{
				sender.sendMessage(Lang.InvalidSyn);
				return true;
			}

			if (args[1].equalsIgnoreCase("add")) 
			{
				if (args.length == 2) 
				{
					sender.sendMessage(Lang.InvalidSyn);
					return true;
				}
				String ArenaName = args[2];
				if (am.getArenaSection(ArenaName) == null) 
				{
					sender.sendMessage(Lang.Tag + Chat.translate("&cAn arena by the name of &6" + ArenaName + " &cdoes not exist!"));
					return true;
				}
				Location loc = player.getLocation();
				ConfigurationSection section = am.getArenaSection(ArenaName);
				int id = am.getSpawnCount(section);
				section.set("SpawnPoints." + id + ".x", loc.getX());
				section.set("SpawnPoints." + id + ".y", loc.getY());
				section.set("SpawnPoints." + id + ".z", loc.getZ());
				am.getFileControl().save();
				sender.sendMessage(Lang.Tag + Chat.translate("&aSpawn successfuly added for arena: &b" + ArenaName + " &aAt location: &b" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ()));
				return true;
			}
			if (args[1].equalsIgnoreCase("remove")) 
			{

				return true;
			}
		}
		if (args[0].equalsIgnoreCase("togglestart"))
		{
			if (cm.getMain().getMatchManager().getEndTimer().isRunning()) {
				sender.sendMessage(Lang.Tag + Chat.translate("&cYou may only togglestart inbetween matches!"));
				return true;
			}
			if (cm.getMain().getMatchManager().getStartTimer().isRunning()) {
				cm.getMain().getMatchManager().getStartTimer().stopTimer();
				sender.sendMessage(Lang.Tag + Chat.translate("&aStart time successfuly stopped!"));
				return true;
			} else {
				cm.getMain().getMatchManager().startTimer.resumeTimer();
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("toggleend"))
		{
			if (!cm.getMain().getMatchManager().getStartTimer().isRunning()) {
				sender.sendMessage(Lang.Tag + Chat.translate("&cYou may only togglestart inbetween matches!"));
				return true;
			}
			if (cm.getMain().getMatchManager().getStartTimer().isRunning()) {
				cm.getMain().getMatchManager().getStartTimer().stopTimer();
				cm.getMain().getMatchManager().getEndTimer().startTimer();
				sender.sendMessage("test");
				sender.sendMessage(Lang.Tag + Chat.translate("&aEnd time successfuly stopped!"));
				return true;
			} else {
				cm.getMain().getMatchManager().startTimer.resumeTimer();
				cm.getMain().getMatchManager().getEndTimer().stopTimer();
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("setcoins") && args.length == 3)
		{
			if(!Bukkit.getOfflinePlayer(args[1]).isOnline())
				return true;
			if(!Numbers.isInt(args[2]))
				return true;
			Player target = Bukkit.getPlayer(args[1]);
			int amount = Integer.parseInt(args[2]);
			User user = cm.getMain().getUserManager().getUser(target);
			user.setCoins(amount);
			return true;
		}
		if(args[0].equalsIgnoreCase("start"))
		{
			if(cm.getMain().getMatchManager().isMatchRunning())
			{
				sender.sendMessage(Chat.translate(Lang.Tag + "&cMatch currently in progress."));
				return true;
			}
			if(args.length == 1)
			{
				if(cm.getMain().getMatchManager().getPlayersQueued() >= cm.getMain().getMatchManager().MIN_PLAYERS)
					cm.getMain().getMatchManager().startMatch();
				else
					sender.sendMessage(Chat.translate(Lang.Tag + "&cNot enough players queued up."));
				return true;
			}
			else if(args.length == 2 && cm.getMain().getMatchManager().getArenaManager().getArenaSection(args[1]) != null)
			{
				if(cm.getMain().getMatchManager().getPlayersQueued() >= cm.getMain().getMatchManager().MIN_PLAYERS)
					cm.getMain().getMatchManager().startMatch(args[1]);
				else
					sender.sendMessage(Chat.translate(Lang.Tag + "&cNot enough players queued up."));
				return true;
			}
			else if(args.length == 2 && MatchType.getTypeByName(args[1]) != null)
			{
				if(cm.getMain().getMatchManager().getPlayersQueued() >= cm.getMain().getMatchManager().MIN_PLAYERS)
					cm.getMain().getMatchManager().startMatch(MatchType.getTypeByName(args[1]));
				else
					sender.sendMessage(Chat.translate(Lang.Tag + "&cNot enough players queued up."));
				return true;
			}
			else if(args.length == 3 && MatchType.getTypeByName(args[1]) != null && Kit.getKitByName(args[2]) != null)
			{
				if(cm.getMain().getMatchManager().getPlayersQueued() >= cm.getMain().getMatchManager().MIN_PLAYERS)
					cm.getMain().getMatchManager().startMatch(MatchType.getTypeByName(args[1]), Kit.getKitByName(args[2]));
				else
					sender.sendMessage(Chat.translate(Lang.Tag + "&cNot enough players queued up."));
				return true;
			}
			else if(cm.getMain().getMatchManager().getArenaManager().getArenaSection(args[1]) == null)
			{
				sender.sendMessage(Chat.translate(Lang.Tag + "&cArena not found."));
				return true;
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("interrupt"))
		{
			if(!cm.getMain().getMatchManager().isMatchRunning())
			{
				sender.sendMessage(Chat.translate(Lang.Tag + "&cNo match is running"));
				return true;
			}
			Chat.bc(Lang.Tag + Chat.translate("&c&lMatch interrupted by &b" + sender.getName()));
			for(Player p : Bukkit.getOnlinePlayers())
				if(p.getGameMode() != GameMode.CREATIVE)
					cm.getMain().getMatchManager().spawnPlayer(p);
			cm.getMain().getMatchManager().startTimer.startTimer();
			return true;
		}
		if(args[0].equalsIgnoreCase("kick"))
		{
			if(args.length == 1)
			{
				sender.sendMessage(Lang.InvalidSyn);
				return true;
			}
			if(!Bukkit.getOfflinePlayer(args[1]).isOnline())
			{
				sender.sendMessage(Lang.NullPlayer);
				return true;
			}
			if(cm.getMain().getMatchManager().getPlayersAlive().contains(sender))
			{
				cm.getMain().getMatchManager().kickPlayer(Bukkit.getPlayer(args[1]));
			}
		}
		return true;
	}


}
