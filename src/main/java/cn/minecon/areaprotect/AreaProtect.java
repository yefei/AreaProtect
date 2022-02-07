package cn.minecon.areaprotect;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import cn.minecon.areaprotect.area.Area;
import cn.minecon.areaprotect.area.AreaManager;
import cn.minecon.areaprotect.commands.AreaProtectCommand;
import cn.minecon.areaprotect.listeners.BucketListener;
import cn.minecon.areaprotect.listeners.DamageListener;
import cn.minecon.areaprotect.listeners.DestroyListener;
import cn.minecon.areaprotect.listeners.EndermanPickupListener;
import cn.minecon.areaprotect.listeners.ExplosionListener;
import cn.minecon.areaprotect.listeners.FireListener;
import cn.minecon.areaprotect.listeners.FlowListener;
import cn.minecon.areaprotect.listeners.InteractListener;
import cn.minecon.areaprotect.listeners.PistonListener;
import cn.minecon.areaprotect.listeners.PlaceListener;
import cn.minecon.areaprotect.storage.YAMLAreaManager;
import cn.minecon.areaprotect.utils.Log;
import net.milkbowl.vault.economy.Economy;

public class AreaProtect extends JavaPlugin {
	private static AreaProtect instance;
	private Economy economy;
	private Set<UUID> adminPlayers;
	private SelectionManager selectionManager;
	private AreaManager areaManager;

	@Override
	public void onLoad() {
		instance = this;
		Log.initLogger(getLogger());
		saveDefaultConfig();
		new Config(getConfig());
		FlagManager.initFlags();
	}

	@Override
	public void onEnable() {
		adminPlayers = new HashSet<UUID>();
		selectionManager = new SelectionManager(this);

		final PluginManager pluginManager = getServer().getPluginManager();

		final Plugin p = pluginManager.getPlugin("Vault");
		if (p != null) {
			Log.info("Found Vault");
			setupVault();
		} else {
			Log.info("Vault NOT found!");
		}

		try {
			areaManager = new YAMLAreaManager(getDataFolder());
		} catch (Exception e) {
			Log.severe("Loading save data", e);
			getServer().shutdown();
			return;
		}

		pluginManager.registerEvents(new InteractListener(this), this);
		pluginManager.registerEvents(new DestroyListener(this), this);
		pluginManager.registerEvents(new PlaceListener(this), this);
		pluginManager.registerEvents(new BucketListener(this), this);
		pluginManager.registerEvents(new DamageListener(this), this);
		pluginManager.registerEvents(new ExplosionListener(this), this);
		pluginManager.registerEvents(new EndermanPickupListener(this), this);
		pluginManager.registerEvents(new FireListener(this), this);
		pluginManager.registerEvents(new FlowListener(this), this);
		pluginManager.registerEvents(new PistonListener(this), this);

		getCommand("area").setExecutor(new AreaProtectCommand(this));
	}

	@Override
	public void onDisable() {
		adminPlayers = null;
		selectionManager = null;
		areaManager = null;
	}

	public static AreaProtect getInstance() {
		return instance;
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	public AreaManager getAreaManager() {
		return areaManager;
	}

	public Economy getEconomy() {
		return economy;
	}

	public boolean isEconomy() {
		return economy != null && Config.isEnabledEconomy();
	}

	private void setupVault() {
		final RegisteredServiceProvider<Economy> econProvider = getServer().getServicesManager()
				.getRegistration(Economy.class);
		if (econProvider != null) {
			economy = econProvider.getProvider();
		}
	}

	public void deactivateAdminMode(OfflinePlayer player) {
		adminPlayers.remove(player.getUniqueId());
	}

	public void activateAdminMode(OfflinePlayer player) {
		adminPlayers.add(player.getUniqueId());
	}

	public boolean isAdminMode(OfflinePlayer player) {
		return adminPlayers.contains(player.getUniqueId());
	}

	public boolean allowAction(Location location, Player player, Flag flag) {
		final Area area = areaManager.getByLocation(location);
		if (area == null) {
			return true;
		}
		if (player == null) {
			return area.allowAction(flag);
		}
		return area.allowAction(player, flag);
	}

	public boolean allowAction(Location location, Flag flag) {
		return allowAction(location, null, flag);
	}

	public BukkitTask runTaskAsynchronously(final Runnable run) {
		return this.getServer().getScheduler().runTaskAsynchronously(this, run);
	}

	public BukkitTask runTaskLaterAsynchronously(final Runnable run, final long delay) {
		return this.getServer().getScheduler().runTaskLaterAsynchronously(this, run, delay);
	}

	public BukkitTask runTaskTimerAsynchronously(final Runnable run, final long delay, final long period) {
		return this.getServer().getScheduler().runTaskTimerAsynchronously(this, run, delay, period);
	}

	public int scheduleSyncDelayedTask(final Runnable run) {
		return this.getServer().getScheduler().scheduleSyncDelayedTask(this, run);
	}

	public int scheduleSyncDelayedTask(final Runnable run, final long delay) {
		return this.getServer().getScheduler().scheduleSyncDelayedTask(this, run, delay);
	}

	public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period) {
		return this.getServer().getScheduler().scheduleSyncRepeatingTask(this, run, delay, period);
	}
}
