package facejup.mce.timers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

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
		for(Player player : Bukkit.getOnlinePlayers())
		{
			main.getUserManager().getUser(player).updateScoreboard();
		}
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
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if (mm.getPlayersAlive().contains(player)) {
						player.setMaxHealth(20);
						player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100.0D);
						if(mm.getPlayerClosestTo(player) != null)
							player.setCompassTarget(mm.getPlayerClosestTo(player).getLocation());
						if(!player.getInventory().contains(ItemCreator.getKitSelector()))
							player.getInventory().setItem(8, ItemCreator.getKitSelector());
						if(mm.getPlayerKit(player) != Kit.NONE && mm.getLives(player) > 0)
						{
							Kit kit = mm.getPlayerKit(player);
							if(kit.pot != null)
							{
								if (!player.hasPotionEffect(kit.pot.getType()))
									player.addPotionEffect(new PotionEffect(kit.pot.getType(), time * 20, kit.pot.getAmplifier()));
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

	public boolean isRunning() 
	{
		return this.running;
	}

}
