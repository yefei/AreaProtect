package cn.minecon.areaprotect.listeners;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.FlagManager;

public class ExplosionListener implements Listener {
	final private AreaProtect plugin;

	public ExplosionListener(AreaProtect plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        Flag flag = null;
        if (event.getEntity() != null) {
            final EntityType entity = event.getEntityType();
            flag = getFlag(entity);
            if (flag == null) {
                return;
            }
        } else {
            flag = FlagManager.BEDEXPLOSION;
        }
        final Iterator<Block> it = event.blockList().iterator();
        while (it.hasNext()) {
            final Location loc = it.next().getLocation();
            if (!plugin.allowAction(loc, flag)) {
                it.remove();
            }
        }
    }

    private Flag getFlag(EntityType entity) {
        switch (entity) {
            case WITHER:
            case WITHER_SKULL:
                return FlagManager.WITHEREXPLOSION;
            case PRIMED_TNT:
            case MINECART_TNT:
                return FlagManager.TNTEXPLOSION;
            case SMALL_FIREBALL:
            case FIREBALL:
                return FlagManager.FIREBALLEXPLOSION;
            case CREEPER:
                return FlagManager.CREEPEREXPLOSION;
            default:
                return null;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onWitherDoSomethingToBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() != EntityType.WITHER) {
            return;
        }
        if (!plugin.allowAction(event.getBlock().getLocation(), FlagManager.WITHEREXPLOSION)) {
            event.setCancelled(true);
        }
    }
}
