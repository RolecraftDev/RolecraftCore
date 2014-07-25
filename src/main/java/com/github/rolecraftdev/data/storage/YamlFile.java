package com.github.rolecraftdev.data.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YamlFile extends YamlConfiguration {

    private final File file;

    public YamlFile(final Plugin plugin, final String fileName,
            final boolean overwrite) {
        file = new File(plugin.getDataFolder(), fileName);

        plugin.saveResource(fileName, overwrite);
        reload();
    }

    public YamlFile(File file) {
        this.file = file;

        reload();
    }

    public File getFile() {
        return file;
    }

    public void reload() {
        try {
            load(getFile());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(getFile());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
