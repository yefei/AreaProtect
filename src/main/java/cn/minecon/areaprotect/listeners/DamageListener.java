package cn.minecon.areaprotect.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import cn.minecon.areaprotect.AreaProtect;
import cn.minecon.areaprotect.Config;
import cn.minecon.areaprotect.Flag;
import cn.minecon.areaprotect.FlagManager;
import cn.minecon.areaprotect.area.Area;

public class DamageListener implements Listener {
	final private AreaProtect plugin;

	public DamageListener(AreaProtect plugin) {
		this.plugin = plugin;
	}

	/*
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSplashPotion(PotionSplashEvent event) {
        Entity ent = event.getEntity();
        boolean srcpvp = plugin.allowAction(ent.getLocation(), FlagManager.PVP);
        Iterator<LivingEntity> it = event.getAffectedEntities().iterator();
        while (it.hasNext()) {
            LivingEntity target = it.next();
            if (target.getType() == EntityType.PLAYER) {
                if (!srcpvp || !plugin.allowAction(target.getLocation(), FlagManager.PVP)) {
                    event.setIntensity(target, 0);
                }
            }
        }
    }
    */

    /*
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity ent = event.getEntity();
        Area area = plugin.getAreaManager().getByLocation(ent.getLocation());
        if (area == null) {
        	return;
        }
        if (!area.allowAction(FlagManager.DAMAGE)) {
            event.setCancelled(true);
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                ent.setFireTicks(0);
            }
            return;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            onEntityDamageByEntity((EntityDamageByEntityEvent) event, area);
        }
    }
    */

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    	final Entity ent = event.getEntity();
        if (ent.hasMetadata("NPC")) {
            return;
        }
        
    	final Area area = plugin.getAreaManager().getByLocation(ent.getLocation());
        if (area == null) {
        	return;
        }
    	
        final Entity damager = event.getDamager();
        final Player player = getPlayer(damager); // 伤害者
        final Player receiver = getPlayer(ent); // 被伤害者
        
        // 展示框、盔甲架
        final Flag flag = getFlag(ent.getType());
        if (flag != null) {
        	// 非玩家例如TNT破坏
            if (player == null && !area.allowAction(flag)) {
	            event.setCancelled(true);
            } else if (!area.allowAction(player, flag)) {
                event.setCancelled(true);
                player.sendMessage(Config.getMessage("FlagDeny", flag.getDescription()));
            }
            return;
        }
        
        if (player != null) {
        	if (receiver != null && !area.allowAction(player, FlagManager.PVP)) {
        		event.setCancelled(true);
        		player.sendMessage(Config.getMessage("FlagDeny", FlagManager.PVP.getDescription()));
        		return;
        	}
        	if (!area.allowAction(player, FlagManager.DAMAGE)) {
        		event.setCancelled(true);
        		player.sendMessage(Config.getMessage("FlagDeny", FlagManager.DAMAGE.getDescription()));
        		return;
        	}
        }
    }

    public Player getPlayer(Entity entity) {
        if (entity instanceof Player) {
            return (Player) entity;
        }
        if (entity instanceof Wolf && ((Wolf) entity).isTamed()) {
            return ((Wolf) entity).getKiller();
        }
        if (entity instanceof Projectile && ((Projectile) entity).getShooter() instanceof Player) {
            return (Player) ((Projectile) entity).getShooter();
        }
        return null;
    }
    
    private Flag getFlag(EntityType ent) {
        switch (ent) {
        case ITEM_FRAME:
        case ARMOR_STAND:
        	return FlagManager.DESTROY;
        default:
            return null;
        }
    }
}
