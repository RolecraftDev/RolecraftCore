package com.github.rolecraftdev.util.messages;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class FormattedMessage {
    /**
     * The message this FormattedMessage wraps.
     */
    private final String message;

    public FormattedMessage(String message) {
        this.message = message;
    }

    public void send(CommandSender sender) {
        sender.sendMessage(message);
    }

    public void log(Level level, Plugin plugin) {
        plugin.getLogger().log(level, message);
    }

    public String get() {
        return message;
    }
}
