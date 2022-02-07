package cn.minecon.areaprotect.area;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.utils.nms.ParticleEffect;

public class AreaRangeSee implements Runnable {
	final private AreaProtect plugin;
	final private UUID playerUUID;
	final private AreaRange areaRange;
	final private long timerStarted;
	final private int timeout;
	private int timerTask = -1;

	public AreaRangeSee(AreaProtect plugin, Player player, AreaRange areaRange, int timeout) {
		this.plugin = plugin;
		this.playerUUID = player.getUniqueId();
		this.areaRange = areaRange;
		this.timerStarted = System.currentTimeMillis();
		this.timeout = timeout;
		this.timerTask = plugin.runTaskTimerAsynchronously(this, 0, 20).getTaskId();
	}

	public AreaRangeSee(AreaProtect plugin, Player player, AreaRange areaRange) {
		this(plugin, player, areaRange, 60);
	}

	public void run() {
		if (timeout > 0 && System.currentTimeMillis() > timeout * 1000 + timerStarted) {
			cancelTimer();
		}

		final Player player = plugin.getServer().getPlayer(this.playerUUID);

		if (player == null || !player.isOnline()) {
			cancelTimer();
			return;
		}

		final Location currLocation = player.getLocation();
		if (currLocation == null) {
			cancelTimer();
			return;
		}

		List<Location> locations = getLocations(areaRange, player);
		for (Location location : locations) {
			ParticleEffect.FIREWORKS_SPARK.display(location, 0, 2, 0, 0, 30, player);
		}
	}

	public void cancelTimer() {
		if (timerTask == -1) {
			return;
		}
		try {
			plugin.getServer().getScheduler().cancelTask(timerTask);
		} finally {
			timerTask = -1;
		}
	}

	public static List<Location> getLocations(AreaRange areaRanage, Player player) {
		// Clean Args
		if (player == null)
			throw new NullPointerException("player");

		// Create Ret
		List<Location> ret = new ArrayList<Location>();

		final Location location = player.getLocation();
		final World world = location.getWorld();

		final int xmin = areaRanage.getLowX();
		final int xmax = areaRanage.getHighX();
		final double y = location.getBlockY() + 2;
		final int zmin = areaRanage.getLowZ();
		final int zmax = areaRanage.getHighZ();

		int x = xmin;
		int z = zmin;

		// Add #1
		while (x + 1 <= xmax) {
			x++;
			ret.add(new Location(world, x + 0.5, y + 0.5, z + 0.5));
		}

		// Add #2
		while (z + 1 <= zmax) {
			z++;
			ret.add(new Location(world, x + 0.5, y + 0.5, z + 0.5));
		}

		// Add #3
		while (x - 1 >= xmin) {
			x--;
			ret.add(new Location(world, x + 0.5, y + 0.5, z + 0.5));
		}

		// Add #4
		while (z - 1 >= zmin) {
			z--;
			ret.add(new Location(world, x + 0.5, y + 0.5, z + 0.5));
		}

		// Return Ret
		return ret;
	}
}
