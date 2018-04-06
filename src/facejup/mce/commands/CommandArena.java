package facejup.mce.commands;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import facejup.mce.enums.AddType;
import facejup.mce.maps.ArenaManager;
import facejup.mce.players.User;

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
			//TODO Must be player
			return true;
		}

		Player player = (Player) sender;

		if(!player.isOp())
			return true;
		
		if (args.length == 0) 
		{
			//TODO Invalid Syntax, Return help
			return true;
		}

		ArenaManager am = cm.getMain().getMatchManager().getArenaManager();
		if (args[0].equalsIgnoreCase("create")) 
		{
			if (args.length == 1) 
			{
				//TODO Invalid Syntax, Return help
				return true;
			}
			String name = args[1];
			am.createArena(name);
			adding.put(player, AddType.BOUND1);
			arenaAdd.put(player, name);
			return true;
		}
		if (args[0].equalsIgnoreCase("set")) 
		{
			if (args.length == 1) 
			{
				//TODO Invalid Syntax, Return help
				return true;
			}
			if (args[1].equals("1")) 
			{
				if (args.length == 2)
				{
					//TODO Invalid Syntax, Return help
					return true;
				}
				String ArenaName = args[2];
				if (am.getArenaSection(ArenaName) == null) {
					// TODO Arena does not exist
					return true;
				}
				arenaAdd.put(player, ArenaName);
				adding.put(player, AddType.BOUND1);
				return true;
			}
			if (args[1].equals("2")) 
			{
				if (args.length == 2)
				{
					//TODO Invalid Syntax, Return help
					return true;
				}
				String ArenaName = args[2];
				if (am.getArenaSection(ArenaName) == null) {
					// TODO Arena does not exist
					return true;
				}
				arenaAdd.put(player, ArenaName);
				adding.put(player, AddType.BOUND2);
				return true;
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("spawn")) 
		{
			if (args.length == 1) 
			{
				// TODO Arena does not exist
				return true;
			}

			if (args[1].equalsIgnoreCase("add")) 
			{
				if (args.length == 2) 
				{
					// TODO Arena does not exist
					return true;
				}
				String ArenaName = args[2];
				if (am.getArenaSection(ArenaName) == null) 
				{
					// TODO Arena does not exist
					return true;
				}
				Location loc = player.getLocation();
				ConfigurationSection section = am.getArenaSection(ArenaName);
				int id = am.getSpawnCount(section);
				section.set("SpawnPoints." + id + ".x", loc.getX());
				section.set("SpawnPoints." + id + ".y", loc.getY());
				section.set("SpawnPoints." + id + ".z", loc.getZ());
				am.getFileControl().save();
				return true;
			}
			if (args[1].equalsIgnoreCase("remove")) 
			{
				
				return true;
			}
		}
		if (args[0].equalsIgnoreCase("toggle"))
		{
			if (cm.getMain().getMatchManager().getEndTimer().isRunning()) {
				// TODO Tell player only toggleable between matches!
				return true;
			}
			if (cm.getMain().getMatchManager().getStartTimer().isRunning()) {
				cm.getMain().getMatchManager().getStartTimer().stopTimer();
				sender.sendMessage("test");
				// TODO Tell player it's stopped!
				return true;
			} else {
				cm.getMain().getMatchManager().startTimer.resumeTimer();
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("test"))
		{
			cm.getMain().getMatchManager().spawnPlayer(player);
		}
		return true;
	}


}
