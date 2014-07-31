package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;

import org.bukkit.event.HandlerList;

public class GuildDisbandEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    public GuildDisbandEvent(final RolecraftCore plugin, final Guild guild) {
        super(plugin, guild);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
