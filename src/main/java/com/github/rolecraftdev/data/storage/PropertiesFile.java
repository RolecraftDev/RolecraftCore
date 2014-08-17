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

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to make dealing with Java {@link Properties} easier
 */
public class PropertiesFile extends Properties {
    /**
     * The {@link File} the {@link Properties} are loaded from
     */
    private final File file;

    public PropertiesFile(final File file) {
        super();
        this.file = file;
        load();
    }

    /**
     * Creates a new PropertiesFile, copying the resource from the plugin jar
     * if it doesn't already exist
     *
     * @param plugin    The plugin for copying the resource from
     * @param fileName  The name of the file
     * @param overwrite Whether to overwrite existing files while copying
     */
    public PropertiesFile(final JavaPlugin plugin, final String fileName,
            final boolean overwrite) {
        file = new File(plugin.getDataFolder(), fileName);
        plugin.saveResource(fileName, overwrite);
        load();
    }

    /**
     * Loads the properties
     */
    private void load() {
        try {
            super.load(new BufferedReader(new FileReader(file)));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the properties
     */
    public void reload() {
        load();
    }

    /**
     * Saves the properties without comments
     */
    public void save() {
        save("");
    }

    /**
     * Saves the properties with the given comments
     *
     * @param comments The comments to save the properties with
     */
    public void save(final String comments) {
        try {
            super.store(new BufferedWriter(new FileWriter(file)), comments);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the properties to the file in XML format
     */
    public void saveXML() {
        saveXML("");
    }

    /**
     * Saves the properties in XML format with the given comments
     *
     * @param comments The comments to add to the XML file
     */
    public void saveXML(final String comments) {
        try {
            super.storeToXML(new FileOutputStream(file), comments);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
