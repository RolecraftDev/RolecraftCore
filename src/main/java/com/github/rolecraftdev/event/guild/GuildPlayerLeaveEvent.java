package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class GuildPlayerLeaveEvent extends GuildEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;

    public GuildPlayerLeaveEvent(final RolecraftCore plugin, final Guild guild,
            final Player player) {
        super(plugin, guild);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
