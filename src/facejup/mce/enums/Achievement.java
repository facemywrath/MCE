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
	PLAYER(false, new ItemCreator(Material.EXP_BOTTLE).setDisplayname("Player").setLore(Arrays.asList("&7&oPlay &r%SCORE% &7&omatches")).getItem(), Arrays.asList(20,50,150,250,500), Arrays.asList(new Reward(25), new Reward(50),new Reward(150),new Reward(300), new Reward(750))),
	HITTER(false, new ItemCreator(Material.WOOD_SWORD).setDisplayname("Hitter").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&ohits")).getItem(), Arrays.asList(100,250,500,1000,5000), Arrays.asList(new Reward(25), new Reward(50),new Reward(150),new Reward(300), new Reward(750))),
	ARCHERY(false, new ItemCreator(Material.BOW).setDisplayname("Archery").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&ohits with arrows")).getItem(), Arrays.asList(60,125,250,500, 2500), Arrays.asList(new Reward(25),new Reward(50),new Reward(150), new Reward(500), new Reward(Kit.GUNNER))),
	MARKSMAN(false, new ItemCreator(Material.ARROW).setDisplayname("Marksman").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&oheadshots with arrows")).getItem(), Arrays.asList(30,75,200,450,1000), Arrays.asList(new Reward(50),new Reward(75),new Reward(150), new Reward(500), new Reward(1000))),
	SPREE(false, new ItemCreator(Material.POTION).setDisplayname("Spree").setLore(Arrays.asList("&7&oGet a &r%SCORE% &7&oplayer killing spree")).getItem(), Arrays.asList(3,5,10,15,20), Arrays.asList(new Reward(50),new Reward(100),new Reward(200),new Reward(300), new Reward(1000))),
	VICTOR(false, new ItemCreator(Material.GOLDEN_APPLE).setDisplayname("Victor").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&owins")).getItem(), Arrays.asList(10,20,50,100,500), Arrays.asList(new Reward(50),new Reward(100),new Reward(225),new Reward(500), new Reward(1250))),
	FLAWLESS(false, new ItemCreator(Material.GOLDEN_CARROT).setDisplayname("Flawless").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&owins with five", "&7&oor more lives left.")).getItem(), Arrays.asList(2,4,8,16,32), Arrays.asList(new Reward(50),new Reward(100),new Reward(300),new Reward(700), new Reward(2500))),
	FAILURE(false, new ItemCreator(Material.GOLDEN_APPLE).setAmount(2).setDisplayname("Failure").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&orunnerups")).getItem(), Arrays.asList(10,20,50,100,500), Arrays.asList(new Reward(50),new Reward(75),new Reward(250),new Reward(500), new Reward(1500))),
	THISISSPARTA(false, new ItemCreator(Material.STONE_SWORD).setDisplayname("THIS IS SPARTA!!!").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills using fall damage.")).getItem(), Arrays.asList(10,20,50,100,200), Arrays.asList(new Reward(50),new Reward(75),new Reward(250),new Reward(750), new Reward(Kit.GRAVITON))),
	ASSASSINATE(false, new ItemCreator(Material.TOTEM).setDisplayname("Assassinate").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills without being hit","&7&owithin the last 8 seconds.")).getItem(), Arrays.asList(10,20,50,75,175), Arrays.asList(new Reward(50),new Reward(75),new Reward(250),new Reward(750), new Reward(Kit.SHADE))),
	HELLFIRE(false, new ItemCreator(Material.FIREBALL).setDisplayname("Hellfire").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills using fire damage.")).getItem(), Arrays.asList(10,20,50,75,150), Arrays.asList(new Reward(50),new Reward(75),new Reward(250), new Reward(1000), new Reward(Kit.DEMON))),
	SKYLORD(false, new ItemCreator(Material.FEATHER).setDisplayname("Skylord").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&okills while falling")).getItem(), Arrays.asList(10,20,50,75,200), Arrays.asList(new Reward(50),new Reward(75),new Reward(250),new Reward(750),new Reward(Kit.HARPY))),
	DESTROYER(false, new ItemCreator(Material.DIAMOND_SWORD).setDisplayname("Destroyer").setLore(Arrays.asList("&7&oEliminate &r%SCORE% &7&oplayers from the game")).getItem(), Arrays.asList(10,20,50,75,200), Arrays.asList(new Reward(50),new Reward(75),new Reward(250),new Reward(750),new Reward(1500))),
	OVERLORD(false, new ItemCreator(Material.GOLD_AXE).setDisplayname("Overlord").setLore(Arrays.asList("&7&oKill &r%SCORE% &7&obosses")).getItem(), Arrays.asList(1,2,4,8,16), Arrays.asList(new Reward(100),new Reward(200),new Reward(400),new Reward(800),new Reward(1600))),
	FISHERMAN(true, new ItemCreator(Material.FISHING_ROD).setData(3).setDisplayname("Fisherman").setLore(Arrays.asList("&d&lFish out an actual fish", "&d&las fisherman.")).getItem(), Arrays.asList(1), Arrays.asList(new Reward(100))),
	FISHSLAP(true, new ItemCreator(Material.RAW_FISH).setData(3).setDisplayname("Fishslap").setLore(Arrays.asList("&d&lGet a kill using a fish.")).getItem(), Arrays.asList(1), Arrays.asList(new Reward(100))),
	PUBLICITY(true, new ItemCreator(Material.SKULL).setData(3).setDisplayname("Publicity").setLore(Arrays.asList("&d&lGet us members by streaming.")).getItem(), Arrays.asList(1), Arrays.asList(new Reward(100))),
	BETA(true, new ItemCreator(Material.EYE_OF_ENDER).setDisplayname("Beta").setLore(Arrays.asList("&d&lBe a beta tester.")).getItem(), Arrays.asList(1), Arrays.asList(new Reward(2000))),
	SPENDER(true, new ItemCreator(Material.GOLD_INGOT).setDisplayname("Spender").setLore(Arrays.asList("&d&lPurchase a kit.")).getItem(), Arrays.asList(1), Arrays.asList(new Reward(100))),
	ARCHITECT(true, new ItemCreator(Material.BRICK).setDisplayname("Architect").setLore(Arrays.asList("&d&lGet an arena accepted by the staff.")).getItem(), Arrays.asList(1), Arrays.asList(new Reward(100)));
	
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
			level = "Stone ";
			break;
		case 1:
			level = "Iron ";
			break;
		case 2:
			level = "Gold ";
			break;
		case 3:
			level = "Diamond ";
			break;
		case 4:
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
