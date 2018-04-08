package facejup.mce.util;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.players.User;


public class InventoryBuilder {

	private Inventory inventory;

	public InventoryBuilder(Player player, InventoryType type)
	{
		this.inventory = Bukkit.createInventory(player, type);
	}

	public InventoryBuilder(Player player, String title, int rows)
	{
		this.inventory = Bukkit.createInventory(player, rows*9, title);
	}

	public InventoryBuilder setItem(int slot, ItemStack item)
	{
		inventory.setItem(slot, item);
		return this;
	}

	public InventoryBuilder addItem(ItemStack item)
	{
		inventory.addItem(item);
		return this;
	}

	public Inventory getInventory()
	{
		return this.inventory;
	}

	public static Kit getKitBySlot(int slot)
	{
		for(Kit kit : Kit.values())
		{
			if(kit.slot == slot)
				return kit;
		}
		return null;
	}

	public static Inventory createKitInventory(Player player)
	{
		Main main = Main.getPlugin(Main.class);
		Kit kit = main.getMatchManager().getPlayerDesiredKit(player);
		User user = main.getUserManager().getUser(player);
		if(kit == Kit.NONE)
			kit = main.getMatchManager().getPlayerKit(player);
		InventoryBuilder ib = new InventoryBuilder(player, "Kits", (int) (( Kit.values().length / 9.0) + 1));
		for (Kit tester : Kit.values()) {
			String name = StringUtils.capitalize(tester.toString().toLowerCase());
			if(tester == Kit.NONE && main.getMatchManager().getEndTimer().isRunning())
				continue;
			ItemCreator item = new ItemCreator(tester.icon).hideFlags(63).setDisplayname((kit == tester)?"&6Kit: " + name:(user.hasKit(tester)?"&2Kit: " + name:"&4Locked: " + name)).setLore(Arrays.asList((kit == tester?"&7&lThis is your kit":(user.hasKit(tester)?"&a&lClick to select":(user.getCoins() >= tester.cost?"&aCost: " + tester.cost:"&4Cost: " + tester.cost))), (user.hasKit(tester)?"":(user.getCoins() >= tester.cost?"&aYour coins: " + user.getCoins():"&4Your coins: " + user.getCoins())), (user.hasKit(tester)?"":(user.getCoins() >= tester.cost)?"&6Click to purchase!":"")));
			if (kit == tester)
				item.addGlowing();
			ib.setItem(tester.slot, item.getItem());
		}

		return ib.getInventory();
	}
	
	public static Inventory createAchievementInventory(Player player)
	{
		Main main = Main.getPlugin(Main.class);
		User user = main.getUserManager().getUser(player);
		InventoryBuilder ib = new InventoryBuilder(player, "Achievements", (int) (( Achievement.values().length / 9.0) + 1));
		
		for (Achievement ach : Achievement.values()) {
			ItemCreator item = new ItemCreator(ach.icon);
			String name = ach.icon.getItemMeta().getDisplayName();
			List<String> lore = ach.icon.getItemMeta().getLore();
			boolean flag = user.hasAchievement(ach);
			String newName = (flag ? "&aUnlocked: " + name : "&cLocked: " + name);
			
			if(!flag) {
				lore.add("&6Score: " + user.getScore(ach) + "/" + ach.score);
				lore.addAll(Arrays.asList("", "&7&lReward: &b" + ach.reward + " coins"));
				
			} else {
				item.addGlowing();
			}
			ib.addItem(item.setDisplayname(newName).setLore(lore).getItem());
		}
		
		return ib.getInventory();
	}

}
