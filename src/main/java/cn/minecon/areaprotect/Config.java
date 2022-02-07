package cn.minecon.areaprotect;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import cn.minecon.areaprotect.utils.MessageColor;
import cn.minecon.areaprotect.utils.equation.Equation;
import cn.minecon.areaprotect.utils.equation.EquationParser;

public class Config {
	private static FileConfiguration config;
	private static boolean debug = false;
	private static Material selectionTool;
	private static boolean stopOnLoadError;
	private static List<String> allowWorlds;
	private static int nameMinLength;
	private static int nameMaxLength;
	private static List<String> nameRetains;
	private static int teleportDelay;
	private static int teleportEnableCost;
	private static Map<Flag, Boolean> defaultAreaFlags;
	private static Equation costEquation;
	private static boolean enabledEconomy;
	private static boolean enterLeaveMessages;
	private static int maxAreas;
	private static int minXSize;
	private static int minZSize;
	private static int maxXSize;
	private static int maxZSize;

	public Config(FileConfiguration config) {
		Config.config = config;
		debug = config.getBoolean("Debug", false);
		selectionTool = Material.matchMaterial(config.getString("SelectionTool"));
		stopOnLoadError = config.getBoolean("ShutdownIfFailLoad", true);
		allowWorlds = config.getStringList("AllowWorlds");
		nameMinLength = config.getInt("Name.MinLength");
		nameMaxLength = config.getInt("Name.MaxLength");
		nameRetains = config.getStringList("Name.Retains");
		teleportDelay = config.getInt("TeleportDelay");
		teleportEnableCost = config.getInt("TeleportEnableCost");
		costEquation = EquationParser.parse(config.getString("CostEquation"));
		enabledEconomy = config.getBoolean("EnabledEconomy", true);
		enterLeaveMessages = config.getBoolean("EnterLeaveMessages", true);
		maxAreas = config.getInt("MaxAreas");
		minXSize = config.getInt("MinXSize");
		minZSize = config.getInt("MinZSize");
		maxXSize = config.getInt("MaxXSize");
		maxZSize = config.getInt("MaxZSize");
	}

	public static boolean isDebug() {
		return debug;
	}

	public static Material getSelectionTool() {
		return selectionTool;
	}

	public static boolean isStopOnLoadError() {
		return stopOnLoadError;
	}

	public static List<String> getAllowWorlds() {
		return allowWorlds;
	}

	public static int getTeleportDelay() {
		return teleportDelay;
	}

	public static int getTeleportEnableCost() {
		return teleportEnableCost;
	}

	public static Equation getCostEquation() {
		return costEquation;
	}

	public static boolean isEnabledEconomy() {
		return enabledEconomy;
	}

	public static boolean isEnterLeaveMessages() {
		return enterLeaveMessages;
	}

	public static int getMaxAreas() {
		return maxAreas;
	}

	public static int getMinXSize() {
		return minXSize;
	}

	public static int getMinZSize() {
		return minZSize;
	}

	public static int getMaxXSize() {
		return maxXSize;
	}

	public static int getMaxZSize() {
		return maxZSize;
	}

	public synchronized static Map<Flag, Boolean> getDefaultAreaFlags() {
		if (defaultAreaFlags == null) {
			defaultAreaFlags = new HashMap<Flag, Boolean>();
			final ConfigurationSection flags = config.getConfigurationSection("DefaultAreaFlags");
			for (String key : flags.getKeys(false)) {
				final Flag flag = FlagManager.getFlag(key);
				if (flag != null) {
					defaultAreaFlags.put(flag, flags.getBoolean(key));
				}
			}
		}
		return defaultAreaFlags;
	}

	public static String getMessage(final String key, final Object... args) {
		final String path = "Messages." + key;
		String message = config.getString(path);

		if (message == null) {
			return ChatColor.DARK_GRAY + "** Miss Messages: " + path;
		}

		if (args != null) {
			MessageFormat formatter = new MessageFormat(message);
			message = formatter.format(args);
		}

		return MessageColor.replaceColors(message);
	}

	public static FileConfiguration getConfig() {
		return config;
	}

	public static int getNameMinLength() {
		return nameMinLength;
	}

	public static int getNameMaxLength() {
		return nameMaxLength;
	}

	public static List<String> getNameRetains() {
		return nameRetains;
	}
}
