package facejup.mce.listeners;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.util.concurrent.Service.State;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.events.PlayerKillEvent;
import facejup.mce.events.PlayerKillThroughEnvironmentEvent;
import facejup.mce.players.User;

public class AchievementListeners implements Listener{

	private EventManager em;
	public HashMap<Player, Integer> killsPerLife = new HashMap<>();

	public AchievementListeners(EventManager em) {
		this.em = em;

		em.getMain().getServer().getPluginManager().registerEvents(this, em.getMain());
	}

	@EventHandler
	public void HitterAchievementModifier(EntityDamageByEntityEvent event) {
		if(event.getFinalDamage() == 0)
			return;
		if (event.getDamager() instanceof Player) {
			if (event.getEntity() instanceof Player) {
				if (em.getMain().getMatchManager().isMatchRunning()) {
					Player player = (Player) event.getDamager();
					if (em.getMain().getMatchManager().getPlayerKit(player) != Kit.NONE) {
						User user = em.getMain().getUserManager().getUser(player);
						user.incScore(Achievement.HITTER);
					}
				}
			}
		}
	}

	@EventHandler
	public void BowAchievementModifier(EntityDamageByEntityEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		if(event.getFinalDamage() == 0)
			return;
		if(!(event.getDamager() instanceof Arrow) || !(((Projectile) event.getDamager()).getShooter() instanceof Player))
			return;
		Player damager = null;
		damager = (Player) ((Projectile) event.getDamager()).getShooter();
		if (em.getMain().getMatchManager().isMatchRunning()) {
			if (em.getMain().getMatchManager().getPlayerKit(damager) != Kit.NONE) {
				User user = em.getMain().getUserManager().getUser(damager);
				user.incScore(Achievement.ARCHERY);
			}
		}
	}
	
	@EventHandler
	public void SpreeAchievementModifier(PlayerKillEvent event)
	{
		killsPerLife.put(event.getTarget(), 0);
		if(killsPerLife.containsKey(event.getPlayer()))
		{
			killsPerLife.put(event.getPlayer(), killsPerLife.get(event.getPlayer())+1);
		}
		else
		{
			killsPerLife.put(event.getPlayer(), 1);
		}
		if(killsPerLife.get(event.getPlayer())%3 == 0)
		{
			event.getPlayer().getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 2));
		}
		User user = em.getMain().getUserManager().getUser(event.getPlayer());
		if(user.getScore(Achievement.SPREE) < killsPerLife.get(event.getPlayer()))
		{
			user.incScore(Achievement.SPREE);
		}
	}
	
	@EventHandler
	public void SkylordAchievementModifier(PlayerKillEvent event)
	{
		if(event.getPlayer().getVelocity().getY() < -0.8)
		{
			em.getMain().getUserManager().getUser(event.getPlayer()).incScore(Achievement.SKYLORD);
		}
	}
	
	@EventHandler
	public void SpartaModifier(PlayerKillThroughEnvironmentEvent event)
	{
		if(event.getCause() == DamageCause.FALL || event.getCause() == DamageCause.VOID)
		{
			em.getMain().getUserManager().getUser(event.getPlayer()).incScore(Achievement.THISISSPARTA);
		}
	}
	
	@EventHandler
	public void assassinateModifier(PlayerKillEvent event)
	{
		if(!em.getDeathListeners().lastDamagedBy.containsKey(event.getPlayer()))
		{
			em.getMain().getUserManager().getUser(event.getPlayer()).incScore(Achievement.ASSASSINATE);
		}
	}
	
	@EventHandler
	public void hellfireModifier(PlayerKillThroughEnvironmentEvent event)
	{
		if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.HOT_FLOOR)
		{
			em.getMain().getUserManager().getUser(event.getPlayer()).incScore(Achievement.HELLFIRE);
		}
	}
	
	@EventHandler
	public void fishermanModifier(PlayerFishEvent event)
	{
		if(event.getState() == PlayerFishEvent.State.CAUGHT_FISH)
		{
			if(em.getMain().getMatchManager().isMatchRunning())
				em.getMain().getUserManager().getUser(event.getPlayer()).incScore(Achievement.FISHERMAN);
		}
	}
}
