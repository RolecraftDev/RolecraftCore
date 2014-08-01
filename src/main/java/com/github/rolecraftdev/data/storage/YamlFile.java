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
package com.github.rolecraftdev.data.storage;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * A utility class for manipulation of a yaml file. Provides all the funcionality
 * of {@link YamlConfiguration} but also makes usage of the file easier
 */
public class YamlFile extends YamlConfiguration {
    /**
     * The YML file for this YamlFile object
     */
    private final File file;

    public YamlFile(final Plugin plugin, final String fileName,
            final boolean overwrite) {
        file = new File(plugin.getDataFolder(), fileName);

        plugin.saveResource(fileName, overwrite);
        reload();
    }

    public YamlFile(final File file) {
        this.file = file;

        reload();
    }

    public File getFile() {
        return file;
    }

    /**
     * Reloads the yaml file without throwing an exception (any stack traces are
     * printed)
     */
    public void reload() {
        try {
            load(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the yaml file without throwing an exception (any stack traces are
     * printed)
     */
    public void save() {
        try {
            save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
