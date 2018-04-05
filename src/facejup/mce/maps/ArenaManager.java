package facejup.mce.maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import facejup.mce.main.MatchManager;
import facejup.mce.util.FileControl;
import facejup.mce.util.Numbers;

public class ArenaManager {
	
	private MatchManager mm;
	
	private FileControl fc;
	private Arena arena;
	
	public ArenaManager(MatchManager mm)
	{
		this.mm = mm;
		fc = new FileControl(new File(mm.getMain().getDataFolder(), "maps.yml"));
	}
	
	public MatchManager getMatchManager()
	{
		return this.mm;
	}
	
	public int getMaxSpawnPoints()
	{
		List<Integer> spawns = new ArrayList<>();
		for(String str : fc.getConfig().getConfigurationSection("Arenas").getKeys(false))
		{
			spawns.add(fc.getConfig().getConfigurationSection("Arenas." + str).getKeys(false).size());
		}
		Optional<Integer> i = spawns.stream().max(Integer::compare);
		return i.get();
	}
	
	public Arena getArena()
	{
		return this.arena;
	}
	
	public Arena getRandomArena()
	{
		ConfigurationSection section = fc.getConfig().getConfigurationSection("Arenas");
		this.arena = new Arena(this, section.getConfigurationSection("" + Numbers.getRandom(0, section.getKeys(false).size())));
		return this.arena;
	}

}