package facejup.mce.enums;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import facejup.mce.main.Main;
import facejup.mce.util.ItemCreator;

public enum Achievement {
	
	MASTER(new ItemCreator(Material.BOOK).setDisplayname("Master").setLore(Arrays.asList("&7&oGet every other achievement to unlock this!")).getItem(), 0, 1000),
	IRON_HITTER(new ItemCreator(Material.IRON_INGOT).setDisplayname("Iron Hitter").setLore(Arrays.asList("&7&oGet &6250&7 hits to unlock this!")).getItem(), 250, 50),
	GOLD_HITTER(new ItemCreator(Material.GOLD_INGOT).setDisplayname("Gold Hitter").setLore(Arrays.asList("&7&oGet &6500&7 hits to unlock this!")).getItem(), 500, 150),
	DIAMOND_HITTER(new ItemCreator(Material.DIAMOND).setDisplayname("Diamond Hitter").setLore(Arrays.asList("&7&oGet &61000&7 hits to unlock this!")).getItem(), 1000, 300),
	IRON_BOW(new ItemCreator(Material.BOW).setDisplayname("Iron Bow").setLore(Arrays.asList("&7&oGet &6125&7 hits to unlock this!")).getItem(), 125, 50),
	GOLD_BOW(new ItemCreator(Material.BOW).setDisplayname("Gold Bow").setLore(Arrays.asList("&7&oGet &6250&7 hits to unlock this!")).getItem(), 250, 150),
	DIAMOND_BOW(new ItemCreator(Material.BOW).setDisplayname("Diamond Bow").setLore(Arrays.asList("&7&oGet &6500&7 hits to unlock this!")).getItem(), 500, 300);
	
	
	public ItemStack icon;
	public int score;
	public int reward;
	
	Achievement(ItemStack icon, int score1, int reward1) {
		this.icon = icon;
		this.score = score1;
		this.reward = reward1;
		
		if (this.toString().equals("MASTER")) {
			Main main = Main.getPlugin(Main.class);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				public void run() {
					score = Achievement.values().length - 1;
					reward = score * 80;
				}
			}, 20L);
			
		}
	}
}
