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
package com.github.rolecraftdev.sign;

import com.github.rolecraftdev.util.SimpleLocation;

import javax.annotation.Nonnull;

/**
 * Represents a Rolecraft sign. RolecraftSigns are represented in the Minecraft
 * world with a physical sign block, and can be used for selecting professions,
 * joining open guilds, or other things, such as starting quests if the server
 * is running the RolecraftQuests add-on.
 *
 * Rolecraft's sign system is extensive and can, technically, be hooked into by
 * other plugins as well as add-ons for Rolecraft itself, by adding new
 * recognised types of signs.
 *
 * @since 0.1.0
 */
/* (non-JavaDoc)
 *
 * an example of a valid Rolecraft sign would be the following:
 *
 *                [Profession]
 *                   Select
 *                   Warrior
 *
 *
 * which would allow players right-clicking the sign to select the warrior
 * profession as their profession, assuming the server has a profession with the
 * name 'Warrior'
 */
public final class RolecraftSign {
    /**
     * First line of the sign, the 'type' of sign.
     */
    @Nonnull
    private final String type;
    /**
     * Second line of the sign, the specific 'function' of the sign.
     */
    @Nonnull
    private final String function;
    /**
     * Third line of the sign, the 'data' added to the function.
     */
    @Nonnull
    private final String data;
    /**
     * The physical location of the sign in the Minecraft world, represented as
     * a Rolecraft {@link SimpleLocation}.
     */
    @Nonnull
    private final SimpleLocation location;

    /**
     * Constructs a new Rolecraft sign, with the given type, function, data and
     * location in Minecraft.
     *
     * @param type the type of the Rolecraft sign
     * @param function the specific function of the sign
     * @param data data to be supplied to an interaction handler
     * @param location the physical Minecraft location of the sign
     * @since 0.1.0
     */
    public RolecraftSign(@Nonnull final String type,
            @Nonnull final String function, @Nonnull final String data,
            @Nonnull final SimpleLocation location) {
        this.type = type;
        this.function = function;
        this.data = data;
        this.location = location;
    }

    /**
     * Gets the base type of this Rolecraft sign.
     *
     * @return the type of this sign
     * @since 0.1.0
     */
    @Nonnull
    public String getType() {
        return type;
    }

    /**
     * Gets the specific function this Rolecraft sign is designed to perform.
     *
     * @return this sign's specific function
     * @since 0.1.0
     */
    @Nonnull
    public String getFunction() {
        return function;
    }

    /**
     * Gets additional data for this Rolecraft sign.
     *
     * @return additional data for this sign
     * @since 0.1.0
     */
    @Nonnull
    public String getData() {
        return data;
    }

    /**
     * Gets the {@link SimpleLocation} representing this sign's physical
     * Minecraft location.
     *
     * @return this sign's physical Minecraft location
     * @since 0.1.0
     */
    @Nonnull
    public SimpleLocation getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final RolecraftSign other = (RolecraftSign) o;
        return type.equals(other.type) && function.equals(other.function)
                && data.equals(other.data) && location.equals(other.location);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + function.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + location.hashCode();
        return result;
    }
}
