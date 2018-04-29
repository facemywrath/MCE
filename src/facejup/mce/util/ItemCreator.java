package facejup.mce.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class ItemCreator{
	private ItemStack item;

	public ItemCreator(Material mat)
	{
		this.item = new ItemStack(mat, 1);
	}

	public ItemCreator(ItemStack item)
	{
		this.item = item.clone();
	}

	public ItemCreator setAmount(int i)
	{
		this.item.setAmount(i);
		return this;
	}

	public ItemCreator setDisplayname(String display)
	{
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Chat.translate(display));
		item.setItemMeta(meta);
		return this;
	}

	public ItemCreator setDyeColor(DyeColor color)
	{
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color.getColor());
		item.setItemMeta(meta);
		return this;
	}

	public ItemCreator setLore(List<String> lore)
	{
		ItemMeta meta = item.getItemMeta();
		List<String> newLore = new ArrayList<>();
		for(String str : lore)
		{
			newLore.add(ChatColor.translateAlternateColorCodes('&', str));
		}
		meta.setLore(newLore);
		item.setItemMeta(meta);
		return this;
	}

	public ItemCreator setData(MaterialData data)
	{
		item.setData(data);
		return this;
	}

	public ItemCreator setData(int i)
	{
		item.setDurability((byte) i);
		return this;
	}

	public ItemCreator addEnchant(Enchantment ench, int i) {
		if(!item.containsEnchantment(ench))
			item.addUnsafeEnchantment(ench, i);
		return this;
	}

	public ItemCreator addGlowing()
	{
		if(getTag(item, "HideFlags") == 0)
			item = setTag(item, "HideFlags", 1);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		return this;
	}

	public ItemCreator hideFlags(int i)
	{
		item = setTag(item, "HideFlags", i);
		return this;
	}

	public ItemCreator setPotionType(PotionEffect effect)
	{
		if(item.getItemMeta() instanceof PotionMeta)
		{
			PotionMeta meta = (PotionMeta) item.getItemMeta();
			meta.addCustomEffect(effect, true);
			item.setItemMeta(meta);
		}
		return this;
	}


	public ItemStack getItem()
	{
		return this.item;
	}	

	private ItemStack setTag(ItemStack item, String tagname, int amt)
	{
		net.minecraft.server.v1_12_R1.ItemStack itemnms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = (itemnms.hasTag() ? itemnms.getTag() : new NBTTagCompound());
		tag.set(tagname, new NBTTagByte((byte) amt));
		itemnms.setTag(tag);
		return CraftItemStack.asBukkitCopy(itemnms);
	}

	private byte getTag(ItemStack item, String tagname)
	{
		net.minecraft.server.v1_12_R1.ItemStack itemnms = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = (itemnms.hasTag() ? itemnms.getTag() : new NBTTagCompound());
		return tag.getByte(tagname);
	}

	public static ItemStack getChameleonArmor(String str)
	{
		if(str.equalsIgnoreCase("helmet"))
			return (new ItemCreator(Material.LEATHER_HELMET).setDisplayname("Chameleon Helmet").getItem());
		if(str.equalsIgnoreCase("chestplate"))
			return (new ItemCreator(Material.LEATHER_CHESTPLATE).setDisplayname("Chameleon Helmet").getItem());
		if(str.equalsIgnoreCase("leggings"))
			return (new ItemCreator(Material.LEATHER_LEGGINGS).setDisplayname("Chameleon Helmet").getItem());
		if(str.equalsIgnoreCase("boots"))
			return (new ItemCreator(Material.LEATHER_BOOTS).setDisplayname("Chameleon Helmet").getItem());
		return null;
	}

	public static String formatItemName(ItemStack item)
	{
		return (item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():Chat.formatName(item.getType().toString()));
	}

	public static ItemStack getKitSelector()
	{
		return new ItemCreator(Material.COMPASS).setDisplayname("&5&lKit Selector").setLore(Arrays.asList("&7&oRightclick with this to", "&7&oopen the kit menu.")).getItem();
	}
	public static double getArmorPoints(ItemStack item) {
		double red = 0.0;
		//
		if(item.getType().toString().contains("HELMET")) {
			if (item.getType() == Material.LEATHER_HELMET) red = red + 0.04;
			else if (item.getType() == Material.GOLD_HELMET) red = red + 0.08;
			else if (item.getType() == Material.CHAINMAIL_HELMET) red = red + 0.08;
			else if (item.getType() == Material.IRON_HELMET) red = red + 0.08;
			else if (item.getType() == Material.DIAMOND_HELMET) red = red + 0.12;
		}
		//
		if(item.getType().toString().contains("CHESTPLATE")) {
			if (item.getType() == Material.LEATHER_CHESTPLATE)    red = red + 0.12;
			else if (item.getType() == Material.GOLD_CHESTPLATE)red = red + 0.20;
			else if (item.getType() == Material.CHAINMAIL_CHESTPLATE) red = red + 0.20;
			else if (item.getType() == Material.IRON_CHESTPLATE) red = red + 0.24;
			else if (item.getType() == Material.DIAMOND_CHESTPLATE) red = red + 0.32;
		}
		//
		if(item.getType().toString().contains("LEGGINGS")) {
			if (item.getType() == Material.LEATHER_LEGGINGS) red = red + 0.08;
			else if (item.getType() == Material.GOLD_LEGGINGS)    red = red + 0.12;
			else if (item.getType() == Material.CHAINMAIL_LEGGINGS) red = red + 0.16;
			else if (item.getType() == Material.IRON_LEGGINGS)    red = red + 0.20;
			else if (item.getType() == Material.DIAMOND_LEGGINGS) red = red + 0.24;
		}
		//
		if(item.getType().toString().contains("BOOTS")) {
			if (item.getType() == Material.LEATHER_BOOTS) red = red + 0.04;
			else if (item.getType() == Material.GOLD_BOOTS) red = red + 0.04;
			else if (item.getType() == Material.CHAINMAIL_BOOTS) red = red + 0.04;
			else if (item.getType() == Material.IRON_BOOTS) red = red + 0.08;
			else if (item.getType() == Material.DIAMOND_BOOTS)    red = red + 0.12;
		}
		//
		return red * (1/0.04);
	}
}