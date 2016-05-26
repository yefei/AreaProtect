package cn.minecon.areaprotect.events;

import cn.minecon.areaprotect.area.Area;

public class AreaFlagsChangedEvent extends AreaEvent {
	public AreaFlagsChangedEvent(Area area) {
		super(area);
	}
}
