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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * A wrapper around a String which allows for parsing of many primitive data
 * types as well as providing methods to check whether the argument is a valid
 * form of said primitive types.
 *
 * @since 0.0.5
 */
public class ChatSection {
    /**
     * The raw string for the argument wrapped by this ChatSection object
     */
    private final String raw;

    /**
     * Creates a new ChatSection, using the given String argument as a raw
     * string
     *
     * @param arg The raw string for this ChatSection
     * @since 0.0.5
     */
    public ChatSection(final String arg) {
        this.raw = arg;
    }

    /**
     * Gets the raw string this ChatSection wraps
     *
     * @return this ChatSection's raw String value
     * @since 0.0.5
     */
    public String get() {
        return rawString();
    }

    /**
     * Gets the raw string this ChatSection wraps
     *
     * @return This ChatSection's raw String value
     * @since 0.0.5
     */
    public String rawString() {
        return raw;
    }

    /**
     * Returns this ChatSection's value parsed as an int
     *
     * @return This ChatSection's value parsed as an int
     * @throws NumberFormatException if the value isn't an int
     * @since 0.0.5
     */
    public int asInt() {
        return Integer.parseInt(raw);
    }

    /**
     * Returns this ChatSection's value parsed as a double
     *
     * @return This ChatSection's value parsed as a double
     * @throws NumberFormatException if the value isn't a double
     * @since 0.0.5
     */
    public double asDouble() {
        return Double.parseDouble(raw);
    }

    /**
     * Returns this ChatSection's value parsed as a float
     *
     * @return This ChatSection's value parsed as a float
     * @throws NumberFormatException if the argument isn't a float
     * @since 0.0.5
     */
    public float asFloat() {
        return Float.parseFloat(raw);
    }

    /**
     * Returns this ChatSection's value parsed as a long
     *
     * @return This ChatSection's value parsed as a long
     * @throws NumberFormatException if the value isn't a long
     * @since 0.0.5
     */
    public long asLong() {
        return Long.parseLong(raw);
    }

    /**
     * Returns this ChatSection's value parsed as a short
     *
     * @return This ChatSection's value parsed as a short
     * @throws NumberFormatException if the value isn't a short
     * @since 0.0.5
     */
    public short asShort() {
        return Short.parseShort(raw);
    }

    /**
     * Returns this ChatSection's value parsed as a boolean
     *
     * @return This ChatSection's value parsed as a boolean. Note that if the
     * value of the ChatSection isn't a valid boolean, false is returned
     * @since 0.0.5
     */
    public Boolean asBoolean() {
        return Boolean.valueOf(raw);
    }

    /**
     * Gets the player by the name of the argument, or null if the player isn't
     * online
     *
     * @return The player by the name of the raw raw - null if (s)he isn't online
     * @since 0.0.5
     */
    public Player asPlayer() {
        return Bukkit.getPlayer(raw);
    }

    /**
     * Gets the offline player by the name of the argument
     *
     * @return The offline player by the name of the raw raw
     * @since 0.0.5
     */
    public OfflinePlayer asOfflinePlayer() {
        return Bukkit.getOfflinePlayer(raw);
    }

    /**
     * Gets the {@link ChatColor} by the name of the argument.
     *
     * @return the chat color with the name of the argument
     * @since 0.1.0
     */
    public ChatColor asChatColor() {
        return ChatColor.valueOf(raw.toUpperCase());
    }

    /**
     * Gets the {@link Material} whose name is the same as the raw value. Names
     * are not case sensitive.
     *
     * @return the {@link Material} whose name is the same as the raw value, or
     *         {@code null} if no {@link Material} has that name
     * @since 0.0.5
     */
    public Material asMaterialFromName() {
        return Material.getMaterial(raw.toUpperCase());
    }

    /**
     * Gets the material whose id is the same as the raw value
     *
     * @return The material whose id is the same as the raw value
     * @throws NumberFormatException If the value isn't a valid int
     * @since 0.0.5
     */
    public Material asMaterialFromId() {
        return Material.getMaterial(asInt());
    }

    /**
     * Checks whether this ChatSection's value can be parsed as an integer
     *
     * @return Whether this ChatSection's value can be parsed as an integer
     * @since 0.0.5
     */
    public boolean isInt() {
        try {
            asInt();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a double
     *
     * @return Whether this ChatSection's value can be parsed as a double
     * @since 0.0.5
     */
    public boolean isDouble() {
        try {
            asDouble();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a float
     *
     * @return Whether this ChatSection's value can be parsed as a float
     * @since 0.0.5
     */
    public boolean isFloat() {
        try {
            asFloat();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a long
     *
     * @return Whether this ChatSection's value can be parsed as a long
     * @since 0.0.5
     */
    public boolean isLong() {
        try {
            asLong();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a short
     *
     * @return Whether this ChatSection's value can be parsed as a short
     * @since 0.0.5
     */
    public boolean isShort() {
        try {
            asShort();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a boolean
     *
     * @return Whether this ChatSection's value can be parsed as a boolean
     * @since 0.0.5
     */
    public boolean isBoolean() {
        return raw.equals("true") || raw.equals("false");
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a player
     *
     * @return Whether this ChatSection's value can be parsed as a player
     * @since 0.0.5
     */
    public boolean isPlayer() {
        return asPlayer() != null;
    }

    /**
     * Checks whether this ChatSection's value can be parsed as an offline
     * player
     *
     * @return Whether this ChatSection's value can be parsed as an offline
     * player
     * @since 0.0.5
     */
    public boolean isOfflinePlayer() {
        return asOfflinePlayer() != null;
    }

    /**
     * Checks whether this value can be parsed as a {@link ChatColor}.
     *
     * @return whether this argument is a valid chat color
     * @since 0.1.0
     */
    public boolean isChatColor() {
        return asChatColor() != null;
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a material
     * from it's name
     *
     * @return Whether this ChatSection's value can be parsed as a material
     * from it's name
     * @since 0.0.5
     */
    public boolean isMaterialName() {
        return asMaterialFromName() != null;
    }

    /**
     * Checks whether this ChatSection's value can be parsed as a material
     * from it's ID
     *
     * @return Whether this ChatSection's value can be parsed as a material
     * from it's ID
     * @since 0.0.5
     */
    public boolean isMaterialId() {
        try {
            return asMaterialFromId() != null;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
