package facejup.mce.storage;

import facejup.mce.main.Main;

public class StartTimer {
	
	private Main main; // Dependency Injection variable.

	public StartTimer(Main main) {
		this.main = main; // Store the current running instance of main.
	}

}
