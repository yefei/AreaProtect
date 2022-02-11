package cn.minecon.areaprotect.area;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.events.AreaFlagsChangedEvent;
import cn.minecon.areaprotect.events.AreaOwnerChangedEvent;
import cn.minecon.areaprotect.events.AreaTeleportLocationChangedEvent;

public abstract class Area extends AreaRange {
	protected Map<Flag, Boolean> areaFlags;
	protected Map<UUID, Map<Flag, Boolean>> playerFlags;
	protected UUID uuid;
	protected String name;
	protected OfflinePlayer owner;
    protected Location teleportLocation;
    protected long creationDate;
    
    public Area(World world, int highX, int lowX, int highZ, int lowZ) {
		super(world, highX, lowX, highZ, lowZ);
	}
    
    public Area(AreaRange areaRange) {
		super(areaRange);
	}
    
    /**
     * 取得区域名称
     */
    public String getName(){
    	return name;
    }
    
    /**
     * 取得区域ID
     */
    public UUID getUUID() {
    	return uuid;
    }
    
    /**
     * 取得创建日期
     */
    public long getCreationDate() {
    	return creationDate;
    }
    
	/**
	 * 取得区域拥有人
	 */
	public OfflinePlayer getOwner() {
		return owner;
	}
	
	/**
	 * 取得区域拥有人
	 */
	public String getOwnerName() {
		return owner == null ? Config.getMessage("ServerLand.Name") : owner.getName();
	}
	
	/**
	 * 设置区域拥有人
	 */
	public void setOwner(OfflinePlayer player) {
		final OfflinePlayer previousOwner = getOwner();
		owner = player;
		AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaOwnerChangedEvent(this, previousOwner));
	}
	
	/**
	 * 检查玩家是否属于区域拥有人
	 */
	public boolean isOwner(OfflinePlayer player) {
		if (owner != null && owner.getUniqueId().equals(player.getUniqueId())) {
			return true;
		}
		return false;
	}
	
    /**
     * 取得传送点
     */
    public Location getTeleportLocation() {
    	return teleportLocation;
    }
    
    /**
     * 设置传送点
     */
    public boolean setTeleportLocation(Location location) {
    	if (containsLocation(location)) {
			teleportLocation = location;
			AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaTeleportLocationChangedEvent(this));
			return true;
        }
        return false;
    }
    
    /**
	 * 检查权限是否允许
	 */
	public boolean allowAction(Flag flag) {
        while (true) {
            Boolean bool = areaFlags.get(flag);
            if (bool != null) {
                return bool;
            }
            if (flag.getParent() != null) {
                flag = flag.getParent();
            } else {
                return false;
            }
        }
	}
	
	/**
	 * 检查玩家的权限是否允许
	 */
    public boolean allowAction(OfflinePlayer player, Flag flag) {
    	if (isOwner(player) || AreaProtect.getInstance().isAdminMode(player)) {
    		return true;
    	}
    	final Map<Flag, Boolean> flags = playerFlags.get(player.getUniqueId());
        while (true) {
            Boolean bool = null;
            if (flags != null) {
                bool = flags.get(flag);
                if (bool != null) {
                    return bool;
                }
            }
            bool = areaFlags.get(flag);
            if (bool != null) {
                return bool;
            }
            if (flag.getParent() != null) {
                flag = flag.getParent();
            } else {
                return false;
            }
        }
    }
    
    /**
     * 设置区域的权限
     */
    public void setAreaFlag(Flag flag, boolean value) {
    	if (flag == null) {
            return;
        }
        areaFlags.put(flag, value);
        AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 删除区域权限
     */
    public void removeAreaFlag(Flag flag) {
    	areaFlags.remove(flag);
        AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 取得区域的全部权限值
     */
    public Map<Flag, Boolean> getAreaFlags() {
    	return areaFlags;
    }
    
    /**
     * 清除全部区域权限
     */
    public void clearAreaFlags() {
    	areaFlags.clear();
		AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 清除所有权限,包括所有玩家的权限
     */
    public void clearAllFlags() {
    	areaFlags.clear();
        playerFlags.clear();
        AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 恢复成默认权限设置
     */
    public void applyDefaultFlags() {
    	areaFlags.clear();
        playerFlags.clear();
        for (Entry<Flag, Boolean> flag : Config.getDefaultAreaFlags().entrySet()) {
            areaFlags.put(flag.getKey(), flag.getValue());
        }
        AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 从目标领地复制权限设置
     */
    public void copyFlags(Area mirror) {
    	areaFlags = new HashMap<Flag, Boolean>(mirror.getAreaFlags());
        playerFlags = new HashMap<UUID, Map<Flag, Boolean>>();
        final Map<UUID, Map<Flag, Boolean>> flags = mirror.getPlayerFlags();
        for (UUID playerUUID : flags.keySet()) {
            playerFlags.put(playerUUID, new HashMap<Flag, Boolean>(flags.get(playerUUID)));
        }
        AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 取得指定玩家在此所特有的权限
     */
    public Map<Flag, Boolean> getPlayerFlags(OfflinePlayer player) {
    	return playerFlags.get(player.getUniqueId());
    }
    
    /**
     * 取得全部玩家的特有权限
     */
    public Map<UUID, Map<Flag, Boolean>> getPlayerFlags() {
    	return playerFlags;
    }
    
    /**
     * 清除指定玩家的全部特有权限
     */
    public void clearPlayerFlags(OfflinePlayer player) {
    	playerFlags.remove(player.getUniqueId());
		AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 设置玩家权限
     */
    public void setPlayerFlag(OfflinePlayer player, Flag flag, boolean value) {
    	Map<Flag, Boolean> flags = playerFlags.get(player.getUniqueId());
        if (flags == null) {
            flags = new HashMap<Flag, Boolean>();
            playerFlags.put(player.getUniqueId(), flags);
        }
        flags.put(flag, value);
        AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 删除玩家权限
     */
    public void removePlayerFlag(OfflinePlayer player, Flag flag) {
    	final Map<Flag, Boolean> flags = playerFlags.get(player.getUniqueId());
    	flags.remove(flag);
    	if (flags.isEmpty()) {
            playerFlags.remove(player.getUniqueId());
        }
    	AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaFlagsChangedEvent(this));
    }
    
    /**
     * 保存
     */
    public abstract void save();
}
