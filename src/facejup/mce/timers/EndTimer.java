package facejup.mce.timers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.main.MatchManager;
import facejup.mce.util.Chat;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;
import net.minecraft.server.v1_12_R1.MinecraftServer;

public class EndTimer {

	private final int MATCH_TIME = 1200; // Timer start time.
	private final String tag = Lang.Tag;

	private Main main; // Dependency Injection variable.
	private MatchManager mm; // Other Dependency Injection.

	private int time; // Time left to the match.
	private boolean running = false; // Whether or not the timer is running.

	public EndTimer(Main main, MatchManager mm) {
		this.mm = mm;
		this.main = main; // Store the current running instance of main.
	}

	public void startTimer()
	{
		if(main.getMatchManager().getStartTimer().isRunning())
			main.getMatchManager().getStartTimer().stopTimer();
		time = MATCH_TIME;
		running = true;
		main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
		{
			public void run()
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					main.getUserManager().getUser(player).updateScoreboard();
				}
			}
		}, 3L);
		countdown();
	}

	public void stopTimer()
	{
		this.running = false;
	}

	public int getTime() {
		return this.time;
	}

	private void countdown()
	{
		int minutes = (int) ((time) / 60.0);
		int seconds = (int) ((time) % 60.0);
		MinecraftServer.getServer().setMotd(Chat.translate("    &9&l(&b&l&oMC&f&l&oEliminations&9&l) &7&l : &e&l Version: 1.8 - 1.12 \n        &a&lMatch ending in: &b" + minutes + ":" + seconds + " &a&lminutes!"));
		if(running)
		{
			if(time > 0)
			{
				if(main.getMatchManager().getArenaManager().getArena().getWorld().getTime() > 1700)
					main.getMatchManager().getArenaManager().getArena().getWorld().setTime(600);
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if (mm.getPlayersAlive().contains(player)) {
						mm.afkCheck(player);
						if(player.getLocation().getY() < 1)
							player.damage(player.getMaxHealth());
						player.setMaxHealth(20);
						player.setFoodLevel(20);
						player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100.0D);
						if(mm.getPlayerClosestTo(player) != null)
							player.setCompassTarget(mm.getPlayerClosestTo(player).getLocation());
						if(!player.getInventory().contains(ItemCreator.getKitSelector()))
							player.getInventory().setItem(8, ItemCreator.getKitSelector());
						if(mm.getPlayerKit(player) != Kit.NONE && mm.getLives(player) > 0)
						{
							Kit kit = mm.getPlayerKit(player);
							if(kit == Kit.SHADE)
								mm.shadeCheck(player);
							if(kit == Kit.MASTER)
								if(player.getInventory().first(Material.ARROW) == -1 || player.getInventory().getItem(player.getInventory().first(Material.ARROW)).getAmount() < 12)
									player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
							if(kit == Kit.TRICKSTER)
								if(player.getInventory().first(Material.ARROW) == -1 || player.getInventory().getItem(player.getInventory().first(Material.ARROW)).getAmount() < 4)
									player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
							if(kit == kit.DEMON)
							{
								if(player.getLevel() <= 93)
									player.setLevel(player.getLevel()+7);
								else
									player.setLevel(100);
								player.setExp((float) (player.getLevel()/100.0));
							}
							if(kit == Kit.HARPY && player.isOnGround() && player.getLevel() < 100)
							{
								if(player.hasPotionEffect(PotionEffectType.SLOW))
									if(player.getLevel()-5 < 0)
										player.setLevel(0);
									else
										player.setLevel(player.getLevel()-5);
								else
									if(player.getLevel()+10 > 100)
										player.setLevel(100);
									else
										player.setLevel(player.getLevel()+10);
								player.setExp((float) (player.getLevel()/100.0));
							}
							if(kit == Kit.GRAVITON)
							{
								if(player.getInventory().first(Material.SNOW_BALL) == -1 && player.getInventory().first(Material.MAGMA_CREAM) == -1)
									player.getInventory().addItem(new ItemCreator(Material.SNOW_BALL).setDisplayname("&9Toggle Levitation On").getItem());
								if(player.hasPotionEffect(PotionEffectType.LEVITATION) && player.getLevel() == 0)
								{
									player.removePotionEffect(PotionEffectType.LEVITATION);
									player.getInventory().setItem(player.getInventory().first(Material.MAGMA_CREAM), new ItemCreator(Material.SNOW_BALL).setDisplayname("&9Toggle Levitation On").getItem());
								}
								else if(!player.hasPotionEffect(PotionEffectType.LEVITATION))
								{
									if(player.getLevel() + 7 < 100)
										player.setLevel(player.getLevel()+7);
									else
										player.setLevel(100);
								}
								else if(player.hasPotionEffect(PotionEffectType.LEVITATION))
								{
									player.setFallDistance(0);
									if(player.getLevel() - 9 > 0)
										player.setLevel(player.getLevel()-9);
									else
										player.setLevel(0);
								}
								player.setExp((float) (player.getLevel()/100.0));
							}
							if(kit == Kit.MAGE && time%15 == 0)
							{
								if(!player.getInventory().contains(Material.POTION))
									player.getInventory().addItem(new ItemCreator(Material.POTION).setPotionType(new PotionEffect(PotionEffectType.HEAL, 0, 1)).getItem());
								if(!player.getInventory().contains(Material.SPLASH_POTION))
									player.getInventory().addItem(getRandomPotion());
							}
							if(kit.pot != null)
							{
								if(kit == Kit.DEMON)
								{
									if (player.getFireTicks() > 0 && !player.hasPotionEffect(kit.pot.getType()))
										player.addPotionEffect(new PotionEffect(kit.pot.getType(), time * 20, kit.pot.getAmplifier()));
									else if(player.getFireTicks() <= 0)
										player.removePotionEffect(kit.pot.getType());

								}
								else
									player.addPotionEffect(new PotionEffect(kit.pot.getType(), time * 20, kit.pot.getAmplifier()));
							}
						}
						else
						{
							if(player.getLocation().getY() < 1)
							{
								if(player.isOp())
								{
									main.getServer().dispatchCommand(player, "spawn");
								}
								else
								{
									player.setOp(true);
									main.getServer().dispatchCommand(player, "spawn");
									player.setOp(false);
								}
							}
						}
					}
					else
					{
						if(player.getLocation().getY() < 1)
						{
							if(player.isOp())
							{
								main.getServer().dispatchCommand(player, "spawn");
							}
							else
							{
								player.setOp(true);
								main.getServer().dispatchCommand(player, "spawn");
								player.setOp(false);
							}
						}
					}
				}
				switch(time)
				{
				case 900:
					Chat.bc(tag + "Fifteen minutes left in the match!");
					break;
				case 600:
					Chat.bc(tag + "Ten minutes left in the match!");
					break;
				case 300:
					Chat.bc(tag + "Five minutes left in the match!");
					break;
				case 120:
					Chat.bc(tag + "Two minutes left in the match!");
					break;
				case 60:
					Chat.bc(tag + "One minute left in the match!");
					break;
				case 30:
					Chat.bc(tag + "30 seconds left in the match!");
					break;
				case 10:
					Chat.bc(tag + "10 seconds left in the match!");
					break;
				case 5:
					Chat.bc(tag + "5 seconds left in the match!");
					break;
				case 4:
					Chat.bc(tag + "4 seconds left in the match!");
					break;
				case 3:
					Chat.bc(tag + "3 seconds left in the match!");
					break;
				case 2:
					Chat.bc(tag + "2 seconds left in the match!");
					break;
				case 1:
					Chat.bc(tag + "1 seconds left in the match!");
					break;
				}
				time--;
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
				{
					public void run()
					{
						countdown();
					}
				}, 20L);
			}
			else
			{
				Chat.bc(tag + "The match has ended.");
				mm.endMatchByTime();
			}
		}
	}

	public ItemStack getRandomPotion()
	{
		int i = new Random().nextInt(7);
		switch(i)
		{
		case 0:
			return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.GLOWING, 200, 1)).setDisplayname("&9Potion Of Glowing").getItem();
		case 1:
			return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.POISON, 200, 0)).setDisplayname("&9Potion Of Poison").getItem();
		case 2:
			return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.WEAKNESS, 200, 2)).setDisplayname("&9Potion Of Weakness").getItem();
		case 3:
			return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.CONFUSION, 200, 2)).setDisplayname("&9Potion Of Confusion").getItem();
		case 4:
			return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.BLINDNESS, 200, 2)).setDisplayname("&9Potion Of Blindness").getItem();
		case 5:
			return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.SLOW, 200, 2)).setDisplayname("&9Potion Of Slowness").getItem();
		case 6:
			return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.LEVITATION, 200, 2)).setDisplayname("&9Potion Of Levitation").getItem();
		}

		return new ItemCreator(Material.SPLASH_POTION).setPotionType(new PotionEffect(PotionEffectType.POISON, 100, 1)).setDisplayname("&9Potion Of Poison").getItem();
	}

	public boolean isRunning() 
	{
		return this.running;
	}

}
