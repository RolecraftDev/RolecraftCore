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
