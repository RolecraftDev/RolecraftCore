package com.github.rolecraftdev.event.exp;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.RolecraftEvent;

public abstract class RCExpEvent extends RolecraftEvent implements Cancellable {

    public enum ChangeReason {
        /**
         * Awarded for killing something
         */
        KILLING,
        /**
         * Deducted for dying
         */
        DEATH,
        /**
         * Changed by a plugin, should be called when modified from outside
         * Rolecraft
         */
        CUSTOM,
        /**
         * Harvesting crops/breeding animals
         */
        HARVEST,
        /**
         * Done via user command
         */
        COMMAND,
        /**
         * Defaults to this, if nothing is specified
         * 
         * @deprecated Should specify a reason when adding exp
         */
        DEFAULT;
    };

    final Player concern;
    private boolean cancelled;

    protected RCExpEvent(RolecraftCore plugin, Player player) {
        super(plugin);
        concern = player;
    }

    public final Player getPlayer() {
        return concern;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
