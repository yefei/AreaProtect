package cn.minecon.areaprotect.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.area.Area;

public class MyCommand extends CommandSub {
	public MyCommand(AreaProtect plugin) {
		super(plugin, "my", "areaprotect.my", "Commands.My");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if (args.length == 1) {
			sender.sendMessage(Config.getMessage("My.Header"));
			final StringBuilder result = new StringBuilder();
			final List<Area> areas = AreaProtect.getInstance().getAreaManager().getByPlayer((Player) sender);
			for (Area area : areas) {
				result.append(area.getName());
				result.append(' ');
			}
			sender.sendMessage(Config.getMessage("My.Result", result.toString()));
			sender.sendMessage(Config.getMessage("My.Footer", areas.size()));
			return true;
		}
		return false;
	}
}
