package facejup.mce.players;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;

public class User {

	private final boolean FREE_KIT = true;
	
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
				config.set("Users." + player.getUniqueId() + ".FreeKit", FREE_KIT);
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

	public boolean hasAchievement(Achievement ach, int level) {
		if (ach.scores.size() >= level && getScore(ach) >= ach.scores.get(level))
			return true;
		return false;
	}

	public boolean hasAchievement(Achievement ach) {
		if (getScore(ach) >= ach.scores.get(ach.scores.size()-1))
			return true;
		return false;
	}

	public int getAchievementLevel(Achievement ach)
	{
		int score = getScore(ach);
		int lower = 0;
		int i = 0;
		for(int higher : ach.scores)
		{
			if(score >= lower && score < higher)
			{
				return i;
			}
			lower = higher;
			i++;
		}
		return (i < ach.scores.size()?i:ach.scores.size());
	}

	public int getAchievementLevelIndex(Achievement ach)
	{
		int score = getScore(ach);
		int lower = 0;
		int i = -1;
		for(int higher : ach.scores)
		{
			if(score >= lower && score < higher)
			{
				return i;
			}
			lower = higher;
			i++;
		}
		return ach.scores.size()-1;
	}

	public int getNextAchievementScore(Achievement ach)
	{
		int score = getScore(ach);
		int lower = 0;
		for(int higher : ach.scores)
		{
			if(score >= lower && score < higher)
			{
				return higher;
			}
			lower = higher;
		}
		return lower;
	}

	public int getCurrentAchievementMaxScore(Achievement ach)
	{
		int score = getScore(ach);
		int lower = 0;
		for(int higher : ach.scores)
		{
			if(score > lower && score <= higher)
			{
				return higher;
			}
			lower = higher;
		}
		return lower;
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
		updateScoreboard();
	}

	public void setCoins(int i)
	{
		section.set("Coins", i);
		um.getFileControl().save();
		updateScoreboard();
	}
	
	public String getAchievementTypeByScore(Achievement ach)
	{
		String level = "";
		if(ach.scores.size() > 1)
			switch(getAchievementLevel(ach))
			{
			case 0:
				level = "Iron ";
				break;
			case 1:
				level = "Iron ";
				break;
			case 2:
				level = "Gold ";
				break;
			case 3:
				level = "Diamond ";
				break;
			}
		return level;
	}

	public void setScore(Achievement ach, int score)
	{
		section.set("Achievements." + ach.toString() + ".Score", score);
		if (score == getCurrentAchievementMaxScore(ach)) {
			if(ach.rewards.get(getAchievementLevelIndex(ach)).getReward().getRight() == null)
			{
				incCoins(ach.rewards.get(getAchievementLevelIndex(ach)).getReward().getLeft());
			}
			else
				unlockKit(ach.rewards.get(getAchievementLevelIndex(ach)).getReward().getRight());
			if (this.player.isOnline()) {
				((Player) player).sendMessage(Lang.Tag + Chat.translate("&aYou have unlocked the achievement: &b" + getAchievementTypeByScore(ach) + StringUtils.capitaliseAllWords(ach.name().toLowerCase().replaceAll("_", " "))));
			}
			if (ach != Achievement.MASTER) {
				incScore(Achievement.MASTER);
			}
		}
		um.getFileControl().save();
		updateScoreboard();
	}
	
	public void useFreeKit(Kit kit)
	{
		if(section.contains("FreeKit") && section.getBoolean("FreeKit"))
		{
			section.set("FreeKit", false);
			unlockKit(kit);
		}
	}

	public void incWin(int i) {
		if(section.contains("Wins"))
			section.set("Wins", section.getInt("Wins") + i);
		else
			section.set("Wins", i);
		incCoins(25);
		incScore(Achievement.VICTOR);
		this.um.getFileControl().save();
		updateScoreboard();
	}

	public void incRunnerup(int i) {
		if(section.contains("Runnerup"))
			section.set("Runnerup", section.getInt("Runnerup") + i);
		else
			section.set("Runnerup", i);
		incCoins(10);
		incScore(Achievement.FAILURE);
		this.um.getFileControl().save();
		updateScoreboard();
	}

	public void incGamesplayed(int i) {
		if(section.contains("Gamesplayed"))
			section.set("Gamesplayed", section.getInt("Gamesplayed") + i);
		else
			section.set("Gamesplayed", i);
		this.um.getFileControl().save();
		updateScoreboard();
	}

	public void incScore(Achievement ach) {
		if (section.contains("Achievements." + ach.toString() + ".Score")) {
			int score = section.getInt("Achievements." + ach.toString() + ".Score") + 1;
			section.set("Achievements." + ach.toString() + ".Score", score);
			if (score == getCurrentAchievementMaxScore(ach)) {
				if(ach.rewards.get(getAchievementLevelIndex(ach)).getReward().getRight() == null)
				{
					incCoins(ach.rewards.get(getAchievementLevelIndex(ach)).getReward().getLeft());
				}
				else
					unlockKit(ach.rewards.get(getAchievementLevelIndex(ach)).getReward().getRight());
				if (this.player.isOnline()) {
					((Player) player).sendMessage(Lang.Tag + Chat.translate("&aYou have unlocked the achievement: &b" + getAchievementTypeByScore(ach) + StringUtils.capitaliseAllWords(ach.name().toLowerCase().replaceAll("_", " "))));
				}
				if (ach != Achievement.MASTER) {
					incScore(Achievement.MASTER);
				}
			}

		} else {
			section.set("Achievements." + ach.toString() + ".Score", 1);
		}
		um.getFileControl().save();
		updateScoreboard();
	}

	public void incCoins(int i)
	{
		if(section.contains("Coins"))
			section.set("Coins", section.getInt("Coins") + i);
		else
			section.set("Coins", i);
		this.um.getFileControl().save();
		updateScoreboard();
	}

	public void incCoins()
	{
		if(section.contains("Coins"))
			section.set("Coins", section.getInt("Coins") + 1);
		else
			section.set("Coins", 1);
		this.um.getFileControl().save();
		updateScoreboard();
	}

	public void incDeaths() {
		if(section.contains("Deaths"))
			section.set("Deaths", (section.getInt("Deaths")) + 1);
		else
			section.set("Deaths", 1);
		this.um.getFileControl().save();
		updateScoreboard();
	}

	public void incKills() {
		if(section.contains("Kills"))
			section.set("Kills", (section.getInt("Kills")) + 1);
		else
			section.set("Kills", 1);
		this.um.getFileControl().save();
		updateScoreboard();
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

	public double getKDR()
	{
		int deaths = getDeaths();
		int kills = getKills();
		if(deaths == 0)
			return kills;
		if(kills == 0)
			return deaths*-1;
		return Double.parseDouble(new DecimalFormat("##.###").format(1.0*kills/deaths));
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
			i+=getAchievementLevel(ach);
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
			objective.setDisplayName(ChatColor.GREEN + "   " + ChatColor.BOLD + "Current Game");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.getScore("           ").setScore(21);
			objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "Match ").setScore(20);
			objective.getScore(ChatColor.GREEN + "" + ChatColor.BOLD + "  Players Alive: " + ChatColor.LIGHT_PURPLE + um.getMain().getMatchManager().getPlayersAlive().size()).setScore(19);
			objective.getScore("           ").setScore(18);
			objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "Lives ").setScore(17);
			for(Player tempPlayer : um.getMain().getMatchManager().getPlayersAlive())
			{
				objective.getScore(ChatColor.GREEN + "  " + tempPlayer.getName() + ": " + ChatColor.LIGHT_PURPLE + um.getMain().getMatchManager().getLives(tempPlayer)).setScore(um.getMain().getMatchManager().getLives(tempPlayer));;
			}
		}
		else
		{		
			Objective objective = (board.getObjective("stats") != null?board.getObjective("stats"):board.registerNewObjective("stats", "dummy"));
			String title = "" + ChatColor.BLUE + ChatColor.BOLD + "(" + ChatColor.AQUA + ChatColor.BOLD + ChatColor.ITALIC + "MC" + ChatColor.WHITE + ChatColor.BOLD + ChatColor.ITALIC + "Elim" + ChatColor.BLUE + ChatColor.BOLD + ")";
			objective.setDisplayName(title);
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.getScore("           ").setScore(14);
			objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "Server ").setScore(20);
			objective.getScore(ChatColor.GREEN + "  IP: " + ChatColor.LIGHT_PURPLE + Bukkit.getServer().getIp()).setScore(19);
			objective.getScore(ChatColor.GREEN + "  Online: " + ChatColor.LIGHT_PURPLE + Bukkit.getServer().getOnlinePlayers().size()).setScore(16);
			objective.getScore(ChatColor.GREEN + "             ").setScore(15);
			objective.getScore(ChatColor.GREEN + "             ").setScore(14);
			objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "Player ").setScore(13);
			objective.getScore(ChatColor.GREEN + "  Name: " + ChatColor.LIGHT_PURPLE + player.getName()).setScore(12);
			objective.getScore(ChatColor.GREEN + "  Coins: " + ChatColor.LIGHT_PURPLE + getCoins()).setScore(11);
			objective.getScore(ChatColor.GREEN + "  Achievements: " + ChatColor.LIGHT_PURPLE + getAchievementCount() + "/" + Achievement.getMaxAchievementCount()).setScore(10);
			objective.getScore(ChatColor.GREEN + "             ").setScore(9);
			objective.getScore(ChatColor.GREEN + "             ").setScore(8);
			objective.getScore(ChatColor.AQUA + "" + ChatColor.BOLD + "Stats ").setScore(7);
			objective.getScore(ChatColor.GREEN + "  KDR: " + ChatColor.LIGHT_PURPLE + getKDR()).setScore(6);
			objective.getScore(ChatColor.GREEN + "  Kills: " + ChatColor.LIGHT_PURPLE + getKills()).setScore(5);
			objective.getScore(ChatColor.GREEN + "  Deaths: " + ChatColor.LIGHT_PURPLE + getDeaths()).setScore(4);
			objective.getScore(ChatColor.GREEN + "  Wins: " + ChatColor.LIGHT_PURPLE + getWins()).setScore(3);
			objective.getScore(ChatColor.GREEN + "  Runnerups: " + ChatColor.LIGHT_PURPLE + getRunnerup()).setScore(2);
			objective.getScore(ChatColor.GREEN + "  Games Played: " + ChatColor.LIGHT_PURPLE + getGamesplayed()).setScore(1);
		}
		player.setScoreboard(board);
	}

}
