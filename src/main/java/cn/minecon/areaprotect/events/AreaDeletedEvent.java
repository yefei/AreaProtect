package cn.minecon.areaprotect.events;

import cn.minecon.areaprotect.area.Area;

public class AreaDeletedEvent extends AreaEvent {
	public AreaDeletedEvent(Area area) {
		super(area);
	}
}
