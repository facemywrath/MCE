package facejup.mce.util;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;


public class FancyTextUtil {

	String message;
	String hovertext;
	ItemStack hoveritem;

	public FancyTextUtil(String message, String hovertext)
	{
		this.message = message;
		this.hovertext = hovertext;
	}

	public FancyTextUtil(String message, ItemStack item)
	{
		this.message = message;
		this.hoveritem = item;
	}

	public FancyTextUtil(ItemStack item)
	{
		this.hoveritem = item;
	}
	public static void sendItemTooltipMessage(Player player, String message, ItemStack item) {
		net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = new NBTTagCompound();
		nmsItemStack.save(compound);
		String json = compound.toString();
		BaseComponent[] hoverEventComponents = new BaseComponent[]{
				new TextComponent(json) // The only element of the hover events basecomponents is the item json
		};
		HoverEvent hover_event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents);
		String name = item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():Chat.formatName(item.getType().toString());
		TextComponent component = new TextComponent("[" + ChatColor.GRAY + name + ChatColor.RESET + " x " + ChatColor.YELLOW + item.getAmount() + ChatColor.RESET + "]");
		component.setHoverEvent(hover_event);


		TextComponent component2 = new TextComponent();
		component2.setText("");
		component2.setHoverEvent(null);

		component.addExtra(component2);

		player.spigot().sendMessage(component);
	}

}
