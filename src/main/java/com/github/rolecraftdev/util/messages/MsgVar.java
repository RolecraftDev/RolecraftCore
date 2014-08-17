package com.github.rolecraftdev.util.messages;

/**
 * Represents a variable in a message
 */
public class MsgVar {
    private String var, val;

    private MsgVar(final String var) {
        this.var = var;
    }

    public MsgVar set(final String val) {
        this.val = val;
        return this;
    }

    public String replace(final String string) {
        return string.replace(var, val);
    }

    public static MsgVar create(final String var, final String val) {
        return new MsgVar(var).set(val);
    }

    public static MsgVar named(final String name) {
        return new MsgVar(name);
    }
}
