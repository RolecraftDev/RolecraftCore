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

import net.minecraft.util.org.apache.commons.io.FileUtils;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.storage.PropertiesFile;
import com.github.rolecraftdev.guild.GuildRank;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * A helper class for managing configurable messages.
 *
 * @since 0.0.5
 */
public class Messages {
    /**
     * Sent to players who don't have permission to perform a command
     *
     * @since 0.0.5
     */
    public static final String NO_PERMISSION = "no-permission";
    /**
     * Sent to non-players who try to execute a player-only command
     *
     * @since 0.0.5
     */
    public static final String NOT_PLAYER = "not-player";
    /**
     * Sent to players with no guild trying to execute commands which need one
     *
     * @since 0.0.5
     */
    public static final String NO_GUILD = "no-guild";
    /**
     * Sent to players who aren't the guild leader when they need to be
     *
     * @since 0.0.5
     */
    public static final String NOT_GUILD_LEADER = "not-guild-leader";
    /**
     * Sent to players who specify an invalid guild rank
     *
     * @since 0.0.5
     */
    public static final String RANK_NOT_EXISTS = "rank-not-exists";
    /**
     * Sent to players who specify an invalid guild name
     *
     * @since 0.0.5
     */
    public static final String GUILD_NOT_EXISTS = "guild-not-exists";
    /**
     * Sent to players who try to join a guild if they're already in one
     *
     * @since 0.0.5
     */
    public static final String ALREADY_IN_GUILD = "already-in-guild";
    /**
     * Sent to players who can't afford what they're trying to do
     *
     * @since 0.0.5
     */
    public static final String CANNOT_AFFORD = "cannot-afford";
    /**
     * Sent to players who create a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_CREATED = "guild-created";
    /**
     * Sent to players who disband a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_DISBANDED = "guild-disbanded";
    /**
     * Sent to players when they set the home of their guild
     *
     * @since 0.0.5
     */
    public static final String SET_GUILD_HOME = "guild-home-set";
    /**
     * Sent to players who teleport to their guild home
     *
     * @since 0.0.5
     */
    public static final String GUILD_TP_HOME = "guild-tp-home";
    /**
     * Sent to players who try to join a guild they aren't invited to
     *
     * @since 0.0.5
     */
    public static final String GUILD_NOT_INVITED = "guild-not-invited";
    /**
     * Sent to someone who has rejected an invitation to a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_INVITE_REJECTED = "guild-invite-rejected";
    /**
     * Sent to a player who has just joined a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_JOINED_PLAYER = "guild-joined-player";
    /**
     * Sent to every member of the guild when a player joins
     *
     * @since 0.0.5
     */
    public static final String GUILD_JOINED_OTHERS = "guild-joined-others";
    /**
     * Sent if someone gives the name of someone who's not played on the server
     *
     * @since 0.0.5
     */
    public static final String PLAYER_NOT_EXISTS = "player-not-exists";
    /**
     * Sent if someone specifies a player who isn't online
     *
     * @since 0.0.5
     */
    public static final String PLAYER_NOT_ONLINE = "player-not-online";
    /**
     * Sent if someone tries to invite a player who already has an invitation
     *
     * @since 0.0.5
     */
    public static final String PLAYER_CONSIDERING_INVITE = "player-considering-invite";
    /**
     * Sent to a player who has just been invited to a guild
     *
     * @since 0.0.5
     */
    public static final String PLAYER_INVITED_RECEIVER = "player-invited-receiver";
    /**
     * Sent to the sender of a guild invitation when it is sent
     *
     * @since 0.0.5
     */
    public static final String PLAYER_INVITED_SENDER = "player-invited-sender";
    /**
     * Sent if someone specifies someone not in their guild for a guild-related
     * action
     *
     * @since 0.0.5
     */
    public static final String PLAYER_NOT_IN_GUILD = "player-not-in-guild";
    /**
     * Sent if someone specifies an invalid action to perform on a guild member
     *
     * @since 0.0.5
     */
    public static final String INVALID_MEMBER_ACTION = "invalid-member-action";
    /**
     * Sent to a player who has been added to a rank within a guild
     *
     * @since 0.0.5
     */
    public static final String PLAYER_ADDED_TO_RANK = "player-added-to-rank";
    /**
     * Sent to someone who has added a player to a guild rank
     *
     * @since 0.0.5
     */
    public static final String ADDED_PLAYER_TO_RANK = "added-player-to-rank";
    /**
     * Sent to a player who has been removed from a rank within a guild
     *
     * @since 0.0.5
     */
    public static final String PLAYER_REMOVED_FROM_RANK = "player-removed-from-rank";
    /**
     * Sent to someone who has removed a player from a guild rank
     *
     * @since 0.0.5
     */
    public static final String REMOVED_PLAYER_FROM_RANK = "removed-player-from-rank";
    /**
     * Sent to someone who has been kicked from their guild
     *
     * @since 0.0.5
     */
    public static final String KICKED_FROM_GUILD = "kicked-from-guild";
    /**
     * Sent to the person who kicked a player from their guild
     *
     * @since 0.0.5
     */
    public static final String PLAYER_KICKED = "player-kicked";
    /**
     * Sent to someone who has created a guild rank
     *
     * @since 0.0.5
     */
    public static final String RANK_CREATED = "rank-created";
    /**
     * Sent to someone who tries to create a rank which already exists
     *
     * @since 0.0.5
     */
    public static final String RANK_ALREADY_EXISTS = "rank-already-exists";
    /**
     * Sent to someone who removes a rank from their guild
     *
     * @since 0.0.5
     */
    public static final String RANK_REMOVED = "rank-removed";
    /**
     * Sent to someone who tries to remove a rank they can't remove
     *
     * @since 0.0.5
     */
    public static final String CANNOT_REMOVE_RANK = "cannot-remove-rank";
    /**
     * Sent to somebody who specifies an invalid guild action
     *
     * @since 0.0.5
     */
    public static final String INVALID_ACTION = "invalid-action";
    /**
     * Sent to somebody who specifies an invalid guild action permission value
     *
     * @since 0.0.5
     */
    public static final String INVALID_VALUE = "invalid-value";
    /**
     * Sent to somebody who has set a value for a rank's permission
     *
     * @since 0.0.5
     */
    public static final String VALUE_SET = "value-set";
    /**
     * Sent to somebody who tries to modify permissions for a rank they can't
     *
     * @since 0.0.5
     */
    public static final String CANNOT_MODIFY_RANK = "cannot-modify-rank";
    /**
     * Sent if someone tries /rcconfirm when they have nothing to confirm
     *
     * @since 0.0.5
     */
    public static final String NOTHING_TO_CONFIRM = "nothing-to-confirm";
    /**
     * Sent if someone specifies a profession which doesn't exist
     *
     * @since 0.0.5
     */
    public static final String PROFESSION_NOT_EXISTS = "profession-not-exists";
    /**
     * Sent to somebody who tries to select a profession they aren't allowed
     *
     * @since 0.0.5
     */
    public static final String PROFESSION_NO_PERMS = "profession-no-perms";
    /**
     * Sent when a player picks a profession
     *
     * @since 0.0.5
     */
    public static final String PROFESSION_SELECTED = "profession-selected";
    /**
     * Sent if a player tries to select a profession when they already have one
     *
     * @since 0.0.5
     */
    public static final String PROFESSION_ALREADY_SELECTED = "profession-already-selected";
    /**
     * Sent to players who try to cast a spell they aren't allowed to cast
     *
     * @since 0.0.5
     */
    public static final String CANNOT_CAST_SPELL = "cannot-cast-spell";
    /**
     * Sent when a player casts a spell
     *
     * @since 0.0.5
     */
    public static final String SPELL_CAST = "spell-cast";
    /**
     * Sent when a player cannot wear armor due to their profession
     *
     * @since 0.0.5
     */
    public static final String PROFESSION_DENY_ARMOR = "profession-deny-armor";
    /**
     * Sent when a player cannot wear enchanted / use enchanted things due to
     * their profession's restrictions
     *
     * @since 0.0.5
     */
    public static final String PROFESSION_DENY_ENCHANTMENT = "profession-deny-enchantment";
    /**
     * Sent when a player cannot equip weapons due to their profession
     *
     * @since 0.0.5
     */
    public static final String PROFESSION_DENY_ITEM = "profession-deny-item";
    /**
     * Send when a player aims below the ground when casting an arrow rain spell
     *
     * @since 0.0.5
     */
    public static final String ARROW_BELOW_GROUND_FAILURE = "arrow-below-ground-failure";
    /**
     * A template for general information about a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_INFO = "guild-info";
    /**
     * A template for information about the members of a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_MEMBERS = "guild-info-members";
    /**
     * A template for information about a guild's influence
     *
     * @since 0.0.5
     */
    public static final String GUILD_INFLUENCE = "guild-info-influence";
    /**
     * A template for information about the guild leader
     *
     * @since 0.0.5
     */
    public static final String GUIlD_LEADER = "guild-info-leader";
    /**
     * A template for information about a guild rank
     *
     * @since 0.0.5
     */
    public static final String GUILD_RANK = "guild-info-rank";
    /**
     * Sent if somebody tries to create a guild with a name which is already
     * taken
     *
     * @since 0.0.5
     */
    public static final String GUILD_ALREADY_EXISTS = "guild-already-exists";
    /**
     * Sent if someone tries to broadcast a guild message when they don't have
     * permission to do so
     *
     * @since 0.0.5
     */
    public static final String BROADCAST_NO_PERMS = "broadcast-no-perms";
    /**
     * Sent when a player leaves a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_LEAVE = "guild-leave";
    /**
     * Sent to players who try to join a guild without an invitation
     *
     * @since 0.0.5
     */
    public static final String NO_GUILD_INVITE = "no-guild-invite";
    /**
     * The name of the leader {@link GuildRank} in a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_LEADER_RANK = "guild-leader-rank";
    /**
     * The name of the default {@link GuildRank} in a guild
     *
     * @since 0.0.5
     */
    public static final String GUILD_DEFAULT_RANK = "guild-default-rank";

    /**
     * General variables such as colours and styles.
     */
    private static final MsgVar[] CONSTANTS = {
            MsgVar.create("$darkred", ChatColor.DARK_RED.toString()),
            MsgVar.create("$gray", ChatColor.GRAY.toString()),
            MsgVar.create("$white", ChatColor.WHITE.toString()),
            MsgVar.create("$red", ChatColor.RED.toString()),
            MsgVar.create("$green", ChatColor.GREEN.toString()),
            MsgVar.create("$darkgreen", ChatColor.DARK_GREEN.toString()),
            MsgVar.create("$purple", ChatColor.DARK_PURPLE.toString()),
            MsgVar.create("$lightpurple", ChatColor.LIGHT_PURPLE.toString()),
            MsgVar.create("$black", ChatColor.BLACK.toString()),
            MsgVar.create("$blue", ChatColor.BLUE.toString()),
            MsgVar.create("$darkblue", ChatColor.DARK_BLUE.toString()),
            MsgVar.create("$magic", ChatColor.MAGIC.toString()),
            MsgVar.create("$gold", ChatColor.GOLD.toString()),
            MsgVar.create("$aqua", ChatColor.AQUA.toString()),
            MsgVar.create("$yellow", ChatColor.YELLOW.toString()),
            MsgVar.create("$darkaqua", ChatColor.DARK_AQUA.toString()),
            MsgVar.create("$darkgray", ChatColor.DARK_GRAY.toString()),
            MsgVar.create("$reset", ChatColor.RESET.toString()),
            // Support all colours which aren't here by allowing $col1 etc
            MsgVar.create("$col", String.valueOf(ChatColor.COLOR_CHAR)),
            MsgVar.create("$strike", ChatColor.STRIKETHROUGH.toString()),
            MsgVar.create("$underline", ChatColor.UNDERLINE.toString()),
            MsgVar.create("$italic", ChatColor.ITALIC.toString()),
            MsgVar.create("$bold", ChatColor.BOLD.toString())
    };

    /**
     * Applies the values of the specified {@link MsgVar}s in the given message.
     *
     * @param message the affected message
     * @param vars the {@link MsgVar}s that should be applied
     * @return the given message after the {@link MsgVar}s have been applied
     * @since 0.0.5
     */
    public static String applyVars(String message, final MsgVar... vars) {
        if (message == null || vars == null || vars.length < 1) {
            return message;
        }
        for (final MsgVar constant : CONSTANTS) {
            message = constant.apply(message);
        }
        for (final MsgVar var : vars) {
            message = var.apply(message);
        }
        return message;
    }

    /**
     * The associated {@link RolecraftCore} instance.
     */
    private final RolecraftCore plugin;
    /**
     * The {@link Map} for messages and their configured values.
     */
    private final Map<String, String> messages;

    /**
     * Constructor.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public Messages(final RolecraftCore plugin) {
        this.plugin = plugin;
        messages = new HashMap<String, String>();
    }

    /**
     * Get the value of the given message after applying the specified
     * {@link MsgVar}s.
     *
     * @param key the message of which the configured value should be retrieved
     * @param vars the {@link MsgVar}s that should be applied
     * @return the edited message value
     * @since 0.0.5
     */
    @Nullable
    public String get(final String key, final MsgVar... vars) {
        return applyVars(messages.get(key), vars);
    }

    /**
     * Load all messages and their values from the appropriate language file
     * or/and the configured <em>messages.properties</em> in the plugin's data
     * folder. If a message is not configured, the default value from the
     * language file is used.
     *
     * @since 0.0.5
     */
    public void load() {
        final String langName = "/messages/en-US.properties";
        // Get the file configured by the user
        final File configuredFile = new File(plugin.getDataFolder(),
                "messages.properties");

        // Avoid IOException in FileUtils#copyInputStreamToFile
        if (!configuredFile.isFile()) {
            configuredFile.delete();
        }
        if (!configuredFile.exists()) {
            try {
                // Creates the target file and writes the source to it
                // (overwrites!)
                FileUtils.copyInputStreamToFile(plugin.getClass()
                        .getResourceAsStream(langName), configuredFile);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        for (final Entry<Object, Object> line : new PropertiesFile(
                configuredFile).entrySet()) {
            messages.put(line.getKey().toString(), line.getValue().toString());
        }

        final Properties langProps = new Properties();

        try {
            langProps.load(plugin.getClass().getResourceAsStream(langName));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        for (final Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);

            final int mods = field.getModifiers();

            // If it's a constant, basically
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)
                    && Modifier.isFinal(mods)) {
                try {
                    final String key = (String) field.get(this);

                    if (!messages.containsKey(key)) {
                        messages.put(key, langProps.getProperty(key));
                    }
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            field.setAccessible(false);
        }
    }
}
