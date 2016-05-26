package cn.minecon.areaprotect;

/**
 * A representation of a Flag.
 */
public final class Flag {
    private final String name;
    private final Flag parent;
    private final String description;
    
    /**
     * Constructs a new Flag.
     *
     * @param flag the unique name of the flag, case insensitive
     * @param parent the parent flag
     * @param description the display description
     */
    public Flag(String flag, Flag parent, String description) {
        this.name = flag;
        this.parent = parent;
        this.description = description;
    }

    /**
     * Gets the name of this Flag.  This will be unique for all registered Flags.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }
    
    /**
     * Gets the parent Flag of this Flag, or returns null if none.
     *
     * @return the parent Flag
     */
    public final Flag getParent() {
        return parent;
    }
    
    /**
     * Gets the display description of this Flag.
     *
     * @return the flag's description
     */
    public final String getDescription() {
        return description;
    }
}
