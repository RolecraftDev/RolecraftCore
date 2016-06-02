package com.github.rolecraftdev.command;

import com.github.rolecraftdev.command.parser.Arguments;
import com.github.rolecraftdev.command.parser.parameters.ParamsBase;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CommandHandler implements CommandExecutor {
    private final JavaPlugin plugin;
    private final String name;

    private String usage;
    private String description;
    private String permission;
    private String noPermissionMessage = ChatColor.RED + "You don't have permission to use this command.";
    private int minArgs, maxArgs;
    private boolean async;
    private boolean validateUsage;
    protected ParamsBase paramsBase;

    public CommandHandler(String name) {
        this(null, name);
    }

    public CommandHandler(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
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

    public void setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
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

    public void setArgsBounds(int min, int max) {
        setMinArgs(min);
        setMaxArgs(max);
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        if (async && plugin == null) {
            throw new IllegalArgumentException("Cannot make command async without a plugin specified in the constructor!");
        }
        this.async = async;
    }

    public void sendUsageMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: " + usage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmnd, String string, String[] args) {
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
     * @param sender
     * @param args
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
     * Command handler method.
     *
     * @param sender
     * @param args
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
                sender.sendMessage(ChatColor.RED + "Invalid usage, " + getUsage());
                return;
            }
        }
        this.onCommand(sender, newArgs);
    }

    /**
     * Command handler method using the Arguments API.
     *
     * @param sender
     * @param args
     */
    public void onCommand(final CommandSender sender, final Arguments args) {
    }
}
