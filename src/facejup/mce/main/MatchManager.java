package facejup.mce.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import facejup.mce.enums.Kit;
import facejup.mce.maps.Arena;
import facejup.mce.maps.ArenaManager;
import facejup.mce.timers.EndTimer;
import facejup.mce.timers.StartTimer;
import facejup.mce.util.Chat;

public class MatchManager {

	public final int MIN_PLAYERS = 4;

	private Main main; // Dependency Injection variable.

	public ArenaManager am;
	public StartTimer startTimer; // The timer variable which decides when a round begins.
	public EndTimer endTimer; // The timer variable which decides when a round begins.

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
		int i = 0;
		Arena arena = am.getRandomArena(desiredKits.keySet().size());
		if(arena == null)
		{
			startTimer.linger();
			Chat.bc("&9(&bMCE&9) &cError: No Arena Found.");
			return;
		}
		if(arena.getSpawnPoints().size() >= desiredKits.keySet().size())
		{
			for(Player player : desiredKits.keySet())
			{
				spawnPlayer(player);
			}
		}
		else
		{
			if(am.getMaxSpawnPoints() > desiredKits.keySet().size())
			{
				
			}
		}
	}
	
	public void spawnPlayer(Player player) 
	{
		if (!lives.containsKey(player))
			return;
		if (lives.get(player) == 0) {
			lives.remove(player);
			kits.put(player, Kit.NONE);
		}
		if (desiredKits.containsKey(player) && !(desiredKits.get(player).equals(kits.get(player)))) {
			kits.put(player, desiredKits.get(player));
		} else if (!(kits.containsKey(player)) || kits.get(player) == Kit.NONE)
			return;
		if (am.getArena() == null)
			return;
		final Location loc = am.getArena().getRandomSpawn();
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			
			public void run() {
				player.teleport(loc);
				player.getInventory().clear();
				Kit kit = kits.get(player);
				kit.storage.stream().forEach(item -> player.getInventory().addItem(item));
				player.getInventory().setHelmet(kit.helmet);
				player.getInventory().setChestplate(kit.chestplate);
				player.getInventory().setLeggings(kit.leggings);
				player.getInventory().setBoots(kit.boots);
			}
			
		}, 5L);
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
			if(lives.get(player) > 0)
				players.add(player);
		}
		return players;
	}

	public int getPlayersQueued()
	{
		int i = 0;
		if(kits.isEmpty())
			return 0;
		for(Player player : kits.keySet())
		{
			if(kits.get(player) != Kit.NONE)
				i++;
		}
		return i;
	}

}
