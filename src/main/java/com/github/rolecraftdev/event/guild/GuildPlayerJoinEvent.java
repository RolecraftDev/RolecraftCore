package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class GuildPlayerJoinEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final GuildRank rank;

    public GuildPlayerJoinEvent(final RolecraftCore plugin, final Guild guild,
            final Player player, final GuildRank rank) {
        super(plugin, guild);
        this.player = player;
        this.rank = rank;
    }

    public Player getPlayer() {
        return player;
    }

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
