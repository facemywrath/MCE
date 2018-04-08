package facejup.mce.timers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.main.MatchManager;
import facejup.mce.util.Chat;
import facejup.mce.util.ItemCreator;
import facejup.mce.util.Lang;

public class EndTimer {

	private final int MATCH_TIME = 1500; // Timer start time.
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
		countdown();
	}

	public void stopTimer()
	{
		this.running = false;
	}

	private void countdown()
	{
		if(running)
		{
			if(time > 0)
			{
				for(Player player : mm.getPlayersAlive())
				{
					if(player.getInventory().getItemInMainHand().getDurability() > 20)
						player.getInventory().getItemInMainHand().setDurability((short) 0);
					if(!player.getInventory().contains(ItemCreator.getKitSelector()))
						player.getInventory().setItem(8, ItemCreator.getKitSelector());
					main.getUserManager().getUser(player).updateScoreboard();
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
