package facejup.mce.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import facejup.mce.enums.Achievement;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import net.md_5.bungee.api.ChatColor;

public class CommandStats implements CommandExecutor {
	
	private CommandManager cm;
	
	public String name = "stats";
	private Main main;
	
	public CommandStats(CommandManager cm)
	{
		this.cm = cm;
		main = cm.getMain();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			// TODO NO CONSOLE USE!
			return true;
		}
		Player player = (Player) sender;
		FileConfiguration config = cm.getMain().getUserManager().getFileControl().getConfig();
		int Coins = 0;
		int Kills = 0;
		int Deaths = 0;
		int Wins = 0;
		int Runnerups = 0;
		int Gmaesplayed = 0;
		int Achievements = 0;
		if (args.length == 0) {
			User p = main.getUserManager().getUser(player);
			p.getKills();
			Coins = p.getCoins();
			Kills = p.getKills();
			Deaths = p.getDeaths();
			Wins = p.getWins();
			Runnerups = p.getRunnerup();
			Gmaesplayed = p.getGamesplayed();
			Achievements = p.getAchievementCount();
			player.sendMessage(ChatColor.AQUA + "----------==={" + ChatColor.GREEN + player.getName() + ChatColor.AQUA + "}===----------");
			player.sendMessage(ChatColor.BLUE + "Coins: " + ChatColor.GRAY + Coins);
			player.sendMessage(ChatColor.BLUE + "Kills: " + ChatColor.GRAY + Kills);
			player.sendMessage(ChatColor.BLUE + "Deaths: " + ChatColor.GRAY + Deaths);
			player.sendMessage(ChatColor.BLUE + "Wins: " + ChatColor.GRAY + Wins);
			player.sendMessage(ChatColor.BLUE + "RunnerUps: " + ChatColor.GRAY + Runnerups);
			player.sendMessage(ChatColor.BLUE + "Games Played: " + ChatColor.GRAY + Gmaesplayed);
			player.sendMessage(ChatColor.BLUE + "Achievements Unlocked: " + ChatColor.GRAY + Achievements + "/" + Achievement.values().length);
		}
		if (args.length == 1) {
			if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
				// TODO Player is null!
				return true;
			}
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			User t = main.getUserManager().getUser(target);
			Coins = t.getCoins();
			Kills = t.getKills();
			Deaths = t.getDeaths();
			Wins = t.getWins();
			Runnerups = t.getRunnerup();
			Gmaesplayed = t.getGamesplayed();
			Achievements = t.getAchievementCount();
			player.sendMessage(ChatColor.AQUA + "----------==={" + ChatColor.GREEN + target.getName() + ChatColor.AQUA + "}===----------");
			player.sendMessage(ChatColor.BLUE + "Coins: " + ChatColor.GRAY + Coins);
			player.sendMessage(ChatColor.BLUE + "Kills: " + ChatColor.GRAY + Kills);
			player.sendMessage(ChatColor.BLUE + "Deaths: " + ChatColor.GRAY + Deaths);
			player.sendMessage(ChatColor.BLUE + "Wins: " + ChatColor.GRAY + Wins);
			player.sendMessage(ChatColor.BLUE + "Runner Ups: " + ChatColor.GRAY + Runnerups);
			player.sendMessage(ChatColor.BLUE + "Games Played: " + ChatColor.GRAY + Gmaesplayed);
			player.sendMessage(ChatColor.BLUE + "Achievements Unlocked: " + ChatColor.GRAY + Achievements + "/" + Achievement.values().length);
		}
		
		return true;
	}

}
