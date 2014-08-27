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
 * Represents a replaceable piece of text.
 *
 * @since 0.0.5
 */
public class MsgVar {
    /**
     * The key of this {@link MsgVar}.
     */
    private final String var;
    /**
     * The value of this {@link MsgVar}.
     */
    private final String val;

    /**
     * Constructor.
     *
     * @param var the key that should be replaced
     * @param val the value of the variable
     * @since 0.0.5
     */
    private MsgVar(final String var, final String val) {
        this.var = var;
        this.val = val;
    }

    /**
     * Replace the predefined piece of text with the set value.
     *
     * @param string the string in which the key should be replaced
     * @return the edited given string
     * @since 0.0.5
     */
    public String apply(final String string) {
        return string.replace(var, val);
    }

    /**
     * Create a new {@link MsgVar} with the given key and replacing value.
     *
     * @param var the replaceable key
     * @param val the replacing value
     * @return a new {@link MsgVar} with the given key and value
     * @since 0.0.5
     */
    public static MsgVar create(final String var, final String val) {
        return new MsgVar(var, val);
    }
}
