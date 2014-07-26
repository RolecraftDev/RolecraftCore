package com.github.rolecraftdev.event;

import com.github.rolecraftdev.RolecraftCore;
import org.bukkit.event.Event;

public abstract class RolecraftEvent extends Event {
    private final RolecraftCore plugin;

    public RolecraftEvent(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    public final RolecraftCore getPlugin() {
        return plugin;
    }
}
