package cn.minecon.areaprotect.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.FlagManager;
import cn.minecon.areaprotect.TimedTeleport;
import cn.minecon.areaprotect.area.Area;

public class TPCommand extends CommandSub {
	public TPCommand(AreaProtect plugin) {
		super(plugin, "tp", "areaprotect.tp", "Commands.TP");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		// tp <area>
		if (args.length == 2) {
			final Player player = (Player) sender;
			final Area area = checkAndGetArea(player, args[1], false);
			if (!area.allowAction(player, FlagManager.TELEPORT)) {
				alert(Config.getMessage("FlagDeny", FlagManager.TELEPORT.getDescription()));
			}
			
			final Location loc = area.getTeleportLocation();
			
			if (loc == null) {
				alert(Config.getMessage("AreaNotSetTPLocation", area.getName()));
			}
			
			final String completeMessage = Config.getMessage("TPComplete", area.getName());
			
			if (Config.getTeleportDelay() > 0 && !AreaProtect.getInstance().isAdminMode(player)) {
				player.sendMessage(Config.getMessage("TPDelay", area.getName(), Config.getTeleportDelay()));
				new TimedTeleport(plugin, player, Config.getTeleportDelay() * 1000, loc, completeMessage);
			} else {
				player.teleport(loc, TeleportCause.COMMAND);
				player.sendMessage(completeMessage);
			}
			return true;
		}
		return false;
	}
}
