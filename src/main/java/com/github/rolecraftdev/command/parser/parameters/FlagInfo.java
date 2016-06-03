package com.github.rolecraftdev.command.parser.parameters;

/**
 * Basic flag information.
 */
public class FlagInfo {
    /**
     * Name of the flag
     */
    private final String name;
    /**
     * One-word description.
     */
    private final String desc;
    /**
     * Whether or not the flag is optional.
     */
    private final boolean optional;

    public FlagInfo(String name, String desc, boolean optional) {
        this.name = name;
        this.desc = desc;
        this.optional = optional;
    }

    /**
     * @since 0.0.5
     */
    public String getName() {
        return name;
    }

    /**
     * @since 0.0.5
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @since 0.0.5
     */
    public boolean isOptional() {
        return optional;
    }
}
