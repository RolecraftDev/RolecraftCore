package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.RolecraftCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class GuildManager {
    private final RolecraftCore plugin;
    private final Map<String, Guild> guilds;

    public GuildManager(final RolecraftCore plugin) {
        this.plugin = plugin;

        guilds = new HashMap<String, Guild>();
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
