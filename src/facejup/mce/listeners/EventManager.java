package facejup.mce.listeners;

import facejup.mce.main.Main;

public class EventManager {
	
	private Main main; // Dependency Injection Variable
	
	private InventoryListeners invListener; // Inventory Listener
	
	public EventManager(Main main)
	{
		//TODO: Constructor which stores dependency injection and registers the listeners.
		this.main = main;
		registerListeners();
	}
	
	private void registerListeners()
	{
		//TODO: Instantiate the listener variables.
		invListener = new InventoryListeners(this);
	}
	
	public Main getMain() 
	{
		//TODO: Return the main instance.
		return this.main;
	}

}
