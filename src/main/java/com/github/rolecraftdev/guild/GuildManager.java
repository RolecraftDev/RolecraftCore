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

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class GuildManager {
    private final RolecraftCore plugin;
    private final Set<Guild> guilds;
    
    private boolean loaded;

    private ConfigAccessor guildConfig;

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

        for (final GuildAction action : GuildAction.values()) {
            action.setAccessLevel(guildConfig.getConfig().getInt(
                    "access-levels." + action.getConfigPath(),
                    action.getDefaultAccessLevel()));
        }
        loaded = false;
        

        plugin.getServer().getPluginManager()
                .registerEvents(new GuildListener(this), plugin);
    }

    /**
     * @param guild The guild to load
     * @param fromDatabase Used internally for loading from database, always use false
     * @return true for success, false if the guild already exists
     */
    public boolean addGuild(final Guild guild, boolean fromDatabase) {
        if(fromDatabase) {
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
     * Called when the database is finished populating guilds from SQL
     */
    public void completeLoad() {
        loaded = true;
    }

    public boolean removeGuild(final Guild guild) {
        if(loaded) {
            plugin.getDataStore().deleteGuild(guild);
            return guilds.remove(guild);
        }
        else return false;
    }

    /**
     * Gets the Guild object for the guild with the given name
     *
     * @param name The name of the guild to get the Guild object for
     * @return The Guild object for the guild with the given name
     */
    public Guild getGuild(final String name) {
        if(loaded) {
            for (final Guild guild : guilds) {
                if (guild.getName().equalsIgnoreCase(name)) {
                    return guild;
                }
            }
            return null;
        }
        else return null;
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
        if(loaded) {
            for (final Guild guild : guilds) {
                if (guild.isMember(player)) {
                    return guild;
                }
            }
            return null;
        }
        else return null;
    }

    /**
     * @return A copy of all loaded guilds, or null if the guildManager hasn't finished loading
     */
    public Set<Guild> getGuilds() {
        if(loaded)
            return new HashSet<Guild>(guilds);
        else return null;
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }
    
    public boolean isLoaded() {
        return loaded;
    }
}
