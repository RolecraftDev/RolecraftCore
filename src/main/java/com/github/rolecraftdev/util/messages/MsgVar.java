/*
 * This file is part of RolecraftCore.
 *
 * Copyright (c) 2014 RolecraftDev <http://rolecraftdev.github.com>
 * RolecraftCore is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc-nd/3.0
 *
 * As long as you follow the following terms, you are free to copy and redistribute
 * the material in any medium or format.
 *
 * You must give appropriate credit, provide a link to the license, and indicate
 * whether any changes were made to the material. You may do so in any reasonable
 * manner, but not in any way which suggests the licensor endorses you or your use.
 *
 * You may not use the material for commercial purposes.
 *
 * If you remix, transform, or build upon the material, you may not distribute the
 * modified material.
 *
 * You may not apply legal terms or technological measures that legally restrict
 * others from doing anything the license permits.
 *
 * DISCLAIMER: This is a human-readable summary of (and not a substitute for) the
 * license.
 */
package com.github.rolecraftdev.util.messages;

/**
 * Represents a variable in a message
 *
 * @since 0.0.5
 */
public class MsgVar {
    /**
     * The name of the variable, for example $blue
     */
    private final String var;
    /**
     * The value of the variable, for example ChatColor.BLUE.toString()
     */
    private String val;

    /**
     * Constructs a new {@link MsgVar} with the given name and no value
     *
     * @param var the name of the variable
     */
    private MsgVar(final String var) {
        this.var = var;
    }

    /**
     * Sets the value of this variable to the given value
     *
     * @param val the new value for the variable
     * @return {@code this}
     */
    public MsgVar set(final String val) {
        this.val = val;
        return this;
    }

    /**
     * Replaces occurrences of this variable with the value of this variable
     * in the given {@link String}
     *
     * @param string the {@link String} to replace variables in
     * @return the {@link String} with replaced values
     */
    public String replace(final String string) {
        return string.replace(var, val);
    }

    /**
     * Creates a new {@link MsgVar} with the given name and value
     *
     * @param var the name of the new variable
     * @param val the value of the new variable
     * @return an {@link MsgVar} with the given name and value
     */
    public static MsgVar create(final String var, final String val) {
        return new MsgVar(var).set(val);
    }

    /**
     * Creates a new {@link MsgVar} with the given name, but without a value.
     * The value can be set later using {@link #set(String)}
     *
     * @param name the name of the new {@link MsgVar}
     * @return a new {@link MsgVar} with the given name and no value
     */
    public static MsgVar named(final String name) {
        return new MsgVar(name);
    }
}
