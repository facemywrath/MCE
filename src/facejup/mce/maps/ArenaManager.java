package facejup.mce.maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

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
	
	public void createArena(String arenaname)
	{
		FileConfiguration config = fc.getConfig();
		config.set("Arenas." + (getArenaCount() + 1) + ".Name", arenaname);
		fc.save();
	}
	
	public ConfigurationSection getArenaSection(String arenaname)
	{
		FileConfiguration config = fc.getConfig();
		for (String str : config.getConfigurationSection("Arenas").getKeys(false)) {
			if (config.getString("Arenas." + str + ".Name").equalsIgnoreCase(arenaname))
				return config.getConfigurationSection("Arenas." + str);
		}
		return null;
	}
	
	public FileControl getFileControl() 
	{
		return this.fc;
	}
	
	public int getArenaCount()
	{
		return fc.getConfig().getConfigurationSection("Arenas").getKeys(false).size();
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
	
	public int getSpawnCount(ConfigurationSection section) {
		return section.getConfigurationSection("SpawnPoints").getKeys(false).size();
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
