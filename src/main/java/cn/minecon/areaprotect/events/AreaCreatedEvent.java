package cn.minecon.areaprotect.events;

import cn.minecon.areaprotect.area.Area;

public class AreaCreatedEvent extends AreaEvent {
	public AreaCreatedEvent(Area area) {
		super(area);
	}
}
