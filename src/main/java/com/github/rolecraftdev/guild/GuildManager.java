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

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.storage.YamlFile;
import com.github.rolecraftdev.event.guild.GuildCreateEvent;
import com.github.rolecraftdev.event.guild.GuildDisbandEvent;
import com.traksag.channels.ChannelBatch;
import com.traksag.channels.DefaultChannelBatch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Stores {@link Guild} data and {@link Guild}-related configuration options and
 * manages {@link Guild}s in Rolecraft.
 */
public final class GuildManager {
    /**
     * The metadata tag used for inviting players to guilds
     */
    public static final String GUILD_INVITE_METADATA = "guild-invite";

    /**
     * The {@link RolecraftCore} plugin object.
     */
    private final RolecraftCore plugin;
    /**
     * The associated {@link ChannelBatch}.
     */
    // We use a ChannelBatch to ensure thread safety for AsyncPlayerChatEvent
    private final ChannelBatch channelBatch = new DefaultChannelBatch();
    /**
     * A {@link Set} of all currently loaded {@link Guild} objects.
     */
    private final Set<Guild> guilds;

    /**
     * Whether the {@link GuildManager} has finished loading.
     */
    private volatile boolean loaded;
    /**
     * A configuration holding options related to {@link Guild}s.
     */
    private YamlFile guildConfig;

    // Config options

    /**
     * The amount of money required to create a {@link Guild}.
     */
    private int creationCost;
    /**
     * The amount of money required to invite somebody to a {@link Guild}.
     */
    private int inviteCost;
    /**
     * The amount of money required to purchase a hall for a {@link Guild}. Or
     * simply, a protected area.
     */
    private int hallCost;
    /**
     * Whether to protect guild halls from natural Minecraft damage, such as
     * creepers or lava / fire spread
     */
    private boolean protectFromEnvironment;
    /**
     * Whether to disallow PvP in guild halls
     */
    private boolean disallowHallPvp;

    /**
     * Creates a new {@link GuildManager} instance using the given
     * {@link RolecraftCore} object as the plugin to register events with.
     *
     * @param plugin - The {@link RolecraftCore} plugin
     */
    public GuildManager(final RolecraftCore plugin) {
        this.plugin = plugin;

        guilds = new HashSet<Guild>();

        // Get guild configuration options
        guildConfig = new YamlFile(plugin, "guildconfig.yml", false);
        creationCost = guildConfig.getInt("economy.creation-cost", 0);
        inviteCost = guildConfig.getInt("economy.invite-cost", 0);
        hallCost = guildConfig.getInt("economy.guild-hall-cost", 0);
        protectFromEnvironment = guildConfig
                .getBoolean("hall.protect-from-environment", true);
        disallowHallPvp = guildConfig.getBoolean("hall.disallow-pvp", true);

        loaded = false;

        /*
         * Load all guilds, protection from SQL errors by not querying a table that does not exist
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

        // Register the guild listener with Bukkit
        plugin.getServer().getPluginManager()
                .registerEvents(new GuildListener(this), plugin);
    }

    /**
     * Get the associated {@link ChannelBatch}.
     *
     * @return Its {@link ChannelBatch}
     */
    public ChannelBatch getChannelBatch() {
        return channelBatch;
    }

    /**
     * Adds the given {@link Guild} object to the {@link Set} of loaded
     * {@link Guild}s if it is valid - i.e if it is loaded from the database or
     * doesn't clash with another {@link Guild}. (being logically equal)
     *
     * @param guild        - The {@link Guild} to load
     * @param fromDatabase - Used internally for loading from database, always
     *                     use false
     * @return True for success, false if the {@link Guild} already exists
     */
    public boolean addGuild(final Guild guild, boolean fromDatabase) {
        if (fromDatabase) {
            guilds.add(guild);
            channelBatch.addChannel(guild.getChannel());
            return true;
        }
        for (final Guild cur : guilds) {
            if (cur.getName().equalsIgnoreCase(guild.getName())) {
                Bukkit.getPlayer(guild.getLeader())
                        .sendMessage(ChatColor.DARK_RED
                                + "A guild by that name already exists!");
                return false;
            }
        }

        final GuildCreateEvent event = new GuildCreateEvent(plugin, guild);
        plugin.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            event.getFounder()
                    .sendMessage(ChatColor.DARK_RED + event.getCancelMessage());
            return false;
        } else {
            guilds.add(guild);
            channelBatch.addChannel(guild.getChannel());
            plugin.getDataStore().createGuild(guild);
            return true;
        }
    }

    /**
     * Attempts to delete the given {@link Guild}, both from memory and from the
     * SQL database. This method will fail if this {@link GuildManager} hasn't
     * loaded yet.
     *
     * @param guild - The {@link Guild} to remove
     * @return True if the {@link Guild} was removed, false if it wasn't
     */
    public boolean removeGuild(final Guild guild) {
        if (loaded) {
            plugin.getServer().getPluginManager()
                    .callEvent(new GuildDisbandEvent(plugin, guild));
            plugin.getDataStore().deleteGuild(guild);
            channelBatch.removeChannel(guild.getChannel());
            return guilds.remove(guild);
        } else {
            return false;
        }
    }

    /**
     * Gets the {@link Guild} object that has the specified name.
     *
     * @param name - The name of the wanted {@link Guild}
     * @return The {@link Guild} with the given name if it is contained by this
     * {@link GuildManager}, or null if none is found, or this manager has not
     * finished loading
     */
    public Guild getGuild(final String name) {
        if (loaded) {
            for (final Guild guild : guilds) {
                if (guild.getName().equalsIgnoreCase(name)) {
                    return guild;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Gets the {@link Guild} object that has the specified UUID.
     *
     * @param uuid - The UUID of the wanted {@link Guild}
     * @return The {@link Guild} with the given name if it is contained by this
     * {@link GuildManager}, or null if none is found, or this manager has not
     * finished loading
     */
    public Guild getGuild(final UUID uuid) {
        if (loaded) {
            for (final Guild guild : guilds) {
                if (guild.getId().equals(uuid)) {
                    return guild;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Gets the {@link Guild} the given player belongs to.
     *
     * @param player - The unique identifier of the player to get the
     *               {@link Guild} of
     * @return The given player's {@link Guild}, or null if they don't have one.
     * Note that this will also return null if this {@link GuildManager}
     * hasn't been fully loaded yet
     */
    public Guild getPlayerGuild(final UUID player) {
        if (loaded) {
            for (final Guild guild : guilds) {
                if (guild.isMember(player)) {
                    return guild;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Get a copy of the {@link Set} used to store all loaded {@link Guild}s.
     *
     * @return A copy of the {@link Set} used to store loaded {@link Guild}s, or
     * null if this {@link GuildManager} remains unloaded.
     */
    public Set<Guild> getGuilds() {
        if (loaded) {
            return new HashSet<Guild>(guilds);
        }
        return null;
    }

    /**
     * Get the amount of money required to create a {@link Guild}.
     *
     * @return The amount of money required to create a {@link Guild}
     */
    public int getCreationCost() {
        return creationCost;
    }

    /**
     * Get the amount of money required to invite somebody to a {@link Guild}.
     *
     * @return The amount of money required to invite somebody to a
     * {@link Guild}
     */
    public int getInvitationCost() {
        return inviteCost;
    }

    /**
     * Get the amount of money required to buy a hall for a {@link Guild}.
     *
     * @return The amount of money required to buy a hall for a {@link Guild}
     */
    public int getGuildHallCost() {
        return hallCost;
    }

    /**
     * Checks whether to protect guild hall's from the environment. Environment
     * -related causes include creepers and lava spread
     *
     * @return Whether to protect guild halls from natural causes
     */
    public boolean protectFromEnvironment() {
        return protectFromEnvironment;
    }

    /**
     * Checks whether to disallow PvP in guild halls
     *
     * @return Whether to disallow PvP in guild halls
     */
    public boolean disallowHallPvp() {
        return disallowHallPvp;
    }

    /**
     * Get the {@link RolecraftCore} plugin object this {@link GuildManager} is
     * attached to.
     *
     * @return Its {@link RolecraftCore} object
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Check whether this {@link GuildManager} has been fully loaded.
     *
     * @return True if it has been completely loaded, else false
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * This should be called when the database is finished populating
     * {@link Guild}s on this {@link GuildManager} from SQL.
     */
    public void completeLoad() {
        loaded = true;
    }

    // Has to go here because you can't access static members in an enum constructor

    /**
     * A {@link Map} of human readable strings to GuildAction enum values
     */
    static final Map<String, GuildAction> actionMap = new HashMap<String, GuildAction>();

    public static GuildAction fromHumanReadable(final String humanReadable) {
        return actionMap.get(humanReadable);
    }
}
