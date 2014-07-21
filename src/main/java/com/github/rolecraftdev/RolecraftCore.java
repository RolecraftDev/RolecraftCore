package com.github.rolecraftdev;

import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.storage.DataStore;
import com.github.rolecraftdev.data.storage.MySQLDataStore;
import com.github.rolecraftdev.data.storage.SQLiteDataStore;
import com.github.rolecraftdev.guild.GuildManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Main class for the core of Rolecraft, the Bukkit RPG plugin
 */
public final class RolecraftCore extends JavaPlugin {
    private Logger logger;
    private DataStore dataStore;
    private DataManager dataManager;
    private GuildManager guildManager;

    @Override
    public void onEnable() {
        logger = getLogger();

        final FileConfiguration config = getConfig();
        final String dbType = config.getString("data-storage-solution",
                "sqlite").toLowerCase();

        if (dbType.equals("sqlite")) {
            dataStore = new SQLiteDataStore();
        } else if (dbType.equals("mysql")) {
            dataStore = new MySQLDataStore();
        }

        logger.info("Using " + dataStore.getStoreTypeName()
                + " for Rolecraft data!");

        dataManager = new DataManager(this, dataStore);
        guildManager = new GuildManager(this);
    }

    @Override
    public void onDisable() {
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public GuildManager getGuildManager() {
        return guildManager;
    }
}
