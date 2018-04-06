package facejup.mce.players;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.util.FileControl;

public class UserManager implements Listener {
	
	private Main main; // Dependency Injection Variable.
	
	private FileControl fc; // FC containing the Users.yml file
	
	private HashMap<Player, User> users = new HashMap<>(); // Map storing the player and their user.
	
	public UserManager(Main main)
	{
		//TODO: Constructor which stores the given variables and loads the users.
		this.main = main;
		this.fc = new FileControl(new File(main.getDataFolder(), "users.yml"));
		for(Player player : Bukkit.getOnlinePlayers())
		{
			users.put(player, new User(this, player));
		}
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	//EventHandlers
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		if(!users.containsKey(event.getPlayer()))
		{
			users.put(event.getPlayer(), new User(this, event.getPlayer()));
		}
		this.main.getMatchManager().setPlayerKit(event.getPlayer(), Kit.NONE);
	}
	
	//Getters
	
	public User getUser(Player player)
	{
		if(users.containsKey(player))
			return users.get(player);
		return null;	
	}
	
	public FileControl getFileControl()
	{
		return this.fc;
	}

}
