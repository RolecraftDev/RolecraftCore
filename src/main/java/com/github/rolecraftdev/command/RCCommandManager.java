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
package com.github.rolecraftdev.command;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.guild.GuildCommand;
import com.github.rolecraftdev.command.other.GCCommand;
import com.github.rolecraftdev.command.other.RCConfirmCommand;
import com.github.rolecraftdev.command.profession.ProfessionCommand;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;

public final class RCCommandManager {
    private final RolecraftCore plugin;

    public RCCommandManager(final RolecraftCore plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        // Set up guild command
        final PluginCommand guildCommand = plugin.getCommand("guild");
        final GuildCommand guildCommandExec = new GuildCommand(plugin);
        guildCommand.setAliases(Arrays.asList(guildCommandExec.getNames()));

        // Set up profession command
        final PluginCommand professionCommand = plugin.getCommand("profession");
        final ProfessionCommand professionExec = new ProfessionCommand(plugin);
        professionCommand.setAliases(Arrays.asList(professionExec.getNames()));

        // Set up guild chat command
        final PluginCommand gcCommand = plugin.getCommand("gc");
        final GCCommand gcCommandExec = new GCCommand(plugin);
        gcCommand.setAliases(Arrays.asList(gcCommandExec.getNames()));

        // Set up confirm command
        final PluginCommand confirmCommand = plugin.getCommand("rcconfirm");
        final RCConfirmCommand confirmExec = new RCConfirmCommand(plugin);
        confirmCommand.setAliases(Arrays.asList(confirmExec.getNames()));

        // Set executors
        guildCommand.setExecutor(guildCommandExec);
        professionCommand.setExecutor(professionExec);
        gcCommand.setExecutor(gcCommandExec);
        confirmCommand.setExecutor(confirmExec);
    }
}
