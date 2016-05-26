package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.area.Area;

public class SetOwnerCommand extends CommandSub {
	public SetOwnerCommand(AreaProtect plugin) {
		super(plugin, "setowner", "areaprotect.setowner", "Commands.SetOwner");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		// setowner <traget player> [area]
		if (args.length == 2 || args.length == 3) {
			final Player player = (Player) sender;
			final Area area = checkAndGetArea(player, args.length == 3 ? args[2] : null);
			final Player targetPlayer = checkAndGetOnlinePlayer(args[1]);
			area.setOwner(targetPlayer);
			area.save();
			player.sendMessage(Config.getMessage("SetOwner.Success", area.getName(), targetPlayer.getName()));
			return true;
		}
		return false;
	}
}
