package cn.minecon.areaprotect.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.FlagManager;
import cn.minecon.areaprotect.area.Area;

public class PistonListener implements Listener {
	final private AreaProtect plugin;

	public PistonListener(AreaProtect plugin) {
		this.plugin = plugin;
	}

	// 活塞拉回
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
    	// 忽略非黏性的
    	if (!event.isSticky()) {
            return;
        }
    	
    	final Area pistonArea = plugin.getAreaManager().getByLocation(event.getBlock().getLocation());
    	
    	for (Block block : event.getBlocks()) {
    		// Is the retracted block air/water/lava? Don't worry about it
    		if (block.isEmpty() || block.isLiquid()) return;
    		
    		// 被推动的目标方块所属区域
    		final Area targetArea = plugin.getAreaManager().getByLocation(block.getLocation());
        	if (targetArea == pistonArea) continue;
        	
        	// 检查目标区域是否允许使用活塞
        	if (targetArea.allowAction(FlagManager.PISTON)) continue;
        	
        	event.setCancelled(true);
			return;
    	}
    }
    
    // 活塞推出
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
    	final Area pistonArea = plugin.getAreaManager().getByLocation(event.getBlock().getLocation());
    	
        for (Block block : event.getBlocks()) {
        	// 被推动的目标方块
        	final Block targetBlock = block.getRelative(event.getDirection());
        	
        	// 被推动的目标方块所属区域
        	final Area targetArea = plugin.getAreaManager().getByLocation(targetBlock.getLocation());
        	if (targetArea == pistonArea) continue;
        	
        	// 检查目标区域是否允许使用活塞
        	if (targetArea.allowAction(FlagManager.PISTON)) continue;
        	
        	event.setCancelled(true);
			return;
        }
    }
}
