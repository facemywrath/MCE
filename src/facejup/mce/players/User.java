package facejup.mce.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import facejup.mce.enums.Kit;

public class User {

	private UserManager um; // Dependency Injection Variable.

	private ConfigurationSection section; // Section storing the players information.
	private Player player; // The player.

	public User(UserManager um, Player player)
	{
		//TODO: Constructor which loads and saves the information from the file.
		this.player = player;
		this.um = um;
		if(um.getFileControl() != null && um.getFileControl().getFile().exists()) // Checks to make sure the file exists.
		{
			FileConfiguration config = um.getFileControl().getConfig(); // Get the Users.yml config
			if(config.contains("Users." + player.getUniqueId())) // Checks if the player is in the config.
			{
				// Store their section if they are.
				this.section = config.getConfigurationSection("Users." + player.getUniqueId());
			}
			else
			{
				// Otherwise, set their default information and save. Then store their section.
				config.set("Users." + player.getUniqueId() + ".Name", player.getName());
				config.set("Users." + player.getUniqueId() + ".Coins", 0);
				config.set("Users." + player.getUniqueId() + ".Kills", 0);
				config.set("Users." + player.getUniqueId() + ".Deaths", 0);
				config.set("Users." + player.getUniqueId() + ".Kits", Arrays.asList("NONE", "ARCHER", "WARRIOR", "GUARD"));
				um.getFileControl().save(config);
				this.section = config.getConfigurationSection("Users." + player.getUniqueId());
			}
		}
	}
	
	public void unlockKit(Kit kit)
	{
		//TODO: Unlocks a kit for a player for future use.
		if(section.contains("Kits"))
		{
			if(section.getStringList("Kits").contains(kit.toString()))
				return;
			List<String> kits = section.getStringList("Kits");
			kits.add(kit.toString());
			section.set("Kits", kits);
			um.getFileControl().save();
		}
		else
		{
			section.set("Kits", Arrays.asList("NONE", "ARCHER", "WARRIOR", "GUARD"));
		}
	}
	
	public void purchaseKit(Kit kit)
	{
		int coins = getCoins();
		if(coins < kit.cost)
			return;
		setCoins(coins - kit.cost);
		unlockKit(kit);
	}
	
	public void setCoins(int i)
	{
		section.set("Coins", i);
		um.getFileControl().save();
	}
	
	public int getCoins()
	{
		if(section.contains("Coins"))
			return section.getInt("Coins");
		return 0;
	}
	
	public void incKills() {
		if(section.contains("Kills"))
			section.set("Kills", (section.getInt("Kills")) + 1);
		else
			section.set("Kills", 1);
	}
	
	public void incDeaths() {
		if(section.contains("Deaths"))
			section.set("Deaths", (section.getInt("Deaths")) + 1);
		else
			section.set("Deaths", 1);
	}
	
	public int getKills()
	{
		if(section.contains("Kills"))
			return section.getInt("Kills");
		return 0;
	}
	
	public int getDeaths()
	{
		if(section.contains("Deaths"))
			return section.getInt("Deaths");
		return 0;
	}
	
	public boolean hasKit(Kit kit)
	{
		if(getKits().contains(kit))
			return true;
		return false;
	}
	
	public List<Kit> getKits()
	{
		//TODO: Retrieve the players kits from their configuration section.
		if(section.contains("Kits"))
		{
			List<Kit> kits = new ArrayList<>();
			for(String str : section.getStringList("Kits"))
			{
				kits.add(Kit.valueOf(str));
			}
			return kits;
		}
		else
		{
			section.set("Kits", Arrays.asList("NONE", "ARCHER", "WARRIOR", "GUARD"));
		}
		return Arrays.asList(Kit.NONE, Kit.ARCHER, Kit.WARRIOR, Kit.GUARD);
	}

}
