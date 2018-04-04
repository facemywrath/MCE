package facejup.mce.util;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import facejup.mce.kits.Kit;
import facejup.mce.main.Main;

public class InventoryBuilder {
	
	private Inventory inventory;
	
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
		Kit kit = main.getMatchManager().getPlayerKit(player);
		ItemStack none = new ItemCreator(Material.BARRIER).setDisplayname(Chat.translate((kit == Kit.NONE?"&6Kit: None":"&2Kit: None"))).setLore(Arrays.asList((kit == Kit.NONE?"&7&oThis is your kit":""))).getItem();
		Inventory inv = new InventoryBuilder(player, "Kits", 2).getInventory();
		
		return inv;
	}

}
