package cn.minecon.areaprotect;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TimedTeleport implements Runnable {
	final private static double MOVE_CONSTANT = 0.3;
	final private AreaProtect plugin;
	final private UUID playerUUID;
	final private long timerStarted;
	final private long timerDelay;
	final private double timerHealth;
	final private long timerInitX;
	final private long timerInitY;
	final private long timerInitZ;
	final private Location timerTeleportTarget;
	final private String completeMessage;
	private int timerTask = -1;
	
	public TimedTeleport(AreaProtect plugin, Player player, long delay, Location target, String completeMessage) {
		this.plugin = plugin;
		this.timerStarted = System.currentTimeMillis();
		this.timerDelay = delay;
		this.timerHealth = player.getHealth();
		this.timerInitX = Math.round(player.getLocation().getX() * MOVE_CONSTANT);
		this.timerInitY = Math.round(player.getLocation().getY() * MOVE_CONSTANT);
		this.timerInitZ = Math.round(player.getLocation().getZ() * MOVE_CONSTANT);
		this.playerUUID = player.getUniqueId();
		this.timerTeleportTarget = target;
		this.completeMessage = completeMessage;
		this.timerTask = plugin.runTaskTimerAsynchronously(this, 20, 20).getTaskId();
	}

	public void run() {
		final Player player = plugin.getServer().getPlayer(this.playerUUID);

		if (player == null || !player.isOnline()) {
			cancelTimer(false);
			return;
		}

		final Location currLocation = player.getLocation();
		if (currLocation == null) {
			cancelTimer(false);
			return;
		}

		if (Math.round(currLocation.getX() * MOVE_CONSTANT) != timerInitX ||
			Math.round(currLocation.getY() * MOVE_CONSTANT) != timerInitY ||
			Math.round(currLocation.getZ() * MOVE_CONSTANT) != timerInitZ ||
			player.getHealth() < timerHealth) {
			cancelTimer(true);
			return;
		}
		
		final long now = System.currentTimeMillis();
		
		if (now > timerStarted + timerDelay) {
			player.sendMessage(Config.getMessage("TeleportationCommencing"));
			plugin.scheduleSyncDelayedTask(new Runnable() {
				public void run() {
					cancelTimer(false);
					player.teleport(timerTeleportTarget, TeleportCause.COMMAND);
					if (completeMessage != null) {
						player.sendMessage(completeMessage);
					}
				}
			});
		} else {
			// 显示倒计时
			String msg = Config.getMessage("TeleportationCountdown", (int)Math.ceil((timerStarted + timerDelay - now) / 1000));
			player.sendTitle(msg, null, 10, 20, 10);
		}
	}
	
	public void cancelTimer(boolean notifyUser) {
		if (timerTask == -1) {
			return;
		}
		try {
			plugin.getServer().getScheduler().cancelTask(timerTask);
			if (notifyUser) {
				final Player player = plugin.getServer().getPlayer(this.playerUUID);
				if (player != null && player.isOnline()) {
					player.sendMessage(Config.getMessage("TPCancelled"));
				}
			}
		} finally {
			timerTask = -1;
		}
	}
}
