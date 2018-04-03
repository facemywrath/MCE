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
		startTimer = new StartTimer(main, this);
		endTimer = new EndTimer(main, this);
		this.main = main;
	}
	
	public int getPlayerCount()
	{
		return kits.keySet().size();
	}

}
