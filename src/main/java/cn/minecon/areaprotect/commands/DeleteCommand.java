package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.area.Area;

public class DeleteCommand extends CommandSub {
	public DeleteCommand(AreaProtect plugin) {
		super(plugin, "delete", "areaprotect.delete", "Commands.Delete");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		final Player player = (Player) sender;
		if (args.length == 2) {
			final Area area = checkAndGetArea(player, args[1]);
			AreaProtect.getInstance().getAreaManager().deleteArea(area);
			player.sendMessage(Config.getMessage("DeleteAreaComplete", area.getName()));
			return true;
		}
		return false;
	}
}
