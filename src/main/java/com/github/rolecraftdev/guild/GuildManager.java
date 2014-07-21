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

    public GuildManager(final RolecraftCore plugin) {
        this.plugin = plugin;

        guilds = new HashMap<String, Guild>();

        plugin.getServer().getPluginManager()
                .registerEvents(new GuildListener(this), plugin);
    }

    public Guild getGuild(final String name) {
        return guilds.get(name);
    }

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
