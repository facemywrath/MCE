package facejup.mce.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerKillEvent extends PlayerEvent {
	
	public static HandlerList handlers = new HandlerList();
	
	private Player killer;
	private Player target;
	
	public PlayerKillEvent(Player killer, Player target)
	{
		super(killer);
		this.target = target;
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
