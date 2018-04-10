package facejup.mce.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
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
			}
			event.getEntity().remove();
		}
	}

	@EventHandler
	public void fishermanHook(PlayerFishEvent event)
	{
		event.getPlayer().setCooldown(Material.FISHING_ROD, 40);
	}

}
