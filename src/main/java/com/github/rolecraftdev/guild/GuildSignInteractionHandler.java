/*
 * This file is part of RolecraftCore.
 *
 * Copyright (c) 2014 RolecraftDev <http://rolecraftdev.github.com>
 * RolecraftCore is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc-nd/3.0
 *
 * As long as you follow the following terms, you are free to copy and redistribute
 * the material in any medium or format.
 *
 * You must give appropriate credit, provide a link to the license, and indicate
 * whether any changes were made to the material. You may do so in any reasonable
 * manner, but not in any way which suggests the licensor endorses you or your use.
 *
 * You may not use the material for commercial purposes.
 *
 * If you remix, transform, or build upon the material, you may not distribute the
 * modified material.
 *
 * You may not apply legal terms or technological measures that legally restrict
 * others from doing anything the license permits.
 *
 * DISCLAIMER: This is a human-readable summary of (and not a substitute for) the
 * license.
 */
package com.github.rolecraftdev.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.guild.GuildPlayerJoinEvent;
import com.github.rolecraftdev.sign.RolecraftSign;
import com.github.rolecraftdev.sign.SignInteractionHandler;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Handles right-click interactions of {@link RolecraftSign}s of guildtype. Can
 * be used to join open guilds etc.
 *
 * @since 0.1.0
 */
public final class GuildSignInteractionHandler
        implements SignInteractionHandler {
    /**
     * The {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;
    /**
     * The {@link RolecraftCore} plugin {@link DataManager} instance.
     */
    private final DataManager dataManager;
    /**
     * The {@link RolecraftCore} plugin {@link GuildManager} instance.
     */
    private final GuildManager guildManager;

    /**
     * Constructs a new handler for guild-related {@link RolecraftSign}
     * interactions.
     *
     * @param plugin the {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public GuildSignInteractionHandler(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getDataManager();
        this.guildManager = plugin.getGuildManager();
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void handleSignInteraction(@Nonnull final Player player,
            @Nonnull final RolecraftSign sign) {
        final String function = sign.getFunction().toLowerCase();

        if (function.equals("join")) {
            final String guildName = sign.getData();
            final Guild guild = guildManager.getGuild(guildName);

            if (guild == null || !guild.isOpen()) {
                player.sendMessage(plugin.getMessage(Messages.BROKEN_SIGN));
                return;
            }

            completeGuildAdd(player, guild);
        }
    }

    /**
     * {@inheritDoc}
     * @since 0.1.0
     */
    @Nonnull @Override
    public String getType() {
        return GuildManager.GUILD_SIGN_TYPE;
    }

    /**
     * Add the given player to the given {@link Guild}. This will automatically
     * send the appropriate chat messages and update the player's
     * {@link PlayerData}.
     *
     * @param player the player to add
     * @param guild the {@link Guild} the given player should be added to
     * @since 0.1.0
     */
    private void completeGuildAdd(final Player player, final Guild guild) {
        final UUID pid = player.getUniqueId();
        final PlayerData data = plugin.getPlayerData(pid);
        if (data.getGuild() != null) {
            player.sendMessage(plugin.getMessage(Messages.ALREADY_IN_GUILD));
            return;
        }

        data.setGuild(guild.getId());
        final GuildPlayerJoinEvent event = guild
                .addMember(pid, guild.getDefaultRank());
        if (event.isCancelled()) {
            data.setGuild(null);
            player.sendMessage(event.getCancelMessage());
            return;
        }

        player.sendMessage(plugin.getMessage(Messages.GUILD_JOINED_PLAYER,
                MessageVariable.GUILD.value(guild.getName())));
        guild.broadcastMessage(plugin.getMessage(Messages.GUILD_JOINED_OTHERS,
                MessageVariable.GUILD.value(guild.getName()),
                MessageVariable.PLAYER.value(player
                        .getName())));
    }
}
