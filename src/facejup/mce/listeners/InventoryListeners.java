package facejup.mce.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.ItemCreator;

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
	
	@EventHandler
	public void invClick(InventoryClickEvent event)
	{
		if(event.getClickedInventory() == null)
			return;
		Inventory inv = event.getClickedInventory();
		if(!inv.getTitle().equals("Kits"))
			return;
		event.setCancelled(true);
		Kit kit = InventoryBuilder.getKitBySlot(event.getSlot());
		Player player = (Player) event.getWhoClicked();
		if(main.getUserManager().getUser(player) == null)
			return;
		User user = main.getUserManager().getUser(player);
		if(user.hasKit(kit))
		{
			main.getMatchManager().setPlayerDesiredKit(player, kit);
			player.openInventory(InventoryBuilder.createKitInventory(player));
		}
		else
		{
			user.purchaseKit(kit);
			player.openInventory(InventoryBuilder.createKitInventory(player));
		}
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent event)
	{
		if(event.getPlayer().isOp())
			return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void playerPickupItem(PlayerPickupItemEvent event)
	{
		if(event.getPlayer().isOp())
			return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		if(main.getMatchManager().getLives(player) > 0 && main.getMatchManager().getPlayerDesiredKit(player) != Kit.NONE)
		{
			main.getMatchManager().spawnPlayer(player);
		}
	}
	
	//Event Handlers
	@EventHandler
	public void playerInteract(PlayerInteractEvent event)
	{
		//TODO: Open the custom inventory for kit selection.
		if(event.getAction().toString().contains("RIGHT_CLICK") && event.getPlayer().getInventory().getItemInMainHand().equals(ItemCreator.getKitSelector()))
		{
			event.getPlayer().openInventory(InventoryBuilder.createKitInventory(event.getPlayer()));
		}
		if(event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if(main.getUserManager().getUser(event.getPlayer()) != null)
			{
				main.getUserManager().getUser(event.getPlayer()).setCoins(3000);
			}
		}
	}
	

}
