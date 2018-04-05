package facejup.mce.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import facejup.mce.kits.Kit;

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
