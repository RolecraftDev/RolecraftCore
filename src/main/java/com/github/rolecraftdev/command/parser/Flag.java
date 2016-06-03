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
