package com.github.rolecraftdev.command.profession;

import pw.ian.albkit.command.CommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.profession.Profession;
import com.github.rolecraftdev.profession.ProfessionManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public final class ProfessionListCommand extends CommandHandler {
    private static final int PROFESSIONS_PER_PAGE = 7;

    private final ProfessionManager profMgr;

    public ProfessionListCommand(final RolecraftCore plugin) {
        super(plugin, "list");
        profMgr = plugin.getProfessionManager();
    }

    @Override
    public void onCommand(final CommandSender sender, final Arguments args) {
        String pageArg = "1";
        if (args.length() >= 2) {
            pageArg = args.getArgument(1).rawString();
        }

        sender.sendMessage(ChatColor.GOLD + "[Professions]");
        for (final Profession profession : CommandHelper.getPageFromArgs(sender,
                new ArrayList<Profession>(profMgr.getProfessions()),
                pageArg, PROFESSIONS_PER_PAGE)) {
            sender.sendMessage(ChatColor.GRAY + profession.getName());
        }
    }
}
