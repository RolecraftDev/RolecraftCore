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
package com.github.rolecraftdev.profession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.util.serial.YamlFile;
import com.github.rolecraftdev.util.serial.ProfessionDeserializer;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A helper class for managing {@link Profession}s.
 *
 * @since 0.0.5
 */
public final class ProfessionManager {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    /**
     * All available {@link Profession}s.
     */
    private final Set<Profession> professions;

    /**
     * Create a new {@link ProfessionManager}. This automatically registers the
     * proper {@link Listener}s.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public ProfessionManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        professions = new HashSet<Profession>();

        Bukkit.getPluginManager().registerEvents(new ProfessionListener(this),
                plugin);
    }

    /**
     * Returns the associated {@link RolecraftCore} instance.
     *
     * @return the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Get all registered {@link Profession}s in this {@link ProfessionManager}.
     *
     * @return all available {@link Profession}s
     * @since 0.0.5
     */
    @Nonnull
    public Set<Profession> getProfessions() {
        return new HashSet<Profession>(professions);
    }

    /**
     * Retrieve the registered {@link Profession} with the specified name.
     *
     * @param name the name of the wanted {@link Profession}
     * @return the {@link Profession} with the given name
     * @since 0.0.5
     */
    @Nullable
    public Profession getProfession(final String name) {
        for (final Profession profession : professions) {
            if (profession.getName().equalsIgnoreCase(name)) {
                return profession;
            }
        }
        return null;
    }

    /**
     * Retrieve the registered {@link Profession} with the specified
     * {@link UUID}.
     *
     * @param id the {@link UUID} of the wanted {@link Profession}
     * @return the {@link Profession} with the given {@link UUID}
     * @since 0.0.5
     */
    @Nullable
    public Profession getProfession(final UUID id) {
        for (final Profession profession : professions) {
            if (profession.getId().equals(id)) {
                return profession;
            }
        }
        return null;
    }

    /**
     * Retrieve the registered {@link Profession} in which the given player is.
     *
     * @param player the {@link UUID} of the player of which the
     *        {@link Profession} is wanted
     * @return the {@link Profession} of the given player
     * @since 0.0.5
     */
    @Nullable
    public Profession getPlayerProfession(final UUID player) {
        return getProfession(plugin.getDataManager().getPlayerData(player)
                .getProfession());
    }

    /**
     * Add the given {@link Profession} to this {@link ProfessionManager}. This
     * will additionally create a new {@link Permission} in the form of:
     * <em>rolecraft.profession.({@link Profession#getName()} to lowercase)</em>
     *
     * @param profession the {@link Profession} that will be added
     * @return {@code true} if the {@link Profession} is added; {@code false}
     *         otherwise
     * @since 0.0.5
     */
    public boolean addProfession(final Profession profession) {
        final boolean result = professions.add(profession);
        if (result) {
            Bukkit.getPluginManager().addPermission(
                    new Permission("rolecraft.profession."
                            + profession.getName().toLowerCase(),
                            "Allows access to the '" + profession.getName()
                                    + "' profession."));
        }
        return result;
    }

    /**
     * Load the {@link Profession}s from the <em>professions</em> directory in
     * the plugin's data folder by using {@link ProfessionDeserializer}s for all
     * embedded files. If the folder is is non-existent, it will automatically
     * be created.
     *
     * @since 0.0.5
     */
    public void loadProfessions() {
        final File directory = new File(plugin.getDataFolder(), "professions");

        if (!directory.isDirectory()) {
            directory.delete();
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (final File professionFile : directory.listFiles()) {
            final ProfessionDeserializer deserializer = new ProfessionDeserializer(
                    new YamlFile(professionFile));

            addProfession(deserializer.getProfession(this));
        }
    }
}
