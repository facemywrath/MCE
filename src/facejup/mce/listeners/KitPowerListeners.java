package facejup.mce.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Statistic;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import facejup.mce.enums.Kit;
import facejup.mce.events.PlayerKillEvent;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import facejup.mce.util.Chat;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Marker;
import facejup.mce.util.Numbers;
import think.rpgitems.item.ItemManager;

public class KitPowerListeners implements Listener {

	private EventManager em;
	private Main main;

	public List<Marker<Location>> ignitedBlocks = new ArrayList<>();

	public HashMap<Player, Inventory> stolenItems = new HashMap<>();

	public KitPowerListeners(EventManager em)
	{
		this.em = em;
		this.main = em.getMain();
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void archerBowCooldown(ProjectileLaunchEvent event)
	{
		if(event.getEntity() instanceof Arrow && (event.getEntity().getShooter() instanceof Player))
		{
			Player player = (Player) event.getEntity().getShooter();
			if(main.getMatchManager().getPlayerKit(player) == Kit.ARCHER)
			{
				player.setCooldown(Material.BOW, 10);
			}
		}
	}

	@EventHandler
	public void negateNinjaFallDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		User user = main.getUserManager().getUser(player);
		if(main.getMatchManager().getPlayerKit(player) == Kit.NINJA && main.getMatchManager().getLives(player) > 0)
		{
			if(event.getCause() == DamageCause.FALL)
			{
				event.setDamage(event.getDamage()/2.0);
			}
		}
	}

	@EventHandler
	public void negateNinjaTeleportDamage(PlayerTeleportEvent event)
	{
		if(event.getCause() == TeleportCause.ENDER_PEARL && main.getMatchManager().getPlayerKit(event.getPlayer()) == Kit.NINJA)
		{
			event.setCancelled(true);
			//	event.getPlayer().setNoDamageTicks(1);
			event.getPlayer().teleport(event.getTo());
		}
	}

	@EventHandler
	public void projectileLaunch(ProjectileLaunchEvent event)
	{
		if(event.getEntity().getType() == EntityType.FISHING_HOOK && ((Projectile) event.getEntity()).getShooter() instanceof Player)
		{
			event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(2));
		}
	}

	@EventHandler
	public void projectileHit(ProjectileHitEvent event)
	{
		if(event.getEntityType() == EntityType.FISHING_HOOK)
		{
			if(event.getEntity().getShooter() instanceof Player && event.getHitEntity() instanceof Player)
			{
				Player player = (Player) event.getEntity().getShooter();
				Marker<Player> marker = new Marker<Player>(player);
				Player target = (Player) event.getHitEntity();
				main.getEventManager().getDeathListeners().setLastDamagedBy(target, marker);
				target.damage(1);
				event.getHitEntity().teleport(player);
				player.setCooldown(Material.FISHING_ROD, 40);
			}
			event.getEntity().remove();
		}
	}

	@EventHandler
	public void harpyFlight(PlayerToggleSprintEvent event)
	{
		if(event.isSprinting())
		{
			Player player = event.getPlayer();
			if(main.getMatchManager().getPlayerKit(player) == Kit.HARPY)
			{
				float stamina = player.getLevel();
				if(stamina > 0)
				{
					main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							runHarpyFlight(player);
						}
					}, 1L);
				}
			}
		}
	}

	public void runHarpyFlight(Player player)
	{
		if(player.isSprinting())
		{
			if(main.getMatchManager().getPlayerKit(player) == Kit.HARPY)
			{
				float stamina = player.getLevel();
				if(stamina > 0)
				{
					player.setVelocity(player.getLocation().getDirection().multiply(0.4));
					int red = 1;
					if(player.hasPotionEffect(PotionEffectType.SLOW))
						red = 2+player.getPotionEffect(PotionEffectType.SLOW).getAmplifier();
					if(stamina-red < 0)
						stamina = 0;
					else
						stamina -= red;
					player.setExp((float) (stamina/100.0));
					player.setLevel((int) stamina);
					main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							runHarpyFlight(player);
						}
					}, 2L);
				}
			}
		}
	}

	@EventHandler
	public void negateFallDamageHarpy(EntityDamageEvent event)
	{
		if(event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player)
		{
			if(main.getMatchManager().getPlayerKit((Player) event.getEntity()) == Kit.HARPY)
			{
				event.setDamage(event.getDamage()/4);
			}
		}
	}

	@EventHandler
	public void sniperHeadshot(ProjectileHitEvent event)
	{
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		if(event.getEntity() instanceof Arrow)
		{
			if(event.getHitEntity() == null)
				return;
			if(event.getEntity().getLocation().getY() > event.getHitEntity().getLocation().getY()+1.5)
			{
			}
		}
	}

	@EventHandler
	public void gravBomb(ProjectileHitEvent event)
	{
		if(event.getEntity().getType() != EntityType.ENDER_PEARL)
			return;
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		Player player = (Player) event.getEntity().getShooter(); 
		if(main.getMatchManager().getPlayerKit(player) != Kit.GRAVITON)
			return;
		Marker<Location> mm = new Marker<Location>(event.getEntity().getLocation());
		runGravPull(player, mm);
	}

	@EventHandler
	public void cancelGravTp(PlayerTeleportEvent event)
	{
		if(event.getCause() == TeleportCause.ENDER_PEARL && main.getMatchManager().getPlayersAlive().contains(event.getPlayer()) && main.getMatchManager().getPlayerKit(event.getPlayer()) == Kit.GRAVITON)
			event.setCancelled(true);
	}

	public void runGravPull(Player shooter, Marker<Location> mm)
	{
		if(mm.getSecondsPassedSince() < 5)
		{
			mm.getItem().getWorld().spawnParticle(Particle.DRAGON_BREATH, mm.getItem(), 1);
			for(Player player : main.getMatchManager().getPlayersAlive())
			{
				if(player.equals(shooter))
					continue;
				Vector vector = mm.getItem().toVector().subtract(player.getLocation().toVector());
				if(player.getLocation().distance(mm.getItem()) < 5)
					player.setVelocity(vector.multiply(0.25));
			}
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					runGravPull(shooter, mm);
				}
			}, 5L);
		}
	}

	@EventHandler
	public void gravbombCooldown(ProjectileLaunchEvent event)
	{
		if(event.getEntity() instanceof EnderPearl && event.getEntity().getShooter() instanceof Player && main.getMatchManager().getPlayersAlive().contains((Player) event.getEntity().getShooter()) && main.getMatchManager().getPlayerKit((Player) event.getEntity().getShooter()) == Kit.GRAVITON)
			((Player) event.getEntity().getShooter()).setCooldown(Material.ENDER_PEARL, 200);
	}

	@EventHandler
	public void gravityToggle(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) != Kit.GRAVITON)
			return;
		Material toggleOn = Material.SLIME_BALL;
		Material toggleOff = Material.MAGMA_CREAM;
		if(player.getInventory().getItemInMainHand().getType() == toggleOn && player.getLevel() > 0)
		{
			event.setCancelled(true);
			player.getInventory().setItemInHand(new ItemCreator(toggleOff).setDisplayname("&9Toggle Levitation Off").getItem());
			if(!player.hasPotionEffect(PotionEffectType.LEVITATION))
				player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 30000, 2));
		}
		else if(player.getInventory().getItemInHand().getType() == toggleOff)
		{
			event.setCancelled(true);
			player.getInventory().setItemInHand(new ItemCreator(toggleOn).setDisplayname("&9Toggle Levitation On").getItem());
			if(player.hasPotionEffect(PotionEffectType.LEVITATION))
				player.removePotionEffect(PotionEffectType.LEVITATION);
		}
	}


	@EventHandler
	public void cancelIgnite(BlockIgniteEvent event)
	{
		ignitedBlocks.add(new Marker<Location>(event.getBlock().getLocation()));
	}

	@EventHandler
	public void stopDemonFireDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		if(main.getMatchManager().getPlayersAlive().contains((Player) event.getEntity()) && main.getMatchManager().getPlayerKit(player) == Kit.DEMON)
		{
			if(event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK || event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.HOT_FLOOR)
			{
				event.setCancelled(true);
			}
		}
	}

	//@EventHandler
	public void stopDemonFireTick(EntityCombustEvent event)
	{
		if(event.getEntityType() != EntityType.PLAYER)
			return;
		Player player =  (Player) event.getEntity();
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) == Kit.DEMON)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void demonLeap(PlayerStatisticIncrementEvent event)
	{
		if(event.getStatistic() != Statistic.JUMP)
			return;
		Player player =  event.getPlayer();
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) == Kit.DEMON)
		{
			if(player.isSneaking() && player.getLevel() == 100)
			{
				player.setLevel(0);
				player.setExp(0);
				callFireFall(player.getLocation(), 1);
				player.setVelocity(player.getLocation().getDirection().multiply(2));
			}
		}
	}

	public void callFireFall(Location loc, int radius)
	{
		for(int x = -1*radius; x <= radius; x++)
		{
			for(int z = -1*radius; z <= radius; z++)
			{
				Location tempLoc = loc.clone().add(new Vector(x,0,z));
				if(tempLoc.distance(loc) < (radius+0.5) && tempLoc.distance(loc) > (radius-0.5))
				{
					if(tempLoc.getBlock().getType() == Material.AIR)
					{
						tempLoc.getWorld().spawnFallingBlock(tempLoc, Material.FIRE, (byte) 0);
					}
				}
			}
		}
		if(radius < 5)
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					callFireFall(loc, radius+1);
				}
			}, 5L);
	}

	@EventHandler
	public void removeFireAndIce(EntityChangeBlockEvent event)
	{
		if(event.getTo() == Material.FIRE)
		{
			ignitedBlocks.add(new Marker<Location>(event.getBlock().getLocation()));
		}
	}

	@EventHandler
	public void potionVelocity(ProjectileLaunchEvent event)
	{
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		Player player = (Player) event.getEntity().getShooter();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) == Kit.MAGE)
		{
			if(event.getEntity() instanceof SplashPotion)
			{
				event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(2));
			}
		}
	}

	@EventHandler
	public void gunnerGrenadeSpeed2(EntityBlockFormEvent event)
	{
		if(event.getEntity() instanceof TNTPrimed)
		{
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
			{
				public void run()
				{
					((TNTPrimed) event.getEntity()).setFuseTicks(20);
				}
			}, 3L);
		}
	}

	@EventHandler
	public void cancelGunnerKnockback(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Projectile) || !(((Projectile) event.getDamager()).getShooter() instanceof Player))
			return;
		Player player = (Player) ((Projectile) event.getDamager()).getShooter();
		if(!(event.getEntity() instanceof Player))
			return;
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) == Kit.GUNNER)
		{
			event.setCancelled(true);
			((Player) event.getEntity()).damage(Numbers.getRandom(2, 4));
		}
	}

	@EventHandler
	public void gunnerGrenadeSpeed(ExplosionPrimeEvent event)
	{
		if(event.getEntity() instanceof TNTPrimed)
		{
			event.setFire(true);
		}
	}

	@EventHandler
	public void tricksterTeleport(ProjectileHitEvent event)
	{
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		Player player = (Player) event.getEntity().getShooter();
		if(!(event.getHitEntity() != null && event.getHitEntity() instanceof Player))
			return;
		Player target = (Player) event.getHitEntity();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) == Kit.TRICKSTER)
		{
			Location loc = player.getLocation().clone();
			player.teleport(target);
			target.teleport(loc);
		}

	}

	@EventHandler
	public void tricksterBowCooldown(ProjectileLaunchEvent event)
	{
		if(!(event.getEntity().getShooter() instanceof Player))
			return;
		Player player = (Player) event.getEntity().getShooter();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) == Kit.TRICKSTER)
		{
			player.setCooldown(Material.BOW, 80);
		}


	}

	@EventHandler
	public void tricksterInvis(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.FIREBALL && ItemManager.toRPGItem(player.getInventory().getItemInMainHand()) != null)
		{
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
			{
				event.setCancelled(true);
				if(!main.getMatchManager().isHidden(player) && player.getCooldown(Material.FIREBALL) == 0)
				{
					main.getMatchManager().hidePlayer(player);
					player.setCooldown(Material.FIREBALL, 60);
					main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							if(main.getMatchManager().isHidden(player))
								main.getMatchManager().showPlayer(player);
						}
					}, 60L);
				}
			}
		}
	}

	@EventHandler
	public void upgraderUpgrade(PlayerKillEvent event)
	{
		Player killer = event.getPlayer();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(killer))
			return;
		if(main.getMatchManager().getPlayerKit(killer) != Kit.UPGRADER)
			return;
		upgradeArmor(killer);
		if(Numbers.getRandom(0, 2) == 2)
			upgradeArmor(killer);
	}

	public void upgradeArmor(Player player)
	{
		int random = Numbers.getRandom(0, 4);
		switch(random)
		{
		case 0:
			player.getInventory().setItemInMainHand(getNextLevel(player.getInventory().getItemInMainHand()));
			break;
		case 1:
			player.getInventory().setHelmet(getNextLevel(player.getInventory().getHelmet()));
			break;
		case 2:
			player.getInventory().setChestplate(getNextLevel(player.getInventory().getChestplate()));
			break;
		case 3:
			player.getInventory().setLeggings(getNextLevel(player.getInventory().getLeggings()));
			break;
		case 4:
			player.getInventory().setBoots(getNextLevel(player.getInventory().getBoots()));
			break;
		}
	}

	public ItemStack getNextLevel(ItemStack item)
	{
		Material mat = item.getType();
		String materialtype = mat.toString().substring(0, mat.toString().indexOf('_'));
		String itemtype = mat.toString().substring(mat.toString().indexOf('_')+1, mat.toString().length());
		String materialName = "";
		switch(materialtype)
		{
		case "WOOD":
			materialName = "STONE_" + itemtype;
			break;
		case "LEATHER":
			materialName = "CHAINMAIL_" + itemtype;
			break;
		case "STONE":
			materialName = "IRON_" + itemtype;
			break;
		case "IRON":
			if(!itemtype.equals("SWORD"))
				materialName = "DIAMOND_" + itemtype;
			break;
		case "CHAINMAIL":
			materialName = "IRON_" + itemtype;
			break;
		}
		if(Material.getMaterial(materialName) != null)
			return new ItemStack(Material.getMaterial(materialName));
		return item;
	}

	@EventHandler
	public void yetiSlowListener(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player))
			return;
		Player killer = (Player) event.getDamager();
		Player target = (Player) event.getEntity();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(killer))
			return;
		if(main.getMatchManager().getPlayerKit(killer) != Kit.YETI)
			return;
		addStack(target, PotionEffectType.SLOW, 60, 2);
	}

	@EventHandler
	public void goblinTheftListener(EntityDamageByEntityEvent event)
	{
		if(event.getFinalDamage() == 0)
			return;
		if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player))
			return;
		Player damager = (Player) event.getDamager();
		Player target = (Player) event.getEntity();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(damager))
			return;
		if(main.getMatchManager().getPlayerKit(target) == Kit.GOBLIN)
		{
			if(target.hasPotionEffect(PotionEffectType.SPEED))
				target.removePotionEffect(PotionEffectType.SPEED);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1));
		}
		if(main.getMatchManager().getPlayerKit(damager) != Kit.GOBLIN)
			return;
		if(Numbers.getRandom(0, 4) != 4)
			return;
		int slot = getRandomItemSlot(target);
		if(slot > -1)
		{
			ItemStack item = target.getInventory().getItem(slot);
			if(item == null)
				return;
			damager.getInventory().addItem(new ItemCreator(item).setAmount(1).getItem());
			damager.sendMessage(Chat.translate("&8You stole 1 &7" + ItemCreator.formatItemName(item)));
			target.sendMessage(Chat.translate("&dYou lost 1 &b" + ItemCreator.formatItemName(item) + " &dto a Goblin. Kill it quickly to get its loot!"));
			if(item.getAmount() > 1)
			{
				target.getInventory().setItem(slot, new ItemCreator(item).setAmount(item.getAmount()-1).getItem());
			}
			else
			{
				target.getInventory().setItem(slot, new ItemStack(Material.AIR));
			}
			if(stolenItems.containsKey(damager))
			{
				stolenItems.get(damager).addItem(new ItemCreator(item).setAmount(1).getItem());
			}
			else
			{
				stolenItems.put(damager, Bukkit.createInventory(damager, 27));
				stolenItems.get(damager).addItem(new ItemCreator(item).setAmount(1).getItem());
			}
		}
	}

	@EventHandler
	public void dropStolenItems(PlayerDeathEvent event)
	{
		Player target = (Player) event.getEntity();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(target))
			return;
		if(main.getMatchManager().getPlayerKit(target) == Kit.GOBLIN)
		{
			if(stolenItems.containsKey(target) && stolenItems.get(target).firstEmpty() > -1)
			{
				Arrays.asList(stolenItems.get(target).getContents()).stream().filter(item -> target.getInventory().contains(item.getType())).forEach(item -> target.getWorld().dropItemNaturally(target.getLocation(), item));
				stolenItems.remove(target);
			}
		}
	}

	/*	public Pair<Boolean, Integer> compareArmor(Player target, Player damager, int i)
	{
		switch(i)
		{
		case 0:
			if(target.getInventory().getHelmet().getItemMeta())
		}
	}*/

	public int getRandomItemSlot(Player target)
	{
		Inventory inv = target.getInventory();
		List<Material> blacklist = Arrays.asList(Material.SLIME_BALL, Material.MAGMA_CREAM, Material.FISHING_ROD, Material.ARROW, Material.SHIELD, Material.GOLD_HOE, Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.BLAZE_ROD, Material.BOW, Material.HOPPER);
		List<Integer> slots = new ArrayList<>();
		for(int i = 0; i < 8; i++)
		{
			if(inv.getItem(i) != null && !blacklist.contains(inv.getItem(i).getType()))
			{
				if(!inv.getItem(i).equals(target.getInventory().getItemInOffHand()))
				{
					slots.add(i);
				}
			}
		}
		if(slots.isEmpty())
			return -1;
		return slots.get(Numbers.getRandom(0, slots.size()-1));
	}

	@EventHandler
	public void shrinkGoblin(EntityToggleGlideEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		if(!main.getMatchManager().isMatchRunning())
			return;
		if(!main.getMatchManager().getPlayersAlive().contains(player))
			return;
		if(main.getMatchManager().getPlayerKit(player) != Kit.GOBLIN)
			return;
		event.setCancelled(true);
	}

	public void addStack(Player player, PotionEffectType type, int time, int max)
	{
		if(!player.hasPotionEffect(type) && max != 0)
		{
			player.addPotionEffect(new PotionEffect(type, time, 0));
		}
		else if(player.hasPotionEffect(type))
		{
			int level = player.getPotionEffect(type).getAmplifier();
			if(player.getPotionEffect(type).getAmplifier() < max)
				level+=1;
			player.removePotionEffect(type);
			player.addPotionEffect(new PotionEffect(type, time, level));
		}
	}

	@EventHandler
	public void snowBallSlow(ProjectileHitEvent event)
	{
		if(event.getHitEntity() == null)
			return;
		if(!(event.getHitEntity() instanceof Player))
			return;
		if(event.getEntity() instanceof Snowball)
		{
			addStack(((Player) event.getHitEntity()), PotionEffectType.SLOW, 60, 1);
		}
	}

}
