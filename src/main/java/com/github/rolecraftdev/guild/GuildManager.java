package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.RolecraftCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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
