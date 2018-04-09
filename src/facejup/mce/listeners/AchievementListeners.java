package facejup.mce.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.events.PlayerKillEvent;
import facejup.mce.players.User;

public class AchievementListeners implements Listener{

	private EventManager em;
	private HashMap<Player, Integer> killsPerLife = new HashMap<>();

	public AchievementListeners(EventManager em) {
		this.em = em;

		em.getMain().getServer().getPluginManager().registerEvents(this, em.getMain());
	}

	@EventHandler
	public void HitterAchievementModifier(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (event.getEntity() instanceof Player) {
				if (em.getMain().getMatchManager().isMatchRunning()) {
					Player player = (Player) event.getDamager();
					if (em.getMain().getMatchManager().getPlayerKit(player) != Kit.NONE) {
						User user = em.getMain().getUserManager().getUser(player);
						user.incScore(Achievement.IRON_HITTER);
						user.incScore(Achievement.GOLD_HITTER);
						user.incScore(Achievement.DIAMOND_HITTER);
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
		if(!(event.getDamager() instanceof Projectile) || !(((Projectile) event.getDamager()).getShooter() instanceof Player))
			return;
		Player damager = null;
		damager = (Player) ((Projectile) event.getDamager()).getShooter();
		if (em.getMain().getMatchManager().isMatchRunning()) {
			if (em.getMain().getMatchManager().getPlayerKit(damager) != Kit.NONE) {
				User user = em.getMain().getUserManager().getUser(damager);
				user.incScore(Achievement.IRON_BOW);
				user.incScore(Achievement.GOLD_BOW);
				user.incScore(Achievement.DIAMOND_BOW);
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
		User user = em.getMain().getUserManager().getUser(event.getPlayer());
		if(user.getScore(Achievement.IMMORTAL) < killsPerLife.get(event.getPlayer()))
		{
			user.incScore(Achievement.SPREE);
			user.incScore(Achievement.UNKILLABLE);
			user.incScore(Achievement.IMMORTAL);
		}
		
	}
}
