package facejup.mce.enums;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import facejup.mce.main.Main;
import facejup.mce.util.ItemCreator;

public enum Achievement {
	
	MASTER(Kit.MASTER, new ItemCreator(Material.BOOK).setDisplayname("Master").setLore(Arrays.asList("&7&oGet every other achievement to unlock this!")).getItem(), Arrays.asList(1), Arrays.asList(0)),
	HITTER(null, new ItemCreator(Material.WOOD_SWORD).setDisplayname("Hitter").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&ohits to unlock this!")).getItem(), Arrays.asList(2,4,6), Arrays.asList(50,150,300)),
	BOW(null, new ItemCreator(Material.BOW).setDisplayname("Bow").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&ohits to unlock this!")).getItem(), Arrays.asList(125,250,500), Arrays.asList(50,150,300)),
	SPREE(null, new ItemCreator(Material.POTION).setDisplayname("Spree").setLore(Arrays.asList("&7&oGet a &r%SCORE% &7&oplayer killing spree!")).getItem(), Arrays.asList(5,10,15), Arrays.asList(100,200,300)),
	VICTOR(null, new ItemCreator(Material.GOLDEN_APPLE).setDisplayname("Victor").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&owins to unlock this!")).getItem(), Arrays.asList(20,50,100), Arrays.asList(100,300,700)),
	FAILURE(null, new ItemCreator(Material.GOLDEN_APPLE).setAmount(2).setDisplayname("Failure").setLore(Arrays.asList("&7&oGet &r%SCORE% &7&orunnerups to unlock this!")).getItem(), Arrays.asList(20,50,100), Arrays.asList(75,250,500));
	
	public ItemStack icon;
	public List<Integer> scores;
	public List<Integer> coinrewards;
	public Kit kitreward;
	
	Achievement(Kit kitreward1, ItemStack icon, List<Integer> scores1, List<Integer> rewards) {
		this.icon = icon;
		this.scores = scores1;
		this.coinrewards = rewards;
		
		if (this.toString().equals("MASTER")) {
			Main main = Main.getPlugin(Main.class);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				public void run() {
					scores = Arrays.asList(Achievement.values().length - 1);
					coinrewards = Arrays.asList(scores.get(0) * 80);
				}
			}, 1L);
			
		}
		if(kitreward1 != null)
		{
			Main main = Main.getPlugin(Main.class);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				public void run() {
					coinrewards = Arrays.asList(0);
					kitreward = kitreward1;
				}
			}, 1L);
		}
	}
}
