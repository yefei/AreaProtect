package cn.minecon.areaprotect.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.area.Area;

public class ListCommand extends CommandSub {
	public ListCommand(AreaProtect plugin) {
		super(plugin, "list", "areaprotect.list", "Commands.List", true);
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if (args.length == 1) {
			sender.sendMessage(Config.getMessage("List.Header"));
			final StringBuilder result = new StringBuilder();
			final List<Area> areas = AreaProtect.getInstance().getAreaManager().getAll();
			for (Area area : areas) {
				result.append(area.getName());
				result.append(' ');
			}
			sender.sendMessage(Config.getMessage("List.Result", result.toString()));
			sender.sendMessage(Config.getMessage("List.Footer", areas.size()));
			return true;
		}
		return false;
	}
}
