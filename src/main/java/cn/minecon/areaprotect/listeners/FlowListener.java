package cn.minecon.areaprotect.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.FlagManager;

public class FlowListener implements Listener {
	final private AreaProtect plugin;

	public FlowListener(final AreaProtect plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFromTo(final BlockFromToEvent event) {
    	final Material mat = event.getBlock().getType();
        Flag flag = null;
        if (mat == Material.LAVA) {
            flag = FlagManager.LAVAFLOW;
        }
        if (mat == Material.WATER) {
            flag = FlagManager.WATERFLOW;
        }
        if (flag == null) {
            return;
        }
        if (!plugin.allowAction(event.getBlock().getLocation(), flag)) {
            event.setCancelled(true);
        }
    }
}
