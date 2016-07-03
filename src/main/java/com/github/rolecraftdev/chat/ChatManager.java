/*
 * This file is part of RolecraftCore.
 *
 * Copyright (c) 2014 RolecraftDev <http://rolecraftdev.github.com>
 * RolecraftCore is licensed under the Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-nc-nd/3.0
 *
 * As long as you follow the following terms, you are free to copy and redistribute
 * the material in any medium or format.
 *
 * You must give appropriate credit, provide a link to the license, and indicate
 * whether any changes were made to the material. You may do so in any reasonable
 * manner, but not in any way which suggests the licensor endorses you or your use.
 *
 * You may not use the material for commercial purposes.
 *
 * If you remix, transform, or build upon the material, you may not distribute the
 * modified material.
 *
 * You may not apply legal terms or technological measures that legally restrict
 * others from doing anything the license permits.
 *
 * DISCLAIMER: This is a human-readable summary of (and not a substitute for) the
 * license.
 */
package com.github.rolecraftdev.chat;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.chat.channel.ChannelListener;
import com.github.rolecraftdev.chat.channel.ChatChannel;
import com.github.rolecraftdev.data.PlayerData;
import com.github.rolecraftdev.event.RolecraftEventFactory;
import com.github.rolecraftdev.event.channel.AsyncChannelPlayerChatEvent;
import com.github.rolecraftdev.event.channel.ChannelPlayerJoinEvent;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.util.serial.YamlFile;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.io.File.separator;

/**
 * The {@link RolecraftCore} plugin chat manager, which deals with channels and
 * other chat-related features in Rolecraft.
 *
 * @since 0.1.0
 */
public final class ChatManager {
    /**
     * The permission required for a player to be allowed to join moderator chat
     * channels.
     */
    private static final String MOD_PERMISSION = "rolecraft.channels.mod";
    /**
     * The permission required for a player to be allowed to join administrator
     * chat channels.
     */
    private static final String ADMIN_PERMISSION = "rolecraft.channels.admin";

    /**
     * The time (in ticks) between saves.
     */
    private static final long SAVE_PERIOD = 20 * 60 * 5;

    /**
     * The associated {@link RolecraftCore} plugin instance.
     */
    private final RolecraftCore plugin;
    /**
     * The {@link ChatFormatter} for the plugin's configured chat format.
     */
    private final ChatFormatter formatter;
    /**
     * A {@link Set} of all loaded {@link ChatChannel}s.
     */
    private final Set<ChatChannel> channels;
    /**
     * A {@link Map} of player {@link UUID}s to {@link Set}s of
     * {@link ChatChannel}s each player is in.
     */
    private final Map<UUID, Set<ChatChannel>> playerChannels;
    /**
     * A {@link Map} of player {@link UUID}s to the {@link ChatChannel} each
     * player is currently talking in.
     */
    private final Map<UUID, ChatChannel> currentPlayerChannels;
    /**
     * A {@link Set} of taken {@link ChatChannel} ids.
     */
    private final Set<Integer> usedIds;
    /**
     * The {@link File} in which channels are stored.
     */
    private final File channelsFile;
    /**
     * The {@link YamlFile} representing the storage file.
     */
    private final YamlFile yamlFile;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public ChatManager(@Nonnull final RolecraftCore plugin) {
        this.plugin = plugin;
        this.formatter = new ChatFormatter(
                plugin.getConfigValues().getChatFormat());
        this.channels = new HashSet<ChatChannel>();
        this.playerChannels = new HashMap<UUID, Set<ChatChannel>>();
        this.currentPlayerChannels = new HashMap<UUID, ChatChannel>();
        this.usedIds = new HashSet<Integer>();

        plugin.saveResource("channels" + separator + "channels.yml", false);
        this.channelsFile = new File(plugin.getDataFolder(),
                "channels" + separator + "channels.yml");
        this.yamlFile = new YamlFile(channelsFile);

        for (final String key : yamlFile.getKeys(false)) {
            final int id = Integer.parseInt(key);
            final ConfigurationSection section = yamlFile
                    .getConfigurationSection(key);

            final String name = section.getString("name");
            final boolean def = section.getBoolean("default", false);
            final boolean mod = section.getBoolean("mod", false);
            final boolean admin = section.getBoolean("admin", false);
            final int range = section.getInt("range", -1);

            String guild = null;
            ChatColor color = null;
            if (section.contains("guild")) {
                guild = section.getString("guild");
            }
            if (section.contains("color")) {
                color = ChatColor.valueOf(section.getString("color"));
            }

            this.channels.add(new ChatChannel(id, name, def, mod, admin, guild,
                    color, range));
            this.usedIds.add(id);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ChatManager.this.saveChannels();
            }
        }.runTaskTimerAsynchronously(plugin, SAVE_PERIOD, SAVE_PERIOD);

        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new ChatListener(this), plugin);
        pm.registerEvents(new ChannelListener(this), plugin);
    }

    /**
     * Gets the next id for a chat channel. Should always be called from the
     * main thread and the value should always be used immediately.
     *
     * @return the next available id
     * @since 0.1.0
     */
    public int getNextId() {
        int i = 0;
        while (usedIds.contains(i)) {
            i++;
        }
        return i;
    }

    /**
     * Gets the associated {@link RolecraftCore} plugin instance.
     *
     * @return the associated {@link RolecraftCore} plugin instance
     * @since 0.1.0
     */
    public RolecraftCore getPlugin() {
        return plugin;
    }

    /**
     * Gets the {@link ChatFormatter} for the plugin's configured chat format.
     * This object can be used to format messages or to change the format used
     * by Rolecraft.
     *
     * @return the {@link ChatFormatter} for the plugin's configured chat format
     * @since 0.1.0
     */
    public ChatFormatter getFormatter() {
        return formatter;
    }

    /**
     * Gets the {@link ChatChannel} with the given name.
     *
     * @param name the name of the channel to get
     * @return the channel with the given name
     * @since 0.1.0
     */
    @Nullable
    public ChatChannel getChannel(@Nonnull final String name) {
        for (final ChatChannel channel : this.channels) {
            if (channel.getName().equalsIgnoreCase(name)) {
                return channel;
            }
        }

        return null;
    }

    /**
     * Get a {@link Set} of all {@link ChatChannel}s.
     *
     * @return all loaded chat channels
     * @since 0.1.0
     */
    public Set<ChatChannel> getAllChannels() {
        return new HashSet<ChatChannel>(this.channels);
    }

    /**
     * Sends the given message {@link String} to the given {@link ChatChannel}.
     * The message is expected to be pre-formatted.
     *
     * @param channel the channel to send the message to
     * @param message the message to send
     * @since 0.1.0
     */
    public void sendToChannel(@Nonnull final ChatChannel channel,
            @Nonnull final Player sender, @Nonnull final String message) {
        this.sendToChannel(channel, sender, message, null);
    }

    /**
     * Sends the given message {@link String} to the given {@link ChatChannel}.
     * The message is expected to be pre-formatted. Only sends the message to
     * players within the channel's range.
     *
     * @param channel the channel to send the message to
     * @param message the message to send
     * @param location the location the message is being sent from
     * @since 0.1.0
     */
    public void sendToChannel(@Nonnull final ChatChannel channel,
            @Nonnull final Player sender, @Nonnull final String message,
            @Nullable final Location location) {
        final AsyncChannelPlayerChatEvent event = RolecraftEventFactory
                .channelPlayerChat(channel, sender, message);
        if (event.isCancelled()) {
            sender.sendMessage(event.getCancelMessage());
            return;
        }

        final String finalisedMessage = event.getMessage();

        final Set<UUID> inChannel = new HashSet<UUID>();
        synchronized (this.playerChannels) {
            for (final Map.Entry<UUID, Set<ChatChannel>> entry :
                    this.playerChannels.entrySet()) {
                if (entry.getValue().contains(channel)) {
                    inChannel.add(entry.getKey());
                }
            }
        }

        if (location != null && !(channel.getRange() < 0)) {
            final int rangeSquared = channel.getRange() * channel.getRange();

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (final UUID uuid : inChannel) {
                        final Player player = Bukkit.getPlayer(uuid);
                        if (player.getLocation().distanceSquared(location)
                                <= rangeSquared) {
                            player.sendMessage(finalisedMessage);
                        }
                    }
                }
            }.runTask(plugin);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (final UUID uuid : inChannel) {
                        Bukkit.getPlayer(uuid).sendMessage(finalisedMessage);
                    }
                }
            }.runTask(plugin);
        }
    }

    /**
     * Checks whether the given {@link Player} is permitted to join the given
     * {@link ChatChannel}.
     *
     * @param player the player to check the permissions of
     * @param channel the channel to check for
     * @return whether the given player can join the given chat channel
     * @since 0.1.0
     */
    public boolean isPermittedEntry(@Nonnull final Player player,
            @Nonnull final ChatChannel channel) {
        if (channel.isMod() && !player.hasPermission(MOD_PERMISSION)) {
            return false;
        }

        if (channel.isAdmin() && !player.hasPermission(ADMIN_PERMISSION)) {
            return false;
        }

        final String chanGuild = channel.getGuild();
        final PlayerData data = plugin.getPlayerData(player);
        final UUID guildId = data.getGuild();

        if (guildId != null) {
            final Guild guild = plugin.getGuild(guildId);
            if (guild != null && !guild.getName().equalsIgnoreCase(chanGuild)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get a {@link Set} of all {@link ChatChannel}s the player with the given
     * {@link UUID} is part of.
     *
     * @param player the id of the player to get channels for
     * @return channels the given player is a part of
     * @since 0.1.0
     */
    public Set<ChatChannel> getChannels(@Nonnull final UUID player) {
        synchronized (playerChannels) {
            return new HashSet<ChatChannel>(this.playerChannels.get(player));
        }
    }

    /**
     * Adds the given {@link ChatChannel} to the channels the player with the
     * given {@link UUID} is part of.
     *
     * @param player the player to add to the channel
     * @param channel the channel to add the player to
     * @since 0.1.0
     */
    public boolean addToChannel(@Nonnull final UUID player,
            @Nonnull final ChatChannel channel) {
        final ChannelPlayerJoinEvent event = RolecraftEventFactory
                .channelPlayerJoin(channel, Bukkit.getPlayer(player));
        if (event.isCancelled()) {
            Bukkit.getPlayer(player).sendMessage(event.getCancelMessage());
            return false;
        }

        synchronized (playerChannels) {
            this.playerChannels.get(player).add(channel);
        }

        return true;
    }

    /**
     * Removes the given {@link ChatChannel} from the channels the player with
     * the given {@link UUID} is part of.
     *
     * @param player the player to remove from the channel
     * @param channel the channel to remove the player from
     * @since 0.1.0
     */
    public void removeFromChannel(@Nonnull final UUID player,
            @Nonnull final ChatChannel channel) {
        RolecraftEventFactory.channelPlayerLeave(channel,
                Bukkit.getPlayer(player));

        synchronized (playerChannels) {
            this.playerChannels.get(player).remove(channel);
        }
    }

    /**
     * Adds the given {@link ChatChannel} to the list of channels.
     *
     * @param channel the channel to add
     * @since 0.1.0
     */
    public void addChannel(@Nonnull final ChatChannel channel) {
        this.channels.add(channel);
        this.usedIds.add(channel.getId());
    }

    /**
     * Removes the given {@link ChatChannel} from the list of channels.
     *
     * @param channel the channel to remove
     * @since 0.1.0
     */
    public void removeChannel(@Nonnull final ChatChannel channel) {
        this.channels.remove(channel);

        synchronized (playerChannels) {
            for (final UUID player : playerChannels.keySet()) {
                this.removeFromChannel(player, channel);
            }
        }

        synchronized (currentPlayerChannels) {
            for (final UUID player : currentPlayerChannels.keySet()) {
                final ChatChannel curChannel = currentPlayerChannels
                        .get(player);
                if (curChannel == channel) {
                    currentPlayerChannels.put(player, null);
                }
            }
        }
    }

    /**
     * Gets the {@link ChatChannel} the player with the given {@link UUID} is
     * currently speaking in.
     *
     * @param player the id of the player to get the current channel for
     * @return the given player's current chat channel
     * @since 0.1.0
     */
    public ChatChannel getCurrentChannel(@Nonnull final UUID player) {
        synchronized (currentPlayerChannels) {
            return this.currentPlayerChannels.get(player);
        }
    }

    /**
     * Sets the {@link ChatChannel} the player with the given {@link UUID} is
     * currently speaking in.
     *
     * @param player the id of the player to set the current channel for
     * @param channel the given player's new current chat channel
     * @since 0.1.0
     */
    public void setCurrentChannel(@Nonnull final UUID player,
            @Nonnull final ChatChannel channel) {
        synchronized (currentPlayerChannels) {
            this.currentPlayerChannels.put(player, channel);
        }
    }

    /**
     * Saves all currently loaded {@link ChatChannel}s to YAML.
     *
     * @since 0.1.0
     */
    public void saveChannels() {
        synchronized (yamlFile) {
            for (final ChatChannel channel : this.channels) {
                if (!yamlFile.contains(String.valueOf(channel.getId()))) {
                    yamlFile.createSection(String.valueOf(channel.getId()));
                }

                final ConfigurationSection section = yamlFile
                        .getConfigurationSection(
                                String.valueOf(channel.getId()));
                section.set("name", channel.getName());
                section.set("default", channel.isDefault());
                section.set("mod", channel.isMod());
                section.set("admin", channel.isAdmin());
                section.set("range", channel.getRange());

                if (channel.getGuild() != null) {
                    section.set("guild", channel.getGuild());
                }
                if (channel.getColor() != null) {
                    section.set("color", channel.getColor().name());
                }
            }

            for (final String key : yamlFile.getKeys(false)) {
                final int id = Integer.parseInt(key);
                boolean found = false;
                for (final ChatChannel channel : this.channels) {
                    if (channel.getId() == id) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    yamlFile.set(key, null);
                }
            }

            yamlFile.save();
        }
    }
}
