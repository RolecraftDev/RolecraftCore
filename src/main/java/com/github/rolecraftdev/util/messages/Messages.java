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
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.util.GeneralUtil;
import com.github.rolecraftdev.util.serial.PropertiesFile;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
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
     * Sent when a player attempts to perform a profession-specific command or
     * action without having a profession.
     *
     * @since 0.1.0
     */
    public static final String NO_PROFESSION = "no-profession";
    /**
     * Sent as a prefix to a list of usable spells which is displayed when
     * information about a profession is shown to a player.
     *
     * @since 0.1.0
     */
    public static final String USABLE_SPELLS = "usable-spells";
    /**
     * Sent as a prefix to a list of usable armour which is displayed when
     * information about a profession is shown to a player.
     *
     * @since 0.1.0
     */
    public static final String USABLE_ARMOR = "usable-armour";
    /**
     * Sent as a prefix to a list of usable enchants which is displayed when
     * information about a profession is shown to a player.
     *
     * @since 0.1.0
     */
    public static final String USABLE_ENCHANTMENTS = "usable-enchantments";
    /**
     * Sent as a prefix to a list of usable items which is displayed when
     * information about a profession is shown to a player.
     *
     * @since 0.1.0
     */
    public static final String USABLE_ITEMS = "usable-items";
    /**
     * Sent as a prefix to the correct usage of a command if a player uses a
     * command incorrectly.
     *
     * @since 0.1.0
     */
    public static final String INVALID_USAGE = "invalid-usage";
    /**
     * The message shown if a player attempts to view a list of guilds on the
     * server but there are no guilds to view.
     *
     * @since 0.1.0
     */
    public static final String NO_GUILDS = "no-guilds";
    /**
     * The message shown if a player attempts to view a list of professions on
     * the server but there are no professions to view.
     *
     * @since 0.1.0
     */
    public static final String NO_PROFESSIONS = "no-professions";
    /**
     * The message shown to a player who has successfully placed a Rolecraft
     * sign.
     *
     * @since 0.1.0
     */
    public static final String SIGN_PLACED = "sign-placed";
    /**
     * The message shown to a player who enters a non-integer value as the page
     * argument in a list command.
     *
     * @since 0.1.0
     */
    public static final String INVALID_PAGE_NUMBER = "invalid-page-number";
    /**
     * The message shown to a player who enters an integer which is too high or
     * too low as the page argument in a list command.
     *
     * @since 0.1.0
     */
    public static final String PAGE_NOT_EXISTS = "page-not-exists";
    /**
     * The message shown to a player who attempts to cast a spell without having
     * the required quantity of mana.
     *
     * @since 0.1.0
     */
    public static final String NOT_ENOUGH_MANA = "not-enough-mana";
    /**
     * The message shown to a player when their guild rank's permissions are not
     * sufficient to perform the action they are attempting.
     *
     * @since 0.1.0
     */
    public static final String GUILD_NO_PERMISSION = "guild-no-permission";
    /**
     * The message sent to a player who is not allowed to perform an action but
     * there is no specific message available.
     *
     * @since 0.1.0
     */
    public static final String NOT_ALLOWED = "not-allowed";
    /**
     * The message sent to a player who attempts to use a non-functional sign.
     * This may be because a sign has been created for a profession which does
     * not exist, amongst other reasons.
     *
     * @since 0.1.0
     */
    public static final String BROKEN_SIGN = "broken-sign";
    /**
     * The message sent to a player who attempts to join a chat channel which
     * does not exist.
     *
     * @since 0.1.0
     */
    public static final String CHANNEL_NOT_EXISTS = "channel-not-exists";
    /**
     * The message sent to a player who has just joined a new channel.
     *
     * @since 0.1.0
     */
    public static final String CHANNEL_JOINED = "channel-joined";
    /**
     * The message sent to a player who has just created a new channel.
     *
     * @since 0.1.0
     */
    public static final String CHANNEL_CREATED = "channel-created";
    /**
     * The message sent to a player who has just deleted a channel.
     *
     * @since 0.1.0
     */
    public static final String CHANNEL_DELETED = "channel-deleted";
    /**
     * The message sent to a player who has just selected a channel to speak in.
     *
     * @since 0.1.0
     */
    public static final String CHANNEL_SELECTED = "channel-selected";
    /**
     * The message sent to a player who has just left a chat channel.
     *
     * @since 0.1.0
     */
    public static final String CHANNEL_LEFT = "channel-left";
    /**
     * The message sent to a player who attempts to select an active channel
     * which they are not currently in.
     *
     * @since 0.1.0
     */
    public static final String NOT_IN_CHANNEL = "not-in-channel";
    /**
     * Sent when a player picks a second profession.
     *
     * @since 0.1.0
     */
    public static final String SECOND_PROFESSION_SELECTED = "second-profession-selected";
    /**
     * Sent when a player attempts to select a second profession despite already
     * having one.
     *
     * @since 0.1.0
     */
    public static final String SECOND_PROFESSION_ALREADY_SELECTED = "second-profession-already-selected";
    /**
     * Sent when a player abandons their profession.
     *
     * @since 0.1.0
     */
    public static final String PROFESSION_ABANDONED = "profession-abandoned";
    /**
     * Sent when a player abandons their secondary profession.
     *
     * @since 0.1.0
     */
    public static final String SECOND_PROFESSION_ABANDONED = "second-profession-abandoned";
    /**
     * Sent when a player attempts to perform an action which requires a second
     * profession despite not having one.
     *
     * @since 0.1.0
     */
    public static final String NO_SECOND_PROFESSION = "no-second-profession";

    /**
     * General variables such as colours and styles.
     *
     * @since 0.0.5
     */
    private static final MessageVariable[] CONSTANTS = {
            new MessageVariable("$darkred", ChatColor.DARK_RED),
            new MessageVariable("$gray", ChatColor.GRAY),
            new MessageVariable("$white", ChatColor.WHITE),
            new MessageVariable("$red", ChatColor.RED),
            new MessageVariable("$green", ChatColor.GREEN),
            new MessageVariable("$darkgreen", ChatColor.DARK_GREEN),
            new MessageVariable("$purple", ChatColor.DARK_PURPLE),
            new MessageVariable("$lightpurple", ChatColor.LIGHT_PURPLE),
            new MessageVariable("$black", ChatColor.BLACK),
            new MessageVariable("$blue", ChatColor.BLUE),
            new MessageVariable("$darkblue", ChatColor.DARK_BLUE),
            new MessageVariable("$magic", ChatColor.MAGIC),
            new MessageVariable("$gold", ChatColor.GOLD),
            new MessageVariable("$aqua", ChatColor.AQUA),
            new MessageVariable("$yellow", ChatColor.YELLOW),
            new MessageVariable("$darkaqua", ChatColor.DARK_AQUA),
            new MessageVariable("$darkgray", ChatColor.DARK_GRAY),
            new MessageVariable("$reset", ChatColor.RESET),
            // Support all colours which aren't here by allowing $col1 etc
            new MessageVariable("$col", ChatColor.COLOR_CHAR),
            new MessageVariable("$strike", ChatColor.STRIKETHROUGH),
            new MessageVariable("$underline", ChatColor.UNDERLINE),
            new MessageVariable("$italic", ChatColor.ITALIC),
            new MessageVariable("$bold", ChatColor.BOLD)
    };

    /**
     * Applies the values of the specified {@link MessageVariable}s in the given
     * message. This will never return {@code null} as if the input message
     * {@link String} is {@code null}, an empty {@link String} is returned.
     *
     * @param message the affected message
     * @param vars the {@link MessageVariable}s that should be applied
     * @return the given message after the {@link MessageVariable}s have been
     *         applied
     * @since 0.0.5
     */
    @Nonnull
    public static String applyVars(@Nullable String message,
            final MessageVariable... vars) {
        if (message == null) {
            return "";
        }
        for (final MessageVariable constant : CONSTANTS) {
            message = constant.apply(message);
        }
        if (vars == null || vars.length < 1) {
            return message;
        }
        for (final MessageVariable var : vars) {
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
     * {@link MessageVariable}s.
     *
     * Note: Though {@code null} is a potential return value for an invocation
     * of this method, it shouldn't be returned if this method is called using a
     * constants in {@link Messages} as the {@code key} argument.
     *
     * @param key the message of which the configured value should be retrieved
     * @param vars the {@link MessageVariable}s that should be applied
     * @return the edited message value
     * @since 0.0.5
     */
    @Nullable
    public String get(final String key, final MessageVariable... vars) {
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
        final String langName = "/messages/" + plugin.getDefaultLocale()
                + ".properties";
        // Get the file configured by the user
        final File configuredFile = new File(plugin.getDataFolder(),
                "messages.properties");

        // Avoid IOException in Utils#copyInputStreamToFile
        if (!configuredFile.isFile()) {
            configuredFile.delete();
        }
        if (!configuredFile.exists()) {
            try {
                // Creates the target file and writes the source to it
                // (overwrites!)
                GeneralUtil.copyInputStreamToFile(plugin.getClass()
                        .getResourceAsStream(langName), configuredFile);
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final NullPointerException e) {
                plugin.getLogger().warning(
                        "Messages locale " + plugin.getDefaultLocale()
                                + " is not available");
                return;
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
            final boolean prev = field.isAccessible();
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

            field.setAccessible(prev);
        }
    }
}
