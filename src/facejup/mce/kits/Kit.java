package facejup.mce.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.ItemCreator;

public enum Kit {
	
   NONE(null, null, null, null, null, null),
   WARRIOR((PlayerInventory) new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.IRON_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), null, null),
   ARCHER((PlayerInventory) new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).getItem()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 3)).addItem(new ItemStack(Material.ARROW, 1)).getInventory(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS), null),
   GUARD((PlayerInventory) new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.STONE_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory(), new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_BOOTS), new PotionEffect(PotionEffectType.SLOW, 1, 1)),
   NINJA((PlayerInventory) new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemStack(Material.ENDER_PEARL, 8)).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory(), new ItemStack(Material.LEATHER, 1), new ItemStack(Material.GOLD_CHESTPLATE, 1), new ItemStack(Material.GOLD_LEGGINGS, 1), null, new PotionEffect(PotionEffectType.SPEED, 1, 1)),
   FISHERMAN((PlayerInventory) new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.FISHING_ROD, 1)).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1), null);
	
	public PotionEffect pot;
	public PlayerInventory inv;
	Kit(PlayerInventory inv, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, PotionEffect potion)
	{
		this.pot = potion;
		((PlayerInventory) inv).setHelmet(helmet);
		((PlayerInventory) inv).setChestplate(chestplate);
		((PlayerInventory) inv).setLeggings(leggings);
		((PlayerInventory) inv).setBoots(boots);
		this.inv = inv;
	}

}
