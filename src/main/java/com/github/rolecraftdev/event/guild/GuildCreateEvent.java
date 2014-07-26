package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * An event representing the creation of a new guild in Rolecraft
 */
public class GuildCreateEvent extends GuildEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private String cancelMessage = "You can't create a guild right now!";

    public GuildCreateEvent(final RolecraftCore plugin, final Guild guild) {
        super(plugin, guild);
    }

    public Player getFounder() {
        return getPlugin().getServer().getPlayer(getGuild().getLeader());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelMessage(final String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
