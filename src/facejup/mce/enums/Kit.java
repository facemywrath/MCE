package facejup.mce.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.ItemCreator;

public enum Kit {

	NONE(Material.BARRIER,null, null, null, null, null, null),
	WARRIOR(Material.IRON_SWORD, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.IRON_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), null, null),
	ARCHER(Material.BOW, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).getItem()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 3)).addItem(new ItemStack(Material.ARROW, 1)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS), null),
	GUARD(Material.DIAMOND_CHESTPLATE, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.STONE_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory()).getContents(), new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_BOOTS), new PotionEffect(PotionEffectType.SLOW, 1, 1)),
	NINJA(Material.ENDER_PEARL, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemStack(Material.ENDER_PEARL, 8)).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory()).getContents(), new ItemStack(Material.LEATHER, 1), new ItemStack(Material.GOLD_CHESTPLATE, 1), new ItemStack(Material.GOLD_LEGGINGS, 1), null, new PotionEffect(PotionEffectType.SPEED, 1, 1)),
	FISHERMAN(Material.FISHING_ROD, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.FISHING_ROD, 1)).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 3)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1), null),
	MAGE(Material.POTION, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemCreator(Material.POTION).setData((short) 16396).getItem()).addItem(new ItemStack(Material.GOLDEN_APPLE, 1)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS), null);

	public PotionEffect pot;
	public List<ItemStack> storage;
	public ItemStack helmet;
	public ItemStack chestplate;
	public ItemStack leggings;
	public ItemStack boots;
	public Material icon;
	Kit(Material icon, ItemStack storage[], ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, PotionEffect potion)
	{
		
		this.helmet = (helmet != null ? helmet : new ItemStack(Material.AIR));
		this.chestplate = (chestplate != null ? chestplate : new ItemStack(Material.AIR));
		this.leggings = (leggings != null ? leggings : new ItemStack(Material.AIR));
		this.boots = (boots != null ? boots : new ItemStack(Material.AIR));
		this.pot = potion;
		this.storage = (storage != null ? Arrays.asList(storage) : new ArrayList<>());
		this.icon = icon;
	}

}
