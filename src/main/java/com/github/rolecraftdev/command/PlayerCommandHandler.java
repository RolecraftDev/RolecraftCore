package com.github.rolecraftdev.command;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Base command handler for commands which may only be executed by players.
 *
 * @since 0.0.5
 */
public abstract class PlayerCommandHandler extends CommandHandler {
    /**
     * The message sent to someone who tries to execute the command but is not a
     * player. Loaded from messages config.
     */
    private String notPlayerMessage;

    public PlayerCommandHandler(@Nonnull final RolecraftCore plugin,
            @Nonnull final String name) {
        super(plugin, name);

        notPlayerMessage = plugin.getMessage(Messages.NOT_PLAYER);
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
                // TODO: add to messages system
                player.sendMessage(
                        ChatColor.RED + "Invalid usage, " + getUsage());
                return;
            }
        }
        this.onCommand(player, newArgs);
    }

    public void onCommand(Player player, Arguments args) {
    }
}
