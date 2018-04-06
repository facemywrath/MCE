package facejup.mce.main;

import org.bukkit.plugin.java.JavaPlugin;

import facejup.mce.commands.CommandManager;
import facejup.mce.listeners.EventManager;
import facejup.mce.players.UserManager;

public class Main extends JavaPlugin {
	
	private MatchManager mm; // The object responsible for dealing with the beginning and ending of matches.
	private UserManager um;
	private EventManager em;
	private CommandManager cm;
	
	public void onEnable()
	{
		mm = new MatchManager(this); // Instantiation. Constructor just starts the countdown.
		um = new UserManager(this);
		em = new EventManager(this);
		cm = new CommandManager(this);
	}

	//Getters
	
	public MatchManager getMatchManager()
	{
		return this.mm;
	}
	
	public CommandManager getCommandManager()
	{
		return this.cm;
	}
	
	public UserManager getUserManager()
	{
		return this.um;
	}
	
}
