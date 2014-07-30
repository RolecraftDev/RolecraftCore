package com.github.rolecraftdev.command.other;

import pw.ian.albkit.command.PlayerCommandHandler;
import pw.ian.albkit.command.parser.Arguments;

import com.github.rolecraftdev.RolecraftCore;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RCConfirmCommand extends PlayerCommandHandler {
    private Map<UUID, Runnable> waiting = new HashMap<UUID, Runnable>();

    private final RolecraftCore plugin;

    public RCConfirmCommand(final RolecraftCore plugin) {
        super(plugin, "rcconfirm");
        this.plugin = plugin;
        plugin.setConfirmCommand(this);

        setUsage("/gc [message]");
        setDescription("Allows communicating in Guild chat");
        setPermission("rolecraft.guild.chat");
    }

    @Override
    public void onCommand(final Player player, final Arguments args) {
        final UUID id = player.getUniqueId();
        if (waiting.containsKey(id)) {
            waiting.get(id).run();
        } else {
            player.sendMessage(
                    ChatColor.DARK_RED + "You have nothing to confirm!");
        }
    }

    public void addWaiting(final UUID id, final Runnable runnable) {
        if (waiting.containsKey(id)) {
            waiting.remove(id);
        }
        waiting.put(id, runnable);
    }
}
