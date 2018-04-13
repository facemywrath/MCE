package facejup.mce.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerEvent;

public class PlayerKillThroughEnvironmentEvent extends PlayerEvent {
	
	public static HandlerList handlers = new HandlerList();
	
	private Player target;
	private DamageCause cause;
	
	public PlayerKillThroughEnvironmentEvent(Player killer, Player target, DamageCause cause)
	{
		super(killer);
		this.cause = cause;
		this.target = target;
	}
	
	public DamageCause getCause()
	{
		return this.cause;
	}
	
	public Player getTarget()
	{
		return this.target;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
