package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.event.HandlerList;

/**
 * A {@link GuildEvent} called when a {@link GuildRank} is modified - i.e when
 * a {@link GuildAction} is allowed or disallowed within the {@link GuildRank}
 */
public class GuildRankModifyEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    /**
     * The {@link GuildRank} which has been modified
     */
    private final GuildRank rank;
    /**
     * The {@link GuildAction} being allowed or disallowed in this event
     */
    private final GuildAction perm;
    /**
     * Whether the {@link GuildAction} is being allowed or disallowed (true for
     * allowed, false for disallowed)
     */
    private final boolean value;

    public GuildRankModifyEvent(final RolecraftCore plugin, final Guild guild,
            final GuildRank rank, final GuildAction perm, final boolean value) {
        super(plugin, guild);
        this.rank = rank;
        this.perm = perm;
        this.value = value;
    }

    /**
     * Gets the {@link GuildRank} which has been modified
     *
     * @return The {@link GuildRank} which has been modified
     */
    public GuildRank getRank() {
        return rank;
    }

    /**
     * Gets the permissible {@link GuildAction} for which the permission value
     * is being set
     *
     * @return The {@link GuildAction} being allowed or disallowed
     */
    public GuildAction getPermission() {
        return perm;
    }

    /**
     * Gets whether the {@link GuildAction} is being allowed or disallowed
     *
     * @return True if the action is being allowed, false if it is being
     *         disallowed
     */
    public boolean getValue() {
        return value;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
