package facejup.mce.players;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import facejup.mce.arenas.ArenaSign;
import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.util.BlockColor;
import facejup.mce.util.Chat;
import facejup.mce.util.FileControl;

public class UserManager implements Listener {
	
	private final boolean BETA = true;

	private Main main; // Dependency Injection Variable.

	private FileControl fc; // FC containing the Users.yml file

	private HashMap<OfflinePlayer, User> users = new HashMap<>(); // Map storing the player and their user.

	public UserManager(Main main)
	{
		//TODO: Constructor which stores the given variables and loads the users.
		this.main = main;
		this.fc = new FileControl(new File(main.getDataFolder(), "users.yml"));
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					addUser(player);
					main.getMatchManager().setPlayerKit(player, Kit.NONE);
					getUser(player).updateScoreboard();
				}
			}
		}, 10L);

		main.getServer().getPluginManager().registerEvents(this, main);
	}

	public Pair<String, Double> getHighestStat(String str)
	{
		double highest = -1;
		String name = null;
		if(str.equals("KDR"))
		{
			for(String key : fc.getConfig().getConfigurationSection("Users").getKeys(false))
			{
				int kills = fc.getConfig().getInt("Users." + key + ".Kills");
				int deaths = fc.getConfig().getInt("Users." + key + ".Deaths");
				double kdr = (kills == 0?(deaths*-1):deaths==0?kills:1.0*kills/deaths);
				if(kdr > highest)
				{
					highest = kdr;
					name = fc.getConfig().getString("Users." + key + ".Name");
				}
			}
		}
		else if(str.equals("Achievements"))
		{
			for(String key : fc.getConfig().getConfigurationSection("Users").getKeys(false))
			{
				if(users.containsKey(Bukkit.getOfflinePlayer(UUID.fromString(key))))
				{
					if(users.get(Bukkit.getOfflinePlayer(UUID.fromString(key))).getAchievementCount() > highest)
					{
						highest = users.get(Bukkit.getOfflinePlayer(UUID.fromString(key))).getAchievementCount();
						name = fc.getConfig().getString("Users." + key + ".Name");
					}
				}
				else
				{
					User user = new User(this, Bukkit.getOfflinePlayer(UUID.fromString(key)));
					if(user.getAchievementCount() > highest)
					{
						highest = user.getAchievementCount();
						name = fc.getConfig().getString("Users." + key + ".Name");
					}
				}
			}
		}
		else
		{
			for(String key : fc.getConfig().getConfigurationSection("Users").getKeys(false))
			{
				int check = fc.getConfig().getInt("Users." + key + "." + str);
				if(check > highest)
				{
					highest = check;
					name = fc.getConfig().getString("Users." + key + ".Name");
				}
			}
		}
		Pair<String, Double> pair = Pair.of(name, highest);
		return pair;
	}

	//EventHandlers

	@EventHandler
	public void playerMove(PlayerMoveEvent event)
	{
		BlockColor.updateArmor(event.getPlayer());
		Player player = event.getPlayer();
		if(main.getMatchManager().isMatchRunning() && main.getMatchManager().getPlayersAlive().contains(player))
		{
			if(main.getMatchManager().getPlayerKit(player) == Kit.SHADE)
			{
				if(main.getMatchManager().isHidden(player) && !main.getMatchManager().getMoveMarker(player).getItem().getBlock().equals(player.getLocation().getBlock()))
				{
					main.getMatchManager().showPlayer(player);
				}
				else if(!main.getMatchManager().getMoveMarker(player).getItem().getBlock().equals(player.getLocation().getBlock()))
				{
					if(player.getLevel()+1 < 100)
						player.setLevel(player.getLevel()+1);
					else
						player.setLevel(100);
					player.setExp((float) (player.getLevel()/100.0));
				}
			}
			else if(main.getMatchManager().getPlayerKit(player) == Kit.DEMON)
			{
				if(!event.getTo().getBlock().equals(event.getFrom().getBlock()) && event.getTo().getBlock().getType() == Material.AIR)
				{
					player.getWorld().spawnFallingBlock(player.getLocation(), Material.FIRE, (byte) 0);
				}
			}
			else if(main.getMatchManager().getPlayerKit(player) == Kit.GOBLIN)
			{
				if(!player.isSneaking() || (player.isGliding() && (player.getVelocity().getY() < -0.08 || player.getVelocity().getY() >= 0.1)))
					player.setGliding(false);
				else if(player.isOnGround() && !player.isGliding() && player.isSneaking())
					player.setGliding(true);
			}
		}
	}


	public void deIgnite(Location loc)
	{
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				if(loc.getBlock().getType() == Material.FIRE)
					loc.getBlock().setType(Material.AIR);
			}
		}, 7L);
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		addUser(event.getPlayer());
		if(!users.get(event.getPlayer()).hasAchievement(Achievement.BETA) && BETA)
			users.get(event.getPlayer()).setScore(Achievement.BETA, 1);
		this.main.getMatchManager().setPlayerKit(event.getPlayer(), Kit.NONE);
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					main.getMatchManager().spawnPlayer(event.getPlayer());
					getUser(p).updateScoreboard();
				}
			}
		}, 10L);
	}

	@EventHandler
	public void playerLeave(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if(main.getMatchManager().voted.containsKey(player))
		{
			ArenaSign sign = main.getMatchManager().voted.get(player);
			main.getMatchManager().votesReceived.put(sign, main.getMatchManager().votesReceived.get(sign)+1);
			main.getMatchManager().voted.remove(player);
		}
		if(main.getMatchManager().getPlayersAlive().contains(player))
		{
			this.main.getMatchManager().setLives(player, 0);
			this.main.getMatchManager().setPlayerDesiredKit(player, Kit.NONE);
			this.main.getMatchManager().setPlayerKit(player, Kit.NONE);
			if(main.getMatchManager().getPlayersAlive().size() == 1)
			{
				main.getMatchManager().kill(player);
				Player winner = main.getMatchManager().getPlayersAlive().get(0);
				main.getUserManager().getUser(winner).incWin(1);
				main.getUserManager().getUser(player).incRunnerup(1);
				String msg = "&9&l(&r&bMCE&9&l) &6The match has ended with " + winner.getName() + " winning, and a runnerup of " + player.getName();
				Chat.bc(msg);
				main.getMatchManager().startTimer.startTimer();
				main.getMatchManager().spawnPlayer(winner);
			}
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					for(Player p : Bukkit.getOnlinePlayers())
					{
						getUser(p).updateScoreboard();
					}
				}
			}, 10L);
			return;
		}
		this.main.getMatchManager().setLives(player, 0);
		this.main.getMatchManager().setPlayerDesiredKit(player, Kit.NONE);
		this.main.getMatchManager().setPlayerKit(player, Kit.NONE);
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				for(Player p : Bukkit.getOnlinePlayers())
				{
					getUser(p).updateScoreboard();
				}
			}
		}, 10L);
	}

	//Getters

	public User getUser(OfflinePlayer player)
	{
		addUser(player);
		return users.get(player);
	}

	public FileControl getFileControl()
	{
		return this.fc;
	}

	public Main getMain()
	{
		return this.main;
	}

	public void addUser(OfflinePlayer player) {
		if(!users.containsKey(player))
		{
			users.put(player, new User(this, player));
			getUser(player).updateScoreboard();
		}
	}

}
