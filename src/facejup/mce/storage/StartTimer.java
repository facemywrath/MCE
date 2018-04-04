package facejup.mce.storage;

import org.bukkit.Bukkit;

import facejup.mce.main.Main;
import facejup.mce.main.MatchManager;
import facejup.mce.util.Chat;

public class StartTimer {

	private final int WAIT_TIME = 180; // Timer start time.
	private final int LINGER_TIME = 60; // Time needed to wait if there are not enough players ready.
	private final String tag = "&9&l(&r&bMCE&9&l) &a&o";

	private Main main; // Dependency Injection variable.
	private MatchManager mm; // Other Dependency Injection.

	private int time; // Time left until the match.
	private boolean running = false; // Whether or not the timer is running.

	public StartTimer(Main main, MatchManager mm) {
		this.mm = mm; // Store the match manager instance.
		this.main = main; // Store the current running instance of main.
	}

	public void startTimer()
	{
		//TODO: End the end timer if it's running and start the countdown until the next match begins.
		if(main.getMatchManager().getEndTimer().isRunning())
			main.getMatchManager().getEndTimer().stopTimer();
		time = WAIT_TIME;
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
				switch(time)
				{
				case 180:
					Chat.bc(tag + "Three minutes left until the match begins!");
					break;
				case 120:
					Chat.bc(tag + "Two minutes left until the match begins!");
					break;
				case 60:
					Chat.bc(tag + "One minute left until the match begins!");
					break;
				case 30:
					Chat.bc(tag + "30 seconds left until the match begins!");
					break;
				case 10:
					Chat.bc(tag + "10 seconds left until the match begins!");
					break;
				case 5:
					Chat.bc(tag + "5 seconds left until the match begins!");
					break;
				case 4:
					Chat.bc(tag + "4 seconds left until the match begins!");
					break;
				case 3:
					Chat.bc(tag + "3 seconds left until the match begins!");
					break;
				case 2:
					Chat.bc(tag + "2 seconds left until the match begins!");
					break;
				case 1:
					Chat.bc(tag + "1 second left until the match begins!");
					break;
				}
				time--;
				main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable()
				{
					public void run()
					{
						countdown();
					}
				}, 1L);
			}
			else
			{
				if(mm.getPlayerCount() < mm.MIN_PLAYERS)
				{
					time = LINGER_TIME;
					Chat.bc(tag + "&cNot enough players in queue to begin a match. Please select a kit with the &5&lKit Selector &cto join queue.");
					countdown();
				}
			}
		}
	}

	public boolean isRunning() 
	{
		return this.running;
	}
	
}
