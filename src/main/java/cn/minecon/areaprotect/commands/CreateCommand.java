package cn.minecon.areaprotect.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.SelectionManager;
import cn.minecon.areaprotect.area.Area;
import cn.minecon.areaprotect.area.AreaManager;
import cn.minecon.areaprotect.area.AreaRange;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class CreateCommand extends CommandSub {
	public CreateCommand(AreaProtect plugin) {
		super(plugin, "create", "areaprotect.create", "Commands.Create");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		final Player player = (Player) sender;
		if (args.length == 2) {
			final AreaManager areaManager = AreaProtect.getInstance().getAreaManager();
			
			// 是否达到最大领地数
			if (!plugin.isAdminMode(player) && areaManager.getByPlayer(player).size() >= Config.getMaxAreas()) {
				alert(Config.getMessage("Create.TooManyAreas"));
			}
			
			final SelectionManager selectionManager = plugin.getSelectionManager();
			// 检查选择是否合法
			if (!selectionManager.isValid(player)) {
				selectionManager.showSelectionInfo(player);
				return true;
			}
			
			// 检查名称是否符合要求是否存在
			final String name = args[1];
			final String vaildNameMessage = areaManager.validName(name);
			if (vaildNameMessage != null) {
				player.sendMessage(vaildNameMessage);
				return true;
			}
			
			final AreaRange areaRange = selectionManager.getSelectionAreaRange(player);
			
			// 选择的世界是否允许圈地
			if (!plugin.isAdminMode(player) && !Config.getAllowWorlds().contains(areaRange.getWorld().getName())) {
				alert(Config.getMessage("Create.NotAllowToCurrentWorld"));
			}
			
			// 扣钱操作
			if (!plugin.isAdminMode(player) && plugin.isEconomy()) {
				final Economy eco = plugin.getEconomy();
				final double charge = Config.getCostEquation().calculate(areaRange.getVariables());
				if (!eco.has(player, charge)) {
					alert(Config.getMessage("Create.NotEnoughMoney", eco.format(charge), eco.format(eco.getBalance(player))));
				}
				final EconomyResponse ecoResp = eco.withdrawPlayer(player, charge);
				if (!ecoResp.transactionSuccess()) {
					alert(ecoResp.errorMessage);
				}
				player.sendMessage(Config.getMessage("Create.Expense", eco.format(charge), eco.format(eco.getBalance(player))));
			}
			
			// 创建区域
			final Area area = areaManager.createArea(player, name, areaRange);
			if (area == null) {
				alert(Config.getMessage("Create.Failed"));
			}
			
			selectionManager.clearSelection(player);
			player.sendMessage(Config.getMessage("Create.Success", area.getName()));
			return true;
		}
		return false;
	}
}
