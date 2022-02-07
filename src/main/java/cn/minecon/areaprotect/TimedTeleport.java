package cn.minecon.areaprotect;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import cn.minecon.areaprotect.utils.nms.NmsPacket;

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
			NmsPacket.sendActionText(player, Config.getMessage("TeleportationCommencing"));
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
			NmsPacket.sendActionText(player, Config.getMessage("TeleportationCountdown", (int)Math.round((timerStarted + timerDelay - now) / 1000)));
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
