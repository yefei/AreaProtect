package cn.minecon.areaprotect.listeners;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.FlagManager;

public class PlaceListener implements Listener {
	final private AreaProtect plugin;

	public PlaceListener(AreaProtect plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        place(event.getPlayer(), event.getEntity().getLocation(), event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        place(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(PlayerInteractEvent event) {
    	final Player player = event.getPlayer();
        if (player.getItemInHand() == null) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final ItemStack item = event.getPlayer().getItemInHand();
        if (item == null) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        final Material mat = item.getType();
        if (block.getType() == Material.GRASS) {
            if (mat != Material.INK_SACK) {
                return;
            }
            if (((Dye) item.getData()).getColor() != DyeColor.WHITE) {
                return;
            }
            place(player, block.getLocation().add(0, 1, 0), event);
            return;
        }
        if (block.getType() == Material.FLOWER_POT) {
            if (mat != Material.SAPLING &&
            	mat != Material.RED_ROSE &&
            	mat != Material.YELLOW_FLOWER &&
            	mat != Material.RED_MUSHROOM &&
            	mat != Material.BROWN_MUSHROOM &&
            	mat != Material.DEAD_BUSH &&
            	mat != Material.CACTUS) {
                return;
            }
            place(player, block.getLocation().add(0, 0, 0), event);
        }
    }

    private void place(Player player, Location location, Cancellable cancellable) {
        if (!plugin.allowAction(location, player, FlagManager.PLACE)) {
            cancellable.setCancelled(true);
            player.sendMessage(Config.getMessage("FlagDeny", FlagManager.PLACE.getDescription()));
        }
    }
}
