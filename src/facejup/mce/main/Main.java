package facejup.mce.main;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;

import facejup.mce.commands.CommandManager;
import facejup.mce.listeners.EventManager;
import facejup.mce.players.UserManager;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;
import facejup.mce.util.Marker;
import facejup.mce.util.Numbers;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.AttributeModifier;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;

public class Main extends JavaPlugin {

	private MatchManager mm; // The object responsible for dealing with the beginning and ending of matches.
	private UserManager um;
	private EventManager em;
	private CommandManager cm;

	public void onEnable()
	{
		mm = new MatchManager(this); // Instantiation. Constructor just starts the countdown.
		um = new UserManager(this);
		em = new EventManager(this);
		cm = new CommandManager(this);
		restartTimer();
	}

	@SuppressWarnings("unchecked")
	public void onDisable()
	{
		for(Marker<Location> marker : em.getKitPowerListeners().ignitedBlocks)
		{
			marker.getItem().getBlock().setType(Material.AIR);
		}
		for(Player player : ((Set<Player>)em.getInventoryListeners().specialBlocks.keySet()))
		{
			em.getInventoryListeners().clearSpecialBlocks(player);
		}
	}

	public void restartTimer()
	{
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
			public void run()
			{
				if(mm.isMatchRunning())
				{
					Chat.bc(Lang.Tag + "&cServer restarting after this match.");
					restartCheckup();
					return;
				}
				getServer().getScheduler().scheduleSyncDelayedTask(mm.getMain(), new Runnable()
				{
					public void run()
					{
						restartCheckup();
					}
				}, 1200L);
			}
		}, 72000L);
	}

	public void restartCheckup()
	{
		if(mm.isMatchRunning())
			this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
			{
				public void run()
				{
					restartCheckup();
				}
			}, 1200L);
		else
		{
			Chat.bc("&cServer restarting in 10 seconds.");
			getServer().getScheduler().scheduleSyncDelayedTask(mm.getMain(), new Runnable()
			{
				public void run()
				{
					mm.getMain().getServer().dispatchCommand(mm.getMain().getServer().getConsoleSender(), "stop");
				}
			}, 200L);
		}
	}

	//Getters

	public MatchManager getMatchManager()
	{
		return this.mm;
	}

	public CommandManager getCommandManager()
	{
		return this.cm;
	}

	public UserManager getUserManager()
	{
		return this.um;
	}

	public EventManager getEventManager()
	{
		return this.em;
	}
	
	public static void spawnZombie(Location loc)
	{
		Main main = Main.getPlugin(Main.class);
		LivingEntity ent = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		ent.setSilent(true);
		ent.setCollidable(false);
		double speed = Numbers.getRandom(40, 100)/100.0;
		double hp = (30-(speed*17));
		double dmg = (5 - (speed*4.5));
		ent.setMaxHealth(hp);
		ent.setHealth(hp);
		ent.setCanPickupItems(false);
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) ent).getHandle();
		AttributeInstance speedatt = nmsEntity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		AttributeModifier speedmod = new AttributeModifier(UUID.randomUUID(), "movement speed multiplier", speed, 1);
		speedatt.b(speedmod);
		AttributeInstance dmgatt = nmsEntity.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);
		AttributeModifier dmgmod = new AttributeModifier(UUID.randomUUID(), "attack damage multiplier", dmg, 1);
		dmgatt.b(dmgmod);
		if(main.getMatchManager().getPlayersAlive().size() > 0)
			((Zombie) ent).setTarget(main.getMatchManager().getPlayersAlive().get(Numbers.getRandom(0, main.getMatchManager().getPlayersAlive().size()-1)));
	}

}
