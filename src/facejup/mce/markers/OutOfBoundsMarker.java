package facejup.mce.markers;

import org.bukkit.entity.Player;

import facejup.mce.arenas.Arena;

public class OutOfBoundsMarker {
	
	public Player oob;
	public Long time;
	
	public OutOfBoundsMarker(Player player)
	{
		this.oob = player;
		this.time = System.currentTimeMillis();
	}
	
	public int timePassedSince()
	{
		return (int) (((System.currentTimeMillis() - time)/1000.0));
	}

}
