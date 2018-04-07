package facejup.mce.players;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.util.Chat;
import facejup.mce.util.FileControl;

public class UserManager implements Listener {

	private Main main; // Dependency Injection Variable.

	private FileControl fc; // FC containing the Users.yml file

	private HashMap<OfflinePlayer, User> users = new HashMap<>(); // Map storing the player and their user.

	public UserManager(Main main)
	{
		//TODO: Constructor which stores the given variables and loads the users.
		this.main = main;
		this.fc = new FileControl(new File(main.getDataFolder(), "users.yml"));
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					addUser(player);
					main.getMatchManager().setPlayerKit(player, Kit.NONE);
				}
			}
		}, 10L);

		main.getServer().getPluginManager().registerEvents(this, main);
	}

	//EventHandlers

	@EventHandler
	public void playerJoin(PlayerJoinEvent event)
	{
		addUser(event.getPlayer());
		this.main.getMatchManager().setPlayerKit(event.getPlayer(), Kit.NONE);
	}

	//Getters

	public User getUser(OfflinePlayer player)
	{
		addUser(player);
		return users.get(player.getUniqueId());
	}

	public FileControl getFileControl()
	{
		return this.fc;
	}

	public void addUser(Player player) {
		if(!users.containsKey(player))
		{
			users.put(player, new User(this, player));
		}
	}

}
