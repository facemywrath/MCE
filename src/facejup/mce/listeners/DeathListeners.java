package facejup.mce.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import facejup.mce.enums.Kit;
import facejup.mce.events.PlayerKillEvent;
import facejup.mce.main.Main;
import facejup.mce.util.Chat;
import facejup.mce.util.Numbers;

public class DeathListeners implements Listener {

	private EventManager em;
	private Main main;

	private HashMap<Player, DamageMarker> lastDamagedBy = new HashMap<>();

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
		if(main.getMatchManager().getLives(player) > 0 && main.getMatchManager().getPlayerDesiredKit(player) != Kit.NONE)
		{
			main.getMatchManager().spawnPlayer(player);
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

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) 
	{
		event.setDeathMessage("");
		Player player = event.getEntity();
		main.getMatchManager().decLives(player);
		main.getUserManager().getUser(player).incDeaths();
		if(!lastDamagedBy.containsKey(event.getEntity()))
			return;
		Player killer = lastDamagedBy.get(event.getEntity()).getDamager();
		if(Numbers.getRandom(0, 4) == 4)
			main.getMatchManager().incLives(killer);
		main.getUserManager().getUser(killer).incCoins();
		main.getUserManager().getUser(killer).incKills();
		main.getServer().getPluginManager().callEvent(new PlayerKillEvent(killer, player));
		if(main.getMatchManager().getLives(player) == 0)
		{
			if(main.getMatchManager().getPlayersAlive().size() == 1)
			{
				main.getMatchManager().kill(player);
				Player winner = main.getMatchManager().getPlayersAlive().get(0);
				main.getUserManager().getUser(winner).incWin(1);
				main.getUserManager().getUser(player).incRunnerup(1);
				String msg = "&9&l(&r&bMCE&9&l) &6The match has ended with " + winner.getName() + " winning, and a runnerup of " + player.getName();
				Chat.bc(msg);
				main.getMatchManager().startTimer.startTimer();
			}
			else
			{
				main.getMatchManager().kill(player);
			}
		}
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				player.spigot().respawn();
			}
		}, 100L);
	}


}
