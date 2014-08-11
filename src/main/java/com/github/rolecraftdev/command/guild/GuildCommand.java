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

import pw.ian.albkit.command.TreeCommandHandler;

import com.github.rolecraftdev.RolecraftCore;

public class GuildCommand extends TreeCommandHandler {
    private final RolecraftCore plugin;

    public GuildCommand(final RolecraftCore plugin) {
        super(plugin, "guild");
        this.plugin = plugin;
    }

    @Override
    public void setupSubcommands() {
        addSubcommand("create", new GuildCreateCommand(plugin));
        addSubcommand("disband", new GuildDisbandCommand(plugin));
        addSubcommand("home", new GuildHomeCommand(plugin));
        addSubcommand("join", new GuildJoinCommand(plugin));
        addSubcommand("leave", new GuildLeaveCommand(plugin));
        addSubcommand("member", new GuildMemberCommand(plugin));
        addSubcommand("rank", new GuildRankCommand(plugin));
        addSubcommand("reject", new GuildRejectCommand(plugin));
        addSubcommand("show", new GuildShowCommand(plugin));
        // TODO more
    }
}
