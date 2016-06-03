package com.github.rolecraftdev.command;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.command.parser.parameters.ParamsBase;
import com.github.rolecraftdev.util.messages.Messages;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Handles a normal command in Rolecraft.
 *
 * @see {@link PlayerCommandHandler} for commands only executable by players
 * @see {@link TreeCommandHandler} for commands with several subcommands
 * @since 0.0.5
 */
public abstract class CommandHandler implements CommandExecutor {
    /**
     * The {@link RolecraftCore} plugin instance.
     */
    protected final RolecraftCore plugin;
    /**
     * The name of this command.
     */
    private final String name;

    /**
     * Usage string for the command, to be sent to anyone who uses it
     * incorrectly.
     */
    private String usage;
    /**
     * A description of the command's functionality.
     */
    private String description;
    /**
     * The permission node required to execute the command.
     */
    private String permission;
    /**
     * The message sent to somebody who does not have the correct permission but
     * attempts to execute the command anyway.
     */
    private String noPermissionMessage;
    /**
     * The minimum number of arguments required to successfully execute the
     * command.
     */
    private int minArgs;
    /**
     * The maximum number of arguments permitted in the command's usage.
     */
    private int maxArgs;
    /**
     * Whether the command is to be executed asynchronously.
     */
    private boolean async;
    /**
     * Whether to check the command against the correct usage before passing it
     * on to the main executor method.
     */
    private boolean validateUsage;

    /**
     * A template for the correct usage of the command with information about
     * each required and optional parameter.
     */
    protected ParamsBase paramsBase;

    public CommandHandler(RolecraftCore plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        noPermissionMessage = plugin.getMessage(Messages.NO_PERMISSION);
        usage = "/" + name;
        description = usage;
        permission = null;
        minArgs = 0;
        maxArgs = Integer.MAX_VALUE;
        async = false;
        validateUsage = true;
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public boolean doesValidateUsage() {
        return validateUsage;
    }

    public ParamsBase getParamsBase() {
        return paramsBase;
    }

    public void setUsage(String usage) {
        this.usage = usage;
        paramsBase = ParamsBase.fromUsageString(usage);
    }

    public void setValidateUsage(boolean validateUsage) {
        this.validateUsage = validateUsage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public int getMinArgs() {
        return minArgs;
    }

    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public int getMaxArgs() {
        return maxArgs;
    }

    public void setMaxArgs(int maxArgs) {
        this.maxArgs = maxArgs;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        if (async && plugin == null) {
            throw new IllegalArgumentException(
                    "Cannot make command async without a plugin specified in the constructor!");
        }
        this.async = async;
    }

    /**
     * Sends a message to the given {@link CommandSender} detailing the correct
     * usage of this command.
     *
     * @param sender the {@link CommandSender} to send the usage to
     */
    public void sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: " + usage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string,
            String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(noPermissionMessage);
            return true;
        }

        if (async) {
            executeAsync(sender, args);
        } else {
            this.onCommand(sender, args);
        }
        return true;
    }

    /**
     * Executes this command asynchronously.
     *
     * @param sender the sender of the command
     * @param args the arguments inputted by the sender
     */
    private void executeAsync(final CommandSender sender, final String[] args) {
        (new BukkitRunnable() {
            @Override
            public void run() {
                CommandHandler.this.onCommand(sender, args);
            }
        }).runTaskAsynchronously(plugin);
    }

    /**
     * Executes the command, sync.
     *
     * @param sender the sender of the command
     * @param args the arguments inputted by the sender
     */
    public void onCommand(final CommandSender sender, final String[] args) {
        if (args.length < getMinArgs()) {
            sendUsageMessage(sender);
            return;
        }
        if (args.length > getMaxArgs()) {
            sendUsageMessage(sender);
            return;
        }

        Arguments newArgs = new Arguments(args);
        if (paramsBase != null) {
            newArgs.withParams(paramsBase.createParams(newArgs));
            if (doesValidateUsage() && !newArgs.getParams().valid()) {
                sender.sendMessage(
                        ChatColor.RED + "Invalid usage, " + getUsage());
                return;
            }
        }
        this.onCommand(sender, newArgs);
    }

    /**
     * Command handler method for subclasses using the Arguments API.
     *
     * @param sender the sender of the command
     * @param args the arguments inputted by the sender
     */
    public void onCommand(final CommandSender sender, final Arguments args) {
    }
}
