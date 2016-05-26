package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;

public class AdminCommand extends CommandSub {
	public AdminCommand(AreaProtect plugin) {
		super(plugin, "admin", "areaprotect.admin", "Commands.Admin");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		final Player player = (Player) sender;
		if (plugin.isAdminMode(player)) {
			plugin.deactivateAdminMode(player);
			player.sendMessage(Config.getMessage("AdminDeactivate"));
		} else {
			plugin.activateAdminMode(player);
			player.sendMessage(Config.getMessage("AdminActivate"));
		}
		return true;
	}
}
