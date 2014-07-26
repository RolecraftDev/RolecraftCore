package com.github.rolecraftdev.event.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.RolecraftEvent;
import com.github.rolecraftdev.guild.Guild;

public abstract class GuildEvent extends RolecraftEvent {
    private final Guild guild;

    public GuildEvent(final RolecraftCore plugin, final Guild guild) {
        super(plugin);
        this.guild = guild;
    }

    public final Guild getGuild() {
        return guild;
    }
}
