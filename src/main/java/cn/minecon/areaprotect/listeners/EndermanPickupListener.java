package cn.minecon.areaprotect.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.FlagManager;

public class EndermanPickupListener implements Listener {
	final private AreaProtect plugin;

	public EndermanPickupListener(AreaProtect plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEndermanChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() != EntityType.ENDERMAN) {
            return;
        }
        if (!plugin.allowAction(event.getBlock().getLocation(), FlagManager.ENDERMANPICKUP)) {
            event.setCancelled(true);
        }
    }
}
