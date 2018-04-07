package facejup.mce.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStats implements CommandExecutor {
	
	private CommandManager cm;
	
	public String name = "stats";
	
	public CommandStats(CommandManager cm)
	{
		this.cm = cm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		return true;
	}

}
