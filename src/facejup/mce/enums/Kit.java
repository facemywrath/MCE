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

	NONE(null, 0, 0, Material.BARRIER, Arrays.asList("&7Exit the queue"),null, null, null, null, null, null, null),
	WARRIOR(null, 0, 2, Material.IRON_SWORD, Arrays.asList("&7The Warrior kit is", "&7a balanced melee fighter class."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.IRON_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), null, null, null),
	ARCHER(null, 0, 3, Material.BOW, Arrays.asList("&7The Archer kit has low armor with", "&7decent ranged and melee damage."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.BOW).addEnchant(Enchantment.ARROW_INFINITE, 1).getItem()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).addItem(new ItemStack(Material.ARROW, 1)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS), null, null),
	GUARD(null, 0, 4, Material.DIAMOND_CHESTPLATE, Arrays.asList("&7The Guard kit has low damage, high armor", "&7and a shield."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.STONE_SWORD, 1)).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_BOOTS), new ItemStack(Material.SHIELD, 1), new PotionEffect(PotionEffectType.SLOW, 100, 1)),
	FISHERMAN(null, 2250, 6, Material.FISHING_ROD, Arrays.asList("&7The Fisherman kit is useful for pulling", "&7enemies to you and knocking them down."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemStack(Material.FISHING_ROD, 1)).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 2).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS, 1), null, null),
	NINJA(null, 2250, 5, Material.ENDER_PEARL, Arrays.asList("&7The Ninja kit has very high mobility and damage", "&7but very little armor."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemStack(Material.ENDER_PEARL, 8)).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), null, new ItemStack(Material.GOLD_CHESTPLATE, 1), new ItemStack(Material.GOLD_LEGGINGS, 1), null, null, new PotionEffect(PotionEffectType.SPEED, 100, 1)),
	YETI(null, 3000, 7, Material.ICE, Arrays.asList("&7The Yeti kit has the ability to slow", "&7enemies on hit, and has high armor."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(ItemManager.getItemByName("wight_sword").toItemStack()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.DIAMOND_HELMET, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemCreator(Material.LEATHER_LEGGINGS).setDyeColor(DyeColor.LIGHT_BLUE).getItem(), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.LIGHT_BLUE).getItem(), null, null),
	MAGE(null, 3000, 8, Material.FIREBALL, Arrays.asList("&7The Mage kit has a fire wand, ice bombs, and", "&7gets a new health potion, and random splash potion", "&7every 15 seconds."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(ItemManager.getItemByName("mage_wand").toItemStack()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemCreator(ItemManager.getItemByName("mage_bomb").toItemStack()).setAmount(5).getItem()).addItem(new ItemCreator(Material.POTION).setData((short) 8197).getItem()).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_BOOTS), null, null),
	TRICKSTER(null, 4000, 9, Material.GOLD_HOE, Arrays.asList("&7The Trickster kit has very good control of their enemies."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_HOE).addEnchant(Enchantment.KNOCKBACK, 1).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemCreator(Material.BOW).getItem()).addItem(new ItemCreator(ItemManager.getItemByName("trickster_smoke").toItemStack()).setAmount(3).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).addItem(new ItemStack(Material.ARROW, 1)).getInventory()).getContents(), new ItemStack(Material.CHAINMAIL_HELMET, 1), null, new ItemCreator(Material.LEATHER_LEGGINGS).setDyeColor(DyeColor.MAGENTA).getItem(), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.MAGENTA).getItem(), null, new PotionEffect(PotionEffectType.SPEED, 100, 0)),
	UPGRADER(null, 4000, 10, Material.MAGENTA_GLAZED_TERRACOTTA, Arrays.asList("&7The Upgrader kit has a random piece of gear", "&7upgraded every kill they get, resetting on death."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getContents(), new ItemStack(Material.LEATHER_HELMET, 1), new ItemCreator(Material.LEATHER_CHESTPLATE).getItem(), new ItemCreator(Material.LEATHER_LEGGINGS).getItem(), new ItemCreator(Material.LEATHER_BOOTS).getItem(), null, null),
	GOBLIN(null, 4000, 11, Material.GOLD_NUGGET, Arrays.asList("&7The Goblin kit is very weak, with low", "&7armor and damage.", "&aTheft: Hitting a player can steal an item.", "&aShrink: Crouching makes you half-sized."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_PICKAXE).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 2)).getInventory()).getContents(), null, null, null, null, null, null),
	HARPY("SKYLORD", 0, 12, Material.FEATHER, Arrays.asList("&7The Harpy kit has low armor and damage ", "&7but very good mobility.", "&aFlight: Sprinting allows you to fly. Regain energy while landed."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 1).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4))).getInventory().getStorageContents(), new ItemCreator(Material.LEATHER_HELMET).setDyeColor(DyeColor.RED).getItem(), new ItemStack(Material.ELYTRA, 1), new ItemCreator(Material.LEATHER_LEGGINGS).setDyeColor(DyeColor.RED).getItem(), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.RED).getItem(), null, new PotionEffect(PotionEffectType.JUMP, 100, 2)),
	GRAVITON("THISISSPARTA", 0, 13, Material.SHULKER_SHELL, Arrays.asList("&7The Graviton kit uses gravity to control", "&7themselves and their opponents."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(ItemManager.getItemByName("graviton_sword").toItemStack()).addItem(new ItemCreator(Material.ENDER_PEARL).setAmount(3).setDisplayname("&5Gravbomb").addGlowing().getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 3))).addItem(new ItemCreator(Material.SNOW_BALL).setDisplayname("&9Toggle Levitation On").getItem()).getInventory().getStorageContents(), new ItemCreator(Material.LEATHER_HELMET).setDyeColor(DyeColor.BLUE).getItem(), null, new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.BLUE).getItem(), null, new PotionEffect(PotionEffectType.SPEED, 100, 0)),
	SHADE("ASSASSINATE", 0, 14, Material.POTION, Arrays.asList("&7The Shade kit has low armor, high", "&7damage, and little mobility.", "&aHide: Stand still to go invisible. Move to regain energy."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).getItem())).getInventory().getStorageContents(), ItemCreator.getChameleonArmor("helmet"), ItemCreator.getChameleonArmor("chestplate"), ItemCreator.getChameleonArmor("leggings"), ItemCreator.getChameleonArmor("boots"), null, null),
	GUNNER("ARCHERY", 0, 15, Material.WOOD_PICKAXE, Arrays.asList("&cWarning: Work in progress", "&7The Gunner kit utilizes a quick ranged ability, and grenades."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(ItemManager.getItemByName("gunner_bow").toItemStack()).addItem(new ItemCreator(Material.WOOD_SWORD).addEnchant(Enchantment.KNOCKBACK, 2).getItem()).addItem(new ItemCreator(ItemManager.getItemByName("gunner_grenade").toItemStack()).setAmount(4).getItem()).addItem(new ItemStack(Material.COOKED_BEEF, 4)).getInventory()).getStorageContents(), new ItemCreator(Material.LEATHER_HELMET).setDyeColor(DyeColor.GRAY).getItem(), new ItemCreator(Material.LEATHER_CHESTPLATE).setDyeColor(DyeColor.GRAY).getItem(), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.GRAY).getItem(), null, new PotionEffect(PotionEffectType.SPEED, 100, 0)),
	DEMON("HELLFIRE", 0, 16, Material.MAGMA, Arrays.asList("&7The Demon kit uses fire","&7to destroy their targets.","&aLeap: Jump while sneaking to launch yourself."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.GOLD_SWORD).addEnchant(Enchantment.FIRE_ASPECT, 2).getItem()).getInventory()).getStorageContents(), new ItemStack(Material.GOLD_HELMET, 1), new ItemCreator(Material.LEATHER_CHESTPLATE).setDyeColor(DyeColor.RED).getItem(), null, new ItemCreator(Material.LEATHER_BOOTS).setDyeColor(DyeColor.RED).getItem(), null, new PotionEffect(PotionEffectType.REGENERATION, 100, 1)),
	MASTER("MASTER", 0, 17, Material.BOOK, Arrays.asList("&7The Master kit utilizes several different combinations", "&7to be a very powerful, and annoying, ranged enemy."), (new InventoryBuilder(null, InventoryType.PLAYER).addItem(new ItemCreator(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 2).getItem()).addItem(ItemManager.getItemByName("master_bow").toItemStack()).addItem(ItemManager.getItemByName("master_wand").toItemStack()).addItem(new ItemStack(Material.ARROW, 10))).getInventory().getStorageContents(), new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_BOOTS, 1), null, new PotionEffect(PotionEffectType.REGENERATION, 100, 0));

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
	public List<String> description;
	Kit(String achcostname, int coincost, int slot, Material icon, List<String> description, ItemStack storage[], ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack offhand, PotionEffect potion)
	{
		this.description = description;
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
