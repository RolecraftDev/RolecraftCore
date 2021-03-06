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
package com.github.rolecraftdev.command.profession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.BaseCommandHandler;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Set;

/**
 * @since 0.0.5
 */
public final class ProfessionListCommand extends BaseCommandHandler {
    private static final int PROFESSIONS_PER_PAGE = 7;

    private final ProfessionManager professionManager;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public ProfessionListCommand(final RolecraftCore plugin) {
        super(plugin, "list");
        professionManager = plugin.getProfessionManager();

        setUsage("/profession list [page]");
        setDescription("View a list of permissions at the given page");
        setPermission("rolecraft.profession.list");
        setSubcommand(true);
    }

    /**
     * @since 0.0.5
     */
    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        final Set<Profession> professions = professionManager.getProfessions();
        if (professions == null || professions.size() == 0) {
            sender.sendMessage(plugin.getMessage(Messages.NO_PROFESSIONS));
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "[Professions]");
        for (final Profession profession : CommandHelper.getPageFromArgs(plugin,
                sender, new ArrayList<Profession>(
                        professionManager.getProfessions()),
                args.length() > 0 ? args.get(0) : null, PROFESSIONS_PER_PAGE)) {
            sender.sendMessage(ChatColor.GRAY + profession.getName());
        }
    }
}
