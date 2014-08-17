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
import pw.ian.albkit.util.ColorScheme;

import com.github.rolecraftdev.command.guild.GuildCommand;
import com.github.rolecraftdev.command.other.DebugCommand;
import com.github.rolecraftdev.command.other.GCCommand;
import com.github.rolecraftdev.command.other.RCConfirmCommand;
import com.github.rolecraftdev.command.profession.ProfessionCommand;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.storage.DataStore;
import com.github.rolecraftdev.data.storage.MySQLDataStore;
import com.github.rolecraftdev.data.storage.SQLiteDataStore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.magic.SpellManager;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.quest.QuestManager;
import com.github.rolecraftdev.util.Messages;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
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
     * Rolecraft's messaging color (colour) scheme
     */
    // TODO: Make configurable
    private ColorScheme colorScheme = ColorScheme.DEFAULT;
    /**
     * All the messages used by the plugin
     */
    private Messages messages;
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
     * Manages Rolecraft {@link com.github.rolecraftdev.magic.Spell}s
     */
    private SpellManager spellManager;
    /**
     * Whether to use economy.
     */
    private boolean useEconomy = false;
    /**
     * The Vault Economy instance.
     */
    private Economy economy;

    // Configuration options

    /**
     * The name of the data store type being used
     */
    private String dbType;
    /**
     * The amount of negative karma a player starts with
     */
    private float originalSin;
    /**
     * Whether to call extra events
     */
    private boolean extraEvents;

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
            // Warn the admin that no economy was found
            logger.warning("Couldn't find Vault, disabling economy support");
        }

        // Create default config if it doesn't exist already
        createDefaultConfiguration("config.yml");
        // Get the configuration object
        final FileConfiguration config = getConfig();

        // Get options from the config
        dbType = config.getString("sqlserver").toLowerCase();
        extraEvents = config.getBoolean("extraevents");
        originalSin = (float) config.getDouble("originalsin");

        // Set up the plugin's data store
        if (dbType.equals("sqlite")) {
            dataStore = new SQLiteDataStore(this);
        } else if (dbType.equals("mysql")) {
            dataStore = new MySQLDataStore(this);
        } else {
            logger.warning(
                    "SQLServer in config was not one of: \"sqlite\" or \"mysql\", defaulting to sqlite");
            dataStore = new SQLiteDataStore(this);
        }

        // Initialise DataStore
        dataStore.initialise();

        // Log the data store we are using
        logger.info("Using " + dataStore.getStoreTypeName()
                + " for Rolecraft data!");

        // Cleanup quests in database
        //        dataStore.finalizeQuests(questManager);

        // Get all plugin messages from the messages file
        messages = new Messages(this);
        messages.load();

        // Get the colour scheme from the messages config
        final ChatColor light = ChatColor.valueOf(messages.get(
                Messages.LIGHT_COLOUR).toUpperCase().replace(" ", "_"));
        final ChatColor dark = ChatColor.valueOf(messages.get(
                Messages.DARK_COLOUR).toUpperCase().replace(" ", "_"));
        final ChatColor prefix = ChatColor.valueOf(messages.get(
                Messages.PREFIX_COLOUR).toUpperCase().replace(" ", "_"));
        final ChatColor message = ChatColor.valueOf(messages.get(
                Messages.MESSAGE_COLOUR).toUpperCase().replace(" ", "_"));
        final ChatColor highlight = ChatColor.valueOf(messages.get(
                Messages.HIGHLIGHT_COLOUR).toUpperCase().replace(" ", "_"));

        if (light == null || dark == null || prefix == null || message == null
                || highlight == null) {
            logger.warning("Invalid colours specified in messages.yml");
            logger.warning("Using default colour scheme!");
            colorScheme = ColorScheme.DEFAULT;
        } else {
            colorScheme = new ColorScheme(light, dark, prefix, message,
                    highlight);
        }

        // Create all the manager objects / load data
        dataManager = new DataManager(this);
        guildManager = new GuildManager(this);
        questManager = new QuestManager(this);
        professionManager = new ProfessionManager(this);
        spellManager = new SpellManager(this);

        professionManager.loadProfessions();

        // Register listeners
        register(new RCListener(this));

        // Register commands
        register(new GuildCommand(this));
        register(new ProfessionCommand(this));
        register(new GCCommand(this));
        register(new RCConfirmCommand(this));
        register(new DebugCommand(this));
    }

    @Override
    public void onDisable() {
        dataManager.unloadAllPlayerData();
    }

    /**
     * Gets the colour scheme used by Rolecraft for messages
     *
     * @return Rolecraft's color scheme
     */
    public ColorScheme getColorScheme() {
        return colorScheme;
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
     * Gets the Rolecraft {@link QuestManager}, which keeps track of all loaded
     * quests
     *
     * @return The instance of Rolecraft's {@link QuestManager}
     */
    public QuestManager getQuestManager() {
        return questManager;
    }

    /**
     * Gets the Rolecraft {@link SpellManager}, which manages Spells and their
     * casting
     *
     * @return The instance of Rolecraft's {@link SpellManager}
     */
    public SpellManager getSpellManager() {
        return spellManager;
    }

    /**
     * Gets the Vault {@link Economy} object in use.
     *
     * @return The used Vault {@link Economy} object used by Rolecraft
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

    /**
     * Gets the name of the database type being used by Rolecraft
     *
     * @return The name of the database implementation being used
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Gets whether extra events are being called
     *
     * @return Whether extra events are being called
     */
    public boolean isExtraEvents() {
        return extraEvents;
    }

    /**
     * Gets the configured amount of sin each player starts with and respawns
     * with
     *
     * @return The amount of sin each player spawns with, from the configuration
     */
    public float getOriginalSin() {
        return originalSin;
    }

    /**
     * Checks whether SQL is fully loaded
     *
     * @return True if SQL is fully loaded, otherwise false
     */
    public boolean isSqlLoaded() {
        return sqlLoaded;
    }

    /**
     * Sets the {@link ColorScheme} object used by Rolecraft for messages
     *
     * @param colorScheme The new {@link ColorScheme}
     */
    public void setColorScheme(final ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    /**
     * Sets whether SQL has finished loading. Should only be called in
     * {@link DataStore} implementations
     *
     * @param loaded Whether SQL has finished loading
     */
    public void setSqlLoaded(boolean loaded) {
        sqlLoaded = loaded;
    }

    /**
     * Used instead of {@link JavaPlugin#saveDefaultConfig()} as it will copy
     * comments as well
     *
     * @param name The name of the config file to create the default for
     */
    public void createDefaultConfiguration(final String name) {
        final File actual = new File(getDataFolder(), name);
        if (!actual.exists()) {
            try {
                actual.createNewFile();
            } catch (IOException e) {
            }

            final InputStream input = getClass()
                    .getResourceAsStream("/" + name);
            if (input != null) {
                FileOutputStream output = null;
                getDataFolder().mkdir();

                try {
                    output = new FileOutputStream(actual);
                    byte[] buf = new byte[8192];
                    int length = 0;
                    while ((length = input.read(buf)) > 0) {
                        output.write(buf, 0, length);
                    }

                    logger.info("Default configuration file written: " + name);
                } catch (final IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        input.close();
                    } catch (final IOException e) {
                    }
                    try {
                        if (output != null) {
                            output.close();
                        }
                    } catch (final IOException e) {
                    }
                }
            }
        }
    }
}
