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
package com.github.rolecraftdev.util.serial;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * A utility class for manipulating of YAML files. This provides all the
 * functionality of {@link YamlConfiguration} along with {@link File}
 * management.
 *
 * @since 0.0.5
 */
public class YamlFile extends YamlConfiguration {
    /**
     * The associated {@link File}.
     */
    private final File file;

    /**
     * Automatically saves the resource defined by the given parameter values to
     * the plugin's folder and loads its {@link YamlConfiguration}.
     *
     * @param plugin the {@link Plugin} that contains the specified resource
     * @param fileName the name of the resource
     * @param overwrite overwrite if the resource is already available
     * @since 0.0.5
     */
    public YamlFile(final Plugin plugin, final String fileName,
            final boolean overwrite) {
        file = new File(plugin.getDataFolder(), fileName);

        plugin.saveResource(fileName, overwrite);
        reload();
    }

    /**
     * This automatically loads the the associated {@link YamlConfiguration}.
     *
     * @param file the {@link File} the {@link YamlConfiguration} should be
     *             loaded from
     * @since 0.0.5
     */
    public YamlFile(final File file) {
        this.file = file;

        reload();
    }

    /**
     * Gets the associated {@link File}.
     *
     * @return the associated {@link File}
     * @since 0.0.5
     */
    public File getFile() {
        return file;
    }

    /**
     * Reload its {@link YamlConfiguration}, printing the stack trace for any
     * exceptions.
     *
     * @since 0.0.5
     * @see #load(File)
     */
    public void reload() {
        try {
            load(getFile());
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save its {@link YamlConfiguration}, printing the stack trace for any
     * exceptions.
     *
     * @since 0.0.5
     * @see #save(File)
     */
    public void save() {
        try {
            save(getFile());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
