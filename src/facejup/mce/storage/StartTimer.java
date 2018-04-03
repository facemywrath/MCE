package facejup.mce.storage;

import facejup.mce.main.Main;
import facejup.mce.main.MatchManager;
import facejup.mce.util.Chat;

public class StartTimer {


	private final int WAIT_TIME = 900; // Timer start time.
	private final int LINGER_TIME = 60; // Time needed to wait if there are not enough players ready.
	private final String tag = "&9&l[&r&bMCE&9&l] &2&o";

	private Main main; // Dependency Injection variable.
	private MatchManager mm; // Other Dependency Injection.

	private int time; // Time left until the match.
	private boolean running = false; // Whether or not the timer is running.

	public StartTimer(Main main, MatchManager mm) {
		this.mm = mm;
		this.main = main; // Store the current running instance of main.
	}

	public void startTimer()
	{
		time = WAIT_TIME;
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
					Chat.bc(tag + "Ten minutes left until the match begins!");
					break;
				case 300:
					Chat.bc(tag + "Five minutes left until the match begins!");
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
				}, 20L);
			}
			else
			{
				if(mm.getPlayerCount() < mm.MIN_PLAYERS)
				{
					time = LINGER_TIME;
					Chat.bc(tag + "Not enough players ready to begin a match.");
				}
			}
		}
	}

	public boolean isRunning() 
	{
		return this.running;
	}
	
}
