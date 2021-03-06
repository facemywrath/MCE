package facejup.mce.timers;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import facejup.mce.arenas.ArenaSign;
import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.enums.MatchType;
import facejup.mce.enums.TeamType;
import facejup.mce.main.Main;
import facejup.mce.main.MatchManager;
import facejup.mce.players.User;
import facejup.mce.util.Chat;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;
import facejup.mce.util.Marker;
import net.minecraft.server.v1_12_R1.MinecraftServer;

public class StartTimer {

	private final int WAIT_TIME = 180; // Timer start time.
	private final int LINGER_TIME = 120; // Time needed to wait if there are not enough players ready.
	private final String tag = Lang.Tag;

	private Main main; // Dependency Injection variable.
	private MatchManager mm; // Other Dependency Injection.

	private int time; // Time left until the match.
	private boolean running = false; // Whether or not the timer is running.

	public StartTimer(Main main, MatchManager mm) {
		this.mm = mm; // Store the match manager instance.
		this.main = main; // Store the current running instance of main.
	}

	public void startTimer()
	{
		// End the end timer if it's running and start the countdown until the next match begins.
		if(main.getMatchManager().getEndTimer().isRunning())
			main.getMatchManager().getEndTimer().stopTimer();
		time = WAIT_TIME;
		running = true;
		main.getMatchManager().matchtype = MatchType.NORMAL;
		main.getMatchManager().teamtype = TeamType.FFA;
		for(Player player : Bukkit.getOnlinePlayers())
		{
			main.getMatchManager().spawnPlayer(player);
			main.getMatchManager().setPlayerKit(player, Kit.NONE);
			if(!main.getUserManager().getUser(player).hasKit(main.getMatchManager().getPlayerDesiredKit(player)))
				main.getMatchManager().setPlayerDesiredKit(player, Kit.NONE);
			main.getUserManager().getUser(player).updateScoreboard();
		}
		if(mm.getArenaManager() != null)
		{
			mm.getArenaManager().loadVoteSigns();
			if(mm.getArenaManager().getArena() != null)
			{
				for(LivingEntity ent : mm.getArenaManager().getArena().getWorld().getLivingEntities())
				{
					if(ent.getType() != EntityType.PLAYER)
					ent.remove();
				}
			}
		}
		if(!main.getEventManager().getKitPowerListeners().ignitedBlocks.isEmpty())
		{
			for(Marker<Location> marker : main.getEventManager().getKitPowerListeners().ignitedBlocks)
			{
				if(marker.getItem().getBlock().getType() == Material.FIRE)
					marker.getItem().getBlock().setType(Material.AIR);
			}
		}
		countdown();
	}

	public void resumeTimer() {
		this.running = true;
		countdown();
	}
	
	public void setTime(int i)
	{
		this.time = i;
	}

	public void stopTimer()
	{
		this.running = false;
	}

	public int getTime() {
		return this.time;
	}

	private void countdown()
	{
		int minutes = (int) ((time) / 60.0);
		int seconds = (int) ((time) % 60.0);
		MinecraftServer.getServer().setMotd(Chat.translate("    &9&l(&b&l&oMC&f&l&oEliminations&9&l) &7&l : &e&l Version: 1.9 - 1.12 \n        &a&lMatch starting in: &b" + minutes + ":" + seconds + " &a&lminutes!"));
		if(running)
		{
			if(time > 0)
			{
				if(time%45 == 0)
					Lang.autoBroadcast();
				for(ArenaSign sign : ((Set<ArenaSign>) mm.votesReceived.keySet()))
				{
					sign.updateSign();
				}
				for(Player player : Bukkit.getOnlinePlayers())
				{
					Location parkour = new Location(Bukkit.getWorld("Lobby"), -723, 132, -372);
					if(player.getWorld().getName().equalsIgnoreCase("Lobby") && player.getLocation().distance(parkour) < 4)
						if(!main.getUserManager().getUser(player).hasAchievement(Achievement.HARDCORE))
							main.getUserManager().getUser(player).incScore(Achievement.HARDCORE);
					mm.afkCheck(player);
					for(PotionEffect pot : player.getActivePotionEffects())
					{
						player.removePotionEffect(pot.getType());
					}
					if(player.getLocation().getY() < 1)
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
					if(!player.isDead())
						player.setHealth(player.getMaxHealth());
					player.setFoodLevel(20);
					User user = main.getUserManager().getUser(player);
					if(!player.getInventory().contains(ItemCreator.getKitSelector()))
						player.getInventory().setItem(8, ItemCreator.getKitSelector());
				}
				switch(time)
				{
				case 300:
					Chat.bc(tag + "Five minutes left until the match begins!");
					break;
				case 240:
					Chat.bc(tag + "Four minutes left until the match begins!");
					break;
				case 180:
					Chat.bc(tag + "Three minutes left until the match begins!");
					break;
				case 120:
					Chat.bc(tag + "Two minutes left until the match begins!");
					break;
				case 60:
					Chat.bc(tag + "One minute left until the match begins!");
					break;
				case 30:
					Chat.bc(tag + "30 seconds left until the match begins!");
					break;
				case 10:
					Chat.bc(tag + "10 seconds left until the match begins!");
					break;
				case 5:
					Chat.bc(tag + "5 seconds left until the match begins!");
					break;
				case 4:
					Chat.bc(tag + "4 seconds left until the match begins!");
					break;
				case 3:
					Chat.bc(tag + "3 seconds left until the match begins!");
					break;
				case 2:
					Chat.bc(tag + "2 seconds left until the match begins!");
					break;
				case 1:
					Chat.bc(tag + "1 second left until the match begins!");
					break;
				}
				time--;
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
				{
					public void run()
					{
						countdown();
					}
				}, 20L); 
			}
			else
			{
				if(mm.getPlayersQueued() < mm.MIN_PLAYERS)
				{
					Chat.bc(tag + "&cNot enough players queued " + "&6(" + mm.getPlayersQueued() + "/" + mm.MIN_PLAYERS + ")" + "&c to begin a match. Please select a kit with the &5&lKit Selector &cto join queue.");
					linger();
				}
				else
				{
					mm.startMatch();
				}
			}
		}
	}

	public boolean isRunning() 
	{
		return this.running;
	}

	public void linger() {
		time = LINGER_TIME;
		for(Player player : Bukkit.getOnlinePlayers())
		{
			main.getUserManager().getUser(player).updateScoreboard();
		}
		countdown();
	}

}
