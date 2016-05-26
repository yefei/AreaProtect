package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.area.Area;

public class ServerLandCommand extends CommandSub {
	public ServerLandCommand(AreaProtect plugin) {
		super(plugin, "serverland", "areaprotect.serverland", "Commands.ServerLand");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		// serverland [area]
		if (args.length == 1 || args.length == 2) {
			final Player player = (Player) sender;
			final Area area = checkAndGetArea(player, args.length == 2 ? args[1] : null);
			area.setOwner(null);
			area.save();
			player.sendMessage(Config.getMessage("ServerLand.Success", area.getName()));
			return true;
		}
		return false;
	}
}
