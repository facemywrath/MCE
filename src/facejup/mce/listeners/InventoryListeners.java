package facejup.mce.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.ItemCreator;

@SuppressWarnings("deprecation")
public class InventoryListeners implements Listener {

	private Main main; // Dependency Injection Variable
	@SuppressWarnings("unused")
	private EventManager em; // Other Dependency Injection Variable

	public InventoryListeners(EventManager eventManager)
	{
		//Constructor which saves the dep inj and registers this instance as a listener.
		this.main = eventManager.getMain();
		this.em = eventManager;
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void invClick(InventoryClickEvent event)
	{
		if(event.getClickedInventory() == null)
			return;
		if(event.getCurrentItem() == null)
			return;
		Inventory inv = event.getClickedInventory();
		if(inv.getTitle().equals("Kits")) {
			event.setCancelled(true);
			Kit kit = InventoryBuilder.getKitBySlot(event.getSlot());
			Player player = (Player) event.getWhoClicked();
			if(main.getUserManager().getUser(player) == null)
				return;
			boolean matchrunning = main.getMatchManager().isMatchRunning();
			User user = main.getUserManager().getUser(player);
			if(user.hasKit(kit))
			{
				if(kit == Kit.NONE && matchrunning)
					return;
				main.getMatchManager().setPlayerDesiredKit(player, kit);
				player.openInventory(InventoryBuilder.createKitInventory(player));
			}
			else
			{
				if(kit.achcost == null)
				{
					user.purchaseKit(kit);
					player.openInventory(InventoryBuilder.createKitInventory(player));
				}
				else
				{
					player.openInventory(InventoryBuilder.createAchievementInventory(player));
				}
			}
		} else if (inv.getTitle().equals("Achievements")) {
			event.setCancelled(true);
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
	//Event Handlers
	@EventHandler
	public void playerInteract(PlayerInteractEvent event)
	{
		//Open the custom inventory for kit selection.
		if(event.getAction().toString().contains("RIGHT_CLICK") && event.getPlayer().getInventory().getItemInMainHand().equals(ItemCreator.getKitSelector()))
		{
			event.getPlayer().openInventory(InventoryBuilder.createKitInventory(event.getPlayer()));
		}
		if(event.getItem() != null && (event.getItem().getType() == Material.POTION || event.getItem().getType() == Material.COOKED_BEEF))
		{
			if(event.getPlayer().getHealth() < event.getPlayer().getMaxHealth())
			{
				for(int i = 0; i < 8; i++)
				{
					if(event.getPlayer().getHealth() > 0 && event.getPlayer().getHealth() < event.getPlayer().getMaxHealth())
					{
						event.getPlayer().setHealth(event.getPlayer().getHealth() + 1);
					}
				}
				if(event.getItem().getAmount() > 1)
					event.getItem().setAmount(event.getItem().getAmount()-1);
				else
					event.getPlayer().setItemInHand(null);
			}
		}
	}


}
