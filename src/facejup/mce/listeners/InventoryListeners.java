package facejup.mce.listeners;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import facejup.mce.util.Chat;
import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;

@SuppressWarnings("deprecation")
public class InventoryListeners<PlayerItemSwapHandEvent> implements Listener {

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
		if(event.getClickedInventory().getType() == InventoryType.PLAYER && (event.getSlot() == 40 || event.getSlot() == 8))
			event.setCancelled(true);
		if(event.getCurrentItem() == null)
			return;
		Inventory inv = event.getClickedInventory();
		if(inv.getTitle().equals("Kits")) {
			event.setCancelled(true);
			Kit kit = InventoryBuilder.getKitBySlot(event.getSlot());
			Player player = (Player) event.getWhoClicked();
			boolean matchrunning = main.getMatchManager().isMatchRunning();
			User user = main.getUserManager().getUser(player);
			if(user.hasKit(kit))
			{
				if(kit == Kit.NONE && matchrunning)
					return;
				main.getMatchManager().setPlayerDesiredKit(player, kit);
				if(kit != Kit.NONE)
					player.sendMessage(Chat.translate(Lang.Tag + "&aYou will spawn with &bKit " + StringUtils.capitalize(kit.toString().toLowerCase())));
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
	public void itemDamage(PlayerItemDamageEvent event)
	{
		if(em.getMain().getMatchManager().isMatchRunning() && em.getMain().getMatchManager().getPlayersAlive().contains(event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void swapItem(PlayerSwapHandItemsEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	public void blockBreak(BlockBreakEvent event)
	{
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void blockPlace(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		if(!main.getMatchManager().isMatchRunning())
		{
			event.setCancelled(true);
			return;
		}
		if(!main.getMatchManager().getPlayersAlive().contains(player))
		{
			event.setCancelled(true);
			return;
		}
		if(event.getBlock().getType() != Material.OBSIDIAN)
		{
			event.setCancelled(true);
			return;
		}
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				event.getBlock().setType(Material.AIR);
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
				{
					public void run()
					{
						if(main.getMatchManager().isMatchRunning() && main.getMatchManager().getPlayersAlive().contains(player))
						{
							player.getInventory().addItem(new ItemCreator(Material.OBSIDIAN).setDisplayname("&aSpecial Stone").setLore(Arrays.asList("&5&lA reward for your architecture.", "&7&oThese blocks automatically", "&7&odespawn after 10 seconds.")).getItem());
						}
					}
				}, 30L);
			}
		}, 200L);
	}

	@EventHandler
	public void playerDropItem(PlayerDropItemEvent event)
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
		if(event.getAction().toString().contains("LEFT_CLICK"))
		{
			Player player = event.getPlayer();
			if(main.getMatchManager().isMatchRunning() && main.getMatchManager().getPlayersAlive().contains(player) && main.getMatchManager().getPlayerKit(player) == Kit.SHADE && main.getMatchManager().isHidden(player))
			{
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.removePotionEffect(PotionEffectType.REGENERATION);
				for(Player player2 : Bukkit.getOnlinePlayers())
				{
					if(!(player.equals(player2)))
					{
						player2.showPlayer(player);
					}
				}
			}
			main.getMatchManager().updateMoveMarker(event.getPlayer());
		}
		if(event.getItem() != null && (event.getItem().getType() == Material.POTION || event.getItem().getType() == Material.COOKED_BEEF))
		{
			if(event.getPlayer().getHealth() < event.getPlayer().getMaxHealth())
			{
				for(int i = 0; i < 8; i++)
				{
					if(event.getPlayer().getHealth() > 0 && event.getPlayer().getHealth()+1 < event.getPlayer().getMaxHealth())
					{
						event.getPlayer().setHealth(event.getPlayer().getHealth() + 1);
					}
				}
				event.getPlayer().setFireTicks(0);
				ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
				if(item.getAmount() > 1)
					item.setAmount(item.getAmount()-1);
				else
					event.getPlayer().getInventory().setItemInMainHand(null);
			}
		}
	}


}
