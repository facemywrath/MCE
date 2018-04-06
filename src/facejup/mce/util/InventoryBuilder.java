package facejup.mce.util;


import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import facejup.mce.enums.Kit;
import facejup.mce.main.Main;


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

	public static Inventory createKitInventory(Player player)
	{
		Main main = Main.getPlugin(Main.class);
		Kit kit = main.getMatchManager().getPlayerDesiredKit(player);
		if(kit == Kit.NONE)
			kit = main.getMatchManager().getPlayerKit(player);
		InventoryBuilder ib = new InventoryBuilder(player, "Kits", 2);
		for (Kit tester : Kit.values()) {
			String name = StringUtils.capitalize(tester.toString().toLowerCase());
			ItemCreator item = new ItemCreator(tester.icon).setDisplayname((kit == tester)?"&6Kit: " + name:"&2Kit: " + name).setLore(Arrays.asList((kit == tester?"&7&lThis is your kit":"")));
			if (kit == tester)
				item.addGlowing();
			ib.addItem(item.getItem());
		}

		return ib.getInventory();
	}

}
