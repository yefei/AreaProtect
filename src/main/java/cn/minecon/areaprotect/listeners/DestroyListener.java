package cn.minecon.areaprotect.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.FlagManager;

public class DestroyListener implements Listener {
	final private AreaProtect plugin;

	public DestroyListener(AreaProtect plugin) {
		this.plugin = plugin;
	}
	
	// 展示框类的物品
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHangingBreak(HangingBreakByEntityEvent event) {
    	final Entity remover = event.getRemover();
        Player player = null;
        if (remover instanceof Player) {
            player = (Player) remover;
        } else if (remover instanceof Projectile) {
            final Projectile projectile = (Projectile) remover;
            final ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player) {
                player = (Player) shooter;
            }
        }
        if (player != null) {
            breakBlock(player, event.getEntity().getLocation(), event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        breakBlock(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    private void breakBlock(Player player, Location location, Cancellable cancellable) {
        if (!plugin.allowAction(location, player, FlagManager.DESTROY)) {
            cancellable.setCancelled(true);
            player.sendMessage(Config.getMessage("FlagDeny", FlagManager.DESTROY.getDescription()));
        }
    }
}
