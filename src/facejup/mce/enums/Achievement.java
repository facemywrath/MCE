package facejup.mce.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import facejup.mce.main.Main;
import facejup.mce.util.ItemCreator;

public enum Achievement {
	
	MASTER(false, new ItemCreator(Material.BOOK).setDisplayname("Master").setLore(Arrays.asList("&7&oGet every other achievement")).getItem(), Arrays.asList(1), new ArrayList<>()),
	HITTER(false, new ItemCreator(Material.WOOD_SWORD).setDisplayname("Hitter").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&ohits")).getItem(), Arrays.asList(250,500,1000), Arrays.asList(new Reward(50),new Reward(150),new Reward(300))),
	BOW(false, new ItemCreator(Material.BOW).setDisplayname("Bow").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&ohits")).getItem(), Arrays.asList(125,250,500), Arrays.asList(new Reward(50),new Reward(150),new Reward(300))),
	SPREE(false, new ItemCreator(Material.POTION).setDisplayname("Spree").setLore(Arrays.asList("&7&oGet a &r%SCORE% &7&oplayer killing spree")).getItem(), Arrays.asList(5,10,15), Arrays.asList(new Reward(100),new Reward(200),new Reward(300))),
	VICTOR(false, new ItemCreator(Material.GOLDEN_APPLE).setDisplayname("Victor").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&owins")).getItem(), Arrays.asList(20,50,100), Arrays.asList(new Reward(100),new Reward(300),new Reward(700))),
	FAILURE(false, new ItemCreator(Material.GOLDEN_APPLE).setAmount(2).setDisplayname("Failure").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&orunnerups")).getItem(), Arrays.asList(20,50,100), Arrays.asList(new Reward(75),new Reward(250),new Reward(500))),
	THISISSPARTA(false, new ItemCreator(Material.STONE_SWORD).setDisplayname("THIS IS SPARTA!!!").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills using fall damage.")).getItem(), Arrays.asList(20,50,100), Arrays.asList(new Reward(75),new Reward(250),new Reward(Kit.GRAVITON))),
	ASSASSINATE(false, new ItemCreator(Material.TOTEM).setDisplayname("Assassinate").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills without being hit within the last 8 seconds.")).getItem(), Arrays.asList(20,50,75), Arrays.asList(new Reward(75),new Reward(250),new Reward(Kit.SHADE))),
	HELLFIRE(false, new ItemCreator(Material.FIREBALL).setDisplayname("Hellfire").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills using fire damage.")).getItem(), Arrays.asList(20,50,75), Arrays.asList(new Reward(75),new Reward(250),new Reward(0))),
	SKYLORD(false, new ItemCreator(Material.FEATHER).setDisplayname("Skylord").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills while falling")).getItem(), Arrays.asList(20,50,75), Arrays.asList(new Reward(75),new Reward(250),new Reward(Kit.HARPY)));
	
	public ItemStack icon;
	public List<Integer> scores;
	public List<Reward> rewards;
	public boolean secret;
	
	Achievement(boolean secret, ItemStack icon, List<Integer> scores1, List<Reward> rewards1) {
		this.icon = icon;
		this.secret = secret;
		this.scores = scores1;
		this.rewards = rewards1;
		
		if (this.toString().equals("MASTER")) {
			Main main = Main.getPlugin(Main.class);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				public void run() {
					scores = Arrays.asList(Achievement.getMaxAchievementCount()-1);
					rewards = Arrays.asList(new Reward(Kit.MASTER));
				}
			}, 1L);
			
		}
	}
	
	public static String getAchievementKitRewardLevel(Achievement ach, Kit k)
	{
		int i = 0;
		for(Reward rew : ach.rewards)
		{
			if(rew.getReward().getRight() != null)
				return getAchievementLevelName(i);
			i++;
		}
		return "";
	}
	
	public static String getAchievementLevelName(int i)
	{
		String level = "";
		switch(i)
		{
		case 0:
			level = "Iron ";
			break;
		case 1:
			level = "Gold ";
			break;
		case 2:
			level = "Diamond ";
			break;
		case 3:
			level = "Ender ";
			break;
		}
		return level;
	}
	
	public static int getMaxAchievementCount()
	{
		int i = 0;
		for(Achievement ach : Achievement.values())
		{
			if(!ach.secret)
			{
				i+=ach.scores.size();
			}
		}
		return i;
	}
}
