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
 * Represents a replaceable piece of text, used within configurable messages in
 * the plugin.
 *
 * @since 0.0.5
 * @see {@link Messages#CONSTANTS}
 * @see {@link Messages#applyVars(String, MessageVariable...)}
 */
public class MessageVariable {
    /**
     * The base for {@link MessageVariable} objects representing the relevant spell in a
     * configurable message.
     *
     * @since 0.0.5
     */
    public static final MessageVariable SPELL = new MessageVariable("$spell",
            "");
    /**
     * The base for {@link MessageVariable} objects representing the relevant profession
     * in a configurable message.
     *
     * @since 0.0.5
     */
    public static final MessageVariable PROFESSION = new MessageVariable(
            "$profession", "");
    /**
     * The base for {@link MessageVariable} objects representing the relevant guild in a
     * configurable message.
     *
     * @since 0.0.5
     */
    public static final MessageVariable GUILD = new MessageVariable("$guild",
            "");
    /**
     * The base for {@link MessageVariable} objects representing the relevant player in a
     * configurable message.
     *
     * @since 0.0.5
     */
    public static final MessageVariable PLAYER = new MessageVariable("$player",
            "");
    /**
     * The base for {@link MessageVariable} objects representing the relevant guild rank
     * in a configurable message.
     *
     * @since 0.0.5
     */
    public static final MessageVariable RANK = new MessageVariable("$rank", "");
    /**
     * The base for {@link MessageVariable} objects representing the relevant action in a
     * configurable message.
     *
     * @since 0.0.5
     */
    public static final MessageVariable ACTION = new MessageVariable("$action",
            "");
    /**
     * The base for {@link MessageVariable} objects representing the relevant value in a
     * configurable message.
     *
     * @since 0.0.5
     */
    public static final MessageVariable VALUE = new MessageVariable("$value",
            "");
    public static final MessageVariable CHANNEL = new MessageVariable(
            "$channel", "");

    /**
     * The key of this {@link MessageVariable}.
     */
    private final String var;
    /**
     * The value of this {@link MessageVariable}.
     */
    private final String val;

    /**
     * Create a new {@link MessageVariable}, in which the given value will be converted
     * to a string representation.
     *
     * @since 0.0.5
     * @see #MessageVariable(String, String)
     */
    public MessageVariable(final String var, final int val) {
        this(var, String.valueOf(val));
    }

    /**
     * Create a new {@link MessageVariable}, in which the given value will be converted
     * to a string representation.
     *
     * @since 0.0.5
     * @see #MessageVariable(String, String)
     */
    public MessageVariable(final String var, final Object val) {
        this(var, String.valueOf(val));
    }

    /**
     * Constructor.
     *
     * @param var the key that should be replaced
     * @param val the value of the variable
     * @since 0.0.5
     */
    public MessageVariable(final String var, final String val) {
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
     * Create a new {@link MessageVariable} with the same key, but with a different
     * value.
     *
     * @param val the value for the new {@link MessageVariable} object
     * @return a new {@link MessageVariable} with the same key but a new value
     * @since 0.0.5
     */
    public MessageVariable value(final String val) {
        return new MessageVariable(var, val);
    }
}
