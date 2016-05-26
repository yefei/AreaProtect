package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.area.Area;

public class PlayerCommand extends CommandSub {
	public PlayerCommand(AreaProtect plugin) {
		super(plugin, "player", "areaprotect.flag.player", "Commands.Player");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if (args.length < 2) {
			return false;
		}
		
		final Player player = (Player) sender;
		
		// player set <player> <flag> yes/no [area]
		if (args[1].equalsIgnoreCase("set") && (args.length == 5 || args.length == 6)) {
			final Area area = checkAndGetArea(player, args.length == 6 ? args[5] : null);
			final Flag flag = checkAndGetFlag(args[3]);
			final Player targetPlayer = checkAndGetOnlinePlayer(args[2]);
			final boolean isYes = args[4].equalsIgnoreCase("yes");
			area.setPlayerFlag(targetPlayer, flag, isYes);
			area.save();
			player.sendMessage(Config.getMessage("SetPlayerFlag", targetPlayer.getName(), area.getName(),
					flag.getDescription(), Config.getMessage(isYes ? "Allow" : "Forbid")));
			return true;
		}
		
		// player remove <player> <flag> [area]
		if (args[1].equalsIgnoreCase("remove") && (args.length == 4 || args.length == 5)) {
			final Area area = checkAndGetArea(player, args.length == 5 ? args[4] : null);
			final Flag flag = checkAndGetFlag(args[3]);
			final Player targetPlayer = checkAndGetOnlinePlayer(args[2]);
			area.removePlayerFlag(targetPlayer, flag);
			area.save();
			player.sendMessage(Config.getMessage("RemovePlayerFlag", targetPlayer.getName(), area.getName(), flag.getDescription()));
			return true;
		}
		
		// player clear <player> [area]
		if (args[1].equalsIgnoreCase("remove") && (args.length == 3 || args.length == 4)) {
			final Area area = checkAndGetArea(player, args.length == 4 ? args[3] : null);
			final Player targetPlayer = checkAndGetOnlinePlayer(args[2]);
			area.clearPlayerFlags(targetPlayer);
			area.save();
			player.sendMessage(Config.getMessage("ClearPlayerFlag", targetPlayer.getName(), area.getName()));
			return true;
		}
		
		return false;
	}
	
}
