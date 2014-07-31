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

import pw.ian.albkit.AlbPlugin;
import pw.ian.albkit.command.Commands;

import com.github.rolecraftdev.command.other.GCCommand;
import com.github.rolecraftdev.command.other.RCConfirmCommand;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.storage.DataStore;
import com.github.rolecraftdev.data.storage.MySQLDataStore;
import com.github.rolecraftdev.data.storage.SQLiteDataStore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.listener.PlayerListener;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.quest.QuestManager;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Main class for the core of Rolecraft, the Bukkit RPG plugin.
 */
public final class RolecraftCore extends AlbPlugin {
    /**
     * The plugin {@link Logger}.
     */
    private Logger logger;
    /**
     * The data storage solution used by Rolecraft.
     */
    private DataStore dataStore;
    /**
     * Manages most data in Rolecraft with SQL.
     */
    private DataManager dataManager;
    /**
     * Manages Rolecraft {@link Guild}s.
     */
    private GuildManager guildManager;
    /**
     * Manages all aspects of questing.
     */
    private QuestManager questManager;
    /**
     * Manages all {@link Profession}s.
     */
    private ProfessionManager professionManager;
    /**
     * Whether to use economy.
     */
    private boolean useEconomy = false;
    /**
     * The Vault Economy instance.
     */
    private Economy economy;
    /**
     * The confirm command instance, used for guild disbanding and anything else
     * which needs confirming
     */
    private RCConfirmCommand confirmCommand;

    /**
     * Whether the SQL has finished loading
     */
    private volatile boolean sqlLoaded;

    @Override
    public void onEnable() {
        super.init();

        logger = getLogger();

        // Check for Vault to decide whether to enable economy support
        if (pluginMgr.isPluginEnabled("Vault")) {
            final RegisteredServiceProvider<Economy> rsp = servicesMgr
                    .getRegistration(Economy.class);
            if (rsp != null) {
                economy = rsp.getProvider();
                useEconomy = economy != null;
            }
        }

        if (!useEconomy) {
            logger.warning("Couldn't find Vault, disabling economy support");
        }

        // Get options from the config

        createDefaultConfiguration("config.yml");
        final FileConfiguration config = getConfig();
        final String dbType = config.getString("sqlserver").toLowerCase();

        // Set up the plugin's data store
        if (dbType.equals("sqlite")) {
            dataStore = new SQLiteDataStore(this);
        } else if (dbType.equals("mysql")) {
            dataStore = new MySQLDataStore(this);
        } else {
            getLogger().warning(
                    "SQLServer in config was not one of: \"sqlite\" or \"mysql,\" defaulting to sqlite");
            dataStore = new SQLiteDataStore(this);
        }

        // Log the data store we are using
        logger.info("Using " + dataStore.getStoreTypeName()
                + " for Rolecraft data!");

        // Create all the manager objects
        dataManager = new DataManager(this);
        guildManager = new GuildManager(this);
        questManager = new QuestManager(this);
        professionManager = new ProfessionManager(this);

        professionManager.loadProfessions();

        // Register listeners
        register(new RCListener(this));
        register(new PlayerListener(this));

        // Register commands
        Commands.registerCommand(this, new GCCommand(this));
        Commands.registerCommand(this, new RCConfirmCommand(this));
    }

    @Override
    public void onDisable() {
        dataManager.unloadAllPlayerData();
    }

    /**
     * Get the {@link DataStore} implementation that is in use by Rolecraft,
     * which is either {@link SQLiteDataStore} or {@link MySQLDataStore},
     * depending on the configuration.
     *
     * @return The used {@link DataStore}
     */
    public DataStore getDataStore() {
        return dataStore;
    }

    /**
     * Gets the Rolecraft {@link DataManager}.
     *
     * @return The used {@link DataManager}
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Gets the Rolecraft {@link GuildManager}, which provides various methods
     * for {@link Guild} manipulation.
     *
     * @return The used {@link GuildManager}
     */
    public GuildManager getGuildManager() {
        return guildManager;
    }

    /**
     * Gets the Rolecraft {@link ProfessionManager}, which provides various
     * methods for {@link Profession} manipulation.
     *
     * @return The used {@link ProfessionManager}
     */
    public ProfessionManager getProfessionManager() {
        return professionManager;
    }

    /**
     * Gets the Vault Economy object.
     *
     * @return The used Vault Economy object
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Check whether the use of economy is enabled.
     *
     * @return True if economy is enabled and false if it isn't
     */
    public boolean useEconomy() {
        return useEconomy;
    }

    public RCConfirmCommand getConfirmCommand() {
        return confirmCommand;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public boolean isSqlLoaded() {
        return sqlLoaded;
    }

    public void setSqlLoaded(boolean loaded) {
        this.sqlLoaded = loaded;
    }

    public void setConfirmCommand(final RCConfirmCommand confirmCommand) {
        if (!isEnabled()) {
            throw new IllegalStateException();
        }
        if (this.confirmCommand != null) {
            throw new IllegalStateException();
        }

        this.confirmCommand = confirmCommand;
    }

    /**
     * Used instead of {@link JavaPlugin#saveDefaultConfig()} as it will copy comments as well
     *
     * @param name
     */
    public void createDefaultConfiguration(String name) {
        File actual = new File(getDataFolder(), name);
        if (!actual.exists()) {

            InputStream input =
                    this.getClass().getResourceAsStream("/" + name);
            if (input != null) {
                FileOutputStream output = null;

                try {
                    output = new FileOutputStream(actual);
                    byte[] buf = new byte[8192];
                    int length = 0;
                    while ((length = input.read(buf)) > 0) {
                        output.write(buf, 0, length);
                    }

                    System.out.println(getDescription().getName()
                            + ": Default configuration file written: " + name);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (input != null) {
                            input.close();
                        }
                    } catch (IOException e) {
                    }

                    try {
                        if (output != null) {
                            output.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}
