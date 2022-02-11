package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.area.Area;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class TPSetCommand extends CommandSub {
	public TPSetCommand(AreaProtect plugin) {
		super(plugin, "tpset", "areaprotect.tp.set", "Commands.TPSet");
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		// tpset
		final Player player = (Player) sender;
		final Area area = checkAndGetArea(player, null);
		
		// 首次开启需要购买
		if (area.getTeleportLocation() == null && !plugin.isAdminMode(player) && plugin.isEconomy()) {
			if (Config.getTeleportEnableCost() > 0) {
				final Economy eco = plugin.getEconomy();
				if (!eco.has(player, Config.getTeleportEnableCost())) {
					alert(Config.getMessage("TPSet.NotEnoughMoney", eco.format(Config.getTeleportEnableCost()), eco.format(eco.getBalance(player))));
				}
				final EconomyResponse ecoResp = eco.withdrawPlayer(player, Config.getTeleportEnableCost());
				if (!ecoResp.transactionSuccess()) {
					alert(ecoResp.errorMessage);
				}
				player.sendMessage(Config.getMessage("TPSet.Expense", eco.format(Config.getTeleportEnableCost()), eco.format(eco.getBalance(player))));
			}
		}
		
		area.setTeleportLocation(player.getLocation());
		area.save();
		player.sendMessage(Config.getMessage("TPSet.Complete", area.getName()));
		return true;
	}
}
