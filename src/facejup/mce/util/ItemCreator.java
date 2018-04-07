package facejup.mce.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

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
			item.addEnchantment(ench, i);
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
	
	public static ItemStack getKitSelector()
	{
		return new ItemCreator(Material.COMPASS).setDisplayname("&5&lKit Selector").setLore(Arrays.asList("&7&oRightclick with this to", "&7&oopen the kit menu.")).getItem();
	}
}