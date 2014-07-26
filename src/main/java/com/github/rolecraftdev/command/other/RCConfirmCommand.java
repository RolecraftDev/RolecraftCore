package com.github.rolecraftdev.command.other;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.RCCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RCConfirmCommand extends RCCommand {
    private Map<UUID, Runnable> waiting = new HashMap<UUID, Runnable>();

    public RCConfirmCommand(final RolecraftCore plugin) {
        super(plugin);

        plugin.setConfirmCommand(this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd,
            final String lbl, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ChatColor.DARK_RED + "Only players can use /confirm!");
            return true;
        }

        final UUID id = ((Player) sender).getUniqueId();
        if (waiting.containsKey(id)) {
            waiting.get(id).run();
        } else {
            sender.sendMessage(
                    ChatColor.DARK_RED + "You have nothing to confirm!");
        }

        return true;
    }

    @Override
    public String[] getNames() {
        return new String[] { "rcconfirm", "rconfirm" };
    }

    public void addWaiting(final UUID id, final Runnable runnable) {
        if (waiting.containsKey(id)) {
            waiting.remove(id);
        }
        waiting.put(id, runnable);
    }
}
