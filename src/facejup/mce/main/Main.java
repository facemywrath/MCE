package facejup.mce.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private MatchManager mm; // The object responsible for dealing with the beginning and ending of matches.
	
	public void onEnable()
	{
		mm = new MatchManager(this); // Instantiation. Constructor just starts the countdown.
	}

}
