package com.github.rolecraftdev.command.parser;

/**
 * A flag which simply has a name and a value.
 *
 * @since 0.0.5
 */
public class Flag {
    /**
     * Information about the flag.
     */
    private final String flag;
    /**
     * The {@link ChatSection} representing the value of this flag, which
     * provides various methods to use the value.
     */
    private final ChatSection valArg;

    /**
     * @since 0.0.5
     */
    public Flag(final String flag, final String value) {
        this.flag = flag;
        valArg = new ChatSection(value);
    }

    /**
     * @since 0.0.5
     */
    public String getName() {
        return flag;
    }

    /**
     * @since 0.0.5
     */
    public ChatSection getValue() {
        return valArg;
    }

    /**
     * @since 0.0.5
     */
    public String getRawValue() {
        return getValue().rawString();
    }
}
