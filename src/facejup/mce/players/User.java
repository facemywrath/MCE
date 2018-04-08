package facejup.mce.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;

public class User {

	private UserManager um; // Dependency Injection Variable.

	private ConfigurationSection section; // Section storing the players information.
	@SuppressWarnings("unused")
	private OfflinePlayer player; // The player.

	public User(UserManager um, OfflinePlayer player)
	{
		//Constructor which loads and saves the information from the file.
		this.player = player;
		this.um = um;
		if(um.getFileControl() != null && um.getFileControl().getFile().exists()) // Checks to make sure the file exists.
		{
			FileConfiguration config = um.getFileControl().getConfig(); // Get the Users.yml config
			if(config.contains("Users." + player.getUniqueId())) // Checks if the player is in the config.
			{
				// Store their section if they are.
				this.section = config.getConfigurationSection("Users." + player.getUniqueId());
				for (Achievement ach : Achievement.values()) {
					if (!config.contains("Users." + player.getUniqueId() + ".Achievements." + ach.toString()))
						config.set("Users." + player.getUniqueId() + ".Achievements." + ach.toString() + ".Score", 0);
				}
				um.getFileControl().save();
			}
			else
			{
				// Otherwise, set their default information and save. Then store their section.
				config.set("Users." + player.getUniqueId() + ".Name", player.getName());
				config.set("Users." + player.getUniqueId() + ".Coins", 0);
				config.set("Users." + player.getUniqueId() + ".Kills", 0);
				config.set("Users." + player.getUniqueId() + ".Deaths", 0);
				config.set("Users." + player.getUniqueId() + ".Wins", 0);
				config.set("Users." + player.getUniqueId() + ".Runnerup", 0);
				config.set("Users." + player.getUniqueId() + ".Gamesplayed", 0);
				config.set("Users." + player.getUniqueId() + ".Kits", Arrays.asList("NONE", "ARCHER", "WARRIOR", "GUARD"));
				for (Achievement ach : Achievement.values()) {
					config.set("Users." + player.getUniqueId() + ".Achievements." + ach.toString() + ".Score", 0);
				}
				um.getFileControl().save(config);
				this.section = config.getConfigurationSection("Users." + player.getUniqueId());
			}
		}
	}

	public boolean hasKit(Kit kit)
	{
		if(getKits().contains(kit))
			return true;
		return false;
	}

	public boolean hasAchievement(Achievement ach) {
		if (getScore(ach) >= ach.score)
			return true;
		return false;
	}

	public void purchaseKit(Kit kit)
	{
		int coins = getCoins();
		if(coins < kit.coincost)
			return;
		setCoins(coins - kit.coincost);
		unlockKit(kit);
	}

	public void unlockKit(Kit kit)
	{
		//Unlocks a kit for a player for future use.
		if(section.contains("Kits"))
		{
			if(section.getStringList("Kits").contains(kit.toString()))
				return;
			List<String> kits = section.getStringList("Kits");
			kits.add(kit.toString());
			section.set("Kits", kits);
		}
		else
		{
			section.set("Kits", Arrays.asList("NONE", "ARCHER", "WARRIOR", "GUARD"));
		}
		um.getFileControl().save();
	}

	public void setCoins(int i)
	{
		section.set("Coins", i);
		um.getFileControl().save();
	}

	public void incWin(int i) {
		if(section.contains("Wins"))
			section.set("Wins", section.getInt("Wins") + i);
		else
			section.set("Wins", i);
		incCoins(25);
		this.um.getFileControl().save();
	}

	public void incRunnerup(int i) {
		if(section.contains("Runnerup"))
			section.set("Runnerup", section.getInt("Runnerup") + i);
		else
			section.set("Runnerup", i);
		incCoins(10);
		this.um.getFileControl().save();
	}

	public void incGamesplayed(int i) {
		if(section.contains("Gamesplayed"))
			section.set("Gamesplayed", section.getInt("Gamesplayed") + i);
		else
			section.set("Gamesplayed", i);
		this.um.getFileControl().save();
	}

	public void incScore(Achievement ach) {
		if (section.contains("Achievements." + ach.toString() + ".Score")) {
			int score = section.getInt("Achievements." + ach.toString() + ".Score") + 1;
			section.set("Achievements." + ach.toString() + ".Score", score);
			if (score == ach.score) {
				if(ach.kitreward == null)
					incCoins(ach.coinreward);
				else
					unlockKit(ach.kitreward);
				if (this.player.isOnline()) {
					((Player) player).sendMessage(Lang.Tag + Chat.translate("&aYou have unlocked the achievement: &b" + ach));
				}
				if (ach != Achievement.MASTER) {
					incScore(Achievement.MASTER);
				}
			}

		} else {
			section.set("Achievements." + ach.toString() + ".Score", 1);
		}
		um.getFileControl().save();
	}

	public void incCoins(int i)
	{
		if(section.contains("Coins"))
			section.set("Coins", section.getInt("Coins") + i);
		else
			section.set("Coins", i);
		this.um.getFileControl().save();
	}

	public void incCoins()
	{
		if(section.contains("Coins"))
			section.set("Coins", section.getInt("Coins") + 1);
		else
			section.set("Coins", 1);
		this.um.getFileControl().save();
	}

	public void incDeaths() {
		if(section.contains("Deaths"))
			section.set("Deaths", (section.getInt("Deaths")) + 1);
		else
			section.set("Deaths", 1);
		this.um.getFileControl().save();
	}

	public void incKills() {
		if(section.contains("Kills"))
			section.set("Kills", (section.getInt("Kills")) + 1);
		else
			section.set("Kills", 1);
		this.um.getFileControl().save();
	}

	public int getWins() {
		if(section.contains("Wins"))
			return section.getInt("Wins");
		return 0;
	}

	public int getRunnerup() {
		if(section.contains("Runnerup"))
			return section.getInt("Runnerup");
		return 0;
	}

	public int getGamesplayed() {
		if(section.contains("Gamesplayed"))
			return section.getInt("Gamesplayed");
		return 0;
	}

	public int getScore(Achievement ach) {
		if (section.contains("Achievements." + ach.toString() + ".Score"))
			return section.getInt("Achievements." + ach.toString() + ".Score");
		return 0;
	}

	public int getCoins()
	{
		if(section.contains("Coins"))
			return section.getInt("Coins");
		return 0;
	}

	public int getDeaths()
	{
		if(section.contains("Deaths"))
			return section.getInt("Deaths");
		return 0;
	}

	public int getKills()
	{
		if(section.contains("Kills"))
			return section.getInt("Kills");
		return 0;
	}

	public int getAchievementCount() {
		int i = 0;
		for (Achievement ach : Achievement.values()) {
			if (hasAchievement(ach)) {
				i++;
			}
		}
		return i;
	}

	public List<Kit> getKits()
	{
		//TODO: Retrieve the players kits from their configuration section.
		if(section.contains("Kits"))
		{
			List<Kit> kits = new ArrayList<>();
			for(String str : section.getStringList("Kits"))
			{
				kits.add(Kit.valueOf(str));
			}
			return kits;
		}
		else
		{
			section.set("Kits", Arrays.asList("NONE", "ARCHER", "WARRIOR", "GUARD"));
		}
		return Arrays.asList(Kit.NONE, Kit.ARCHER, Kit.WARRIOR, Kit.GUARD);
	}

	public void updateScoreboard() {
		if(!player.isOnline())
			return;
		boolean running = this.um.getMain().getMatchManager().isMatchRunning();
		ScoreboardManager manager = this.um.getMain().getServer().getScoreboardManager();
		Player player = (Player) this.player;
		int lives = this.um.getMain().getMatchManager().getLives(player);
		Scoreboard board = manager.getNewScoreboard();
		if(running)
		{
			Objective objective = board.getObjective("lives") != null?board.getObjective("lives"):board.registerNewObjective("lives", "dummy");
			objective.setDisplayName(ChatColor.GREEN + "   " + ChatColor.BOLD + "Lives");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			for(Player tempPlayer : um.getMain().getMatchManager().getPlayersAlive())
			{
				objective.getScore(ChatColor.GOLD + tempPlayer.getName()).setScore(um.getMain().getMatchManager().getLives(tempPlayer));;
			}
		}
		else
		{		
			Objective objective = (board.getObjective("stats") != null?board.getObjective("stats"):board.registerNewObjective("stats", "dummy"));
			objective.setDisplayName(ChatColor.GREEN + "Stats");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.getScore(ChatColor.GOLD + "Coins: " + getCoins()).setScore(8);
			objective.getScore(ChatColor.GOLD + "Kills: " + getKills()).setScore(7);
			objective.getScore(ChatColor.GOLD + "Deaths: " + getDeaths()).setScore(6);
			objective.getScore(ChatColor.GOLD + "Wins: " + getWins()).setScore(5);
			objective.getScore(ChatColor.GOLD + "Runnerups: " + getRunnerup()).setScore(4);
			objective.getScore(ChatColor.GOLD + "Games Played: " + getGamesplayed()).setScore(3);
			objective.getScore(ChatColor.GOLD + "Achievements: " + getAchievementCount()).setScore(2);
		}
			player.setScoreboard(board);
	}

}
