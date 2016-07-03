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
package com.github.rolecraftdev;

import com.github.rolecraftdev.util.serial.YamlFile;

import javax.annotation.Nonnull;

/**
 * Contains configuration values for Rolecraft.
 *
 * @since 0.1.0
 */
public final class RolecraftConfig {
    // general configuration values

    /**
     * The default messages locale, used when there are unspecified values.
     */
    private final String defaultLocale;
    /**
     * Whether to call extra events (used for spells).
     */
    private final boolean extraEvents;
    /**
     * The configured chat format for the plugin.
     */
    private final String chatFormat;

    // mana configuration

    /**
     * The maximum mana a player is allowed to have.
     */
    private final float maximumMana;
    /**
     * The rate, per 2 seconds, at which mana regenerates.
     */
    private final float manaRegen;

    // second profession configuration

    /**
     * Whether second professions are enabled on this server.
     */
    private final boolean secondProfessions;
    /**
     * Whether people can use spells usable by their second profession.
     */
    private final boolean secondProfessionSpells;
    /**
     * Whether people can use armor usable by their second profession.
     */
    private final boolean secondProfessionArmor;
    /**
     * Whether people can use items usable by their second profession.
     */
    private final boolean secondProfessionItems;
    /**
     * Whether people can use enchantments usable by their second profession.
     */
    private final boolean secondProfessionEnchantments;

    // configuration values only used by addon plugins

    /**
     * The amount of negative karma a player starts with.
     */
    private final float originalSin;

    /**
     * Constructor. Loads configuration values from the given {@link YamlFile}
     * configuration.
     *
     * @param plugin the associated {@link RolecraftCore} plugin instance
     * @param yamlConfig the config file to load values from
     * @since 0.1.0
     */
    public RolecraftConfig(@Nonnull final RolecraftCore plugin,
            @Nonnull final YamlFile yamlConfig) {
        // load general plugin settings
        this.defaultLocale = yamlConfig.getString("default-locale");
        this.extraEvents = yamlConfig.getBoolean("extraevents");
        this.chatFormat = yamlConfig.getString("chatformat",
                "[channel] prefix <player> suffix: msg");

        // load mana-related settings
        this.maximumMana = (float) yamlConfig.getDouble("maximummana", 2000.0);
        this.manaRegen = (float) yamlConfig.getDouble("manaregen", 5.0);

        // load second profession settings
        this.secondProfessions = yamlConfig
                .getBoolean("secondprofessions.enabled", false);
        this.secondProfessionSpells = yamlConfig
                .getBoolean("secondprofessions.spells", true);
        this.secondProfessionArmor = yamlConfig
                .getBoolean("secondprofessions.armor", false);
        this.secondProfessionItems = yamlConfig
                .getBoolean("secondprofessions.items", true);
        this.secondProfessionEnchantments = yamlConfig
                .getBoolean("secondprofessions.enchantments", true);

        // load settings for addon plugins
        this.originalSin = (float) yamlConfig.getDouble("originalsin");
    }

    /**
     * Gets the default messages locale, used for unspecified values.
     *
     * @return the default messages locale
     * @since 0.1.0
     */
    public String getDefaultLocale() {
        return defaultLocale;
    }

    /**
     * Check if the use of extra events is enabled.
     *
     * @return {@code true} if extra events are enabled
     * @since 0.1.0
     */
    public boolean isExtraEvents() {
        return extraEvents;
    }

    /**
     * Gets the configured chat format for the plugin.
     *
     * @return the plugin's configured chat format
     * @since 0.1.0
     */
    public String getChatFormat() {
        return chatFormat;
    }

    /**
     * Get the maximum amount of mana players are permitted to have. This is
     * also the amount of mana a player will start with.
     *
     * @return the maximum mana on the server
     * @since 0.1.0
     */
    public float getMaximumMana() {
        return maximumMana;
    }

    /**
     * Get the regeneration rate of mana per 2 seconds.
     *
     * @return the mana regeneration rate
     * @since 0.1.0
     */
    public float getManaRegenRate() {
        return manaRegen;
    }

    /**
     * Whether the use of secondary professions is allowed on this server.
     *
     * @return whether second professions are allowed
     * @since 0.1.0
     */
    public boolean allowSecondProfessions() {
        return secondProfessions;
    }

    /**
     * Whether people can use spells usable by their second profession.
     *
     * @return whether players may use second profession spells
     * @since 0.1.0
     */
    public boolean allowSecondProfessionSpells() {
        return secondProfessionSpells;
    }

    /**
     * Whether people can use armor usable by their second profession.
     *
     * @return whether players may use second profession armor
     * @since 0.1.0
     */
    public boolean allowSecondProfessionArmor() {
        return secondProfessionArmor;
    }

    /**
     * Whether people can use items usable by their second profession.
     *
     * @return whether players may use second profession items
     * @since 0.1.0
     */
    public boolean allowSecondProfessionItems() {
        return secondProfessionItems;
    }

    /**
     * Whether people can use enchantments usable by their second profession.
     *
     * @return whether players may use second profession enchantments
     * @since 0.1.0
     */
    public boolean allowSecondProfessionEnchantments() {
        return secondProfessionEnchantments;
    }

    /**
     * Get the amount of negative karma a player should start with when he joins
     * for the first time or respawns.
     *
     * @return the negative karma level a player begins with
     * @since 0.1.0
     */
    public float getOriginalSin() {
        return originalSin;
    }
}
