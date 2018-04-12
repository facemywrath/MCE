package facejup.mce.markers;

import org.bukkit.entity.Player;

public class DamageMarker {
	
	private Long systemTimeOnHit;
	private Player damager;
	
	public DamageMarker(Player player, Long time)
	{
		damager = player;
		systemTimeOnHit = time;
	}
	
	public Long getTime()
	{
		return systemTimeOnHit;
	}
	
	public Player getDamager()
	{
		return this.damager;
	}

}
