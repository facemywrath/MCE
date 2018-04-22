package facejup.mce.listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import facejup.mce.arenas.ArenaManager;
import facejup.mce.commands.CommandArena;
import facejup.mce.enums.AddType;
import facejup.mce.util.Chat;
import facejup.mce.util.Lang;

public class ArenaModListener implements Listener{

	private EventManager em;
	private HashMap<Player, Long> cooldown = new HashMap<>();
	
	public ArenaModListener(EventManager em) {
		this.em = em;
		em.getMain().getServer().getPluginManager().registerEvents(this, em.getMain());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		ArenaManager am = em.getMain().getMatchManager().getArenaManager();
		CommandArena ca = em.getMain().getCommandManager().getCommandArena();
		
		if ((!cooldown.containsKey(event.getPlayer()) || cooldown.get(event.getPlayer()) + 1000 <= System.currentTimeMillis()) && ca.adding.containsKey(event.getPlayer()) && ca.arenaAdd.containsKey(event.getPlayer())) {
			Player player = event.getPlayer();
			cooldown.put(player, System.currentTimeMillis());
			Location loc = event.getClickedBlock().getLocation();
			ConfigurationSection section = am.getArenaSection(ca.arenaAdd.get(player));
			if (ca.adding.get(player) == AddType.BOUND1) {
				section.set("Bound1.x", loc.getX());
				section.set("Bound1.y", loc.getY());
				section.set("Bound1.z", loc.getZ());
				am.getFileControl().save();
				ca.adding.put(player, AddType.BOUND2);
				player.sendMessage(Chat.translate("&9&l(&r&bMCE&9&l) &aBound 1 set at " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ". Now you must select the second bounding coordinate."));
				return;
			} else if (ca.adding.get(player) == AddType.BOUND2) {
				section.set("Bound2.x", loc.getX());
				section.set("Bound2.y", loc.getY());
				section.set("Bound2.z", loc.getZ());
				am.getFileControl().save();				
				player.sendMessage(Chat.translate("&9&l(&r&bMCE&9&l) &aBound 2 set at " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ". Now you can add spawn points with &7/arena spawn add " + ca.arenaAdd.get(player)));
				ca.adding.remove(player);
				ca.arenaAdd.remove(player);
				return;
			}
			else if(ca.adding.get(player) == AddType.SIGN)
			{
				section.set("Sign.Name", ca.arenaAdd.get(player));
				section.set("Sign.World", player.getWorld().getName());
				section.set("Sign.x", loc.getX());
				section.set("Sign.y", loc.getY());
				section.set("Sign.z", loc.getZ());
				am.getFileControl().save();				
				player.sendMessage(Lang.Tag + Chat.translate("Sign placed for &6" + ca.arenaAdd.get(player)));
				ca.adding.remove(player);
				ca.arenaAdd.remove(player);
				em.getMain().getMatchManager().am.loadVoteSigns();
			}
		}
		
	}
	
}
