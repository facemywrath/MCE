package facejup.mce.arenas;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;

import facejup.mce.util.Chat;
import facejup.mce.util.Lang;

public class ArenaSign {

	private ConfigurationSection section;
	private Location loc;
	private String name;
	private ArenaManager am;

	public ArenaSign(ArenaManager am, ConfigurationSection section)
	{
		this.section = section;
		this.am = am;
		if(!section.contains("World") || !section.contains("Name") || !section.contains("x") || !section.contains("y") || !section.contains("z"))
			loc = null;
		loc = new Location(Bukkit.getWorld(section.getString("World")), section.getDouble("x"), section.getDouble("y"), section.getDouble("z"));
		name = section.getString("Name");
	}

	public void updateSign()
	{
		if(loc.getBlock().getType() == Material.WALL_SIGN)
		{
			Sign sign = ((Sign) loc.getBlock().getState());
			sign.setLine(0, Chat.translate("&1" + StringUtils.capitaliseAllWords(name.toLowerCase().replaceAll("_", " "))));
			sign.setLine(1, "Size: " + getSize());
			if(am.getMatchManager().votesReceived.containsKey(this))
				sign.setLine(2, "Votes: " + am.getMatchManager().votesReceived.get(this));
			sign.update();
		}
	}

	public String getArenaName()
	{
		return this.name;
	}

	public Location getLocation()
	{
		return loc;
	}

	private String getSize()
	{
		int size = am.getFileControl().getConfig().getConfigurationSection(section.getCurrentPath().substring(0, section.getCurrentPath().lastIndexOf(".")) + ".SpawnPoints").getKeys(false).size();
		if(size >= 0 && size < 12)
			return "Small";
		if(size >= 12 && size < 26)
			return "Medium";
		return "Huge";
	}

}
