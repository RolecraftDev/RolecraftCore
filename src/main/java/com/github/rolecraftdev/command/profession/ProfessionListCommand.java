package com.github.rolecraftdev.command.profession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.profession.Profession;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public final class ProfessionListCommand extends ProfessionSubCommand {
    public static final int PROFESSIONS_PER_PAGE = 7;

    public ProfessionListCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        String pageArg = "1";
        if (args.length >= 2) {
            pageArg = args[1];
        }

        sender.sendMessage(ChatColor.GOLD + "[Professions]");
        for (final Profession profession : CommandHelper.getPageFromArgs(sender,
                new ArrayList<Profession>(professionManager.getProfessions()),
                pageArg, PROFESSIONS_PER_PAGE)) {
            sender.sendMessage(ChatColor.GRAY + profession.getName());
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "list" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.profession.list";
    }

    @Override
    public String getUsage() {
        return "/profession list <page>";
    }

    @Override
    public String getDescription() {
        return "Shows a list of available professions";
    }
}
