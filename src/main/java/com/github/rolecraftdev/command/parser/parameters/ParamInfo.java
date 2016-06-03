package com.github.rolecraftdev.command.parser.parameters;

/**
 * Represents information about a parameter, which can be required or optional.
 *
 * @since 0.0.5
 */
public class ParamInfo {
    /**
     * The name of this parameter.
     */
    private final String name;
    /**
     * Whether this parameter is optional.
     */
    private final boolean optional;

    /**
     * Constructs a new ParamInfo with the given name.
     *
     * @param name the name of this Parameter
     * @param optional whether this parameter is optional
     * @since 0.0.5
     */
    public ParamInfo(String name, boolean optional) {
        this.name = name;
        this.optional = optional;
    }

    /**
     * Gets the name of this parameter.
     *
     * @return the name of this parameter
     * @since 0.0.5
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether this parameter is optional.
     *
     * @return {@code true} if the parameter is optional, {@code false} if it is
     *          required
     * @since 0.0.5
     */
    public boolean isOptional() {
        return optional;
    }
}
