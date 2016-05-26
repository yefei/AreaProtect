package cn.minecon.areaprotect.area;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

public interface AreaManager {
	/**
	 * 添加区域
	 */
	public void addArea(Area area);
	
	/**
	 * 删除区域
	 */
	public void deleteArea(Area area);
	
	/**
	 * 取得所有区域
	 */
	public List<Area> getAll();
	
	/**
	 * 根据名称取得区域
	 */
	public Area getByName(String name);
	
	/**
	 * 根据世界取得所有区域
	 */
	public List<Area> getByWorld(World world);
	
	/**
	 * 根据玩家取得所有区域
	 */
	public List<Area> getByPlayer(OfflinePlayer player);
	
	/**
	 * 检测给定区域是否与现存区域有冲撞
	 */
	public Area getCollision(AreaRange areaRange);
	
	/**
	 * 检查名称是否符合要求
	 */
	public String validName(String name);
	
	/**
	 * 验证名称是否符合规则
	 */
	public boolean validNameRule(String name);
	
	/**
	 * 保存区域
	 */
	public void saveArea(Area area);
	
	/**
	 * 创建新的区域
	 */
	public Area createArea(OfflinePlayer player, String name, AreaRange areaRange);
	
	/**
	 * 通过位置取得区域
	 */
	public Area getByLocation(Location location);
}
