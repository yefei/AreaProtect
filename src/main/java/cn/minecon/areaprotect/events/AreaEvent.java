package cn.minecon.areaprotect.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import cn.minecon.areaprotect.area.Area;

public class AreaEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final Area area;
	
	public AreaEvent(Area area) {
		this.area = area;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	public Area getArea() {
		return area;
	}
}
