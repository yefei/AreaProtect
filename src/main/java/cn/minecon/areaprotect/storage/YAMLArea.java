package cn.minecon.areaprotect.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.FlagManager;
import cn.minecon.areaprotect.area.Area;
import cn.minecon.areaprotect.area.AreaRange;

public class YAMLArea extends Area {
	final private YAMLAreaManager areaManager;
	
	public YAMLArea(YAMLAreaManager manager, World world, ConfigurationSection section) {
		super(world, section.getInt("HighX"), section.getInt("LowX"), section.getInt("HighZ"), section.getInt("LowZ"));
		this.areaManager = manager;
		this.uuid = UUID.fromString(section.getString("UUID"));
		this.name = section.getString("Name");
		this.owner = loadOwner(section);
		this.teleportLocation = loadTeleportLocation(section);
		this.creationDate = section.getLong("CreationDate");
		this.areaFlags = getFlags(section.getConfigurationSection("AreaFlags"));
		loadPlayerFlags(section.getConfigurationSection("PlayerFlags"));
	}
	
	public YAMLArea(YAMLAreaManager manager, OfflinePlayer player, AreaRange areaRange, String name) {
		super(areaRange);
		this.areaManager = manager;
		this.name = name;
		this.uuid = UUID.randomUUID();
		this.owner = player;
		this.creationDate = System.currentTimeMillis();
		this.areaFlags = new HashMap<Flag, Boolean>();
		for (Entry<Flag, Boolean> flag : Config.getDefaultAreaFlags().entrySet()) {
			this.areaFlags.put(flag.getKey(), flag.getValue());
        }
		this.playerFlags = new HashMap<UUID, Map<Flag, Boolean>>();
	}
	
	private OfflinePlayer loadOwner(ConfigurationSection section) {
		String uuid = section.getString("Owner");
		if (uuid != null) {
			return AreaProtect.getInstance().getServer().getOfflinePlayer(UUID.fromString(uuid));
		}
		return null;
	}
	
	private Location loadTeleportLocation(ConfigurationSection section) {
		Object loc = section.get("TeleportLocation");
		if (loc instanceof Location) {
			return (Location) loc;
		}
		return null;
	}
	
	private Map<Flag, Boolean> getFlags(ConfigurationSection section) {
		final Map<Flag, Boolean> flags = new HashMap<Flag, Boolean>();
		if (section != null) {
			for (String flagKey : section.getKeys(false)) {
				final Flag flag = FlagManager.getFlag(flagKey);
				if (flag == null) {
					continue;
	            }
				flags.put(flag, section.getBoolean(flagKey));
			}
		}
		return flags;
	}
	
	private void loadPlayerFlags(ConfigurationSection section) {
		playerFlags = new HashMap<UUID, Map<Flag, Boolean>>();
		if (section != null) {
			for (String key : section.getKeys(false)) {
				playerFlags.put(UUID.fromString(key), getFlags(section.getConfigurationSection(key)));
			}
		}
	}
	
	@Override
	public void save() {
		areaManager.saveArea(this);
	}
}
