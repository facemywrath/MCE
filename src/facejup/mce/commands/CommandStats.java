package facejup.mce.commands;

import org.apache.commons.lang3.tuple.Pair;
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
import facejup.mce.players.UserManager;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;
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
			sender.sendMessage(Lang.ConsoleUse);
			return true;
		}
		Player player = (Player) sender;
		FileConfiguration config = cm.getMain().getUserManager().getFileControl().getConfig();
		if(player.isOp())
		{
			if(args.length > 1 && args[0].equalsIgnoreCase("reset") && Bukkit.getOfflinePlayer(args[1]).hasPlayedBefore())
			{
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
				if(main.getUserManager().getFileControl().getConfig().isConfigurationSection("Users." + target.getUniqueId().toString()))
				{
					main.getUserManager().getFileControl().getConfig().set("Users." + target.getUniqueId().toString(),null);
					main.getUserManager().getFileControl().save();
					main.getUserManager().reloadUser(target);
					sender.sendMessage(Chat.translate(Lang.Tag + "Player &b" + target.getName() + "&a's file reset."));
				}
			}
		}
		if (args.length == 0) {
			User p = main.getUserManager().getUser(player);
			int Coins = p.getCoins();
			double KDR = p.getKDR();
			int Kills = p.getKills();
			int Deaths = p.getDeaths();
			int Wins = p.getWins();
			int Runnerups = p.getRunnerup();
			int Gamesplayed = p.getGamesplayed();
			int Achievements = p.getAchievementCount();
			player.sendMessage(ChatColor.AQUA + "----------==={" + ChatColor.GREEN + player.getName() + ChatColor.AQUA + "}===----------");
			player.sendMessage(ChatColor.BLUE + "Coins: " + ChatColor.GRAY + Coins);
			player.sendMessage(ChatColor.BLUE + "KDR: " + ChatColor.GRAY + KDR);
			player.sendMessage(ChatColor.BLUE + "Kills: " + ChatColor.GRAY + Kills);
			player.sendMessage(ChatColor.BLUE + "Deaths: " + ChatColor.GRAY + Deaths);
			player.sendMessage(ChatColor.BLUE + "Wins: " + ChatColor.GRAY + Wins);
			player.sendMessage(ChatColor.BLUE + "RunnerUps: " + ChatColor.GRAY + Runnerups);
			player.sendMessage(ChatColor.BLUE + "Games Played: " + ChatColor.GRAY + Gamesplayed);
			player.sendMessage(ChatColor.BLUE + "Achievements Unlocked: " + ChatColor.GRAY + Achievements + "/" + Achievement.getMaxAchievementCount());
		}
		if (args.length == 1) {
			if(args[0].equalsIgnoreCase("top"))
			{
				UserManager um = main.getUserManager();
				Pair<String, Double> Coins = um.getHighestStat("Coins");
				Pair<String, Double> Kills = um.getHighestStat("Kills");
				Pair<String, Double> KDR = um.getHighestStat("KDR");
				Pair<String, Double> Deaths = um.getHighestStat("Deaths");
				Pair<String, Double> Wins = um.getHighestStat("Wins");
				Pair<String, Double> Runnerups = um.getHighestStat("Runnerup");
				Pair<String, Double> Gamesplayed = um.getHighestStat("Gamesplayed");
				Pair<String, Double> Achievements = um.getHighestStat("Achievements");
				player.sendMessage(ChatColor.AQUA + "----------==={" + ChatColor.GREEN + "Top Stats" + ChatColor.AQUA + "}===----------");
				player.sendMessage(ChatColor.BLUE + "Coins: " + ChatColor.GRAY + Coins.getLeft() + " - " + Coins.getRight().intValue());
				player.sendMessage(ChatColor.BLUE + "KDR: " + ChatColor.GRAY + KDR.getLeft() + " - " + KDR.getRight());
				player.sendMessage(ChatColor.BLUE + "Kills: " + ChatColor.GRAY + Kills.getLeft() + " - " + Kills.getRight().intValue());
				player.sendMessage(ChatColor.BLUE + "Deaths: " + ChatColor.GRAY + Deaths.getLeft() + " - " + Deaths.getRight().intValue());
				player.sendMessage(ChatColor.BLUE + "Wins: " + ChatColor.GRAY + Wins.getLeft() + " - " + Wins.getRight().intValue());
				player.sendMessage(ChatColor.BLUE + "Runner Ups: " + ChatColor.GRAY + Runnerups.getLeft() + " - " + Runnerups.getRight().intValue());
				player.sendMessage(ChatColor.BLUE + "Games Played: " + ChatColor.GRAY + Gamesplayed.getLeft() + " - " + Gamesplayed.getRight().intValue());
				player.sendMessage(ChatColor.BLUE + "Achievements Unlocked: " + ChatColor.GRAY + Achievements.getLeft() + " - " + Achievements.getRight().intValue());
			
			}
			else
			{
				if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
					player.sendMessage(Lang.NullPlayer);
					return true;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				User t = main.getUserManager().getUser(target);
				int Coins = t.getCoins();
				double KDR = t.getKDR();
				int Kills = t.getKills();
				int Deaths = t.getDeaths();
				int Wins = t.getWins();
				int Runnerups = t.getRunnerup();
				int Gamesplayed = t.getGamesplayed();
				int Achievements = t.getAchievementCount();
				player.sendMessage(ChatColor.AQUA + "----------==={" + ChatColor.GREEN + target.getName() + ChatColor.AQUA + "}===----------");
				player.sendMessage(ChatColor.BLUE + "Coins: " + ChatColor.GRAY + Coins);
				player.sendMessage(ChatColor.BLUE + "KDR: " + ChatColor.GRAY + KDR);
				player.sendMessage(ChatColor.BLUE + "Kills: " + ChatColor.GRAY + Kills);
				player.sendMessage(ChatColor.BLUE + "Deaths: " + ChatColor.GRAY + Deaths);
				player.sendMessage(ChatColor.BLUE + "Wins: " + ChatColor.GRAY + Wins);
				player.sendMessage(ChatColor.BLUE + "Runner Ups: " + ChatColor.GRAY + Runnerups);
				player.sendMessage(ChatColor.BLUE + "Games Played: " + ChatColor.GRAY + Gamesplayed);
				player.sendMessage(ChatColor.BLUE + "Achievements Unlocked: " + ChatColor.GRAY + Achievements + "/" + Achievement.getMaxAchievementCount());
			}
		}

		return true;
	}

}
