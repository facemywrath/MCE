package facejup.mce.markers;

import org.bukkit.Location;

import facejup.mce.util.Chat;

public class MoveMarker {

	private Location loc;
	private Long timeMoved;
	
	public MoveMarker(Location loc)
	{
		timeMoved = System.currentTimeMillis();
		this.loc = loc;
	}
	
	public Location getLocation()
	{
		return this.loc;
	}
	
	public int timePassedSince()
	{
		return (int) (((System.currentTimeMillis() - timeMoved)/1000.0));
	}
	
}
