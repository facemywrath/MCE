package facejup.mce.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import facejup.mce.main.Main;

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
	public void playerDeathEvent(PlayerDeathEvent event) {

		if (!(event.getEntity().getKiller() instanceof Player)) {
			return;
		}
		if(!lastDamagedBy.containsKey(event.getEntity()))
			return;
		Player killer = lastDamagedBy.get(event.getEntity()).getDamager();
		Player player = event.getEntity();
		if(main.getUserManager().getUser(player) == null)
			main.getUserManager().addUser(player);
		if(main.getUserManager().getUser(killer) == null)
			main.getUserManager().addUser(killer);
		main.getMatchManager().decLives(player);
		main.getUserManager().getUser(player).incDeaths();
		main.getUserManager().getUser(killer).incKills();
	}


}
