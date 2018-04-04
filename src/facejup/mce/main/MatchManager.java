package facejup.mce.main;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

import facejup.mce.kits.Kit;
import facejup.mce.storage.EndTimer;
import facejup.mce.storage.StartTimer;

public class MatchManager {

	public final int MIN_PLAYERS = 4;

	private Main main; // Dependency Injection variable.

	public StartTimer startTimer; // The timer variable which decides when a round begins.
	public EndTimer endTimer; // The timer variable which decides when a round begins.

	private HashMap<Player, Kit> kits = new HashMap<>();

	public MatchManager(Main main)
	{
		this.main = main;
		endTimer = new EndTimer(main, this);
		startTimer = new StartTimer(main, this);
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				startTimer.startTimer();
			}
		}, 20L);	
	}

	public void setPlayerKit(Player player, Kit kit)
	{
		this.kits.put(player, kit);
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

	public int getPlayerCount()
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
