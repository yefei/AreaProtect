package cn.minecon.areaprotect.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.area.Area;

public class MoveListener implements Listener {
	private final AreaProtect plugin;

    public MoveListener(AreaProtect plugin) {
        this.plugin = plugin;
	}

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        final Area toArea = this.plugin.getAreaManager().getByLocation(event.getTo());
        final Area fromArea = this.plugin.getAreaManager().getByLocation(event.getFrom());
        if (toArea == null && fromArea == null) {
            // 不在任何区域活动过
            return;
        }
        if (toArea != null && fromArea == null) {
            event.getPlayer().sendTitle(Config.getMessage("AreaIn", toArea.getName()), null, 10, 70, 10);
        } else if (toArea == null && fromArea != null) {
            event.getPlayer().sendTitle(Config.getMessage("AreaOut", fromArea.getName()), null, 10, 70, 10);
        }
    }
}
