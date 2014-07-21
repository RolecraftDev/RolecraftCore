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
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GuildManager {
    private final RolecraftCore plugin;
    private final Map<String, Guild> guilds;

    private YamlConfiguration guildConfig;

    /**
     * Creates a new GuildManager instance using the given RolecraftCore object
     * as the plugin to register things with
     *
     * @param plugin The RolecraftCore plugin
     */
    public GuildManager(final RolecraftCore plugin) {
        this.plugin = plugin;

        guilds = new HashMap<String, Guild>();

        guildConfig = YamlConfiguration.loadConfiguration(
                new File(plugin.getDataFolder(), "guildconfig.yml"));

        for (final GuildAction action : GuildAction.values()) {
            action.setAccessLevel(guildConfig.getInt(
                    "access-levels." + action.getConfigPath(),
                    action.getDefaultAccessLevel()));
        }

        plugin.getServer().getPluginManager()
                .registerEvents(new GuildListener(this), plugin);
    }

    public void addGuild(final Guild guild) {
        guilds.put(guild.getName(), guild);
    }

    public void removeGuild(final Guild guild) {
        guilds.remove(guild.getName());
    }

    /**
     * Gets the Guild object for the guild with the given name
     *
     * @param name The name of the guild to get the Guild object for
     * @return The Guild object for the guild with the given name
     */
    public Guild getGuild(final String name) {
        return guilds.get(name);
    }

    /**
     * Gets the guild the given player belongs to, returning null if the given
     * player doesn't have a guild
     *
     * @param player The unique identifier of the player to get the guild of
     * @return The given player's guild, or null if they don't have one
     */
    public Guild getPlayerGuild(final UUID player) {
        for (final Guild guild : guilds.values()) {
            if (guild.isMember(player)) {
                return guild;
            }
        }
        return null;
    }

    public RolecraftCore getPlugin() {
        return plugin;
    }
}
