package cn.minecon.areaprotect.storage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.area.Area;
import cn.minecon.areaprotect.area.AreaManager;
import cn.minecon.areaprotect.area.AreaRange;
import cn.minecon.areaprotect.events.AreaCreatedEvent;
import cn.minecon.areaprotect.events.AreaDeletedEvent;
import cn.minecon.areaprotect.utils.Log;

public class YAMLAreaManager implements AreaManager {
	final private Map<String, Area> areasByName;
	final private File saveFolder;

	public YAMLAreaManager(File dataFolder) throws Exception {
		areasByName = new HashMap<String, Area>();

		// 区域数据保存文件夹
		saveFolder = new File(dataFolder, "save");
		if (!saveFolder.isDirectory()) {
			saveFolder.mkdirs();
		}

		for (World world : AreaProtect.getInstance().getServer().getWorlds()) {
			final File worldFolder = new File(saveFolder, world.getName());
			if (!worldFolder.isDirectory()) {
				continue;
			}
			// 遍历文件夹
			int loads = 0;
			for (File file : worldFolder.listFiles(new AreaFileFilter())) {
				final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				final Area area = new YAMLArea(this, world, config);
				addArea(area);
				loads++;
			}
			Log.info("Loaded " + world.getName() + " areas: " + loads);
		}
	}

	public void addArea(Area area) {
		areasByName.put(area.getName().toLowerCase(), area);
	}

	public void deleteArea(Area area) {
		areasByName.remove(area.getName().toLowerCase());
		final File file = new File(new File(saveFolder, area.getWorld().getName()), area.getUUID().toString() + ".yml");
		if (file.exists()) {
			file.delete();
		}
		AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaDeletedEvent(area));
	}

	public List<Area> getAll() {
		return new ArrayList<Area>(areasByName.values());
	}

	public Area getByName(String name) {
		return areasByName.get(name.toLowerCase());
	}

	public List<Area> getByWorld(World world) {
		final List<Area> areas = new ArrayList<Area>();
		for (Area area : areasByName.values()) {
			if (area.getWorld().equals(world)) {
				areas.add(area);
			}
		}
		return areas;
	}

	public List<Area> getByPlayer(OfflinePlayer player) {
		final List<Area> areas = new ArrayList<Area>();
		for (Area area : areasByName.values()) {
			if (area.isOwner(player)) {
				areas.add(area);
			}
		}
		return areas;
	}

	public Area getCollision(AreaRange areaRange) {
		for (Area area : areasByName.values()) {
			if (area.checkCollision(areaRange)) {
				return area;
			}
		}
		return null;
	}

	public String validName(String name) {
		if (Config.getNameMinLength() >= name.length()) {
			return Config.getMessage("NameTooShort", Config.getNameMinLength(), name.length());
		}
		if (Config.getNameMaxLength() <= name.length()) {
			return Config.getMessage("NameTooLong", Config.getNameMaxLength(), name.length());
		}
		if (!validNameRule(name)) {
			return Config.getMessage("InvalidName");
		}
		if (Config.getNameRetains().contains(name.toLowerCase())) {
			return Config.getMessage("NameInRetains", name);
		}
		if (areasByName.containsKey(name.toLowerCase())) {
			return Config.getMessage("NameAlreadyExists", name);
		}
		return null;
	}

	public boolean validNameRule(String name) {
		return name.replaceAll("^[a-zA-Z0-9_]*$", "").equals("");
	}

	public void saveArea(Area area) {
		final YamlConfiguration config = new YamlConfiguration();

		config.set("HighX", area.getHighX());
		config.set("LowX", area.getLowX());
		config.set("HighZ", area.getHighZ());
		config.set("LowZ", area.getLowZ());
		config.set("UUID", area.getUUID().toString());
		config.set("Name", area.getName());
		if (area.getOwner() != null) {
			config.set("Owner", area.getOwner().getUniqueId().toString());
		}
		config.set("CreationDate", area.getCreationDate());

		if (area.getTeleportLocation() != null) {
			config.set("TeleportLocation", area.getTeleportLocation());
		}

		final ConfigurationSection areaFlagsSection = config.createSection("AreaFlags");
		for (Entry<Flag, Boolean> flag : area.getAreaFlags().entrySet()) {
			areaFlagsSection.set(flag.getKey().getName(), flag.getValue());
		}

		final ConfigurationSection playerFlagsSection = config.createSection("PlayerFlags");
		for (Entry<OfflinePlayer, Map<Flag, Boolean>> i : area.getPlayerFlags().entrySet()) {
			final ConfigurationSection s = playerFlagsSection.createSection(i.getKey().getUniqueId().toString());
			for (Entry<Flag, Boolean> flag : i.getValue().entrySet()) {
				s.set(flag.getKey().getName(), flag.getValue());
			}
		}

		final File folder = new File(saveFolder, area.getWorld().getName());
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}

		final File file = new File(folder, area.getUUID().toString() + ".yml");
		try {
			config.save(file);
		} catch (IOException e) {
			Log.severe("Error save world area: " + file.getName(), e);
		}
	}

	public Area createArea(OfflinePlayer player, String name, AreaRange areaRange) {
		final YAMLArea area = new YAMLArea(this, player, areaRange, name);
		area.save();
		addArea(area);
		AreaProtect.getInstance().getServer().getPluginManager().callEvent(new AreaCreatedEvent(area));
		return area;
	}

	public Area getByLocation(Location location) {
		if (location != null) {
			for (Area area : areasByName.values()) {
				if (area.containsLocation(location)) {
					return area;
				}
			}
		}
		return null;
	}

	static class AreaFileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return name.length() == 40 && name.endsWith(".yml");
		}
	}
}
