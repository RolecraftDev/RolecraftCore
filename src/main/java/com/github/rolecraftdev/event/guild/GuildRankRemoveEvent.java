package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.event.HandlerList;

/**
 * A {@link GuildEvent} called when a {@link GuildRank} is removed from a
 * {@link Guild}
 */
public class GuildRankRemoveEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link GuildRank} being removed from the {@link Guild}
     */
    private final GuildRank rank;

    public GuildRankRemoveEvent(final RolecraftCore plugin, final Guild guild,
            final GuildRank rank) {
        super(plugin, guild);
        this.rank = rank;
    }

    /**
     * Gets the {@link GuildRank} being removed from the {@link Guild}
     *
     * @return The {@link GuildRank} being removed from the {@link Guild}
     */
    public GuildRank getRank() {
        return rank;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
