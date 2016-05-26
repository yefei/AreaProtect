package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.area.Area;

public class FlagCommand extends CommandSub {
	public FlagCommand(AreaProtect plugin) {
		super(plugin, "flag", "areaprotect.flag", "Commands.Flag");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if (args.length < 2) {
			return false;
		}
		
		final Player player = (Player) sender;
		
		// flag set <flag> yes/no [area]
		if (args[1].equalsIgnoreCase("set") && (args.length == 4 || args.length == 5)) {
			final Area area = checkAndGetArea(player, args.length == 5 ? args[4] : null);
			final Flag flag = checkAndGetFlag(args[2]);
			final boolean isYes = args[3].equalsIgnoreCase("yes");
			area.setAreaFlag(flag, isYes);
			area.save();
			player.sendMessage(Config.getMessage("SetAreaFlag", area.getName(), flag.getDescription(), Config.getMessage(isYes ? "Allow" : "Forbid")));
			return true;
		}
		
		// flag remove <flag> [area]
		if (args[1].equalsIgnoreCase("remove") && (args.length == 3 || args.length == 4)) {
			final Area area = checkAndGetArea(player, args.length == 4 ? args[3] : null);
			final Flag flag = checkAndGetFlag(args[2]);
			area.removeAreaFlag(flag);
			area.save();
			player.sendMessage(Config.getMessage("RemoveAreaFlag", area.getName(), flag.getDescription()));
			return true;
		}
		
		// flag reset [area]
		if (args[1].equalsIgnoreCase("reset") && (args.length == 2 || args.length == 3)) {
			final Area area = checkAndGetArea(player, args.length == 3 ? args[2] : null);
			area.applyDefaultFlags();
			area.save();
			player.sendMessage(Config.getMessage("ResetAreaFlag", area.getName()));
			return true;
		}
		
		// flag clear [area]
		if (args[1].equalsIgnoreCase("clear") && (args.length == 2 || args.length == 3)) {
			final Area area = checkAndGetArea(player, args.length == 3 ? args[2] : null);
			area.clearAllFlags();
			area.save();
			player.sendMessage(Config.getMessage("ClearAreaFlag", area.getName()));
			return true;
		}
		
		// flag copy <traget area> [area]
		if (args[1].equalsIgnoreCase("copy") && (args.length == 3 || args.length == 4)) {
			final Area targetArea = AreaProtect.getInstance().getAreaManager().getByName(args[2]);
			final Area area = checkAndGetArea(player, args.length == 4 ? args[3] : null);
			if (targetArea.equals(area)) {
				alert(Config.getMessage("CannotCopySelfFlag"));
			}
			area.copyFlags(targetArea);
			area.save();
			player.sendMessage(Config.getMessage("CopyAreaFlag", targetArea.getName(), area.getName()));
			return true;
		}
		
		return false;
	}
}
