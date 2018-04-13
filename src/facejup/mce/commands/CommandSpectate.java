package facejup.mce.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import facejup.mce.main.Main;
import facejup.mce.util.Chat;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;

public class CommandSpectate implements CommandExecutor{

	private Main main;
	private CommandManager cm;

	public String name = "spectate";

	public CommandSpectate(CommandManager cm)
	{
		this.main = cm.getMain();
		this.cm = cm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage(Lang.ConsoleUse);
			return true;
		}
		Player player = (Player) sender;
		if(!main.getMatchManager().isMatchRunning())
		{
			player.sendMessage(Lang.MatchNotRunning);
			return true;
		}
		if(main.getMatchManager().getLives(player) > 0)
		{
			player.sendMessage(Lang.CurrentlyPlaying);
			return true;
		}
		if(player.getGameMode() != GameMode.SPECTATOR)
		{
			player.teleport(main.getMatchManager().getPlayersAlive().get(0));
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					player.setGameMode(GameMode.SPECTATOR);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.Tag + "&aYou are now spectating. Re-type this command to return to lobby."));
					player.getInventory().clear();
				}
			}, 20L);
		}
		else
		{
			main.getMatchManager().spawnPlayer(player);
		}


		return true;
	}

}
