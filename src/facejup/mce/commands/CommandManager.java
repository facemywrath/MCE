package facejup.mce.commands;

import facejup.mce.main.Main;

public class CommandManager {
	
	private Main main;
	private CommandArena ca;
	
	public CommandManager(Main main)
	{
		this.main = main;
		ca = new CommandArena(this);
		main.getCommand(ca.name).setExecutor(ca);
	}
	
	public CommandArena getCommandArena() 
	{
		return this.ca;
	}
	
	public Main getMain() 
	{
		return this.main;
	}
	
}
