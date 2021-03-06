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
package com.github.rolecraftdev.command.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.PlayerCommandHandler;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.data.DataManager;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildManager;
import com.github.rolecraftdev.util.messages.MessageVariable;
import com.github.rolecraftdev.util.messages.Messages;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @since 0.0.5
 */
public class GuildCreateCommand extends PlayerCommandHandler {
    private final GuildManager guildManager;
    private final DataManager dataManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public GuildCreateCommand(final RolecraftCore plugin) {
        super(plugin, "create");
        guildManager = plugin.getGuildManager();
        dataManager = plugin.getDataManager();

        setUsage("/guild create <name>");
        setDescription("Create a guild");
        setPermission("rolecraft.guild.create");
        setSubcommand(true);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final Player player, final Arguments args) {
        if (args.length() == 0) {
            sendUsageMessage(player);
            return;
        }
        final UUID playerId = player.getUniqueId();
        if (guildManager.getPlayerGuild(playerId) != null) {
            player.sendMessage(plugin.getMessage(Messages.ALREADY_IN_GUILD));
            return;
        }
        if (plugin.doesUseEconomy()) {
            if (!plugin.getVaultEcon().has(player.getName(), guildManager.getCreationCost())) {
                player.sendMessage(plugin.getMessage(Messages.CANNOT_AFFORD));
                return;
            }
        }

        final String name = args.getRaw(0);
        final Guild guild = new Guild(guildManager);

        guild.create(name, playerId);
        if (guildManager.addGuild(guild, false)) {
            player.sendMessage(plugin.getMessage(Messages.GUILD_CREATED,
                    MessageVariable.GUILD.value(name)));
            dataManager.getPlayerData(playerId).setGuild(guild.getId());

            if (plugin.doesUseEconomy()) {
                plugin.getVaultEcon().bankWithdraw(player.getName(),
                        guildManager.getCreationCost());
            }
        } else {
            player.sendMessage(plugin.getMessage(Messages.GUILD_ALREADY_EXISTS,
                    MessageVariable.GUILD.value(name)));
        }
    }
}
