package cn.minecon.areaprotect.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.FlagManager;
import cn.minecon.areaprotect.area.Area;

public class FireListener implements Listener {
	final private AreaProtect plugin;

	public FireListener(AreaProtect plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        if (!plugin.allowAction(event.getBlock().getLocation(), FlagManager.FIRESPREAD)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == IgniteCause.SPREAD) {
            if (!plugin.allowAction(event.getBlock().getLocation(), FlagManager.FIRESPREAD)) {
                event.setCancelled(true);
            }
            return;
        }
        
        final Area area = plugin.getAreaManager().getByLocation(event.getBlock().getLocation());
        
        if (area == null) {
        	return;
        }
        
        if (event.getCause() == IgniteCause.FLINT_AND_STEEL) {
            final Player player = event.getPlayer();
            if (!area.allowAction(player, FlagManager.IGNITE)) {
                event.setCancelled(true);
                player.sendMessage(Config.getMessage("FlagDeny", FlagManager.IGNITE.getDescription()));
            }
            return;
        }
        if (!area.allowAction(FlagManager.IGNITE)) {
            event.setCancelled(true);
        }
    }
}
