package facejup.mce.arenas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class Arena {
	
	private ArenaManager am;
	
	private ConfigurationSection section;
	private World world;
	private Location bound1;
	private Location bound2;
	
	public Arena(ArenaManager am, ConfigurationSection section)
	{
		this.am = am;
		this.section = section;
		this.world = Bukkit.getWorld(section.getString("World"));
		this.bound1 = new Location(world, section.getDouble("Bound1.x"), section.getDouble("Bound1.y"), section.getDouble("Bound1.z"));
		this.bound2 = new Location(world, section.getDouble("Bound2.x"), section.getDouble("Bound2.y"), section.getDouble("Bound2.z"));
	}
	
	public String getName()
	{
		if(section.contains("Name"))
			return section.getString("Name");
		return null;
	}
	
	public World getWorld()
	{
		return this.world;
	}
	
	public Location getRandomSpawn()
	{
		List<Location> currentSpawnPoints = getSpawnPoints();
		List<Location> sortedSpawnPoints = currentSpawnPoints.stream().sorted((loc1, loc2) -> Double.compare(getDistanceToNearestPlayer(loc1),getDistanceToNearestPlayer(loc2))).collect(Collectors.toList());
		currentSpawnPoints = Lists.reverse(sortedSpawnPoints);
		return currentSpawnPoints.get(0);
	}
	
	public List<Location> getSpawnPoints()
	{
		List<Location> locs = new ArrayList<>();
		if(section.contains("SpawnPoints"))
		{
			for(String str : section.getConfigurationSection("SpawnPoints").getKeys(false))
			{
				String key = "SpawnPoints." + str;
				if(section.contains(key + ".x") && section.contains(key + ".y") && section.contains(key + ".z"))
				{
					locs.add(new Location(world, section.getDouble(key + ".x"), section.getDouble(key + ".y"), section.getDouble(key + ".z")));
				}
			}
		}
		
		return locs;
	}
	
	private double getDistanceToNearestPlayer(Location loc)
	{
		double i = Double.MAX_VALUE;
		for(Player player : am.getMatchManager().getPlayersAlive())
		{
			if(player.getWorld().equals(loc.getWorld()) && loc.distance(player.getLocation()) < i)
				i = loc.distance(player.getLocation());
		}
		return i;
	}

}
