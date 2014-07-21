package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.RolecraftCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GuildManager {
    /**
     * The amount of influence required to claim a single chunk of land
     */
    public static final int INFLUENCE_PER_CHUNK = 100;

    private final RolecraftCore plugin;
    private final Map<String, Guild> guilds;

    /**
     * Creates a new GuildManager instance using the given RolecraftCore object
     * as the plugin to register things with
     *
     * @param plugin The RolecraftCore plugin
     */
    public GuildManager(final RolecraftCore plugin) {
        this.plugin = plugin;

        guilds = new HashMap<String, Guild>();

        plugin.getServer().getPluginManager()
                .registerEvents(new GuildListener(this), plugin);
    }

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
