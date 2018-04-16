package facejup.mce.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import facejup.mce.main.Main;
import facejup.mce.util.InventoryBuilder;
import facejup.mce.util.ItemCreator;
import think.rpgitems.item.ItemManager;

public enum Kit {

	NONE(null, 0, 0, Material.BARRIER,null, null, null, null, null, null, null),
	WARRIOR(null, 0, 2, Material.IRON_SWORD, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.IRON_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), null, null, null),
	ARCHER(null, 0, 3, Material.BOW, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).getItem()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).addItem(new ItemStack(Material.ARROW, 1)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS), null, null),
	GUARD(null, 0, 4, Material.DIAMOND_CHESTPLATE, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.STONE_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.SHIELD, 1), new PotionEffect(PotionEffectType.SLOW, 100, 1)),
	NINJA(null, 2500, 5, Material.ENDER_PEARL, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemStack(Material.ENDER_PEARL, 8)).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), null, new ItemStack(Material.GOLD_CHESTPLATE, 1), new ItemStack(Material.GOLD_LEGGINGS, 1), null, null, new PotionEffect(PotionEffectType.SPEED, 100, 1)),
	FISHERMAN(null, 2000, 6, Material.FISHING_ROD, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.FISHING_ROD, 1)).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 2).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1), null, null),
	MAGE(null, 3500, 7, Material.FIREBALL, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(ItemManager.getItemByName("mage_wand").toItemStack()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemCreator(ItemManager.getItemByName("mage_bomb").toItemStack()).setAmount(5).getItem()).addItem(new ItemCreator(Material.POTION).setData((short) 8197).getItem()).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS), null, null),
	HARPY("SKYLORD", 0, 8, Material.FEATHER, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4))).getInventory().getStorageContents(), new ItemCreator(Material.LEATHER_HELMET).setDyeColor(DyeColor.RED).getItem(), new ItemStack(Material.ELYTRA, 1), new ItemCreator(Material.LEATHER_LEGGINGS).setDyeColor(DyeColor.RED).getItem(), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.RED).getItem(), null, new PotionEffect(PotionEffectType.JUMP, 100, 2)),
	GRAVITON("THISISSPARTA", 0, 9, Material.SHULKER_SHELL, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(ItemManager.getItemByName("graviton_sword").toItemStack()).addItem(new ItemCreator(Material.ENDER_PEARL).setAmount(3).setDisplayname("&5Gravbomb").addGlowing().getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 3))).getInventory().getStorageContents(), new ItemCreator(Material.LEATHER_HELMET).setDyeColor(DyeColor.BLUE).getItem(), null, new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.BLUE).getItem(), null, new PotionEffect(PotionEffectType.SPEED, 100, 0)),
	SHADE("ASSASSINATE", 0, 10, Material.POTION, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).getItem())).getInventory().getStorageContents(), ItemCreator.getChameleonArmor("helmet"), ItemCreator.getChameleonArmor("chestplate"), ItemCreator.getChameleonArmor("leggings"), ItemCreator.getChameleonArmor("boots"), null, null),
	GUNNER("ARCHERY", 0, 11, Material.WOOD_PICKAXE, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(ItemManager.getItemByName("gunner_bow").toItemStack()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 2).getItem()).addItem(new ItemCreator(ItemManager.getItemByName("gunner_grenade").toItemStack()).setAmount(4).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getStorageContents(), new ItemCreator(Material.LEATHER_HELMET).setDyeColor(DyeColor.GRAY).getItem(), new ItemCreator(Material.LEATHER_CHESTPLATE).setDyeColor(DyeColor.GRAY).getItem(), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.GRAY).getItem(), null, new PotionEffect(PotionEffectType.SPEED, 100, 0)),
	DEMON("HELLFIRE", 0, 12, Material.MAGMA, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.FIRE_ASPECT, 2).getItem()).getInventory()).getStorageContents(), new ItemStack(Material.GOLD_HELMET, 1), new ItemCreator(Material.LEATHER_CHESTPLATE).setDyeColor(DyeColor.RED).getItem(), new ItemStack(Material.GOLD_LEGGINGS, 1), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.RED).getItem(), null, new PotionEffect(PotionEffectType.REGENERATION, 100, 1)),
	MASTER("MASTER", 0, 13, Material.BOOK, (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 2).getItem()).addItem(ItemManager.getItemByName("master_bow").toItemStack()).addItem(ItemManager.getItemByName("master_wand").toItemStack()).addItem(new ItemStack(Material.ARROW, 10))).getInventory().getStorageContents(), new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_BOOTS, 1), null, new PotionEffect(PotionEffectType.REGENERATION, 100, 0));

	
	public PotionEffect pot;
	public List<ItemStack> storage;
	public ItemStack helmet;
	public ItemStack chestplate;
	public ItemStack leggings;
	public ItemStack boots;
	public ItemStack offhand;
	public Material icon;
	public int coincost;
	public Achievement achcost;
	public int slot;
	Kit(String achcostname, int coincost, int slot, Material icon, ItemStack storage[], ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack offhand, PotionEffect potion)
	{
		this.offhand = offhand;
		this.coincost = coincost;
		this.slot = slot;
		this.helmet = (helmet != null ? helmet : new ItemStack(Material.AIR));
		this.chestplate = (chestplate != null ? chestplate : new ItemStack(Material.AIR));
		this.leggings = (leggings != null ? leggings : new ItemStack(Material.AIR));
		this.boots = (boots != null ? boots : new ItemStack(Material.AIR));
		this.pot = potion;
		this.storage = (storage != null ? Arrays.asList(storage) : new ArrayList<>());
		this.icon = icon;
		if(achcostname != null)
		{
			Main main = Main.getPlugin(Main.class);
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				public void run() {
					achcost = Achievement.valueOf(achcostname);
				}
			}, 1L);
		}
	}

}
