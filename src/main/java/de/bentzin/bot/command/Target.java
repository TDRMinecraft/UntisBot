package de.bentzin.bot.command;

/**
 * The enum Target.
 */
public enum Target {
    /**
     * Console target.
     */
    CONSOLE,
    /**
     * Group target.
     */
    GROUP,
    /**
     * Direct target.
     */
    DIRECT,
    /**
     * All target.
     */
    ALL;

    private final Target[] targets;

    Target(Target... extendsTargets) {
        this.targets = extendsTargets;
    }
}
