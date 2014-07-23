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
import com.github.rolecraftdev.data.storage.ConfigAccessor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GuildManager {
    /**
     * The RolecraftCore plugin object
     */
    private final RolecraftCore plugin;
    /**
     * A Set of all currently loaded Guild objects
     */
    private final Set<Guild> guilds;

    /**
     * Whether the guild manager has finished loading
     */
    private boolean loaded;
    /**
     * A configuration holding options related to guilds
     */
    private ConfigAccessor guildConfig;

    // Config options

    /**
     * The amount of money required to create a guild
     */
    private int creationCost;
    /**
     * The amount of money required to invite somebody to a guild
     */
    private int inviteCost;
    /**
     * The amount of money required to purchase a guild hall
     */
    private int hallCost;

    /**
     * Creates a new GuildManager instance using the given RolecraftCore object
     * as the plugin to register things with
     *
     * @param plugin The RolecraftCore plugin
     */
    public GuildManager(final RolecraftCore plugin) {
        this.plugin = plugin;

        guilds = new HashSet<Guild>();

        guildConfig = new ConfigAccessor(plugin, "guildconfig.yml");
        creationCost = guildConfig.getConfig()
                .getInt("economy.creation-cost", 0);
        inviteCost = guildConfig.getConfig().getInt("economy.invite-cost", 0);
        hallCost = guildConfig.getConfig().getInt("economy.guild-hall-cost", 0);

        loaded = false;

        plugin.getServer().getPluginManager()
                .registerEvents(new GuildListener(this), plugin);
    }

    /**
     * Adds the given Guild object to the Set of loaded guilds if it is valid -
     * i.e if it is loaded from the database or it doesn't clash with another
     * guild's name
     *
     * @param guild        The guild to load
     * @param fromDatabase Used internally for loading from database, always use false
     * @return true for success, false if the guild already exists
     */
    public boolean addGuild(final Guild guild, boolean fromDatabase) {
        if (fromDatabase) {
            guilds.add(guild);
            return true;
        }
        if (guilds.contains(guild)) {
            return false;
        }
        guilds.add(guild);
        plugin.getDataStore().createGuild(guild);
        return true;
    }

    /**
     * Deletes the given guild, both from memory and from the database
     *
     * @param guild The guild to remove
     * @return true if the guild was removed, false if it wasn't
     */
    public boolean removeGuild(final Guild guild) {
        if (loaded) {
            plugin.getDataStore().deleteGuild(guild);
            return guilds.remove(guild);
        } else {
            return false;
        }
    }

    /**
     * Gets the Guild object for the guild with the given name
     *
     * @param name The name of the guild to get the Guild object for
     * @return The Guild object for the guild with the given name
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
     * Gets the guild the given player belongs to, returning null if the given
     * player doesn't have a guild
     *
     * @param player The unique identifier of the player to get the guild of
     * @return The given player's guild, or null if they don't have one, or null if the guildmanager hasn't
     * finished loading yet
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
     * Gets a copy of the set used to store all loaded guilds
     *
     * @return A copy of the Set used to store loaded guilds, or null if the
     * guilds haven't been loaded
     */
    public Set<Guild> getGuilds() {
        if (loaded) {
            return new HashSet<Guild>(guilds);
        } else {
            return null;
        }
    }

    /**
     * Gets the amount of money required to create a guild
     *
     * @return The amount of money required to create a guild
     */
    public int getCreationCost() {
        return creationCost;
    }

    /**
     * Gets the amount of money required to invite somebody to a guild
     *
     * @return The amount of money required to invite somebody to a guild
     */
    public int getInvitationCost() {
        return inviteCost;
    }

    /**
     * Gets the amount of money required to buy a guild hall
     *
     * @return The amount of money required to buy a guild hall
     */
    public int getGuildHallCost() {
        return hallCost;
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }

    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Called when the database is finished populating guilds from SQL
     */
    public void completeLoad() {
        loaded = true;
    }
}
