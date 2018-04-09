package facejup.mce.listeners;

import facejup.mce.main.Main;

public class EventManager {
	
	private Main main; //Dependency Injection Variable
	
	@SuppressWarnings("unused")
	private InventoryListeners invListener; //Inventory Listener
	@SuppressWarnings("unused")
	private ArenaModListener aml;
	@SuppressWarnings("unused")
	private DeathListeners dl;
	@SuppressWarnings("unused")
	private AchievementListeners al;
	private KitPowerListeners kpl;
	
	public EventManager(Main main)
	{
		//Constructor which stores dependency injection and registers the listeners.
		this.main = main;
		registerListeners();
	}
	
	private void registerListeners()
	{
		//Instantiate the listener variables.
		invListener = new InventoryListeners(this);
		al = new AchievementListeners(this);
		aml = new ArenaModListener(this);
		dl = new DeathListeners(this);
		kpl = new KitPowerListeners(this);
	}
	
	public DeathListeners getDeathListeners()
	{
		return this.dl;
	}
	
	public Main getMain() 
	{
		//Return the main instance.
		return this.main;
	}

}
