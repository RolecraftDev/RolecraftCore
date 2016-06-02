package com.github.rolecraftdev.util.messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageHelper {
    public static void sendBanner(CommandSender sender, Object... message) {
        sendBanner(ColourScheme.DEFAULT, sender, message);
    }

    public static void sendBanner(ColourScheme scheme, CommandSender sender, Object... message) {
        sender.sendMessage(ChatColor.GRAY + " " + ChatColor.STRIKETHROUGH + "-" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "--------------------------------------------------" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-");
        for (Object line : message) {
            sender.sendMessage(scheme.getLight() + " " + format(scheme, line.toString()).get());
        }
        sender.sendMessage(ChatColor.GRAY + " " + ChatColor.STRIKETHROUGH + "-" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "--------------------------------------------------" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-");
    }

    public static void formatAndSend(String message, CommandSender sender) {
        format(message).send(sender);
    }

    public static void formatAndSend(ColourScheme scheme, String message, CommandSender sender) {
        format(scheme, message).send(sender);
    }

    public static FormattedMessage format(String message) {
        return format(ColourScheme.DEFAULT, message);
    }

    public static FormattedMessage format(ColourScheme scheme, String message) {
        return createMessage(scheme).append(message).toMessage();
    }

    public static MessageBuilder createMessage() {
        return createMessage(ColourScheme.DEFAULT);
    }

    public static MessageBuilder createMessage(String message) {
        return createMessage(ColourScheme.DEFAULT, message);
    }

    public static MessageBuilder createMessage(ColourScheme scheme) {
        return new MessageBuilder(scheme);
    }

    public static MessageBuilder createMessage(ColourScheme scheme, String message) {
        return createMessage(scheme).append(message);
    }
}
