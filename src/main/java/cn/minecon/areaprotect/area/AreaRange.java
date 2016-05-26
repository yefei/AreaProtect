package cn.minecon.areaprotect.area;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

public class AreaRange {
	protected World world;
	protected int highX;
    protected int highZ;
    protected int lowX;
    protected int lowZ;
    
    public AreaRange(World world, int highX, int lowX, int highZ, int lowZ) {
    	this.world = world;
    	this.highX = highX;
    	this.lowX = lowX;
    	this.highZ = highZ;
    	this.lowZ = lowZ;
    }
    
    public AreaRange(AreaRange areaRange) {
    	this.world = areaRange.getWorld();
    	this.highX = areaRange.getHighX();
    	this.lowX = areaRange.getLowX();
    	this.highZ = areaRange.getHighZ();
    	this.lowZ = areaRange.getLowZ();
    }
    
    public AreaRange(Location startLoc, Location endLoc) {
    	this.world = startLoc.getWorld();
		if (startLoc.getBlockX() > endLoc.getBlockX()) {
            highX = startLoc.getBlockX();
            lowX = endLoc.getBlockX();
        } else {
            highX = endLoc.getBlockX();
            lowX = startLoc.getBlockX();
        }
        if (startLoc.getBlockZ() > endLoc.getBlockZ()) {
            highZ = startLoc.getBlockZ();
            lowZ = endLoc.getBlockZ();
        } else {
            highZ = endLoc.getBlockZ();
            lowZ = startLoc.getBlockZ();
        }
	}
    
    /**
     * 检测给定的选区是否包含在当前选区内
     */
    public boolean isAreaWithin(AreaRange area) {
        if (!area.getWorld().equals(world)) {
            return false;
        }
        return (containsLocation(area.getHighX(), area.getHighZ()) && containsLocation(area.getLowX(), area.getLowZ()));
    }
    
    /**
     * 检测选区是否与当前选区冲撞
     */
    public boolean checkCollision(AreaRange area) {
    	if (!area.getWorld().equals(world)) {
            return false;
        }
        if (area.containsLocation(world, highX, highZ) ||
        	area.containsLocation(world, lowX, lowZ) ||
        	containsLocation(area.getHighX(), area.getHighZ()) ||
        	containsLocation(area.getLowX(), area.getLowZ())) {
            return true;
        }
        return advCuboidCheckCollision(area);
    }

    private boolean advCuboidCheckCollision(AreaRange area) {
        if ((highX >= area.getLowX() && highX <= area.getHighX()) ||
        	(lowX >= area.getLowX() && lowX <= area.getHighX()) ||
        	(area.getHighX() >= lowX && area.getHighX() <= highX) ||
        	(area.getLowX() >= lowX && area.getLowX() <= highX)) {
            if ((highZ >= area.getLowZ() && highZ <= area.getHighZ()) ||
            	(lowZ >= area.getLowZ() && lowZ <= area.getHighZ()) ||
            	(area.getHighZ() >= lowZ && area.getHighZ() <= highZ) ||
            	(area.getLowZ() >= lowZ && area.getLowZ() <= highZ)) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * 检测给定的 Location 是否属于区域内
	 */
	public boolean containsLocation(Location loc) {
		if (loc == null) {
            return false;
        }
        return containsLocation(loc.getWorld(), loc.getBlockX(), loc.getBlockZ());
	}
    
    /**
	 * 检测给定的坐标是否属于区域内
	 */
    public boolean containsLocation(World world, int x, int z) {
        if (!world.equals(this.world)) {
            return false;
        }
        return containsLocation(x, z);
    }
    
    private boolean containsLocation(int x, int z) {
    	if (lowX <= x && highX >= x) {
            if (lowZ <= z && highZ >= z) {
            	return true;
            }
        }
        return false;
    }
    
    /**
     * 取得所属世界
     */
    public World getWorld() {
    	return world;
    }
    
    public int getHighX() {
		return highX;
	}

	public int getHighZ() {
		return highZ;
	}

	public int getLowX() {
		return lowX;
	}

	public int getLowZ() {
		return lowZ;
	}
    
    /**
     * 取得区域面积
     */
    public long getSize() {
    	return getXSize() * getZSize();
    }
    
    /**
     * 取得区域 X 长度
     */
    public int getXSize() {
    	return (highX - lowX) + 1;
    }
    
    /**
     * 取得区域 Z 长度
     */
    public int getZSize() {
    	return (highZ - lowZ) + 1;
    }
    
    /**
     * 取得区域的所有变量值, 主要用于价格公式计算
     */
    public Map<String, Double> getVariables() {
    	final Map<String, Double> variables = new HashMap<String, Double>();
        variables.put("XSize", (double) getXSize());
        variables.put("ZSize", (double) getZSize());
        return variables;
    }
}
