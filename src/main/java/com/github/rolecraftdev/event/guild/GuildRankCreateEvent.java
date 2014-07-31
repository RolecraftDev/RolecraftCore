package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.event.HandlerList;

/**
 * A {@link GuildEvent} called when a new {@link GuildRank} is created
 */
public class GuildRankCreateEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The newly created {@link GuildRank}
     */
    private final GuildRank rank;

    public GuildRankCreateEvent(final RolecraftCore plugin, final Guild guild,
            final GuildRank rank) {
        super(plugin, guild);
        this.rank = rank;
    }

    /**
     * Gets the {@link GuildRank} which has been newly created
     *
     * @return The newly created {@link GuildRank}
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
