package facejup.mce.util;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


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
		//Main main = Main.getPlugin(Main.class);
		//Kit kit = main.getMatchManager().getPlayerKit(player);
		Inventory inv = new InventoryBuilder(player, "Kits", 2).getInventory();
		
		return inv;
	}

}
