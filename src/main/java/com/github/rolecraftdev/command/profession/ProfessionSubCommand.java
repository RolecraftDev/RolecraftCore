package com.github.rolecraftdev.command.profession;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.RCSubCommand;
import com.github.rolecraftdev.profession.ProfessionManager;

public abstract class ProfessionSubCommand extends RCSubCommand {
    protected final ProfessionManager professionManager;

    protected ProfessionSubCommand(final RolecraftCore plugin) {
        super(plugin);

        professionManager = plugin.getProfessionManager();
    }
}
