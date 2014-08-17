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
package com.github.rolecraftdev.util.messages;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.command.CommandHelper;
import com.github.rolecraftdev.data.storage.YamlFile;

import org.bukkit.ChatColor;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores configuration key constants for all messages used in Rolecraft so that
 * each message can be completely configurable
 */
public class Messages {
    // This would be so much easier if Java has associative arrays, just saying

    // The light colour used in messages
    public static final String LIGHT_COLOUR = "light-colour";
    // The dark colour used in messages
    public static final String DARK_COLOUR = "dark-colour";
    // The colour used for prefixes
    public static final String PREFIX_COLOUR = "prefix-colour";
    // The colour used for most of the message
    public static final String MESSAGE_COLOUR = "message-colour";
    // The colour used for highlighting small sections of text
    public static final String HIGHLIGHT_COLOUR = "highlight-colour";
    // Sent to players who don't have permission to perform a command
    public static final String NO_PERMISSION = "no-permission";
    // Sent to non-players who try to execute a player-only command
    public static final String NOT_PLAYER = "not-player";
    // Sent to players with no guild trying to execute commands which need one
    public static final String NO_GUILD = "no-guild";
    // Sent to players who aren't the guild leader when they need to be
    public static final String NOT_GUILD_LEADER = "not-guild-leader";
    // Sent to players who specify an invalid guild rank
    public static final String RANK_NOT_EXISTS = "rank-not-exists";
    // Sent to players who specify an invalid guild name
    public static final String GUILD_NOT_EXISTS = "guild-not-exists";
    // Sent to players who try to join a guild if they're already in one
    public static final String ALREADY_IN_GUILD = "already-in-guild";
    // Sent to players who can't affort what they're trying to do
    public static final String CANNOT_AFFORD = "cannot-afford";
    // Sent to players who create a guild
    public static final String GUILD_CREATED = "guild-created";
    // Sent to players who disband a guild
    public static final String GUILD_DISBANDED = "guild-disbanded";
    // Sent to players when they set the home of their guild
    public static final String SET_GUILD_HOME = "guild-home-set";
    // Sent to players who teleport to their guild home
    public static final String GUILD_TP_HOME = "guild-tp-home";
    // Sent to players who try to join a guild they aren't invited to
    public static final String GUILD_NOT_INVITED = "guild-not-invited";
    // Sent to someone who has rejected an invitation to a guild
    public static final String GUILD_INVITE_REJECTED = "guild-invite-rejected";
    // Sent to a player who has just joined a guild
    public static final String GUILD_JOINED_PLAYER = "guild-joined-player";
    // Sent to every member of the guild when a player joins
    public static final String GUILD_JOINED_OTHERS = "guild-joined-others";
    // Sent if someone gives the name of someone who's not played on the server
    public static final String PLAYER_NOT_EXISTS = "player-not-exists";
    // Sent if someone specifies a player who isn't online
    public static final String PLAYER_NOT_ONLINE = "player-not-online";
    // Sent if someone tries to invite a player who already has an invitation
    public static final String PLAYER_CONSIDERING_INVITE = "player-considering-invite";
    // Sent to a player who has just been invited to a guild
    public static final String PLAYER_INVITED_RECEIVER = "player-invited-receiver";
    // Sent to the sender of a guild invitation when it is sent
    public static final String PLAYER_INVITED_SENDER = "player-invited-sender";
    // Sent if someone specifies someone not in their guild for a guild-related action
    public static final String PLAYER_NOT_IN_GUILD = "player-not-in-guild";
    // Sent if someone specifies an invalid action to perform on a guild member
    public static final String INVALID_MEMBER_ACTION = "invalid-member-action";
    // Sent to a player who has been added to a rank within a guild
    public static final String PLAYER_ADDED_TO_RANK = "player-added-to-rank";
    // Sent to someone who has added a player to a guild rank
    public static final String ADDED_PLAYER_TO_RANK = "added-player-to-rank";
    // Sent to a player who has been removed from a rank within a guild
    public static final String PLAYER_REMOVED_FROM_RANK = "player-removed-from-rank";
    // Sent to someone who has removed a player from a guild rank
    public static final String REMOVED_PLAYER_FROM_RANK = "removed-player-from-rank";
    // Sent to someone who has been kicked from their guild
    public static final String KICKED_FROM_GUILD = "kicked-from-guild";
    // Sent to the person who kicked a player from their guild
    public static final String PLAYER_KICKED = "player-kicked";
    // Sent to someone who has created a guild rank
    public static final String RANK_CREATED = "rank-created";
    // Sent to someone who tries to create a rank which already exists
    public static final String RANK_ALREADY_EXISTS = "rank-already-exists";
    // Sent to someone who removes a rank from their guild
    public static final String RANK_REMOVED = "rank-removed";
    // Sent to someone who tries to remove a rank they can't remove
    public static final String CANNOT_REMOVE_RANK = "cannot-remove-rank";
    // Sent to somebody who specifies an invalid guild action
    public static final String INVALID_ACTION = "invalid-action";
    // Sent to somebody who specifies an invalid guild action permission value
    public static final String INVALID_VALUE = "invalid-value";
    // Sent to somebody who has set a value for a rank's permission
    public static final String VALUE_SET = "value-set";
    // Sent to somebody who tries to modify permissions for a rank they can't
    public static final String CANNOT_MODIFY_RANK = "cannot-modify-rank";

    /**
     * The {@link RolecraftCore} plugin object
     */
    private final RolecraftCore plugin;
    /**
     * A {@link Map} of configuration keys to configured messages
     */
    private final Map<String, String> messages;

    /**
     * Construct a new Messages object
     *
     * @param plugin The {@link RolecraftCore} plugin object to use the data
     *               directory from
     */
    public Messages(final RolecraftCore plugin) {
        this.plugin = plugin;
        messages = new HashMap<String, String>();
    }

    /**
     * Gets the configured message for the given key
     *
     * @param key The key of the message to get
     * @return The message configured for the given key
     */
    public String get(final String key, final MsgVar... vars) {
        return CommandHelper.applyVars(parseColours(messages.get(key)), vars);
    }

    /**
     * Loads all configurable messages from the messages.yml file in the
     * Rolecraft data folder, located at server/plugins/RolecraftCore
     * <p/>
     * For any messages not configured in messages.yml, the default message
     * (from the RolecraftCore jar) is used instead. Reflection is used to go
     * through all of the constants in this class and check whether they have
     * a value
     */
    public void load() {
        // Save the default messages to defaults.yml in the data folder
        plugin.saveResource("defaults.yml", false);

        final YamlFile yaml = new YamlFile(
                new File(plugin.getDataFolder(), "messages.yml"));
        for (final String key : yaml.getKeys(false)) {
            messages.put(key, yaml.getString(key));
        }

        // Create the YamlFile for the defaults
        final YamlFile defaults = new YamlFile(
                new File(plugin.getDataFolder(), "defaults.yml"));
        for (final Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            int mods = field.getModifiers();
            // If it's a constant, basically
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier
                    .isFinal(mods)) {
                try {
                    final String key = (String) field.get(this);
                    if (!messages.containsKey(key)) {
                        messages.put(key, defaults.getString(key));
                    }
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            field.setAccessible(false);
        }
    }

    private MsgVar[] colours = new MsgVar[] {
            MsgVar.create("$darkred", ChatColor.DARK_RED.toString()),
            MsgVar.create("$gray", ChatColor.GRAY.toString()),
            MsgVar.create("$white", ChatColor.WHITE.toString()),
            MsgVar.create("$red", ChatColor.RED.toString()),
            MsgVar.create("$green", ChatColor.GREEN.toString()),
            MsgVar.create("$darkgreen", ChatColor.DARK_GREEN.toString()),
            MsgVar.create("$purple", ChatColor.DARK_PURPLE.toString()),
            MsgVar.create("$lightpurple", ChatColor.LIGHT_PURPLE.toString()),
            MsgVar.create("$magic", ChatColor.MAGIC.toString()),
            // Support all colours which aren't here by allowing $col1 etc
            MsgVar.create("$col", String.valueOf(ChatColor.COLOR_CHAR))
    };

    private String parseColours(final String original) {
        return CommandHelper.applyVars(original, colours);
    }
}
