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
package com.github.rolecraftdev;

import com.github.rolecraftdev.command.guild.GuildCommand;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.storage.DataStore;
import com.github.rolecraftdev.data.storage.MySQLDataStore;
import com.github.rolecraftdev.data.storage.SQLiteDataStore;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.quest.QuestManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Main class for the core of Rolecraft, the Bukkit RPG plugin
 */
public final class RolecraftCore extends JavaPlugin {
    /**
     * The plugin logger
     */
    private Logger logger;
    /**
     * The data storage solution used by Rolecraft
     */
    private DataStore dataStore;
    /**
     * Manages most data in Rolecraft with SQL
     */
    private DataManager dataManager;
    /**
     * Manages Rolecraft guilds
     */
    private GuildManager guildManager;
    /**
     * Manages all aspects of questing
     */
    private QuestManager questManager;
    /**
     * Manages all professions
     */
    private ProfessionManager professionManager;

    @Override
    public void onEnable() {
        logger = getLogger();

        // Get options from the config
        final FileConfiguration config = getConfig();
        final String dbType = config.getString("data-storage-solution",
                "sqlite").toLowerCase();

        // Set up the plugin's data store
        if (dbType.equals("sqlite")) {
            dataStore = new SQLiteDataStore();
        } else if (dbType.equals("mysql")) {
            dataStore = new MySQLDataStore();
        }

        // Log the data store we are using
        logger.info("Using " + dataStore.getStoreTypeName()
                + " for Rolecraft data!");

        dataManager = new DataManager(this, dataStore);
        guildManager = new GuildManager(this);
        questManager = new QuestManager(this);
        professionManager = new ProfessionManager(this);

        professionManager.loadProfessions();

        final PluginManager pm = getServer().getPluginManager();

        // Register listeners
        pm.registerEvents(new RCListener(this), this);

        // Register commands
        getCommand("guild").setExecutor(new GuildCommand(this));
    }

    @Override
    public void onDisable() {
        dataManager.unloadAllPlayerData();
    }

    /**
     * Gets the DataStore implementation which is currently in use by Rolecraft,
     * i.e either the SQLiteDataStore or the MySQLDataStore depending on the
     * configuration
     *
     * @return The plugin DataStore
     */
    public DataStore getDataStore() {
        return dataStore;
    }

    /**
     * Gets the Rolecraft data manager
     *
     * @return Rolecraft's DataManager object
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Gets the Rolecraft guild manager, which has various methods for
     * manipulation of guilds
     *
     * @return Rolecraft's GuildManager object
     */
    public GuildManager getGuildManager() {
        return guildManager;
    }

    public ProfessionManager getProfessionManager() {
        return professionManager;
    }
}
