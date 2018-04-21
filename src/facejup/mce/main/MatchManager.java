package facejup.mce.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import facejup.mce.arenas.Arena;
import facejup.mce.arenas.ArenaManager;
import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.players.User;
import facejup.mce.timers.EndTimer;
import facejup.mce.timers.StartTimer;
import facejup.mce.util.Chat;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;
import facejup.mce.util.Marker;
import facejup.mce.util.Numbers;
import me.libraryaddict.disguise.DisguiseAPI;
import net.md_5.bungee.api.ChatColor;

public class MatchManager {

	public final int MIN_PLAYERS = 2;

	private Main main; // Dependency Injection variable.

	public ArenaManager am;
	public StartTimer startTimer; // The timer variable which decides when a round begins.
	public EndTimer endTimer; // The timer variable which decides when a round begins.
	public String Tag = Lang.Tag;

	public boolean randomKits = false;
	private HashMap<Player, Marker<Location>> lastMovement = new HashMap<>();
	private HashMap<Player, Integer> lives = new HashMap<>();
	private HashMap<Player, Kit> kits = new HashMap<>();
	private HashMap<Player, Kit> desiredKits = new HashMap<>();

	public MatchManager(Main main)
	{
		this.main = main;
		endTimer = new EndTimer(main, this);
		startTimer = new StartTimer(main, this);
		this.am = new ArenaManager(this);
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				startTimer.startTimer();
			}
		}, 20L);	
	}

	public boolean isMatchRunning()
	{
		return this.endTimer.isRunning();
	}

	public void updateMoveMarker(Player player)
	{
		lastMovement.put(player, new Marker<Location>(player.getLocation()));
	}

	public Marker<Location> getMoveMarker(Player player)
	{
		if(lastMovement.containsKey(player))
			return lastMovement.get(player);
		return null;
	}

	public void shadeCheck(Player player) {
		if(lastMovement.containsKey(player))
		{
			if(lastMovement.get(player).getItem().getBlock().equals(player.getLocation().getBlock()))
			{
				if(lastMovement.get(player).getSecondsPassedSince() > 2)
				{
					int stamina = player.getLevel();
					if(stamina > 0)
					{
						if(stamina-7 > 0)
							stamina-=7;
						else
							stamina = 0;
						player.setLevel(stamina);
						player.setExp((float) (stamina/100.0));
						hidePlayer(player);
						return;
					}
					else
					{
						showPlayer(player);
					}
				}
			}
		}
	}

	public void showPlayer(Player player)
	{
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY))
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		if(player.hasPotionEffect(PotionEffectType.REGENERATION))
			player.removePotionEffect(PotionEffectType.REGENERATION);
		for(Player player2 : Bukkit.getOnlinePlayers())
		{
			if(!(player.equals(player2)))
			{
				player2.showPlayer(player);
			}
		}
	}

	public void hidePlayer(Player player)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000, 0));
		if(getPlayerKit(player) == Kit.SHADE)
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10000, 2));
		for(Player player2 : Bukkit.getOnlinePlayers())
		{
			if(!(player.equals(player2)))
			{
				player2.hidePlayer(player);
			}
		}
	}

	public boolean isHidden(Player player)
	{
		if(player.hasPotionEffect(PotionEffectType.INVISIBILITY))
			return true;
		return (lastMovement.containsKey(player)?lastMovement.get(player).getSecondsPassedSince() > 2:false);
	}

	public void afkCheck(Player player)
	{
		Marker<Location> mm = new Marker<Location>(player.getLocation());
		if(!lastMovement.containsKey(player))
		{
			lastMovement.put(player, mm);
			return;
		}
		if(lastMovement.get(player).getItem().getBlock().equals(mm.getItem().getBlock()))
		{
			if(isMatchRunning() && lastMovement.get(player).getSecondsPassedSince() >= 60)
			{
				switch(lastMovement.get(player).getSecondsPassedSince())
				{
				case 60:
					player.sendMessage(Chat.translate(Lang.afkcheckTag + "You will be removed for AFK in 10 seconds if you don't move."));
					player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
					break;
				case 65:
					player.sendMessage(Chat.translate(Lang.afkcheckTag + "You will be removed for AFK in 5 seconds if you don't move."));
					player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
					break;
				case 66:
					player.sendMessage(Chat.translate(Lang.afkcheckTag + "You will be removed for AFK in 4 seconds if you don't move."));
					player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
					break;
				case 67:
					player.sendMessage(Chat.translate(Lang.afkcheckTag + "You will be removed for AFK in 3 seconds if you don't move."));
					player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
					break;
				case 68:
					player.sendMessage(Chat.translate(Lang.afkcheckTag + "You will be removed for AFK in 2 seconds if you don't move."));
					player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
					break;
				case 69:
					player.sendMessage(Chat.translate(Lang.afkcheckTag + "You will be removed for AFK in 1 second if you don't move."));
					player.playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1);
					break;
				case 70:
					if (player.isOp())
						return;
					player.kickPlayer("AFK");
					break;
				}
			}
			else 
			{
				if(!isMatchRunning() && desiredKits.containsKey(player) && desiredKits.get(player) != Kit.NONE)
					switch(lastMovement.get(player).getSecondsPassedSince())
					{
					case 180:
						player.sendMessage(Chat.translate(Lang.afkcheckTag + "If you don't move, your kit will be deselected in 10 seconds."));
						break;
					case 190:
						player.sendMessage(Chat.translate(Lang.afkcheckTag + "Your kit has been deselected."));
						setPlayerDesiredKit(player, Kit.NONE);
					}
			}
		}
		else 
		{
			lastMovement.put(player, mm);
		}
	}

	public void startMatch()
	{
		Arena arena = am.getRandomArena(desiredKits.keySet().size());
		if(arena == null)
		{
			startTimer.linger();
			Chat.bc(Tag + "&cError: No Arena found within parameters.");
			return;
		}
		if(arena.getSpawnPoints().size() >= desiredKits.keySet().size())
		{
			Chat.bc(Tag + "&b&l Arena selected: " + arena.getName().replaceAll("_"," "));
			endTimer.startTimer();
			randomKits = false;
			if(Numbers.getRandom(0, 10) == 10)
				randomKits = true;
			if(randomKits)
				Chat.bc(Lang.Tag + "&5Random Heroes Mode");
			for(Player player : desiredKits.keySet())
			{
				main.getUserManager().getUser(player).incGamesplayed(1);
				if(!main.getUserManager().getUser(player).hasAchievement(Achievement.SPENDER))
					lives.put(player, 5);
				else
					lives.put(player, 6);
				spawnPlayer(player);
				main.getUserManager().getUser(player).updateScoreboard();
			}
		}
		else
		{
			startTimer.linger();
			Chat.bc(Tag + "&cError: No Arena Found.");
			return;
		}
	}

	public void startMatch(String arenaname)
	{
		Arena arena = am.setArena(new Arena(am, am.getArenaSection(arenaname)));
		if(arena == null)
		{
			startTimer.linger();
			Chat.bc(Tag + "&cError: The chosen arena doesn't exist.");
			return;
		}
		if(arena.getSpawnPoints().size() >= desiredKits.keySet().size())
		{
			Chat.bc(Tag + "&b&l Arena selected: " + arena.getName().replaceAll("_"," "));
			endTimer.startTimer();
			randomKits = false;
			if(Numbers.getRandom(0, 1) == 1)
				randomKits = true;
			if(randomKits)
				Chat.bc(Lang.Tag + "&5Random Heroes Mode");
			for(Player player : desiredKits.keySet())
			{
				main.getUserManager().getUser(player).incGamesplayed(1);
				if(!main.getUserManager().getUser(player).hasAchievement(Achievement.SPENDER))
					lives.put(player, 5);
				else
					lives.put(player, 6);
				spawnPlayer(player);
				main.getUserManager().getUser(player).updateScoreboard();
			}
		}
		else
		{
			startTimer.linger();
			Chat.bc(Tag + "&cError: No Arena found within parameters.");
			return;
		}
	}

	public void spawnPlayer(Player player) 
	{
		showPlayer(player);
		player.getInventory().clear();
		if(DisguiseAPI.isDisguised(player))
			DisguiseAPI.undisguiseToAll(player);
		player.setFallDistance(0);
		if(player.isDead())
			player.spigot().respawn();
		player.setGameMode(GameMode.SURVIVAL);
		if(!this.isMatchRunning())
		{
			if(player.isOp())
			{
				main.getServer().dispatchCommand(player, "spawn");
			}
			else
			{
				player.setOp(true);
				main.getServer().dispatchCommand(player, "spawn");
				player.setOp(false);
			}
			player.getInventory().clear();
			player.setHealth(player.getMaxHealth());
			player.setFoodLevel(20);
			player.getInventory().setItem(8, ItemCreator.getKitSelector());
			return;
		}
		if (!lives.containsKey(player))
		{
			if(player.isOp())
			{
				main.getServer().dispatchCommand(player, "spawn");
			}
			else
			{
				player.setOp(true);
				main.getServer().dispatchCommand(player, "spawn");
				player.setOp(false);
			}
			return;
		}
		if(randomKits)
				setPlayerDesiredKit(player, Kit.values()[Numbers.getRandom(1, Kit.values().length-1)]);
		if(randomKits)
			player.sendMessage(Chat.translate(Lang.Tag + "You spawned with &bKit " + StringUtils.capitalize(getPlayerDesiredKit(player).toString().toLowerCase())));
		if (lives.get(player) == 0) {
			lives.remove(player);
			kits.put(player, Kit.NONE);
			desiredKits.put(player, Kit.NONE);
			if(player.isOp())
			{
				main.getServer().dispatchCommand(player, "spawn");
			}
			else
			{
				player.setOp(true);
				main.getServer().dispatchCommand(player, "spawn");
				player.setOp(false);
			}
			return;
		}
		if (desiredKits.containsKey(player) && !(desiredKits.get(player).equals(kits.get(player)))) {
			kits.put(player, desiredKits.get(player));
		} else if (!(kits.containsKey(player)) || kits.get(player) == Kit.NONE)
			return;
		if (am.getArena() == null)
			return;
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {

			@SuppressWarnings("deprecation")
			public void run() {
				player.setHealth(player.getMaxHealth());
				player.setFoodLevel(20);
				if(isMatchRunning())
				{
					for(PotionEffect pot : player.getActivePotionEffects())
					{
						player.removePotionEffect(pot.getType());
					}
					if(!randomKits)
					player.getInventory().setItem(8, ItemCreator.getKitSelector());
					Kit kit = kits.get(player);
					if(kit == Kit.HARPY || kit == Kit.SHADE || kit == Kit.DEMON || kit == Kit.GRAVITON)
						player.setLevel(100);
					else
						player.setLevel(0);
					player.setExp((float) (player.getLevel()/100.0));
					main.getEventManager().getAchievementListeners().killsPerLife.put(player, 0);
					kit.storage.stream().filter(item -> item != null).forEach(item -> player.getInventory().addItem(item));
					player.getInventory().setHelmet(kit.helmet);
					player.getInventory().setChestplate(kit.chestplate);
					player.getInventory().setLeggings(kit.leggings);
					player.getInventory().setBoots(kit.boots);
					player.getInventory().setItemInOffHand(kit.offhand);
					if(main.getUserManager().getUser(player).hasAchievement(Achievement.ARCHITECT))
						player.getInventory().addItem(new ItemCreator(Material.OBSIDIAN).setAmount(16).setDisplayname("&aSpecial Stone").setLore(Arrays.asList("&5&lA reward for your architecture.", "&7&oThese blocks automatically", "&7&odespawn after 10 seconds.")).getItem());
					if(kit == Kit.MAGE)
						player.getInventory().addItem(endTimer.getRandomPotion());
					Location loc = am.getArena().getRandomSpawn();
					player.teleport(loc);
				}
				else
				{
					if(player.isOp())
					{
						main.getServer().dispatchCommand(player, "spawn");
					}
					else
					{
						player.setOp(true);
						main.getServer().dispatchCommand(player, "spawn");
						player.setOp(false);
					}

				}
			}

		}, 5L);
	}

	public void checkMatchEnd(Player check)
	{
		if(getPlayersAlive().size() == 1)
		{
			Player winner = main.getMatchManager().getPlayersAlive().get(0);
			main.getUserManager().getUser(check).incRunnerup(1);
			main.getUserManager().getUser(winner).incWin(1);
			String msg = "&9&l(&r&bMCE&9&l) &6The match has ended with " + winner.getName() + " winning, and a runnerup of " + check.getName();
			Chat.bc(msg);
			startTimer.startTimer();
		}
	}

	public void endMatchByTime()
	{
		if(lives.keySet().size() >= 2)
		{
			Optional<Integer> i = lives.keySet().stream().map(player -> lives.get(player)).max((i1,i2) -> Integer.compare(i1, i2));

			if(i.isPresent())
			{
				int j = (int) lives.keySet().stream().filter(player -> lives.get(player) == i.get()).count();
				if(j > 1)
				{
					List<Player> players = lives.keySet().stream().filter(player -> lives.get(player) == i.get()).collect(Collectors.toList());
					for(Player player : players)
					{
						User user = main.getUserManager().getUser(player);
						user.incRunnerup(1);
						if(player.isOnline())
							main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
							{
								public void run()
								{
									for(Player player : Bukkit.getOnlinePlayers())
									{
										spawnPlayer(player);
									}
								}
							}, 1L);
					}
					String msg = Tag + "&6The match has ended in a tie between: " + players.stream().map(Player::getName).collect(Collectors.joining(", "));
					Chat.bc(msg);
				}
				else
				{
					Player winner = lives.keySet().stream().filter(player -> lives.get(player) == i.get()).collect(Collectors.toList()).get(0);
					Optional<Integer> k = lives.keySet().stream().filter(player -> lives.get(player) != i.get()).map(player -> lives.get(player)).max((i1, i2) -> Integer.compare(i1, i2));
					if(k.isPresent())
					{
						int l = (int) lives.keySet().stream().filter(player -> lives.get(player) == k.get()).count();
						if(l > 1)
						{
							List<Player> players = lives.keySet().stream().filter(player -> lives.get(player) == k.get()).collect(Collectors.toList());
							for(Player player : players)
							{
								main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
								{
									public void run()
									{
										for(Player player : Bukkit.getOnlinePlayers())
										{
											spawnPlayer(player);
										}
									}
								}, 1L);
								User user = main.getUserManager().getUser(player);
								user.incRunnerup(1);
							}
							String msg = Tag + "&6The match has ended with " + winner.getName() + " winning, and a runnerup tie between: " + players.stream().map(Player::getName).collect(Collectors.joining(", "));
							Chat.bc(msg);	
						}
						else
						{
							Player runnerup = lives.keySet().stream().filter(player -> lives.get(player) == k.get()).collect(Collectors.toList()).get(0);
							String msg = Tag + "&6The match has ended with " + winner.getName() + " winning, and a runnerup of " + runnerup.getName();
							Chat.bc(msg);
							main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
							{
								public void run()
								{
									for(Player player : Bukkit.getOnlinePlayers())
									{
										spawnPlayer(player);
									}
								}
							}, 1L);
							main.getUserManager().getUser(winner).incWin(1);
							main.getUserManager().getUser(runnerup).incRunnerup(1);
						}
					}
				}
			}
		}
		this.startTimer.startTimer();
	}

	public void setPlayerKit(Player player, Kit kit)
	{
		this.kits.put(player, kit);
	}

	public void setPlayerDesiredKit(Player player, Kit kit)
	{
		if(!isMatchRunning() && main.getMatchManager().getPlayerDesiredKit(player) == Kit.NONE && kit != Kit.NONE)
		{
			this.desiredKits.put(player, kit);
			for(Player p : Bukkit.getOnlinePlayers())
			{
				main.getUserManager().getUser(player).updateScoreboard();
			}
			Chat.bc(Lang.Tag + ChatColor.LIGHT_PURPLE + player.getName() + " has chosen a kit. &6(" + (main.getMatchManager().getPlayersQueued()) + "/" + main.getMatchManager().MIN_PLAYERS + ")");
		}
		else if(!isMatchRunning() && main.getMatchManager().getPlayerDesiredKit(player) != Kit.NONE && kit == Kit.NONE)
		{
			this.desiredKits.put(player, kit);
			for(Player p : Bukkit.getOnlinePlayers())
			{
				main.getUserManager().getUser(player).updateScoreboard();
			}
			Chat.bc(Lang.Tag + ChatColor.LIGHT_PURPLE + player.getName() + " has left queue. &6(" + (main.getMatchManager().getPlayersQueued()) + "/" + main.getMatchManager().MIN_PLAYERS + ")");
		}
		this.desiredKits.put(player, kit);
	}

	public ArenaManager getArenaManager() {
		return this.am;
	}

	public void setLives(Player player, int i)
	{
		lives.put(player, i);
	}

	public int getLives(Player player)
	{
		if(lives.containsKey(player))
			return lives.get(player);
		return 0;
	}

	public void incLives(Player player)
	{
		if(lives.containsKey(player))
			if(lives.get(player) > 0)
			{
				lives.put(player, lives.get(player)+1);
				player.sendMessage(Lang.Tag + ChatColor.GOLD + " You gained an extra life!");
			}
	}

	public void decLives(Player player) {
		if(lives.containsKey(player))
			if((lives.get(player) - 1) >= 0)
				lives.put(player, (lives.get(player) - 1));
	}

	public Kit getPlayerKit(Player player)
	{
		if(kits.containsKey(player))
			return kits.get(player);
		return Kit.NONE;
	}

	public Kit getPlayerDesiredKit(Player player)
	{
		if(desiredKits.containsKey(player))
			return desiredKits.get(player);
		return Kit.NONE;
	}

	public StartTimer getStartTimer()
	{
		return this.startTimer;
	}

	public EndTimer getEndTimer()
	{
		return this.endTimer;
	}

	public Main getMain()
	{
		return this.main;
	}

	public List<Player> getPlayersAlive()
	{
		List<Player> players = new ArrayList<>();
		for(Player player : lives.keySet())
		{
			if(lives.get(player) > 0 && kits.get(player) != Kit.NONE)
				players.add(player);
		}
		return players;
	}

	public int getPlayersQueued()
	{
		int i = 0;
		if(desiredKits.isEmpty())
			return 0;
		for(Player player : desiredKits.keySet())
		{
			if(desiredKits.get(player) != Kit.NONE)
				i++;
		}
		return i;
	}

	public Player getPlayerClosestTo(Player target)
	{
		Double d = Double.MAX_VALUE;
		Player ret = null;
		for(Player player : getPlayersAlive())
		{
			if(player.equals(target))
				continue;
			if(player.getWorld().equals(target.getWorld()))
			{
				if(player.getLocation().distance(target.getLocation()) < d)
				{
					d = player.getLocation().distance(target.getLocation());
					ret = player;
				}
			}
		}
		return ret;

	}

	public void kill(Player player) {
		this.lives.remove(player);

	}

}
