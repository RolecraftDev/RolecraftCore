package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.event.HandlerList;

public class GuildRankModifyEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    private final GuildRank rank;
    private final GuildAction perm;
    private final boolean value;

    public GuildRankModifyEvent(final RolecraftCore plugin, final Guild guild,
            final GuildRank rank, final GuildAction perm, final boolean value) {
        super(plugin, guild);
        this.rank = rank;
        this.perm = perm;
        this.value = value;
    }

    public GuildRank getRank() {
        return rank;
    }

    public GuildAction getPermission() {
        return perm;
    }

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
