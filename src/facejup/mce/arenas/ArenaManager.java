package facejup.mce.arenas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.World;
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

	public void createArena(String arenaname, World w)
	{
		FileConfiguration config = fc.getConfig();
		int count = getArenaCount();
		config.set("Arenas." + (count+1) + ".World", w.getName());
		config.set("Arenas." + (count+1) + ".Name", arenaname);
		fc.save(config);
	}

	public ConfigurationSection getArenaSection(String arenaname)
	{
		for (String str : fc.getConfig().getConfigurationSection("Arenas").getKeys(false)) {
			if (fc.getConfig().getString("Arenas." + str + ".Name").equalsIgnoreCase(arenaname))
				return fc.getConfig().getConfigurationSection("Arenas." + str);
		}
		return null;
	}

	public List<String> getArenaList()
	{
		List<String> arenas = new ArrayList<>();
		for (String s : fc.getConfig().getConfigurationSection("Arenas").getKeys(false)) {
			if (fc.getConfig().contains("Arenas." + s + ".Name"))
				arenas.add(fc.getConfig().getString("Arenas." + s + ".Name"));
		}
		return arenas;
	}
	
	public FileControl getFileControl() 
	{
		return this.fc;
	}

	public int getArenaCount()
	{
		if(fc.getConfig().contains("Arenas"))
			return fc.getConfig().getConfigurationSection("Arenas").getKeys(false).size();
		return 0;
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
		if(section.contains("SpawnPoints"))
			return section.getConfigurationSection("SpawnPoints").getKeys(false).size();
		return 0;
	}

	public Arena getArena()
	{
		return this.arena;
	}

	public Arena getRandomArena(int playercount)
	{
		List<ConfigurationSection> arenas = getArenasBigEnough(playercount);
		if(arenas.isEmpty())
		{
			return null;
		}
		ConfigurationSection section = fc.getConfig().getConfigurationSection("Arenas");
		this.arena = new Arena(this, arenas.get(Numbers.getRandom(0, arenas.size()-1)));
		return this.arena;
	}

	public List<ConfigurationSection> getArenasBigEnough(int size)
	{
		List<ConfigurationSection> arenas = new ArrayList<>();
		if(!fc.getConfig().contains("Arenas"))
			return arenas;
		for(String str : fc.getConfig().getConfigurationSection("Arenas").getKeys(false))
		{
			int spawnamts = fc.getConfig().getConfigurationSection("Arenas." + str + ".SpawnPoints").getKeys(false).size();
			if(spawnamts >= size && spawnamts <= size*3);
				arenas.add(fc.getConfig().getConfigurationSection("Arenas." + str));
		}
		return arenas;
	}


}
