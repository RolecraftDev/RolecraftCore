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
 * Utility class to make dealing with {@link Properties} easier.
 *
 * @since 0.0.5
 */
public class PropertiesFile extends Properties {
    private static final long serialVersionUID = 1L;

    /**
     * The associated {@link File}.
     */
    private final File file;

    /**
     * This automatically loads the the associated {@link Properties}.
     *
     * @param file the {@link File} the {@link Properties} should be loaded from
     * @since 0.0.5
     */
    public PropertiesFile(final File file) {
        super();
        this.file = file;
        load();
    }

    /**
     * Automatically saves the resource defined by the given parameter values to
     * the plugin's folder and loads its {@link Properties}.
     *
     * @param plugin the {@link JavaPlugin} that contains the specified resource
     * @param fileName the name of the resource
     * @param overwrite overwrite if the resource is already available
     * @since 0.0.5
     */
    public PropertiesFile(final JavaPlugin plugin, final String fileName,
            final boolean overwrite) {
        file = new File(plugin.getDataFolder(), fileName);
        plugin.saveResource(fileName, overwrite);
        load();
    }

    /**
     * Load its {@link Properties}.
     *
     * @since 0.0.5
     */
    private void load() {
        try {
            super.load(new BufferedReader(new FileReader(file)));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reload its {@link Properties}.
     *
     * @since 0.0.5
     * @see #load()
     */
    public void reload() {
        load();
    }

    /**
     * Save the {@link Properties} without any predefined comments.
     *
     * @since 0.0.5
     * @see #save(String)
     */
    public void save() {
        save("");
    }

    /**
     * Save the {@link Properties} with the given comments.
     *
     * @param comments the comments that should be added
     * @since 0.0.5
     */
    public void save(final String comments) {
        try {
            super.store(new BufferedWriter(new FileWriter(file)), comments);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the {@link Properties} in XML format, without any predefined
     * comments.
     *
     * @since 0.0.5
     * @see #save(String)
     */
    public void saveXML() {
        saveXML("");
    }

    /**
     * Save the {@link Properties} in XML format with the given comments.
     *
     * @param comments the comments that should be added
     * @since 0.0.5
     */
    public void saveXML(final String comments) {
        try {
            super.storeToXML(new FileOutputStream(file), comments);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
