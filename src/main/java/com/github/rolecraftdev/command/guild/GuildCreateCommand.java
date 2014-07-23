package com.github.rolecraftdev.command.guild;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.guild.Guild;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GuildCreateCommand extends GuildSubCommand {
    public GuildCreateCommand(final RolecraftCore plugin) {
        super(plugin);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    ChatColor.DARK_RED + "Only players can make guilds!");
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(ChatColor.DARK_RED
                    + "Invalid syntax, /guild create <name>");
            return;
        }
        final Player thePlayer = ((Player) sender);
        final UUID player = thePlayer.getUniqueId();
        if (guildManager.getPlayerGuild(player) != null) {
            sender.sendMessage(
                    ChatColor.DARK_RED + "You are already in a guild!");
            return;
        }
        if (plugin.useEconomy()) {
            final EconomyResponse response = plugin.getEconomy().bankHas(
                    thePlayer.getName(),
                    guildManager.getCreationCost());
            if (!response.transactionSuccess()) {
                sender.sendMessage(
                        ChatColor.DARK_RED + "You can't afford to do that!");
                return;
            }
        }

        final String name = args[1];
        final Guild guild = new Guild(guildManager);

        guild.setName(name);
        guild.setLeader(player);
        if (guildManager.addGuild(guild, false)) {
            if (plugin.useEconomy()) {
                plugin.getEconomy().bankWithdraw(thePlayer.getName(),
                        guildManager.getCreationCost());
            }
            sender.sendMessage(ChatColor.GRAY
                    + "You created a guild named '" + name + "'!");
        } else {
            sender.sendMessage(ChatColor.DARK_RED
                    + "Couldn't create guild! The name may have already been taken!");
        }
    }

    @Override
    public String[] getNames() {
        return new String[] { "create", "new" };
    }

    @Override
    public String getPermission() {
        return "rolecraft.guild.create";
    }

    @Override
    public String getUsage() {
        return "/guild create <name>";
    }

    @Override
    public String getDescription() {
        return "Creates a new guild with the given name";
    }
}
