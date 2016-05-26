package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.FlagManager;
import cn.minecon.areaprotect.area.Area;

public abstract class CommandSub {
	final protected AreaProtect plugin;
	final private String command;
	final private String permissionNode;
	final private String helpKey;
	final private boolean allowConsoleSender;
	
    public CommandSub(AreaProtect plugin, String command, String permissionNode, String helpKey, boolean allowConsoleSender) {
        this.plugin = plugin;
        this.command = command;
        this.permissionNode = permissionNode;
        this.helpKey = helpKey;
        this.allowConsoleSender = allowConsoleSender;
    }
    
    public CommandSub(AreaProtect plugin, String command, String permissionNode, String helpKey) {
    	this(plugin, command, permissionNode, helpKey, false);
    }

    public String getCommand() {
    	return command;
    }

    public String getPermissionNode() {
    	return permissionNode;
    }

    public String getHelp() {
    	return helpKey;
    }

    public boolean allowConsoleSender() {
    	return allowConsoleSender;
    }

    public abstract boolean onCommand(CommandSender sender, String[] args);
    
    /**
     * 提示信息并终止执行
     */
    public void alert(String message) {
    	throw new CommandAlert(message);
    }
    
    /**
     * 检查玩家是否是区域拥有人
     */
    public void checkOwner(Area area, Player player) {
    	if (AreaProtect.getInstance().isAdminMode(player) || area.isOwner(player)) {
    		return;
    	}
    	alert(Config.getMessage("YouNotOwner"));
    }
    
    /**
     * 取得区域
     */
    public Area checkAndGetArea(Player player, String name, boolean isCheckOwner) {
		Area area = null;
		if (name != null) {
			area = AreaProtect.getInstance().getAreaManager().getByName(name);
			if (area == null) {
				alert(Config.getMessage("AreaDoesNotExist", name));
			}
		} else {
			area = AreaProtect.getInstance().getAreaManager().getByLocation(player.getLocation());
			if (area == null) {
				alert(Config.getMessage("NotInArea"));
			}
		}
		if (isCheckOwner) {
			checkOwner(area, player);
		}
		return area;
	}
    
    /**
     * 取得区域并检查是否是区域拥有人
     */
    public Area checkAndGetArea(Player player, String name) {
		return checkAndGetArea(player, name, true);
	}
    
    /**
     * 检查并取得 Flag
     */
    public Flag checkAndGetFlag(String flagName) {
		final Flag flag = FlagManager.getFlag(flagName);
		if (flag == null) {
			alert(Config.getMessage("FlagDoesNotExist", flagName));
		}
		return flag;
	}
    
    /**
     * 检查并取得在线玩家
     */
    public Player checkAndGetOnlinePlayer(String name) {
    	for (Player player : AreaProtect.getInstance().getServer().getOnlinePlayers()) {
    		if (player.getName().equalsIgnoreCase(name)) {
    			return player;
    		}
    	}
    	alert(Config.getMessage("PlayerNotOnline", name));
    	return null;
    }
}
