package facejup.mce.listeners;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.util.Vector;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.markers.DamageMarker;
import facejup.mce.markers.MoveMarker;
import facejup.mce.players.User;

public class KitPowerListeners implements Listener {

	private EventManager em;
	private Main main;

	public KitPowerListeners(EventManager em)
	{
		this.em = em;
		this.main = em.getMain();
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void negateNinjaFallDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		User user = main.getUserManager().getUser(player);
		if(main.getMatchManager().getPlayerKit(player) == Kit.NINJA && main.getMatchManager().getLives(player) > 0)
		{
			if(event.getCause() == DamageCause.FALL)
			{
				event.setDamage(event.getDamage()/2.0);
			}
		}
	}

	@EventHandler
	public void negateNinjaTeleportDamage(PlayerTeleportEvent event)
	{
		if(event.getCause() == TeleportCause.ENDER_PEARL && main.getMatchManager().getPlayerKit(event.getPlayer()) == Kit.NINJA)
		{
			event.setCancelled(true);
			//	event.getPlayer().setNoDamageTicks(1);
			event.getPlayer().teleport(event.getTo());
		}
	}

	@EventHandler
	public void projectileLaunch(ProjectileLaunchEvent event)
	{
		if(event.getEntity().getType() == EntityType.FISHING_HOOK && ((Projectile) event.getEntity()).getShooter() instanceof Player)
		{
			event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(2));
		}
	}

	@EventHandler
	public void projectileHit(ProjectileHitEvent event)
	{
		if(event.getEntityType() == EntityType.FISHING_HOOK)
		{
			if(event.getEntity().getShooter() instanceof Player && event.getHitEntity() instanceof Player)
			{
				Player player = (Player) event.getEntity().getShooter();
				DamageMarker marker = new DamageMarker(player, System.currentTimeMillis());
				Player target = (Player) event.getHitEntity();
				main.getEventManager().getDeathListeners().setLastDamagedBy(target, marker);
				target.damage(1);
				event.getHitEntity().teleport(player);
				player.setCooldown(Material.FISHING_ROD, 40);
			}
			event.getEntity().remove();
		}
	}

	@EventHandler
	public void harpyFlight(PlayerToggleSprintEvent event)
	{
		if(event.isSprinting())
		{
			Player player = event.getPlayer();
			if(main.getMatchManager().getPlayerKit(player) == Kit.HARPY)
			{
				float stamina = player.getLevel();
				if(stamina > 0)
				{
					main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							runHarpyFlight(player);
						}
					}, 1L);
				}
			}
		}
	}

	public void runHarpyFlight(Player player)
	{
		if(player.isSprinting())
		{
			if(main.getMatchManager().getPlayerKit(player) == Kit.HARPY)
			{
				float stamina = player.getLevel();
				if(stamina > 0)
				{
					player.setVelocity(player.getLocation().getDirection().multiply(0.4));
					if(stamina-1 < 0)
						stamina = 0;
					else
						stamina -= 1;
					player.setExp((float) (stamina/100.0));
					player.setLevel((int) stamina);
					main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							runHarpyFlight(player);
						}
					}, 2L);
				}
			}
		}
	}

	@EventHandler
	public void negateFallDamageHarpy(EntityDamageEvent event)
	{
		if(event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player)
		{
			if(main.getMatchManager().getPlayerKit((Player) event.getEntity()) == Kit.HARPY)
			{
				event.setDamage(event.getDamage()/4);
			}
		}
	}

	@EventHandler
	public void sniperHeadshot(ProjectileHitEvent event)
	{
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		if(event.getEntity() instanceof Arrow)
		{
			if(event.getHitEntity() == null)
				return;
			if(event.getEntity().getLocation().getY() > event.getHitEntity().getLocation().getY()+1.5)
			{
			}
		}
	}

	@EventHandler
	public void gravBomb(ProjectileHitEvent event)
	{
		if(event.getEntity().getType() != EntityType.ENDER_PEARL)
			return;
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		Player player = (Player) event.getEntity().getShooter(); 
		if(main.getMatchManager().getPlayerKit(player) != Kit.GRAVITON)
			return;
		MoveMarker mm = new MoveMarker(event.getEntity().getLocation());
		runGravPull(player, mm);
	}
	
	@EventHandler
	public void cancelGravTp(PlayerTeleportEvent event)
	{
		if(event.getCause() == TeleportCause.ENDER_PEARL && main.getMatchManager().getPlayersAlive().contains(event.getPlayer()) && main.getMatchManager().getPlayerKit(event.getPlayer()) == Kit.GRAVITON)
			event.setCancelled(true);
	}

	public void runGravPull(Player shooter, MoveMarker mm)
	{
		if(mm.timePassedSince() < 5)
		{
			mm.getLocation().getWorld().spawnParticle(Particle.DRAGON_BREATH, mm.getLocation(), 1);
			for(Player player : main.getMatchManager().getPlayersAlive())
			{
				if(player.equals(shooter))
					continue;
				Vector vector = mm.getLocation().toVector().subtract(player.getLocation().toVector());
				if(player.getLocation().distance(mm.getLocation()) < 5)
					player.setVelocity(vector.multiply(0.25));
			}
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					runGravPull(shooter, mm);
				}
			}, 5L);
		}
	}

}
