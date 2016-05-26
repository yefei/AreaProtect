package cn.minecon.areaprotect;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.area.Area;
import cn.minecon.areaprotect.area.AreaManager;
import cn.minecon.areaprotect.area.AreaRange;
import cn.minecon.areaprotect.area.AreaRangeSee;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager {
	final private AreaProtect plugin;
	final private Map<UUID, Location> playerPrimaryLocs;
	final private Map<UUID, Location> playerSecondaryLocs;
	final private Map<UUID, AreaRangeSee> playerRangeSeeTasks;
    
    public SelectionManager(AreaProtect plugin) {
    	this.plugin = plugin;
    	playerPrimaryLocs = new HashMap<UUID, Location>();
    	playerSecondaryLocs = new HashMap<UUID, Location>();
    	playerRangeSeeTasks = new HashMap<UUID, AreaRangeSee>();
    }

    public void placePrimaryLoc(Player player, Location loc) {
        if (loc != null) {
        	playerPrimaryLocs.put(player.getUniqueId(), loc);
        	showSelectionInfo(player);
        }
    }

    public void placeSecondaryLoc(Player player, Location loc) {
        if (loc != null) {
        	playerSecondaryLocs.put(player.getUniqueId(), loc);
        	showSelectionInfo(player);
        }
    }
    
    public Location getPlayerPrimaryLoc(Player player) {
        return playerPrimaryLocs.get(player.getUniqueId());
    }

    public Location getPlayerSecondaryLoc(Player player) {
        return playerSecondaryLocs.get(player.getUniqueId());
    }
    
    public boolean hasPlacedBoth(Player player) {
    	return playerPrimaryLocs.containsKey(player.getUniqueId()) && playerSecondaryLocs.containsKey(player.getUniqueId());
    }
    
    public AreaRange getSelectionAreaRange(Player player) {
    	if (hasPlacedBoth(player)) {
    		return new AreaRange(getPlayerPrimaryLoc(player), getPlayerSecondaryLoc(player));
    	}
    	return null;
    }
    
    public boolean isValid(Player player) {
    	if (!hasPlacedBoth(player)) {
    		return false;
    	}
    	final AreaManager areaManager = AreaProtect.getInstance().getAreaManager();
    	final AreaRange areaRange = getSelectionAreaRange(player);
    	final Area area = areaManager.getCollision(areaRange);
    	return area == null &&
    			areaRange.getXSize() >= Config.getMinXSize() && areaRange.getXSize() <= Config.getMaxXSize() &&
    			areaRange.getZSize() >= Config.getMinZSize() && areaRange.getZSize() <= Config.getMaxZSize();
    }

    public void showSelectionInfo(Player player) {
    	final AreaRangeSee see = playerRangeSeeTasks.remove(player.getUniqueId());
		if (see != null) {
			see.cancelTimer();
		}
    	
    	if (!playerPrimaryLocs.containsKey(player.getUniqueId())) {
    		player.sendMessage(Config.getMessage("Select.NotPrimaryPoint"));
    		return;
    	}
    	if (!playerSecondaryLocs.containsKey(player.getUniqueId())) {
    		player.sendMessage(Config.getMessage("Select.NotSecondaryPoint"));
    		return;
    	}
    	
    	final AreaRange areaRange = getSelectionAreaRange(player);
    	
    	// 尺寸检查
    	if (areaRange.getXSize() < Config.getMinXSize()) {
    		player.sendMessage(Config.getMessage("Select.XSizeGTE", Config.getMinXSize(), areaRange.getXSize()));
    		return;
    	}
    	if (areaRange.getXSize() > Config.getMaxXSize()) {
    		player.sendMessage(Config.getMessage("Select.XSizeLTE", Config.getMaxXSize(), areaRange.getXSize()));
    		return;
    	}
    	if (areaRange.getZSize() < Config.getMinZSize()) {
    		player.sendMessage(Config.getMessage("Select.ZSizeGTE", Config.getMinZSize(), areaRange.getZSize()));
    		return;
    	}
    	if (areaRange.getZSize() > Config.getMaxZSize()) {
    		player.sendMessage(Config.getMessage("Select.ZSizeLTE", Config.getMaxZSize(), areaRange.getZSize()));
    		return;
    	}
    	
    	// 检查区域冲突
    	final AreaManager areaManager = AreaProtect.getInstance().getAreaManager();
    	final Area area = areaManager.getCollision(areaRange);
    	if (area != null) {
    		player.sendMessage(Config.getMessage("Select.AreaCollision", area.getName()));
    		return;
    	}
    	
    	// 显示选择的区域
		playerRangeSeeTasks.put(player.getUniqueId(), new AreaRangeSee(plugin, player, areaRange));
    	
		// 显示区域信息
    	player.sendMessage(Config.getMessage("Select.SizeX", areaRange.getXSize()));
        player.sendMessage(Config.getMessage("Select.SizeZ", areaRange.getZSize()));
    	player.sendMessage(Config.getMessage("Select.Size", areaRange.getSize()));
    	
    	if (plugin.isEconomy()) {
    		player.sendMessage(Config.getMessage("Select.Cost", plugin.getEconomy().format(Config.getCostEquation().calculate(areaRange.getVariables()))));
    	}
    }

    public void clearSelection(Player player) {
        playerPrimaryLocs.remove(player.getUniqueId());
        playerSecondaryLocs.remove(player.getUniqueId());
        AreaRangeSee see = playerRangeSeeTasks.remove(player.getUniqueId());
		if (see != null) {
			see.cancelTimer();
		}
    }
    
    public void selectBySize(Player player, int xsize, int zsize) {
    	if (xsize < 1 || zsize < 1) {
    		player.sendMessage(Config.getMessage("Select.BySizeMustGreaterZero"));
    		return;
    	}
    	final Location myloc = player.getLocation();
    	final int x1 = xsize / 2;
    	final int z1 = zsize / 2;
    	final int x2 = x1 * 2 != xsize ? x1 : x1 - 1; // 判断数值是否能被整除
    	final int z2 = z1 * 2 != zsize ? z1 : z1 - 1;
    	final Location loc1 = new Location(myloc.getWorld(), myloc.getBlockX() + x1, myloc.getBlockY(), myloc.getBlockZ() + z1);
    	final Location loc2 = new Location(myloc.getWorld(), myloc.getBlockX() - x2, myloc.getBlockY(), myloc.getBlockZ() - z2);
        playerPrimaryLocs.put(player.getUniqueId(), loc1);
        playerSecondaryLocs.put(player.getUniqueId(), loc2);
        showSelectionInfo(player);
    }
}
