package facejup.mce.main;

import org.bukkit.plugin.java.JavaPlugin;

import facejup.mce.storage.StartTimer;

public class Main extends JavaPlugin {
	
	public StartTimer startTimer; // The timer variable which decides when a round begins.
	
	public void onEnable()
	{
		startTimer = new StartTimer(this); // Instantiate the timer.
	}

}
