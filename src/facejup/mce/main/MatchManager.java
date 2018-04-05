package facejup.mce.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import facejup.mce.kits.Kit;
import facejup.mce.maps.Arena;
import facejup.mce.maps.ArenaManager;
import facejup.mce.timers.EndTimer;
import facejup.mce.timers.StartTimer;

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
		Arena arena = am.getRandomArena();
		if(arena.getSpawnPoints().size() >= desiredKits.keySet().size())
		{
			for(Player player : desiredKits.keySet())
			{
				
			}
		}
	}

	public void setPlayerKit(Player player, Kit kit)
	{
		this.kits.put(player, kit);
	}

	public void setPlayerDesiredKit(Player player, Kit kit)
	{
		this.desiredKits.put(player, kit);
	}

	public Kit getPlayerKit(Player player)
	{
		if(kits.containsKey(player))
			return kits.get(player);
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
