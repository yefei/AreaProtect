package cn.minecon.areaprotect.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.FlagManager;

public class InteractListener implements Listener {
	final private AreaProtect plugin;

	public InteractListener(AreaProtect plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            pressure(player, block, event);
            return;
        }
        if (player.getItemInHand().getType() == Config.getSelectionTool()) {
            select(player, block, event);
        }
        if (event.isCancelled()) {
            return;
        }
        interact(player, block, event);
    }
    
    private void interact(Player player, Block block, PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        final Flag flag = getFlag(block.getType());
        if (flag == null) {
            return;
        }
        if (!plugin.allowAction(block.getLocation(), player, flag)) {
            event.setCancelled(true);
            player.sendMessage(Config.getMessage("FlagDeny", flag.getDescription()));
        }
    }

    private Flag getFlag(Material mat) {
        switch (mat) {
            case CHEST:
            case TRAPPED_CHEST:
                return FlagManager.CHEST;
            case FURNACE:
            case BURNING_FURNACE:
                return FlagManager.FURNACE;
            case BREWING_STAND:
                return FlagManager.BREW;
            case STONE_BUTTON:
            // 木质按钮可以被弓箭触发, 直接取消权限判断
            // case WOOD_BUTTON:
                return FlagManager.BUTTON;
            case LEVER:
                return FlagManager.LEVER;
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
            case DIODE:
                return FlagManager.DIODE;
            case CAKE_BLOCK:
                return FlagManager.CAKE;
            case DRAGON_EGG:
                return FlagManager.DRAGONEGG;
            case FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case ACACIA_FENCE_GATE:
                return FlagManager.FENCEGATE;
            case WOODEN_DOOR:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
            case IRON_DOOR_BLOCK:
                return FlagManager.HINGEDDOOR;
            case TRAP_DOOR:
                return FlagManager.TRAPDOOR;
            case ANVIL:
                return FlagManager.ANVIL;
            case BED:
            case BED_BLOCK:
                return FlagManager.BED;
            case ENCHANTMENT_TABLE:
                return FlagManager.ENCHANTMENTTABLE;
            case ENDER_CHEST:
                return FlagManager.ENDERCHEST;
            case WORKBENCH:
                return FlagManager.WORKBENCH;
            case HOPPER:
                return FlagManager.HOPPER;
            case DROPPER:
                return FlagManager.DROPPER;
            case DISPENSER:
                return FlagManager.DISPENSER;
            default:
                return null;
        }
    }
    
    private void select(Player player, Block block, PlayerInteractEvent event) {
        if (plugin.isAdminMode(player) || player.hasPermission("areaprotect.select")) {
	        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
	            final Location loc = block.getLocation();
	            player.sendMessage(Config.getMessage("Select.PrimaryPoint", loc.getBlockX(), loc.getBlockZ()));
	            plugin.getSelectionManager().placePrimaryLoc(player, loc);
	        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	            final Location loc = block.getLocation();
	            player.sendMessage(Config.getMessage("Select.SecondaryPoint", loc.getBlockX(), loc.getBlockZ()));
	            plugin.getSelectionManager().placeSecondaryLoc(player, loc);
	        }
	        event.setCancelled(true);
        }
    }
    
    private void pressure(Player player, Block block, PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        final Material mat = block.getType();
        if (mat == Material.SOIL || mat == Material.SOUL_SAND) {
            if (!plugin.allowAction(block.getLocation(), player, FlagManager.TRAMPLE)) {
                event.setCancelled(true);
                return;
            }
            return;
        }
        /*
         * 无效的压力板, 详见 FlagManager 里面说明
        if (block.getType() != Material.STONE_PLATE &&
        	block.getType() != Material.WOOD_PLATE &&
        	block.getType() != Material.GOLD_PLATE &&
        	block.getType() != Material.IRON_PLATE) {
            return;
        }
        if (!plugin.allowAction(block.getLocation(), player, FlagManager.PRESSUREPLATE)) {
            event.setCancelled(true);
            // player.sendMessage(Config.getMessage("FlagDeny", FlagManager.PRESSUREPLATE.getDescription()));
        }
        */
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onClick(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final Entity ent = event.getRightClicked();
        if (ent == null) {
            return;
        }
        if (ent.getType() != EntityType.ITEM_FRAME) {
            return;
        }
        if (!plugin.allowAction(ent.getLocation(), player, FlagManager.ITEMFRAME)) {
            event.setCancelled(true);
            player.sendMessage(Config.getMessage("FlagDeny", FlagManager.ITEMFRAME.getDescription()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityInteract(EntityInteractEvent event) {
        final Block block = event.getBlock();
        final Material mat = block.getType();
        final Entity entity = event.getEntity();
        if ((entity.getType() == EntityType.FALLING_BLOCK) || !(mat == Material.SOIL || mat == Material.SOUL_SAND)) {
            return;
        }
        if (!plugin.allowAction(block.getLocation(), FlagManager.TRAMPLE)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
    	final Player player = event.getPlayer();
		final Entity ent = event.getRightClicked();
        if (ent == null) {
            return;
        }
        if (!plugin.allowAction(ent.getLocation(), player, FlagManager.ARMORSTAND)) {
            event.setCancelled(true);
            player.sendMessage(Config.getMessage("FlagDeny", FlagManager.ARMORSTAND.getDescription()));
        }
	}
}
