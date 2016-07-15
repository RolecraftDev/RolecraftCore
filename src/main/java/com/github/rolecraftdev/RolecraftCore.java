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

import com.github.rolecraftdev.chat.ChatManager;
import com.github.rolecraftdev.command.BaseCommandHandler;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.command.channel.ChannelCommand;
import com.github.rolecraftdev.command.guild.GuildCommand;
import com.github.rolecraftdev.command.other.DebugCommand;
import com.github.rolecraftdev.command.other.GCCommand;
import com.github.rolecraftdev.command.other.ManaCommand;
import com.github.rolecraftdev.command.other.RCConfirmCommand;
import com.github.rolecraftdev.command.profession.ProfessionCommand;
import com.github.rolecraftdev.command.secondprofession.SecondprofessionCommand;
import com.github.rolecraftdev.data.DataListener;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.data.storage.DataStore;
import com.github.rolecraftdev.data.storage.MySQLDataStore;
import com.github.rolecraftdev.data.storage.SQLiteDataStore;
import com.github.rolecraftdev.display.DisplayUpdater;
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.experience.ExperienceListener;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.magic.Spell;
import com.github.rolecraftdev.magic.SpellManager;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.sign.RolecraftSign;
import com.github.rolecraftdev.sign.RolecraftSignManager;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;
import com.github.rolecraftdev.util.serial.YamlFile;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Main class for the core of Rolecraft, the Bukkit RPG plugin.
 *
 * @since 0.0.5
 */
public final class RolecraftCore extends JavaPlugin {
    /**
     * Whether the storage solution has finished initialisation and is thus
     * fully loaded.
     */
    private volatile boolean sqlLoaded;

    /**
     * Configuration value holder.
     */
    @Nonnull
    private RolecraftConfig config;
    /**
     * All the messages used by Rolecraft.
     */
    @Nonnull
    private Messages messages;
    /**
     * Manages Rolecraft's persistent data.
     */
    @Nonnull
    private DataManager dataManager;
    /**
     * Manages {@link RolecraftSign}s.
     */
    @Nonnull
    private RolecraftSignManager signManager;
    /**
     * Manages Rolecraft's {@link Guild}s.
     */
    @Nonnull
    private GuildManager guildManager;
    /**
     * Manages Rolecraft's {@link Profession}s.
     */
    @Nonnull
    private ProfessionManager professionManager;
    /**
     * Manages Rolecraft's {@link Spell}s
     */
    @Nonnull
    private SpellManager spellManager;
    /**
     * Manages Rolecraft's chat channel system.
     */
    @Nonnull
    private ChatManager chatManager;
    /**
     * Updates scoreboard displays for players.
     */
    @Nonnull
    private DisplayUpdater displayUpdater;
    /**
     * The Vault {@link Economy} instance.
     */
    @Nullable
    private Economy economy;
    /**
     * The Vault {@link Chat} instance.
     */
    @Nullable
    private Chat chat;

    /**
     * @since 0.0.5
     */
    @Override
    public void onEnable() {
        final Server server = this.getServer();
        final PluginManager pluginManager = server.getPluginManager();
        final Logger logger = getLogger();

        // Check for Vault to decide whether to enable economy support
        economy = hookVaultEcon();
        if (!vaultEconHooked()) {
            // Warn the admin that no economy was found
            logger.warning(
                    "Couldn't hook Vault economy, disabling economy support");
        }

        chat = hookVaultChat();
        if (!vaultChatHooked()) {
            logger.warning(
                    "Couldn't hook Vault chat, some chat features will not work");
        }

        // Create default configuration file if it doesn't exist already
        final YamlFile config = new YamlFile(this, "config.yml", false);

        // Get options from the config
        this.config = new RolecraftConfig(this, config);

        final String dbType = config.getString("sqlserver").toLowerCase();

        // Set the plugin object for event construction in RolecraftEventFactory
        RolecraftEventFactory.setPlugin(this);

        // Load all plugin messages from the messages file
        messages = new Messages(this);
        messages.load();

        // Set up the Rolecraft database solution
        final DataStore dataStore;

        if (dbType.equals("sqlite")) {
            dataStore = new SQLiteDataStore(this);
        } else if (dbType.equals("mysql")) {
            dataStore = new MySQLDataStore(this);
        } else {
            logger.warning(
                    "SQLServer in config was not one of: \"sqlite\" or \"mysql\", defaulting to sqlite");
            dataStore = new SQLiteDataStore(this);
        }

        // Log the data store we are using
        logger.info("Using " + dataStore.getStoreTypeName()
                + " for Rolecraft data!");

        // Initialise DataStore
        dataStore.initialise();
        // Cleanup quests in database
        // dataStore.finalizeQuests(questManager);

        // Create all the manager objects / load data
        dataManager = new DataManager(this, dataStore);
        signManager = new RolecraftSignManager(
                this); // must be instantiated prior to other managers as they register sign interaction handlers
        guildManager = new GuildManager(this);
        professionManager = new ProfessionManager(this);
        spellManager = new SpellManager(this);
        chatManager = new ChatManager(this);

        professionManager.loadProfessions();
        signManager.loadSigns();

        displayUpdater = new DisplayUpdater(this);

        // Register listeners
        // magic related listeners are registered in SpellManager
        // specific listeners are registered in their own manager e.g ProfessionListener in ProfessionManager
        pluginManager.registerEvents(new DataListener(this), this);
        pluginManager.registerEvents(new ExperienceListener(this), this);

        // Register commands
        register(new GuildCommand(this));
        register(new ProfessionCommand(this));
        register(new GCCommand(this));
        register(new RCConfirmCommand(this));
        register(new DebugCommand(this));
        register(new ChannelCommand(this));
        register(new ManaCommand(this));

        if (this.config.allowSecondProfessions()) { // only register second profession command if second professions are enabled
            register(new SecondprofessionCommand(this));
        }
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onDisable() {
        guildManager.getTerritoryManager().saveTerritory();

        dataManager.cleanup();
        signManager.saveSigns();
        chatManager.saveChannels();
    }

    /**
     * Get the Rolecraft configuration value holder object.
     *
     * @return Rolecraft's config value holder
     * @since 0.1.0
     */
    public RolecraftConfig getConfigValues() {
        return config;
    }

    /**
     * Get the configured messages used by Rolecraft.
     *
     * @return the configured {@link Messages}
     * @since 0.0.5
     */
    @Nonnull
    public Messages getMessages() {
        return messages;
    }

    /**
     * Retrieves the value of configured message associated to the given key and
     * applies the specified variables to it.
     *
     * @param key the key of which the value (message) should be returned
     * @param vars the {@link MessageVariable}s that should be applied to the message for
     *        the specified key
     * @return the configured message for the given key
     * @since 0.0.5
     */
    @Nullable
    public String getMessage(final String key, final MessageVariable... vars) {
        return getMessages().get(key, vars);
    }

    /**
     * Get the used {@link DataStore} implementation, which is configured by the
     * user.
     *
     * @return the used {@link DataStore} implementation
     * @since 0.0.5
     */
    @Nonnull
    public DataStore getDataStore() {
        return dataManager.getStore();
    }

    /**
     * Gets the used {@link DataManager}.
     *
     * @return the used {@link DataManager}
     * @since 0.0.5
     */
    @Nonnull
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Gets the Rolecraft {@link PlayerData} object for the player with the
     * given {@link UUID}.
     *
     * @param playerId the unique id of the player to get data for
     * @return the player data for the player with the given id
     * @since 0.1.0
     */
    public PlayerData getPlayerData(final UUID playerId) {
        return getDataManager().getPlayerData(playerId);
    }

    /**
     * Gets the Rolecraft {@link PlayerData} object for the given {@link Player}.
     *
     * @param player the player to get data for
     * @return the player data for the given player
     * @since 0.1.0
     */
    public PlayerData getPlayerData(final Player player) {
        return getPlayerData(player.getUniqueId());
    }

    /**
     * Gets the used {@link RolecraftSignManager}.
     *
     * @return the used {@link RolecraftSignManager}
     * @since 0.1.0
     */
    @Nonnull
    public RolecraftSignManager getSignManager() {
        return signManager;
    }

    /**
     * Get the {@link GuildManager} that is responsible for managing all
     * available {@link Guild}s.
     *
     * @return the used {@link GuildManager}
     * @since 0.0.5
     */
    @Nonnull
    public GuildManager getGuildManager() {
        return guildManager;
    }

    /**
     * Gets the {@link Guild} with the given {@link UUID}.
     *
     * @param guildId the id of the guild to get
     * @return the guild with the given unique identifier
     * @since 0.1.0
     */
    @Nullable
    public Guild getGuild(final UUID guildId) {
        return getGuildManager().getGuild(guildId);
    }

    /**
     * Gets the {@link Guild} with the given name.
     *
     * @param guildName the name of the guild to get
     * @return the guild with the given name
     * @since 0.1.0
     */
    @Nullable
    public Guild getGuild(final String guildName) {
        return getGuildManager().getGuild(guildName);
    }

    /**
     * Get the {@link ProfessionManager} that is responsible for managing all
     * available {@link Profession}s.
     *
     * @return the used {@link ProfessionManager}
     * @since 0.0.5
     */
    @Nonnull
    public ProfessionManager getProfessionManager() {
        return professionManager;
    }

    /**
     * Get the {@link SpellManager} that is responsible for managing all
     * available {@link Spell}s.
     *
     * @return the used {@link SpellManager}
     * @since 0.0.5
     */
    @Nonnull
    public SpellManager getSpellManager() {
        return spellManager;
    }

    /**
     * Gets the {@link ChatManager} responsible for managing all chat-related
     * features of this Rolecraft plugin instance.
     *
     * @return the associated {@link ChatManager}
     * @since 0.1.0
     */
    @Nonnull
    public ChatManager getChatManager() {
        return chatManager;
    }

    /**
     * Gets the {@link DisplayUpdater} used by Rolecraft for player scoreboards.
     *
     * @return the used {@link DisplayUpdater}
     * @since 0.0.5
     */
    @Nonnull
    public DisplayUpdater getDisplayUpdater() {
        return displayUpdater;
    }

    /**
     * Check whether economy is available and in use.
     *
     * @return {@code true} if economy is available and in use; {@code false}
     *         otherwise
     * @since 0.0.5
     */
    public boolean doesUseEconomy() {
        return vaultEconHooked();
    }

    /**
     * Gets the {@link RegisteredServiceProvider} for the given type.
     *
     * @param clazz the class to get the {@link RegisteredServiceProvider} for
     * @param <T> the type of {@link RegisteredServiceProvider} to be returned
     * @return the {@link RegisteredServiceProvider} for the given type
     * @since 0.0.5
     */
    @Nullable
    public <T> RegisteredServiceProvider<T> getRegistration(Class<T> clazz) {
        return this.getServer().getServicesManager().getRegistration(clazz);
    }

    /**
     * Gets the providing object for the {@link RegisteredServiceProvider} for
     * the given type
     *
     * @param clazz the class to get the providing object for
     * @param <T> the type of provider to get
     * @return the registered provider for the given type
     * @since 0.0.5
     */
    @Nullable
    public <T> T getProvider(Class<T> clazz) {
        RegisteredServiceProvider<T> rsp = getRegistration(clazz);
        if (rsp == null) {
            return null;
        }
        return getRegistration(clazz).getProvider();
    }

    /**
     * Attempts to hook Vault's {@link Economy}. Note that this can fail if
     * Vault isn't present and/or there isn't an economy plugin on the server.
     * If this fails, {@code null} is returned.
     *
     * @return the hooked {@link Economy} object
     * @since 0.0.5
     */
    private Economy hookVaultEcon() {
        return economy = getProvider(Economy.class);
    }

    /**
     * Gets Vault's {@link Economy} implementation, returning null if Vault's
     * economy hasn't been hooked successfully. To attempt to hook Vault's
     * economy, call {@link #hookVaultEcon()}
     *
     * @return Vault's {@link Economy} object
     * @since 0.0.5
     */
    @Nullable
    public Economy getVaultEcon() {
        return economy;
    }

    /**
     * Checks whether Vault economy has been successfully hooked
     *
     * @return {@code true} if Vault's economy has been hooked, else
     *         {@code false}
     * @since 0.0.5
     */
    public boolean vaultEconHooked() {
        return getVaultEcon() != null;
    }

    /**
     * Attempts to hook Vault's {@link Chat}. Note that this can fail if Vault
     * isn't present and/or there isn't an economy plugin on the server. If this
     * fails, {@code null} is returned.
     *
     * @return the hooked {@link Chat} object
     * @since 0.1.0
     */
    private Chat hookVaultChat() {
        return chat = getProvider(Chat.class);
    }

    /**
     * Gets Vault's {@link Chat} implementation, returning null if Vault's chat
     * hasn't been hooked successfully. To attempt to hook Vault's chat, call
     * {@link #hookVaultChat()}.
     *
     * @return Vault's {@link Chat} object
     * @since 0.1.0
     */
    @Nullable
    public Chat getVaultChat() {
        return chat;
    }

    /**
     * Checks whether Vault chat has been successfully hooked.
     *
     * @return {@code true} if Vault's chat has been hooked, else {@code false}
     * @since 0.1.0
     */
    public boolean vaultChatHooked() {
        return getVaultChat() != null;
    }

    /**
     * Returns whether the used storage implementation has finished
     * initialisation and is thus completely loaded.
     *
     * @return only {@code true} if the storage system has fully been made
     *         available on this Minecraft server
     * @since 0.0.5
     */
    public boolean isSqlLoaded() {
        return sqlLoaded;
    }

    /**
     * Mark the state of the used storage mechanism as wholly loaded on this
     * Minecraft server. Calling this method from any code outside the used
     * {@link DataStore} implementation can result in unknown problems.
     *
     * @param loaded the new state of the used storage mechanism
     * @since 0.0.5
     */
    public void setSqlLoaded(final boolean loaded) {
        sqlLoaded = loaded;
    }

    /**
     * @since 0.0.5
     */
    private void register(BaseCommandHandler handler) {
        CommandHelper.registerCommand(this, handler.getName(), handler);
    }
}
