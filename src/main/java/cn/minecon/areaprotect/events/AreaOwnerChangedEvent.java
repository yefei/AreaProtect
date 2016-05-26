package cn.minecon.areaprotect.events;

import org.bukkit.OfflinePlayer;

import cn.minecon.areaprotect.area.Area;

public class AreaOwnerChangedEvent extends AreaEvent {
	private final OfflinePlayer previousOwner;

	public AreaOwnerChangedEvent(Area area, OfflinePlayer previousOwner) {
		super(area);
		this.previousOwner = previousOwner;
	}
	
	public OfflinePlayer getPreviousOwner() {
		return previousOwner;
	}
}
