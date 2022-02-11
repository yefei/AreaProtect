package cn.minecon.areaprotect.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.area.Area;
import cn.minecon.areaprotect.utils.MessageColor;

public class InfoCommand extends CommandSub {
	public InfoCommand(AreaProtect plugin) {
		super(plugin, "info", "areaprotect.info", "Commands.Info");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		final Player player = (Player) sender;
		final Area area = checkAndGetArea(player, args.length == 2 ? args[1] : null, false);

		if (area != null) {
			player.sendMessage(Config.getMessage("Info.Header"));
			player.sendMessage(Config.getMessage("Info.Name", area.getName()));
			player.sendMessage(Config.getMessage("Info.Owner", area.getOwnerName()));
			player.sendMessage(Config.getMessage("Info.Size", area.getSize(), area.getXSize(), area.getZSize()));
			player.sendMessage(Config.getMessage("Info.CreationDate",
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(area.getCreationDate()))));
			player.sendMessage(Config.getMessage("Info.AreaFlags", getFlagsDisplay(area.getAreaFlags())));
			if (area.getPlayerFlags().size() > 0) {
				player.sendMessage(Config.getMessage("Info.PlayerFlags"));
				Map<UUID, Map<Flag, Boolean>> playerFlags = area.getPlayerFlags();
				for (UUID uuid : playerFlags.keySet()) {
					Map<Flag, Boolean> flags = playerFlags.get(uuid);
					if (flags == null || flags.size() == 0) continue;
					final OfflinePlayer p = AreaProtect.getInstance().getServer().getOfflinePlayer(uuid);
					player.sendMessage("  " + (p == null ? uuid.toString() : p.getName()) + ": " + getFlagsDisplay(flags));
				}
			} else {
				player.sendMessage(Config.getMessage("Info.PlayerFlags", Config.getMessage("NotFlags")));
			}
			return true;
		}

		return false;
	}

	private String getFlagsDisplay(Map<Flag, Boolean> flags) {
		if (flags == null) {
			return null;
		}
		StringBuilder out = new StringBuilder();
		for (Entry<Flag, Boolean> flag : flags.entrySet()) {
			if (flag.getValue()) {
				out.append("<GREEN>+");
			} else {
				out.append("<RED>-");
			}
			out.append(flag.getKey().getName());
			out.append(' ');
		}
		return out.length() == 0 ? Config.getMessage("NotFlags") : MessageColor.replaceColors(out.toString());
	}

}
