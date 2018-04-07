package facejup.mce.commands;

import facejup.mce.main.Main;

public class CommandManager {
	
	private Main main;
	private CommandArena ca;
	private CommandStats cs;
	private CommandAchievements cach;
	
	public CommandManager(Main main)
	{
		this.main = main;
		ca = new CommandArena(this);
		cs = new CommandStats(this);
		cach = new CommandAchievements();
		main.getCommand(ca.name).setExecutor(ca);
		main.getCommand(cs.name).setExecutor(cs);
		main.getCommand(cach.name).setExecutor(cach);
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
