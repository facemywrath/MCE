package facejup.mce.commands;

import facejup.mce.main.Main;

public class CommandManager {
	
	private Main main;
	private CommandArena ca;
	private CommandStats cs;
	private CommandAchievements cach;
	private CommandKits ck;
	private CommandPreprocess cpp;
	private CommandSpectate cspec;
	
	public CommandManager(Main main)
	{
		this.main = main;
		ca = new CommandArena(this);
		cs = new CommandStats(this);
		cach = new CommandAchievements(main);
		ck = new CommandKits(main);
		cspec = new CommandSpectate(this);
		cpp = new CommandPreprocess(main); // Don't need to set an executor for this.
		main.getCommand(ca.name).setExecutor(ca);
		main.getCommand(cs.name).setExecutor(cs);
		main.getCommand(cach.name).setExecutor(cach);
		main.getCommand(ck.name).setExecutor(ck);
		main.getCommand(cspec.name).setExecutor(cspec);
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
