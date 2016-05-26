package cn.minecon.areaprotect.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.FlagManager;

public class BucketListener implements Listener {
	final private AreaProtect plugin;

	public BucketListener(AreaProtect plugin) {
		this.plugin = plugin;
	}

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Flag flag = null;
        if (event.getBucket() == Material.LAVA_BUCKET) {
            flag = FlagManager.LAVABUCKET;
        }
        if (event.getBucket() == Material.WATER_BUCKET) {
            flag = FlagManager.WATERBUCKET;
        }
        if (flag == null) {
            return;
        }
        final Player player = event.getPlayer();
        final BlockFace face = event.getBlockFace();
        final Location blockLocation = event.getBlockClicked().getLocation().add(face.getModX(), face.getModY(), face.getModZ());
        if (!plugin.allowAction(blockLocation, player, flag)) {
            event.setCancelled(true);
            player.sendMessage(Config.getMessage("FlagDeny", flag.getDescription()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Flag flag = null;
        if (event.getBlockClicked().getType() == Material.LAVA) {
            flag = FlagManager.LAVABUCKET;
        }
        if (event.getBlockClicked().getType() == Material.WATER) {
            flag = FlagManager.WATERBUCKET;
        }
        if (flag == null) {
            return;
        }
        final Player player = event.getPlayer();
        if (!plugin.allowAction(event.getBlockClicked().getLocation(), player, flag)) {
            event.setCancelled(true);
            player.sendMessage(Config.getMessage("FlagDeny", flag.getDescription()));
        }
    }
}
