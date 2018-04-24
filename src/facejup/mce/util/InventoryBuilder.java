package facejup.mce.util;


import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import facejup.mce.enums.Achievement;
import facejup.mce.enums.Kit;
import facejup.mce.main.Main;
import facejup.mce.players.User;
import net.md_5.bungee.api.ChatColor;


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

	public static Kit getKitBySlot(int slot)
	{
		for(Kit kit : Kit.values())
		{
			if(kit.slot == slot)
				return kit;
		}
		return Kit.NONE;
	}

	public static Inventory createKitInventory(Player player)
	{
		Main main = Main.getPlugin(Main.class);
		Kit kit = main.getMatchManager().getPlayerDesiredKit(player);
		User user = main.getUserManager().getUser(player);
		if(kit == Kit.NONE)
			kit = main.getMatchManager().getPlayerKit(player);
		InventoryBuilder ib = new InventoryBuilder(player, "Kits", (int) (( (Kit.values().length-1) / 9.0)+1));
		for (Kit tester : Kit.values()) {
			String name = StringUtils.capitalize(tester.toString().toLowerCase());
			if(tester == Kit.NONE && main.getMatchManager().getEndTimer().isRunning())
				continue;
			boolean achkit = (tester.achcost != null);
			String level = "";
			if(achkit && tester.achcost.scores.size() > 1)
				level = Achievement.getAchievementKitRewardLevel(tester.achcost, tester);
			ItemCreator item = new ItemCreator(tester.icon).hideFlags(63).setDisplayname((kit == tester)?"&6Kit: " + name:(user.hasKit(tester)?"&2Kit: " + name:"&4Locked: " + name)).setLore(Arrays.asList((kit == tester?"&7&lThis is your kit":(user.hasKit(tester)?"&a&lClick to select":(achkit?("&bRequires achievement &6" + level + tester.achcost.icon.getItemMeta().getDisplayName() + "&b to unlock."):user.getCoins() >= tester.coincost?"&aCost: " + tester.coincost:"&4Cost: " + tester.coincost))), (user.hasKit(tester)?"":(achkit?"":(user.getCoins() >= tester.coincost?"&aYour coins: " + user.getCoins():"&4Your coins: " + user.getCoins()))), (user.hasKit(tester)?"":(achkit?"&7Type &6&l/achievements &7or click here to":(user.getCoins() >= tester.coincost)?"&6Click to purchase!":"")), (user.hasKit(tester)?"":(achkit?"&7view your list of achievements.":""))));
			if (kit == tester)
				item.addGlowing();
			List<String> lore = item.getItem().getItemMeta().getLore();
			if(lore.size() > 2 && lore.get(2).equals(""))
			{
				lore.remove(2);
			}
			if(lore.size() > 2 && lore.get(2).equals(""))
			{
				lore.remove(2);
			}
			lore.addAll(tester.description);
			item.setLore(lore);
			ib.setItem(tester.slot, item.getItem());
		}

		return ib.getInventory();
	}

	public static Inventory createAchievementInventory(Player player)
	{
		Main main = Main.getPlugin(Main.class);
		User user = main.getUserManager().getUser(player);
		InventoryBuilder ib = new InventoryBuilder(player, "Achievements", (int) (( Achievement.values().length / 9.0) + 1));

		for (Achievement ach : Achievement.values()) {
			ib.addItem(InventoryBuilder.getAchievementItem(ach, player));
		}

		return ib.getInventory();
	}
	
	public static ItemStack getAchievementItem(Achievement ach, Player player)
	{
		Main main = Main.getPlugin(Main.class);
		User user = main.getUserManager().getUser(player);
		ItemCreator item = new ItemCreator(ach.icon);
		String name = ach.icon.getItemMeta().getDisplayName();
		List<String> lore = ach.icon.getItemMeta().getLore();
		boolean flag = user.hasAchievement(ach);
		if(ach.secret && !flag)
			return new ItemStack(Material.AIR);
		String newName = (flag ? "&aUnlocked: " : "&cLocked: ");
		String level = "";
		if(ach.scores.size() > 1)
			level = Achievement.getAchievementLevelName(user.getAchievementLevel(ach));
		newName += level + name;
		for(int i = 0; i < lore.size(); i++)
		{
			String str = lore.get(i);
			lore.set(i, str.replaceAll("%SCORE%", "&6" + user.getNextAchievementScore(ach)));
		}
		if(!flag) {
			lore.add("&6Score: " + user.getScore(ach) + "/" + ach.scores.get(user.getAchievementLevel(ach)));
			if(ach.rewards.get(user.getAchievementLevel(ach)).getReward().getRight() == null)
				lore.addAll(Arrays.asList("", "&7&lReward: &b" + ach.rewards.get(user.getAchievementLevel(ach)).getReward().getLeft() + " coins"));
			else
				lore.addAll(Arrays.asList("", "&7&lReward: &bKit " + StringUtils.capitalize(ach.rewards.get(user.getAchievementLevel(ach)).getReward().getRight().toString().toLowerCase())));

		} else {
			item.addGlowing();
		}
		return item.setDisplayname(newName).setLore(lore).hideFlags(63).getItem();
	
	}

}
