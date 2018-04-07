package facejup.mce.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import facejup.mce.main.Main;

public class FileControl {

	private File file; 
	private FileConfiguration config;

	public FileControl()
	{
		FileControl.checkBaseFiles();
	}

	public FileControl(File f)
	{
		FileControl.checkBaseFiles();
		this.file = f;
		if(!f.exists())
		{
			try{
				f.createNewFile();
			}
			catch(Exception e){}
		}
		this.config = YamlConfiguration.loadConfiguration(f);
	}

	public FileControl setConfig(File file)
	{
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		return this;
	}

	public FileControl setFile(File file)
	{
		this.file = file;
		return this;
	}

	public File getFile()
	{
		return this.file;
	}

	public void save()
	{
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save(FileConfiguration config)
	{
		try {
			config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig(File file)
	{
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		return this.config;
	}

	public FileConfiguration getConfig()
	{
		return YamlConfiguration.loadConfiguration(file);
	}

	public static void checkBaseFiles()
	{
		Main main = Main.getPlugin(Main.class);
		if(!main.getDataFolder().exists())
			main.getDataFolder().mkdirs();
		File file = new File(main.getDataFolder(), "users.yml");
		if(!file.exists())
		{
			try {
				file.createNewFile();
			}
			catch(Exception e) {}
		}
		file = new File(main.getDataFolder(), "maps.yml");
		if(!file.exists())
		{
			try {
				file.createNewFile();
			}
			catch(Exception e) {}
		}
	}
}
