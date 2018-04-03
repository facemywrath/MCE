package facejup.mce.storage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import facejup.mce.main.Main;
import facejup.mce.main.MatchManager;
import facejup.mce.util.Chat;

public class EndTimer {

	private final int MATCH_TIME = 900; // Timer start time.
	private final String tag = "&9&l[&r&bMCE&9&l] &2&o";

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
		time = MATCH_TIME;
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
				switch(time)
				{
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
			}
		}
	}

}
