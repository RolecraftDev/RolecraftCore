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
import com.github.rolecraftdev.data.storage.YamlFile;
import com.github.rolecraftdev.util.ProfessionDeserializer;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Groups a bunch of {@link Profession}s together, to make them easier
 * accessible and to provide additional functionality.
 */
public final class ProfessionManager {
    /**
     * The {@link RolecraftCore} plugin object this {@link ProfessionManager} is
     * attached to.
     */
    private final RolecraftCore plugin;
    /**
     * A {@link Set} of {@link Profession}s that are grouped by this
     * {@link ProfessionManager}.
     */
    private final Set<Profession> professions;
    /**
     * Used to enforce some profession rules
     */
    private final ProfessionListener professionListener;

    /**
     * Create a new {@link ProfessionManager} and immediately attach it to a
     * {@link RolecraftCore} object.
     *
     * @param plugin - The {@link RolecraftCore} plugin
     */
    public ProfessionManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        professions = new HashSet<Profession>();
        this.professionListener = new ProfessionListener(this);
    }

    /**
     * Get the {@link RolecraftCore} plugin object this
     * {@link ProfessionManager} is attached to.
     *
     * @return Its {@link RolecraftCore} object
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Get the {@link Profession}s that are grouped by this
     * {@link ProfessionManager}.
     *
     * @return A copy of the original {@link Set}, which isn't updated when the
     * original version is
     */
    public Set<Profession> getProfessions() {
        return new HashSet<Profession>(professions);
    }

    /**
     * Get a {@link Profession} that is contained by this
     * {@link ProfessionManager}, by its unique name.
     *
     * @param name - The unique name of the wanted {@link Profession}
     * @return Null if no {@link Profession} is found in this
     * {@link ProfessionManager} with the given name. Otherwise, the
     * first {@link Profession} with the specified name.
     */
    public Profession getProfession(final String name) {
        for (final Profession profession : professions) {
            if (profession.getName().equalsIgnoreCase(name)) {
                return profession;
            }
        }
        return null;
    }

    /**
     * Get a {@link Profession} that is contained by this
     * {@link ProfessionManager}, by its unique identifier.
     *
     * @param id - The unique identifier of the wanted {@link Profession}
     * @return Null if no {@link Profession} is found in this
     * {@link ProfessionManager} with the given identifier. Otherwise,
     * the first {@link Profession} with the specified identifier.
     */
    public Profession getProfession(final UUID id) {
        for (final Profession profession : professions) {
            if (profession.getId().equals(id)) {
                return profession;
            }
        }
        return null;
    }

    /**
     * Gets the {@link Profession} for the player with the given unique
     * identifier, if this ProfessionManager holds a profession with the ID of
     * the player's profession
     *
     * @param player The unique identifier of the player
     * @return The {@link Profession} of the player with the given {@link UUID}
     */
    public Profession getPlayerProfession(final UUID player) {
        return getProfession(
                plugin.getDataManager().getPlayerData(player).getProfession());
    }

    /**
     * Add a {@link Profession} to this {@link ProfessionManager}. Make sure its
     * {@link ProfessionManager} is equivalent to the one it is added to, before
     * doing so.
     *
     * @param profession - The {@link Profession} that should be added
     * @return False if the given {@link Profession} is already contained by
     * this {@link Profession} and thus, isn't added. True otherwise.
     */
    public boolean addProfession(final Profession profession) {
        boolean result = professions.add(profession);
        if (result) {
            Bukkit.getPluginManager().addPermission(new Permission(
                    "rolecraft.profession." + profession.getName()
                            .toLowerCase(), "Allows access to the '"
                    + profession.getName() + "' profession."));
        }
        return result;
    }

    /**
     * Load all serialized {@link Profession} objects from their representing
     * files, in the professions folder of the plugin returned by
     * {@link #getPlugin()}.
     */
    public void loadProfessions() {
        final File directory = new File(plugin.getDataFolder(), "professions");

        if (directory.isFile()) {
            directory.delete();
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (final File professionFile : directory.listFiles()) {
            final ProfessionDeserializer deserializer =
                    new ProfessionDeserializer(new YamlFile(professionFile));

            addProfession(deserializer.getProfession(this));
        }
    }
}
