package facejup.mce.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import facejup.mce.arenas.Arena;
import facejup.mce.arenas.ArenaManager;
import facejup.mce.enums.Kit;
import facejup.mce.players.User;
import facejup.mce.timers.EndTimer;
import facejup.mce.timers.StartTimer;
import facejup.mce.util.Chat;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;
import net.md_5.bungee.api.ChatColor;

public class MatchManager {

	public final int MIN_PLAYERS = 2;

	private Main main; // Dependency Injection variable.

	public ArenaManager am;
	public StartTimer startTimer; // The timer variable which decides when a round begins.
	public EndTimer endTimer; // The timer variable which decides when a round begins.
	public String Tag = Lang.Tag;

	private HashMap<Player, Integer> lives = new HashMap<>();
	private HashMap<Player, Kit> kits = new HashMap<>();
	private HashMap<Player, Kit> desiredKits = new HashMap<>();

	public MatchManager(Main main)
	{
		this.main = main;
		endTimer = new EndTimer(main, this);
		startTimer = new StartTimer(main, this);
		this.am = new ArenaManager(this);
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				startTimer.startTimer();
			}
		}, 20L);	
	}

	public boolean isMatchRunning()
	{
		return this.endTimer.isRunning();
	}

	public void startMatch()
	{
		Arena arena = am.getRandomArena(desiredKits.keySet().size());
		if(arena == null)
		{
			startTimer.linger();
			Chat.bc(Tag + "&cError: No Arena Found.");
			return;
		}
		if(arena.getSpawnPoints().size() >= desiredKits.keySet().size())
		{
			Chat.bc(Tag + "&b&l Arena selected: " + arena.getName().replaceAll("_"," "));
			for(Player player : desiredKits.keySet())
			{
				main.getUserManager().getUser(player).incGamesplayed(1);
				lives.put(player, 5);
				spawnPlayer(player);
			}
			endTimer.startTimer();
		}
		else
		{
			startTimer.linger();
			Chat.bc(Tag + "&cError: No Arena Found.");
			return;
		}
	}

	public void spawnPlayer(Player player) 
	{
		if(!this.isMatchRunning())
		{
			if(player.isOp())
			{
				main.getServer().dispatchCommand(player, "spawn");
			}
			else
			{
				player.setOp(true);
				main.getServer().dispatchCommand(player, "spawn");
				player.setOp(false);
			}
			player.getInventory().clear();
			player.setHealth(player.getMaxHealth());
			player.setFoodLevel(20);
			player.getInventory().setItem(8, ItemCreator.getKitSelector());
			return;
		}
		if (!lives.containsKey(player))
		{
			if(player.isOp())
			{
				main.getServer().dispatchCommand(player, "spawn");
			}
			else
			{
				player.setOp(true);
				main.getServer().dispatchCommand(player, "spawn");
				player.setOp(false);
			}
			return;
		}
		if (lives.get(player) == 0) {
			lives.remove(player);
			kits.put(player, Kit.NONE);
			desiredKits.put(player, Kit.NONE);
			if(player.isOp())
			{
				main.getServer().dispatchCommand(player, "spawn");
			}
			else
			{
				player.setOp(true);
				main.getServer().dispatchCommand(player, "spawn");
				player.setOp(false);
			}
		}
		if (desiredKits.containsKey(player) && !(desiredKits.get(player).equals(kits.get(player)))) {
			kits.put(player, desiredKits.get(player));
		} else if (!(kits.containsKey(player)) || kits.get(player) == Kit.NONE)
			return;
		if (am.getArena() == null)
			return;
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

			@SuppressWarnings("deprecation")
			public void run() {
				player.getInventory().clear();
				player.setHealth(player.getMaxHealth());
				player.setFoodLevel(20);
				player.getInventory().setItem(8, ItemCreator.getKitSelector());
				Kit kit = kits.get(player);
				kit.storage.stream().filter(item -> item != null).forEach(item -> player.getInventory().addItem(item));
				player.getInventory().setHelmet(kit.helmet);
				player.getInventory().setChestplate(kit.chestplate);
				player.getInventory().setLeggings(kit.leggings);
				player.getInventory().setBoots(kit.boots);
				Location loc = am.getArena().getRandomSpawn();
				player.teleport(loc);
			}

		}, 5L);
	}

	public void endMatchByTime()
	{
		if(lives.keySet().size() >= 2)
		{
			Optional<Integer> i = lives.keySet().stream().map(player -> lives.get(player)).max((i1,i2) -> Integer.compare(i1, i2));

			int j = (int) lives.keySet().stream().filter(player -> lives.get(player) == i.get()).count();
			if(j > 1)
			{
				List<Player> players = lives.keySet().stream().filter(player -> lives.get(player) == i.get()).collect(Collectors.toList());
				for(Player player : players)
				{
					User user = main.getUserManager().getUser(player);
					user.incRunnerup(1);
					if(player.isOnline())
						main.getMatchManager().spawnPlayer(player);
				}
				String msg = Tag + "&6The match has ended in a tie between: " + players.stream().map(Player::getName).collect(Collectors.joining(", "));
				Chat.bc(msg);
			}
			else
			{
				Player winner = lives.keySet().stream().filter(player -> lives.get(player) == i.get()).collect(Collectors.toList()).get(0);
				Optional<Integer> k = lives.keySet().stream().filter(player -> lives.get(player) != i.get()).map(player -> lives.get(player)).max((i1, i2) -> Integer.compare(i1, i2));

				int l = (int) lives.keySet().stream().filter(player -> lives.get(player) == k.get()).count();
				if(l > 1)
				{
					List<Player> players = lives.keySet().stream().filter(player -> lives.get(player) == k.get()).collect(Collectors.toList());
					for(Player player : players)
					{
						if(player.isOnline())
							main.getMatchManager().spawnPlayer(player);
						User user = main.getUserManager().getUser(player);
						user.incRunnerup(1);
					}
					String msg = Tag + "&6The match has ended with " + winner.getName() + " winning, and a runnerup tie between: " + players.stream().map(Player::getName).collect(Collectors.joining(", "));
					Chat.bc(msg);	
				}
				else
				{
					Player runnerup = lives.keySet().stream().filter(player -> lives.get(player) == k.get()).collect(Collectors.toList()).get(0);
					String msg = Tag + "&6The match has ended with " + winner.getName() + " winning, and a runnerup of " + runnerup.getName();
					Chat.bc(msg);
					if(winner.isOnline())
						main.getMatchManager().spawnPlayer(winner);
					if(runnerup.isOnline())
						main.getMatchManager().spawnPlayer(runnerup);
					main.getUserManager().getUser(winner).incWin(1);
					main.getUserManager().getUser(runnerup).incRunnerup(1);
				}
			}
		}
		this.startTimer.startTimer();
	}

	public void setPlayerKit(Player player, Kit kit)
	{
		this.kits.put(player, kit);
	}

	public void setPlayerDesiredKit(Player player, Kit kit)
	{
		this.desiredKits.put(player, kit);
	}

	public ArenaManager getArenaManager() {
		return this.am;
	}

	public void setLives(Player player, int i)
	{
		lives.put(player, i);
	}

	public int getLives(Player player)
	{
		if(lives.containsKey(player))
			return lives.get(player);
		return 0;
	}

	public void incLives(Player player)
	{
		if(lives.containsKey(player))
			if(lives.get(player) > 0)
			{
				lives.put(player, lives.get(player)+1);
				player.sendMessage(Lang.Tag + ChatColor.GOLD + " You gained an extra life!");
			}
	}

	public void decLives(Player player) {
		if(lives.containsKey(player))
			if((lives.get(player) - 1) >= 0)
				lives.put(player, (lives.get(player) - 1));
	}

	public Kit getPlayerKit(Player player)
	{
		if(kits.containsKey(player))
			return kits.get(player);
		return Kit.NONE;
	}

	public Kit getPlayerDesiredKit(Player player)
	{
		if(desiredKits.containsKey(player))
			return desiredKits.get(player);
		return Kit.NONE;
	}

	public StartTimer getStartTimer()
	{
		return this.startTimer;
	}

	public EndTimer getEndTimer()
	{
		return this.endTimer;
	}

	public Main getMain()
	{
		return this.main;
	}

	public List<Player> getPlayersAlive()
	{
		List<Player> players = new ArrayList<>();
		for(Player player : lives.keySet())
		{
			if(lives.get(player) > 0 && kits.get(player) != Kit.NONE)
				players.add(player);
		}
		return players;
	}

	public int getPlayersQueued()
	{
		int i = 0;
		if(desiredKits.isEmpty())
			return 0;
		for(Player player : desiredKits.keySet())
		{
			if(desiredKits.get(player) != Kit.NONE)
				i++;
		}
		return i;
	}

	public Player getPlayerClosestTo(Player target)
	{
		Double d = Double.MAX_VALUE;
		Player ret = null;
		for(Player player : getPlayersAlive())
		{
			if(player.equals(target))
				continue;
			if(player.getWorld().equals(target.getWorld()))
			{
				if(player.getLocation().distance(target.getLocation()) < d)
				{
					d = player.getLocation().distance(target.getLocation());
					ret = player;
				}
			}
		}
		return ret;

	}

	public void kill(Player player) {
		this.lives.remove(player);

	}

}
