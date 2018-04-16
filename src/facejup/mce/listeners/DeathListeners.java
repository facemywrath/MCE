package facejup.mce.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import facejup.mce.events.PlayerKillEvent;
import facejup.mce.events.PlayerKillThroughEnvironmentEvent;
import facejup.mce.main.Main;
import facejup.mce.markers.DamageMarker;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;
import facejup.mce.util.Numbers;
import net.md_5.bungee.api.ChatColor;

public class DeathListeners implements Listener {

	private EventManager em;
	private Main main;

	public HashMap<Player, DamageMarker> lastDamagedBy = new HashMap<>();

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
				PlayerKillThroughEnvironmentEvent eventcall = new PlayerKillThroughEnvironmentEvent(lastDamagedBy.get(player).getDamager(), player, event.getCause());
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
		DamageMarker marker = new DamageMarker(damager, System.currentTimeMillis());
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

	public void setLastDamagedBy(Player player, DamageMarker marker)
	{
		if(!em.getMain().getMatchManager().isMatchRunning())
			return;
		lastDamagedBy.put(player, marker);
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
		Player killer = lastDamagedBy.get(event.getEntity()).getDamager();	
		event.setDeathMessage(Lang.Tag + Chat.translate(ChatColor.AQUA + killer.getName() + " &ahas killed &b" + player.getName()));
		if(Numbers.getRandom(0, 4) == 4 && (!em.getAchievementListeners().killsPerLife.containsKey(killer) || em.getAchievementListeners().killsPerLife.get(killer)%2==0))
			main.getMatchManager().incLives(killer);
		for(Player player2 : Bukkit.getOnlinePlayers())
		{
			main.getUserManager().getUser(player2).updateScoreboard();
		}
		main.getUserManager().getUser(killer).incCoins();
		main.getUserManager().getUser(killer).incKills();
		main.getServer().getPluginManager().callEvent(new PlayerKillEvent(killer, player));
		if(main.getMatchManager().getLives(player) == 0)
		{
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


}
