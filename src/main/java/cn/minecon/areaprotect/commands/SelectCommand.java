package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;

public class SelectCommand extends CommandSub {
	public SelectCommand(AreaProtect plugin) {
		super(plugin, "select", "areaprotect.select", "Commands.Select");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		final Player player = (Player) sender;
		if (args.length == 1) {
			plugin.getSelectionManager().showSelectionInfo(player);
			return true;
		} else if (args.length == 3) {
			int xsize, zsize;
			try {
				xsize = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				player.sendMessage(Config.getMessage("InvalidNumber", args[1]));
				return true;
			}
			try {
				zsize = Integer.parseInt(args[2]);
			} catch (Exception ex) {
				player.sendMessage(Config.getMessage("InvalidNumber", args[2]));
				return true;
			}
			plugin.getSelectionManager().selectBySize(player, xsize, zsize);
			return true;
		}
		return false;
	}
}
