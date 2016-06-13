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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.util.SimpleLocation;
import com.github.rolecraftdev.util.serial.YamlFile;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages {@link RolecraftSign}s.
 *
 * @since 0.1.0
 */
public final class RolecraftSignManager {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    /**
     * The directory used for saving signs.
     */
    private final File signsFile;
    /**
     * A {@link Set} of all {@link RolecraftSign}s currently loaded.
     */
    private final Set<RolecraftSign> signs;
    /**
     * A {@link Map} of types of {@link RolecraftSign} to the handlers for that
     * type. All keys should be full lowercase.
     */
    private final Map<String, SignInteractionHandler> handlers = new HashMap<String, SignInteractionHandler>();

    /**
     * Whether signs have been loaded or not.
     */
    private boolean signsLoaded = false;

    /**
     * Create a new {@link RolecraftSignManager}. This automatically registers
     * the proper {@link Listener}(s).
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.1.0
     */
    public RolecraftSignManager(final RolecraftCore plugin) {
        this.plugin = plugin;
        this.signs = new HashSet<RolecraftSign>();

        final File dataFolder = plugin.getDataFolder();
        final File signsFolder = new File(dataFolder, "signs");
        if (!signsFolder.exists() && !signsFolder.mkdirs()) {
            plugin.getLogger()
                    .severe("Could not create signs directory for Rolecraft's signs. This feature will not function correctly");
        }

        this.signsFile = new File(signsFolder, "signs.yml");
        try {
            if (!signsFile.exists() && !signsFile.createNewFile()) {
                plugin.getLogger()
                        .severe("Could not create signs file for Rolecraft's signs. This feature will not function correctly");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the {@link RolecraftCore} plugin instance associated with this
     * manager.
     *
     * @return the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public RolecraftCore getPlugin() {
        return this.plugin;
    }

    /**
     * Gets a {@link Set} of the names of types of sign which currently have a
     * registered {@link SignInteractionHandler} associated with them.
     *
     * @return Set of all currently registered {@link RolecraftSign} type names
     * @since 0.1.0
     */
    @Nullable
    public Set<String> getRegisteredSignTypes() {
        if (!signsLoaded) {
            return null;
        }

        return Collections.unmodifiableSet(this.handlers.keySet());
    }

    /**
     * Checks whether a type with the given name is registered with this
     * manager.
     *
     * @param typeName the name of the type to check the existence of
     * @return whether a type with the given name is registered
     * @since 0.1.0
     */
    public boolean isRegisteredType(@Nonnull final String typeName) {
        return this.handlers.containsKey(typeName.toLowerCase());
    }

    /**
     * Registers the given handle to handle sign interactions with
     * {@link RolecraftSign}s of type {@code handler.getType()}.
     *
     * @param handler the interaction handler to register
     * @since 0.1.0
     */
    public void registerHandler(@Nonnull final SignInteractionHandler handler) {
        this.handlers.put(handler.getType().toLowerCase(), handler);
    }

    /**
     * Handles an interaction with a {@link RolecraftSign}.
     *
     * @param event the Bukkit event representing the interaction
     * @param sign the sign involved in the interaction
     * @since 0.1.0
     */
    public void handleInteraction(@Nonnull final PlayerInteractEvent event,
            @Nonnull final RolecraftSign sign) {
        final Player player = event.getPlayer();
        final String type = sign.getType().toLowerCase();

        this.handlers.get(type).handleSignInteraction(player, sign);
    }

    /**
     * Gets an unmodifiable {@link Set} of loaded {@link RolecraftSign}s. May
     * return {@code null} if signs have not finished loading.
     *
     * @return all loaded {@link RolecraftSign}s in the form of a {@link Set}
     * @since 0.1.0
     */
    @Nullable
    public Set<RolecraftSign> getSigns() {
        if (!signsLoaded) {
            return null;
        }

        return Collections.unmodifiableSet(this.signs);
    }

    /**
     * Adds the given {@link RolecraftSign}.
     *
     * @param sign the sign to add
     * @since 0.1.0
     */
    public void addSign(@Nonnull final RolecraftSign sign) {
        this.signs.add(sign);
    }

    /**
     * Checks whether there is a {@link RolecraftSign} at the given location.
     *
     * @param location the location to check for the existence of a sign at
     * @return whether there is a {@link RolecraftSign} at the given location
     * @since 0.1.0
     */
    public boolean isSignAt(@Nonnull final SimpleLocation location) {
        for (final RolecraftSign sign : this.signs) {
            if (sign.getLocation().equals(location)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes the given {@link RolecraftSign} from the known signs.
     *
     * @param sign the sign to remove
     * @since 0.1.0
     */
    public void removeSign(@Nonnull final RolecraftSign sign) {
        this.signs.remove(sign);
    }

    /**
     * Removes the {@link RolecraftSign} at the given location from the known
     * signs.
     *
     * @param location the location of the sign to remove
     * @since 0.1.0
     */
    public void removeSignAt(@Nonnull final SimpleLocation location) {
        final RolecraftSign sign = this.getSignAt(location);
        if (sign != null) {
            this.removeSign(sign);
        }
    }

    /**
     * Gets the {@link RolecraftSign} at the given {@link SimpleLocation}, or
     * returns {@code null} if the given location doesn't have a sign.
     *
     * @param location the location to get the sign at
     * @return the Rolecraft sign at the given location, or {@code null} if
     *         there is no Rolecraft sign at that location
     * @since 0.1.0
     */
    @Nullable
    public RolecraftSign getSignAt(@Nonnull final SimpleLocation location) {
        if (!this.signsLoaded || this.signs.isEmpty()) {
            return null;
        }

        for (final RolecraftSign sign : this.signs) {
            if (sign.getLocation().equals(location)) {
                return sign;
            }
        }

        return null;
    }

    /**
     * Loads {@link RolecraftSign}s from storage.
     *
     * @since 0.1.0
     */
    public void loadSigns() {
        final YamlFile yamlFile = new YamlFile(this.signsFile);
        if (!yamlFile.contains("signs")) {
            this.signsLoaded = true;
            return;
        }

        final ConfigurationSection signs = yamlFile
                .getConfigurationSection("signs");
        for (final String key : signs.getKeys(false)) {
            final ConfigurationSection section = signs
                    .getConfigurationSection(key);

            final String type = section.getString("type");
            final String function = section.getString("function");
            final String data = section.getString("data");
            final String locationSerial = section.getString("location");
            final SimpleLocation location = SimpleLocation
                    .fromString(locationSerial);

            if (location == null) {
                // shouldn't happen outside of admins tampering with file (sigh)
                continue;
            }

            this.signs.add(new RolecraftSign(type, function, data, location));
        }

        this.signsLoaded = true;
    }

    /**
     * Stores all loaded {@link RolecraftSign}s.
     *
     * @since 0.1.0
     */
    public void saveSigns() {
        final YamlFile yamlFile = new YamlFile(this.signsFile);
        final Set<Integer> hashCodes = new HashSet<Integer>();

        for (final RolecraftSign sign : this.signs) {
            final int hashCode = sign.hashCode();
            final String basePath = "signs." + hashCode + ".";
            yamlFile.set(basePath + "type", sign.getType());
            yamlFile.set(basePath + "function", sign.getFunction());
            yamlFile.set(basePath + "data", sign.getData());
            yamlFile.set(basePath + "location", sign.getLocation().toString());
            hashCodes.add(hashCode);
        }

        // remove any deleted signs from the configuration
        for (final String key : yamlFile.getConfigurationSection("signs")
                .getKeys(false)) {
            if (!hashCodes.contains(Integer.parseInt(key))) {
                yamlFile.getConfigurationSection("signs").set(key, null);
            }
        }

        try {
            yamlFile.save(this.signsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
