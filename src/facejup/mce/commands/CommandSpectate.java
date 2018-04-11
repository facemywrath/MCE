package facejup.mce.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import facejup.mce.main.Main;

public class CommandSpectate implements CommandExecutor{

	private Main main;
	private CommandManager cm;

	public CommandSpectate(CommandManager cm)
	{
		this.main = cm.getMain();
		this.cm = cm;
	}

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
