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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile extends Properties {
    private final File file;

    public PropertiesFile(final File file) {
        super();
        this.file = file;
        load();
    }

    public PropertiesFile(final JavaPlugin plugin, final String fileName,
            final boolean overwrite) {
        file = new File(plugin.getDataFolder(), fileName);
        plugin.saveResource(fileName, overwrite);
        load();
    }

    private void load() {
        try {
            super.load(new BufferedReader(new FileReader(file)));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        load();
    }

    public void save() {
        save("");
    }

    public void save(final String comments) {
        try {
            super.store(new BufferedWriter(new FileWriter(file)), comments);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
