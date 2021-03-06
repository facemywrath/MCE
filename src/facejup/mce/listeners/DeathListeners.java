package facejup.mce.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.MatchType;
import facejup.mce.enums.TeamType;
import facejup.mce.events.PlayerEliminatedEvent;
import facejup.mce.events.PlayerKillEvent;
import facejup.mce.events.PlayerKillThroughEnvironmentEvent;
import facejup.mce.main.Main;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;
import facejup.mce.util.Marker;
import facejup.mce.util.Numbers;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.AttributeModifier;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;

public class DeathListeners implements Listener {

	private EventManager em;
	private Main main;

	public List<Marker<Location>> zombieSpawns = new ArrayList<>();
	public HashMap<Player, Marker<Player>> lastDamagedBy = new HashMap<>();

	public DeathListeners(EventManager em)
	{
		this.em = em;
		this.main = em.getMain();
		em.getMain().getServer().getPluginManager().registerEvents(this, em.getMain());
	}

	@EventHandler
	public void playerRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				main.getMatchManager().spawnPlayer(player);
			}
		}, 3L);
	}

	@EventHandler
	public void anyDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		if(!main.getMatchManager().isMatchRunning() || !em.getMain().getMatchManager().getPlayersAlive().contains(event.getEntity()))
		{
			event.setCancelled(true);
			return;
		}
		Player player = (Player) event.getEntity();
		if(player.getHealth() <= event.getFinalDamage())
		{
			if(lastDamagedBy.containsKey(player))
			{
				PlayerKillThroughEnvironmentEvent eventcall = new PlayerKillThroughEnvironmentEvent(lastDamagedBy.get(player).getItem(), player, event.getCause());
				main.getServer().getPluginManager().callEvent(eventcall);
			}
		}
	}

	@EventHandler
	public void registerLastHit(EntityDamageByEntityEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		if(!(event.getDamager() instanceof Projectile || event.getDamager() instanceof Player))
			return;
		if(event.getDamager() instanceof Projectile && !(((Projectile) event.getDamager()).getShooter() instanceof Player))
			return;
		Player damager = null;
		if(event.getDamager() instanceof Projectile)
			damager = (Player) ((Projectile) event.getDamager()).getShooter();
		else
			damager = (Player) event.getDamager();
		Player target = (Player) event.getEntity();
		Marker<Player> marker = new Marker<Player>(damager);
		lastDamagedBy.put(target, marker);
		em.getMain().getServer().getScheduler().scheduleSyncDelayedTask(em.getMain(), new Runnable()
		{
			public void run()
			{
				if(lastDamagedBy.containsKey(target) && lastDamagedBy.get(target).equals(marker))
					lastDamagedBy.remove(target);
			}
		}, 160L);
	}

	public void setLastDamagedBy(Player player, Marker<Player> marker)
	{
		if(!em.getMain().getMatchManager().isMatchRunning())
			return;
		lastDamagedBy.put(player, marker);
		if(em.getMain() != null)
			em.getMain().getServer().getScheduler().scheduleSyncDelayedTask(em.getMain(), new Runnable()
			{
				public void run()
				{
					if(lastDamagedBy.containsKey(player) && lastDamagedBy.get(player).equals(marker))
						lastDamagedBy.remove(player);
				}
			}, 160L);
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) 
	{
		em.getInventoryListeners().clearSpecialBlocks(event.getEntity());
		if(!em.getMain().getMatchManager().isMatchRunning()) // Only run this function during a match
			return;
		if(!em.getMain().getMatchManager().getPlayersAlive().contains(event.getEntity()))
			return;
		event.setKeepInventory(true); // Let the player keep their items.
		Player player = event.getEntity();
		main.getMatchManager().decLives(player); // Lower their lives.
		main.getUserManager().getUser(player).incDeaths(); // Increase their deaths.
		for(Player player2 : Bukkit.getOnlinePlayers())
		{ // Update every players scoreboard
			main.getUserManager().getUser(player2).updateScoreboard();
		}
		if(main.getMatchManager().teamtype != TeamType.FFA && main.getMatchManager().team.containsKey(player) && main.getMatchManager().teamlives.containsKey(main.getMatchManager().team.get(player)) && main.getMatchManager().teamlives.get(main.getMatchManager().team.get(player)) == 0 && main.getMatchManager().getLives(player) > 0)
		{
			Chat.bc(Chat.translate(Lang.Tag + "" + main.getMatchManager().team.get(player) + Chat.formatName(main.getMatchManager().team.get(player).name()) + " &aTeam has no more lives. They no longer respawn. Eliminate them!"));
		}
		if(!lastDamagedBy.containsKey(event.getEntity()))
		{ // If they weren't killed by a player:
			// Broadcast msg
			event.setDeathMessage(Lang.Tag + Chat.translate(ChatColor.AQUA + player.getName() + " &ahas died from natural causes."));
			if(main.getMatchManager().getPlayersAlive().size() == 1)
			{ // If there's only one player left

				main.getMatchManager().checkMatchEnd(player);

			}
			return;
		}
		Player killer = lastDamagedBy.get(event.getEntity()).getItem();	
		if(main.getMatchManager().matchtype == MatchType.BOSS)
		{
			if(main.getMatchManager().team.containsKey(player) && main.getMatchManager().team.get(player) == org.bukkit.ChatColor.RED)
			{
				main.getUserManager().getUser(killer).incScore(Achievement.OVERLORD);
			}
		}
		if(killer.getInventory().getItemInMainHand().getType() == Material.RAW_FISH)
			main.getUserManager().getUser(killer).incScore(Achievement.FISHSLAP);
		if(main.getMatchManager().getLives(player) > 0)
			event.setDeathMessage(Lang.Tag + Chat.translate(ChatColor.AQUA + killer.getName() + " &ahas killed &b" + player.getName()));
		else
			event.setDeathMessage(Lang.Tag + Chat.translate(ChatColor.GOLD + player.getName() + " &d&ohas been eliminated from the game."));
		if(main.getMatchManager().matchtype == MatchType.SUDDENDEATH || (main.getMatchManager().matchtype != MatchType.BOSS && !killer.equals(player) && Numbers.getRandom(0, 4) == 4 && (!em.getAchievementListeners().killsPerLife.containsKey(killer) || em.getAchievementListeners().killsPerLife.get(killer)%2==0)))
			main.getMatchManager().incLives(killer);
		for(Player player2 : Bukkit.getOnlinePlayers())
		{
			main.getUserManager().getUser(player2).updateScoreboard();
		}
		main.getUserManager().getUser(killer).incCoins();
		main.getUserManager().getUser(killer).incKills();
		main.getServer().getPluginManager().callEvent(new PlayerKillEvent(killer, player));
		if(main.getMatchManager().teamtype != TeamType.FFA)
		{
			if(main.getMatchManager().team.containsKey(player))
			{
				if(main.getMatchManager().getLives(main.getMatchManager().team.get(player)) == 0 && main.getMatchManager().getLives(player) > 0)
				{
					//	Chat.translate(Chat.translate(Lang.Tag + ChatColor.valueOf(main.getMatchManager().team.get(player))))
				}
			}
		}
		if(main.getMatchManager().getLives(player) == 0)
		{
			main.getUserManager().getUser(killer).incScore(Achievement.DESTROYER);
			//player.spigot().respawn();
			//player.performCommand("spectate");
			main.getServer().getPluginManager().callEvent(new PlayerEliminatedEvent(killer, player));
			main.getMatchManager().checkMatchEnd(player);
		}
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				if(player.isDead())
					player.spigot().respawn();
			}
		}, 100L);
	}

	@EventHandler
	public void zombieRespawn(EntityDeathEvent event)
	{
		if(event.getEntity() instanceof Zombie && event.getEntity().getFireTicks() < 0 && event.getEntity().getLocation().getY() > 0)
		{
			if(event.getEntity().getKiller() != null)
			{
				main.getUserManager().getUser(event.getEntity().getKiller()).incScore(Achievement.ZOMBIELAND);
			}
			for(int i = 0; i < Numbers.getRandom(1, 3); i++)
			{
				zombieSpawns.add(new Marker<Location>(event.getEntity().getLocation()));
			}
		}
	}

	public void spawnZombies()
	{
		List<Marker<Location>> removeQueue = new ArrayList<>();
		for(Marker<Location> loc : zombieSpawns)
		{
			if(loc.getSecondsPassedSince() > 5)
			{
				removeQueue.add(loc);
				Main.spawnZombie(loc.getItem());
			}
		}
		removeQueue.stream().forEach(item -> zombieSpawns.remove(item));
	}

}
