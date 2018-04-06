package facejup.mce.listeners;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import facejup.mce.commands.CommandArena;
import facejup.mce.enums.AddType;
import facejup.mce.maps.ArenaManager;

public class ArenaModListener implements Listener{

	private EventManager em;
	
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
		
		if (ca.adding.containsKey(event.getPlayer()) && ca.arenaAdd.containsKey(event.getPlayer())) {
			Player player = event.getPlayer();
			Location loc = event.getClickedBlock().getLocation();
			ConfigurationSection section = am.getArenaSection(ca.arenaAdd.get(player));
			if (ca.adding.get(player) == AddType.BOUND1) {
				section.set("Bound1.x", loc.getX());
				section.set("Bound1.y", loc.getY());
				section.set("Bound1.z", loc.getZ());
				am.getFileControl().save();
				ca.adding.put(player, AddType.BOUND2);
				//TODO Tell player added to bound 2
			} else if (ca.adding.get(player) == AddType.BOUND2) {
				section.set("Bound2.x", loc.getX());
				section.set("Bound2.y", loc.getY());
				section.set("Bound2.z", loc.getZ());
				am.getFileControl().save();
				ca.adding.remove(player);
				ca.arenaAdd.remove(player);
			}
		}
		
	}
	
}
