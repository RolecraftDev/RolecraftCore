package com.github.rolecraftdev.command;

import com.github.rolecraftdev.command.parser.Arguments;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PlayerCommandHandler extends CommandHandler {
    private String notPlayerMessage = ChatColor.RED + "You must be a player to use this command.";

    public PlayerCommandHandler(String name) {
        super(name);
    }

    public PlayerCommandHandler(JavaPlugin plugin, String name) {
        super(plugin, name);
    }

    public String getNotPlayerMessage() {
        return notPlayerMessage;
    }

    public void setNotPlayerMessage(String notPlayerMessage) {
        this.notPlayerMessage = notPlayerMessage;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(notPlayerMessage);
            return;
        }

        onCommand((Player) sender, args);
    }

    public void onCommand(Player player, String[] args) {
        if (args.length < getMinArgs()) {
            sendUsageMessage(player);
            return;
        }
        if (args.length > getMaxArgs()) {
            sendUsageMessage(player);
            return;
        }

        Arguments newArgs = new Arguments(args);
        if (paramsBase != null) {
            newArgs.withParams(paramsBase.createParams(newArgs));
            if (doesValidateUsage() && !newArgs.getParams().valid()) {
                player.sendMessage(ChatColor.RED + "Invalid usage, " + getUsage());
                return;
            }
        }
        this.onCommand(player, newArgs);
    }

    public void onCommand(Player player, Arguments args) {
    }
}
