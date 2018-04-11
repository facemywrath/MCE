package facejup.mce.enums;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import facejup.mce.main.Main;
import facejup.mce.util.ItemCreator;

public enum Achievement {
	
	MASTER(Kit.MASTER, new ItemCreator(Material.BOOK).setDisplayname("Master").setLore(Arrays.asList("&7&oGet every other achievement to unlock this!")).getItem(), 0, 1000),
	IRON_HITTER(null, new ItemCreator(Material.IRON_INGOT).setDisplayname("Iron Hitter").setLore(Arrays.asList("&7&oGet &6250 &7&ohits to unlock this!")).getItem(), 250, 50),
	GOLD_HITTER(null, new ItemCreator(Material.GOLD_INGOT).setDisplayname("Gold Hitter").setLore(Arrays.asList("&7&oGet &6500 &7&ohits to unlock this!")).getItem(), 500, 150),
	DIAMOND_HITTER(null, new ItemCreator(Material.DIAMOND).setDisplayname("Diamond Hitter").setLore(Arrays.asList("&7&oGet &61000 &7&ohits to unlock this!")).getItem(), 1000, 300),
	IRON_BOW(null, new ItemCreator(Material.BOW).setDisplayname("Iron Bow").setLore(Arrays.asList("&7&oGet &6125 &7&ohits to unlock this!")).getItem(), 125, 50),
	GOLD_BOW(null, new ItemCreator(Material.BOW).setDisplayname("Gold Bow").setLore(Arrays.asList("&7&oGet &6250 &7&ohits to unlock this!")).getItem(), 250, 150),
	DIAMOND_BOW(null, new ItemCreator(Material.BOW).setDisplayname("Diamond Bow").setLore(Arrays.asList("&7&oGet &6500 &7&ohits to unlock this!")).getItem(), 500, 300),
	SPREE(null, new ItemCreator(Material.POTION).setDisplayname("Killing Spree").setLore(Arrays.asList("&7&oGet a &65 &7&oplayer killing spree!")).getItem(), 5, 100),
	UNKILLABLE(null, new ItemCreator(Material.POTION).setDisplayname("Unkillable").setLore(Arrays.asList("&7&oGet a &610 &7&oplayer killing spree!")).getItem(), 10, 200),
	IMMORTAL(null, new ItemCreator(Material.POTION).setDisplayname("Immortal").setLore(Arrays.asList("&7&oGet a &615 &7&oplayer killing spree!")).getItem(), 15, 300),
	IRON_VICTOR(null, new ItemCreator(Material.BOW).setDisplayname("Iron Victor").setLore(Arrays.asList("&7&oGet &615 &7&owins to unlock this!")).getItem(), 15, 100),
	GOLD_VICTOR(null, new ItemCreator(Material.BOW).setDisplayname("Gold Victor").setLore(Arrays.asList("&7&oGet &650 &7&owins to unlock this!")).getItem(), 50, 300),
	DIAMOND_VICTOR(null, new ItemCreator(Material.BOW).setDisplayname("Diamond Victor").setLore(Arrays.asList("&7&oGet &6100 &7&owins to unlock this!")).getItem(), 100, 700);
	
	
	public ItemStack icon;
	public int score;
	public int coinreward;
	public Kit kitreward;
	
	Achievement(Kit kitreward1, ItemStack icon, int score1, int reward1) {
		this.icon = icon;
		this.score = score1;
		this.coinreward = reward1;
		
		if (this.toString().equals("MASTER")) {
			Main main = Main.getPlugin(Main.class);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				public void run() {
					score = Achievement.values().length - 1;
					coinreward = score * 80;
				}
			}, 1L);
			
		}
		if(kitreward1 != null)
		{
			Main main = Main.getPlugin(Main.class);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				public void run() {
					coinreward = 0;
					kitreward = kitreward1;
				}
			}, 1L);
		}
	}
}
