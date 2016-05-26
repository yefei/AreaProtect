package cn.minecon.areaprotect.events;

import cn.minecon.areaprotect.area.Area;

public class AreaTeleportLocationChangedEvent extends AreaEvent {
	public AreaTeleportLocationChangedEvent(Area area) {
		super(area);
	}
}
