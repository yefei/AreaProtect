package cn.minecon.areaprotect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is for the registration and tracking of Flags.
 */
public class FlagManager {
    public static final Flag FIRESPREAD = new Flag("FireSpread", null, Config.getMessage("Flags.FireSpread"));
    public static final Flag ENDERMANPICKUP = new Flag("EndermanPickup", null, Config.getMessage("Flags.EndermanPickup"));
    public static final Flag PISTON = new Flag("Piston", null, Config.getMessage("Flags.Piston"));
    
    public static final Flag EXPLOSION = new Flag("Explosion", null, Config.getMessage("Flags.Explosion"));
    public static final Flag BEDEXPLOSION = new Flag("BedExplosion", EXPLOSION, Config.getMessage("Flags.BedExplosion"));
    public static final Flag CREEPEREXPLOSION = new Flag("Creeper", EXPLOSION, Config.getMessage("Flags.Creeper"));
    public static final Flag FIREBALLEXPLOSION = new Flag("Fireball", EXPLOSION, Config.getMessage("Flags.Fireball"));
    public static final Flag TNTEXPLOSION = new Flag("TNT", EXPLOSION, Config.getMessage("Flags.TNT"));
    public static final Flag WITHEREXPLOSION = new Flag("WitherExplosion", EXPLOSION, Config.getMessage("Flags.WitherExplosion"));
    
    public static final Flag FLOW = new Flag("Flow", null, Config.getMessage("Flags.Flow"));
    public static final Flag LAVAFLOW = new Flag("LavaFlow", FLOW, Config.getMessage("Flags.LavaFlow"));
    public static final Flag WATERFLOW = new Flag("WaterFlow", FLOW, Config.getMessage("Flags.WaterFlow"));
    
    public static final Flag ALL = new Flag("All", null, Config.getMessage("Flags.All"));
    
    public static final Flag DAMAGE = new Flag("Damage", ALL, Config.getMessage("Flags.Damage"));
    public static final Flag PVP = new Flag("PVP", DAMAGE, Config.getMessage("Flags.PVP"));
    
    public static final Flag BUILD = new Flag("Build", ALL, Config.getMessage("Flags.Build"));
    
    public static final Flag PLACE = new Flag("Place", BUILD, Config.getMessage("Flags.Place"));
    public static final Flag IGNITE = new Flag("Ignite", PLACE, Config.getMessage("Flags.Ignite"));
    public static final Flag DESTROY = new Flag("Destroy", BUILD, Config.getMessage("Flags.Destroy"));
    public static final Flag TRAMPLE = new Flag("Trample", DESTROY, Config.getMessage("Flags.Trample"));
    
    public static final Flag BUCKET = new Flag("Bucket", BUILD, Config.getMessage("Flags.Bucket"));
    public static final Flag LAVABUCKET = new Flag("LavaBucket", BUCKET, Config.getMessage("Flags.LavaBucket"));
    public static final Flag WATERBUCKET = new Flag("WaterBucket", BUCKET, Config.getMessage("Flags.WaterBucket"));
    
    public static final Flag USE = new Flag("Use", ALL, Config.getMessage("Flags.Use"));
    
    /*
    public static final Flag BUTTON = new Flag("Button", USE, Config.getMessage("Flags.Button"));
    // 压力板可以被非玩家激活, 所以等于无效, 为避免漏洞产生直接开放压力板
    // public static final Flag PRESSUREPLATE = new Flag("PressurePlate", USE, Config.getMessage("Flags.PressurePlate"));
    public static final Flag LEVER = new Flag("Lever", USE, Config.getMessage("Flags.Lever"));
    public static final Flag DIODE = new Flag("Diode", USE, Config.getMessage("Flags.Diode"));

    public static final Flag CAKE = new Flag("Cake", USE, Config.getMessage("Flags.Cake"));
    public static final Flag DRAGONEGG = new Flag("DragonEgg", BUILD, Config.getMessage("Flags.DragonEgg"));

    public static final Flag DOOR = new Flag("Door", USE, Config.getMessage("Flags.Door"));
    public static final Flag FENCEGATE = new Flag("FenceGate", DOOR, Config.getMessage("Flags.FenceGate"));
    public static final Flag HINGEDDOOR = new Flag("HingedDoor", DOOR, Config.getMessage("Flags.HingedDoor"));
    public static final Flag TRAPDOOR = new Flag("TrapDoor", DOOR, Config.getMessage("Flags.TrapDoor"));

    public static final Flag UTILITY = new Flag("Utility", USE, Config.getMessage("Flags.Utility"));
    public static final Flag ANVIL = new Flag("Anvil", UTILITY, Config.getMessage("Flags.Anvil"));
    public static final Flag BEACON = new Flag("Beacon", UTILITY, Config.getMessage("Flags.Beacon"));
    public static final Flag BED = new Flag("Bed", UTILITY, Config.getMessage("Flags.Bed"));
    public static final Flag ENCHANTMENTTABLE = new Flag("EnchantmentTable", UTILITY, Config.getMessage("Flags.EnchantmentTable"));
    public static final Flag ENDERCHEST = new Flag("EnderChest", UTILITY, Config.getMessage("Flags.EnderChest"));
    public static final Flag WORKBENCH = new Flag("WorkBench", UTILITY, Config.getMessage("Flags.WorkBench"));

    public static final Flag CONTAINER = new Flag("Container", USE, Config.getMessage("Flags.Container"));
    public static final Flag ITEMFRAME = new Flag("ItemFrame", CONTAINER, Config.getMessage("Flags.ItemFrame"));
    public static final Flag ARMORSTAND = new Flag("ArmorStand", CONTAINER, Config.getMessage("Flags.ArmorStand"));
    public static final Flag CHEST = new Flag("Chest", CONTAINER, Config.getMessage("Flags.Chest"));
    public static final Flag FURNACE = new Flag("Furnace", CONTAINER, Config.getMessage("Flags.Furnace"));
    public static final Flag BREW = new Flag("Brew", CONTAINER, Config.getMessage("Flags.Brew"));
    public static final Flag HOPPER = new Flag("Hopper", CONTAINER, Config.getMessage("Flags.Hopper"));
    public static final Flag DROPPER = new Flag("Dropper", CONTAINER, Config.getMessage("Flags.Dropper"));
    public static final Flag DISPENSER = new Flag("Dispenser", CONTAINER, Config.getMessage("Flags.Dispenser"));
    */
    
    /*
    public static final Flag SPAWN = new Flag("Spawn", null, Config.getMessage("Flags.Spawn"));
    public static final Flag MONSTERSPAWN = new Flag("MonsterSpawn", SPAWN, Config.getMessage("Flags.MonsterSpawn"));
    public static final Flag ANIMALSPAWN = new Flag("AnimalSpawn", SPAWN, Config.getMessage("Flags.AnimalSpawn"));
	*/
    
    public static final Flag TELEPORT = new Flag("TP", ALL, Config.getMessage("Flags.TP"));

    private static Map<String, Flag> validFlags;

    /**
     * This method should only ever be called from JavaPlugin.onLoad().  Any other origin
     * will fail to properly register the flag.
     *
     * @param flag
     */
    public static void addFlag(Flag flag) {
        if (AreaProtect.getInstance().isEnabled()) {
            return;
        }
        validFlags.put(flag.getName().toLowerCase(), flag);
    }

    /**
     * Gets the flag by the given name.
     *
     * @param flagName
     * @return the flag by that name, or null
     */
    public static Flag getFlag(String flag) {
        return validFlags.get(flag.toLowerCase());
    }

    /**
     * Gets a list of all flags in the registry.
     *
     * @return a collection of all flags
     */
    public static Collection<Flag> getFlags() {
        return validFlags.values();
    }

    /**
     * FOR INTERNAL USE ONLY.
     * Re-initializes Residence flags, will remove all third party flags from the valid flags list.
     * Generally should not be called for any reason.
     */
    public static void initFlags() {
        validFlags = new HashMap<String, Flag>();
        addFlag(FIRESPREAD);
        addFlag(ENDERMANPICKUP);
        addFlag(PISTON);
        addFlag(EXPLOSION);
        addFlag(BEDEXPLOSION);
        addFlag(CREEPEREXPLOSION);
        addFlag(FIREBALLEXPLOSION);
        addFlag(TNTEXPLOSION);
        addFlag(WITHEREXPLOSION);
        addFlag(FLOW);
        addFlag(WATERFLOW);
        addFlag(LAVAFLOW);
        addFlag(ALL);
        addFlag(DAMAGE);
        addFlag(PVP);
        addFlag(BUILD);
        addFlag(PLACE);
        addFlag(IGNITE);
        addFlag(DESTROY);
        addFlag(TRAMPLE);
        addFlag(BUCKET);
        addFlag(LAVABUCKET);
        addFlag(WATERBUCKET);
        addFlag(USE);
        /*
        addFlag(BUTTON);
        //addFlag(PRESSUREPLATE);
        addFlag(LEVER);
        addFlag(DIODE);
        addFlag(CAKE);
        addFlag(DRAGONEGG);
        addFlag(DOOR);
        addFlag(FENCEGATE);
        addFlag(HINGEDDOOR);
        addFlag(TRAPDOOR);
        addFlag(UTILITY);
        addFlag(ANVIL);
        addFlag(BEACON);
        addFlag(BED);
        addFlag(ENCHANTMENTTABLE);
        addFlag(ENDERCHEST);
        addFlag(WORKBENCH);
        addFlag(CONTAINER);
        addFlag(ITEMFRAME);
        addFlag(ARMORSTAND);
        addFlag(CHEST);
        addFlag(FURNACE);
        addFlag(BREW);
        addFlag(HOPPER);
        addFlag(DROPPER);
        addFlag(DISPENSER);
        */
        /*
        addFlag(SPAWN);
        addFlag(MONSTERSPAWN);
        addFlag(ANIMALSPAWN);
        */
        addFlag(TELEPORT);
    }
}
