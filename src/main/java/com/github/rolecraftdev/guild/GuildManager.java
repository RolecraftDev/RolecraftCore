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
package com.github.rolecraftdev.guild;

import org.apache.commons.lang.Validate;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.storage.DataStore;
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.guild.GuildCreateEvent;
import com.github.rolecraftdev.event.guild.GuildDisbandEvent;
import com.github.rolecraftdev.util.serial.YamlFile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A helper class for managing {@link Guild}s and global configurable options
 * affecting them.
 *
 * @since 0.0.5
 */
public final class GuildManager {
    /**
     * The type of sign associated with guild-related functions.
     *
     * @since 0.1.0
     */
    public static final String GUILD_SIGN_TYPE = "Guild";
    /**
     * The key used for metadata for when a player has been invited to join a
     * {@link Guild}.
     *
     * @since 0.0.5
     */
    public static final String GUILD_INVITE_METADATA = "guild-invite";

    /**
     * A {@link Map} of human-readable names and their related
     * {@link GuildAction}.
     *
     * @since 0.0.5
     */
    static final Map<String, GuildAction> actionMap = new HashMap<String, GuildAction>();

    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    /**
     * The linked configuration file.
     */
    private final YamlFile guildConfig;
    /**
     * All available {@link Guild}s.
     */
    private final Set<Guild> guilds;

    /**
     * Whether the data is wholly loaded.
     */
    private volatile boolean loaded;

    // Config options

    /**
     * Protect guild-halls from the environment and hostile mobs.
     */
    private final boolean protectFromEnvironment;
    /**
     * Disallow PvP in guild-halls.
     */
    private final boolean disallowHallPvp;
    /**
     * Whether to disallow block-breaks for people without guild permissions in
     * a guild hall.
     */
    private final boolean hallAntiGrief;
    /**
     * Whether to prevent piston usage in guild halls.
     */
    private final boolean hallNoPistons;

    /**
     * The price for making a new {@link Guild}.
     */
    private int creationCost;
    /**
     * The price for inviting a player to a {@link Guild}.
     */
    private int inviteCost;
    /**
     * The price for a guild-hall, which essentially is a protected area.
     */
    private int hallCost;

    /**
     * Create a new {@link GuildManager} and load the guild.yml file from the
     * given plugin's folder, or create it when nonexistent, from which the
     * configuration options will be loaded. Furthermore, this will also invoke
     * {@link DataStore#loadGuilds(GuildManager)} when all data has been loaded
     * from the used database. Registers a new {@link GuildListener}
     * automatically as well.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public GuildManager(final RolecraftCore plugin) {
        this.plugin = plugin;

        guilds = new HashSet<Guild>();

        // Get guild configuration options
        guildConfig = new YamlFile(plugin, "guilds.yml", false);
        creationCost = guildConfig.getInt("economy.creation-cost", 0);
        inviteCost = guildConfig.getInt("economy.invite-cost", 0);
        hallCost = guildConfig.getInt("economy.guild-hall-cost", 0);
        protectFromEnvironment = guildConfig
                .getBoolean("hall.protect-from-environment", true);
        disallowHallPvp = guildConfig.getBoolean("hall.disallow-pvp", true);
        hallAntiGrief = guildConfig.getBoolean("hall.anti-grief", true);
        hallNoPistons = guildConfig.getBoolean("hall.no-pistons", false);

        loaded = false;

        /*
         * Load all guilds, protection from SQL errors by not querying a table
         * that does not exist
         */
        if (plugin.isSqlLoaded()) {
            plugin.getDataStore().loadGuilds(this);
        } else {
            final GuildManager callback = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (plugin.isSqlLoaded()) {
                        cancel();
                        plugin.getDataStore().loadGuilds(callback);
                    }
                }
            }.runTaskTimer(plugin, 1, 5);
        }

        plugin.getSignManager()
                .registerHandler(new GuildSignInteractionHandler(plugin));

        // Register the guild listener with Bukkit
        Bukkit.getPluginManager().registerEvents(new GuildListener(this),
                plugin);
    }

    /**
     * Get the linked {@link YamlFile} that is used for global {@link Guild}
     * configuration.
     *
     * @return the linked {@link YamlFile}
     * @since 0.0.5
     */
    public YamlFile getGuildConfig() {
        return guildConfig;
    }

    /**
     * Add the given {@link Guild} to this {@link GuildManager}. The new
     * {@link Guild} cannot be added when its name equals, ignoring case, the
     * name of another {@link Guild} in this {@link GuildManager}. This will
     * also call a new {@link GuildCreateEvent} and continue upon its final
     * status. Note that adding a {@link Guild} from DAOs will avoid the
     * aforementioned statements.
     *
     * @param guild the {@link Guild} that will be added
     * @param fromDatabase whether the specified {@link Guild} is added by a
     *        DAO, or in other words, for initialisation
     * @return {@code true} if the {@link Guild} is added; {@code false}
     *         otherwise
     * @since 0.0.5
     */
    public boolean addGuild(@Nonnull final Guild guild,
            final boolean fromDatabase) {
        // If a Guild is constructed with a null GuildManager, every getter
        // method will return null - make sure there isn't a 'null' guild given
        final String name = guild.getName();
        Validate.notNull(name);

        if (fromDatabase) {
            guilds.add(guild);
            return true;
        }

        for (final Guild cur : guilds) {
            if (name.equalsIgnoreCase(cur.getName())) {
                // TODO: plugin message
                Bukkit.getPlayer(guild.getLeader()).sendMessage(
                        ChatColor.DARK_RED +
                                "A guild by that name already exists!");
                return false;
            }
        }

        final GuildCreateEvent event = RolecraftEventFactory
                .guildCreated(guild);
        if (event.isCancelled()) {
            event.getFounder().sendMessage(ChatColor.DARK_RED +
                    event.getCancelMessage());
            return false;
        } else {
            // call an event for the guild leader 'joining' the guild (s)he has just created
            RolecraftEventFactory.guildPlayerJoined(guild,
                    Bukkit.getPlayer(guild.getLeader()), guild.getLeaderRank());

            guilds.add(guild);
            plugin.getDataStore().createGuild(guild);
            return true;
        }
    }

    /**
     * Deletes the specified {@link Guild} when this is fully loaded. Gets rid
     * of all the {@link Guild}'s associated properties that are stored within
     * this {@link GuildManager} and calls a new {@link GuildDisbandEvent}.
     *
     * @param guild the {@link Guild} to remove
     * @return only {@code true} if the {@link Guild} has truly been removed
     * @since 0.0.5
     */
    public boolean removeGuild(@Nonnull final Guild guild) {
        if (loaded) {
            RolecraftEventFactory.guildDisbanded(guild);
            plugin.getDataStore().deleteGuild(guild);
            return guilds.remove(guild);
        } else {
            return false;
        }
    }

    /**
     * Retrieve the registered {@link Guild} with the specified name. Note that
     * {@code null} will automatically be returned when this isn't loaded.
     *
     * @param name the name of the wanted {@link Guild}
     * @return the {@link Guild} with the given name
     * @since 0.0.5
     */
    @Nullable
    public Guild getGuild(@Nonnull final String name) {
        Validate.notNull(name);
        if (loaded) {
            for (final Guild guild : guilds) {
                if (name.equalsIgnoreCase(guild.getName())) {
                    return guild;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve the registered {@link Guild} with the specified {@link UUID}.
     * Note that {@code null} will automatically be returned when this isn't
     * loaded.
     *
     * @param uuid the {@link UUID} of the wanted {@link Guild}
     * @return the {@link Guild} with the given {@link UUID}
     * @since 0.0.5
     */
    @Nullable
    public Guild getGuild(final UUID uuid) {
        Validate.notNull(uuid);
        if (loaded) {
            for (final Guild guild : guilds) {
                if (uuid.equals(guild.getId())) {
                    return guild;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve the registered {@link Guild} in which the given player is. Note
     * that {@code null} will automatically be returned when this isn't loaded.
     *
     * @param player the {@link UUID} of the player of which the {@link Guild}
     *        is wanted
     * @return the {@link Guild} of the given player
     * @since 0.0.5
     */
    @Nullable
    public Guild getPlayerGuild(@Nonnull final UUID player) {
        Validate.notNull(player);
        if (loaded) {
            for (final Guild guild : guilds) {
                if (guild.isMember(player)) {
                    return guild;
                }
            }
        }
        return null;
    }

    /**
     * Get all registered {@link Guild}s in this {@link GuildManager}. Note that
     * {@code null} will automatically be returned when this isn't loaded.
     *
     * @return all available {@link Guild}s
     * @since 0.0.5
     */
    @Nullable
    public Set<Guild> getGuilds() {
        if (loaded) {
            return new HashSet<Guild>(guilds);
        }
        return null;
    }

    /**
     * Obtain the price for creating a new {@link Guild}.
     *
     * @return the {@link Guild} creation price
     * @since 0.0.5
     */
    public int getCreationCost() {
        return creationCost;
    }

    /**
     * Get the price for inviting a player to a {@link Guild}.
     *
     * @return the {@link Guild} invitation price
     * @since 0.0.5
     */
    public int getInvitationCost() {
        return inviteCost;
    }

    /**
     * Get the price for a guild-hall.
     *
     * @return the guild-hall price
     * @since 0.0.5
     */
    public int getGuildHallCost() {
        return hallCost;
    }

    /**
     * Check whether guild-halls are protected from the environment and hostile
     * mobs.
     *
     * @return the state of guild-hall protection
     * @since 0.0.5
     */
    public boolean protectFromEnvironment() {
        return protectFromEnvironment;
    }

    /**
     * Check whether PvP is disallowed in guild-halls.
     *
     * @return the state of disallowance of PvP in guild-halls
     * @since 0.0.5
     */
    public boolean disallowHallPvp() {
        return disallowHallPvp;
    }

    /**
     * Check whether griefing is disallowed in guild-halls
     *
     * @return the state of disallowance of griefing in guild-halls
     * @since 0.0.5
     */
    public boolean hallAntiGrief() {
        return hallAntiGrief;
    }

    /**
     * Check whether pistons are disallowed in guild-halls
     *
     * @return the state of disallowance of pistons in guild-halls
     * @since 0.0.5
     */
    public boolean disallowHallPistons() {
        return hallNoPistons;
    }

    /**
     * Returns the associated {@link RolecraftCore} instance.
     *
     * @return the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Check if the data in this object has been fully loaded.
     *
     * @return {@code true} when the data is completely loaded; {@code false}
     *         otherwise
     * @since 0.0.5
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Set the price for inviting a player to a {@link Guild}.
     *
     * @param inviteCost the new {@link Guild} invitation price
     * @since 0.0.5
     */
    public void setInviteCost(final int inviteCost) {
        this.inviteCost = inviteCost;
    }

    /**
     * Set the price for creating a new {@link Guild}.
     *
     * @param creationCost the new {@link Guild} creation price
     * @since 0.0.5
     */
    public void setCreationCost(final int creationCost) {
        this.creationCost = creationCost;
    }

    /**
     * Set the price for a guild-hall.
     *
     * @param hallCost the new guild-hall price
     * @since 0.0.5
     */
    public void setHallCost(final int hallCost) {
        this.hallCost = hallCost;
    }

    /**
     * Complete the loading phase. This should only be called by DAOs after this
     * {@link GuildManager} has been populated with all stored {@link Guild}s.
     *
     * @since 0.0.5
     * @deprecated for internal use only
     */
    @Deprecated
    public void completeLoad() {
        loaded = true;

        RolecraftEventFactory.guildsLoaded();
    }

    /**
     * Get the {@link GuildAction} associated to the given human-readable
     * string.
     *
     * @param humanReadable the human-readable string that represents the wanted
     *        {@link GuildAction}
     * @return the appropriate {@link GuildAction}
     * @since 0.0.5
     */
    @Nullable
    public static GuildAction fromHumanReadable(
            @Nonnull final String humanReadable) {
        Validate.notNull(humanReadable);
        return actionMap.get(humanReadable);
    }
}
