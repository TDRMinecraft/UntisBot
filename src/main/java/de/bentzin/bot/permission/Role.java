package de.bentzin.bot.permission;

/**
 * The enum Role.
 */
public enum Role {
    /**
     * Manager role.
     */
    MANAGER(0),
    /**
     * Admin role.
     */
    ADMIN(1),
    /**
     * User role.
     */
    USER(2),
    /**
     * Unknown role.
     */
    UNKNOWN(3);

    /**
     * The .
     */
    public int i;

    Role(int i) {
        this.i = i;
    }
}
