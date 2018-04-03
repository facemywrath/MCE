package facejup.mce.main;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import facejup.mce.inventory.ItemCreator;
import facejup.mce.storage.EndTimer;
import facejup.mce.storage.StartTimer;

public class Main extends JavaPlugin {
	
	private MatchManager mm; // The object responsible for dealing with the beginning and ending of matches.
	
	public void onEnable()
	{
		mm = new MatchManager(this); // Instantiation. Constructor just starts the countdown.
	}

}
