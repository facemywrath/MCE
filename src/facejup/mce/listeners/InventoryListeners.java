package facejup.mce.listeners;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.players.User;

public class InventoryListeners implements Listener {
	
	private Main main; // Dependency Injection Variable
	private EventManager em; // Other Dependency Injection Variable
	
	public InventoryListeners(EventManager eventManager)
	{
		//TODO: Constructor which saves the dep inj and registers this instance as a listener.
		this.main = eventManager.getMain();
		this.em = eventManager;
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	//Event Handlers
	@EventHandler
	public void playerInteract(PlayerInteractEvent event)
	{
		//TODO: Open the custom inventory for kit selection.
		event.getPlayer().sendMessage(main.getUserManager().getUser(event.getPlayer()).getKits().stream().map(Kit::toString).collect(Collectors.joining(", ")));
	}
	

}
