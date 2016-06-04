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
package com.github.rolecraftdev.event;

import org.apache.commons.lang.Validate;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.experience.RCExperienceChangeEvent;
import com.github.rolecraftdev.event.experience.RCExperienceEvent;
import com.github.rolecraftdev.event.experience.RCLevelChangeEvent;
import com.github.rolecraftdev.event.guild.GuildCreateEvent;
import com.github.rolecraftdev.event.guild.GuildDisbandEvent;
import com.github.rolecraftdev.event.guild.GuildPlayerJoinEvent;
import com.github.rolecraftdev.event.guild.GuildPlayerKickedEvent;
import com.github.rolecraftdev.event.guild.GuildPlayerLeaveEvent;
import com.github.rolecraftdev.event.guild.GuildRankCreateEvent;
import com.github.rolecraftdev.event.guild.GuildRankModifyEvent;
import com.github.rolecraftdev.event.guild.GuildRankRemoveEvent;
import com.github.rolecraftdev.event.spell.SpellCastEvent;
import com.github.rolecraftdev.event.spell.SpellCastEvent.SpellCastType;
import com.github.rolecraftdev.experience.ExperienceHelper;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildAction;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.magic.Spell;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

/**
 * A utility factory for constructing and calling {@link Event}s easily.
 *
 * @since 0.0.5
 */
public class RolecraftEventFactory {
    /**
     * The associated {@link RolecraftCore} instance.
     */
    private static RolecraftCore plugin;

    /**
     * @since 0.0.5
     */
    private RolecraftEventFactory() {
    }

    /**
     * Calls a {@link GuildRankRemoveEvent} with the {@link RolecraftCore}
     * instance and the given parameters
     *
     * @param guild the {@link Guild} the rank belongs to
     * @param rank the {@link GuildRank} being removed
     * @return a {@link GuildRankRemoveEvent} constructed and called with the
     *         given parameters
     * @since 0.0.5
     */
    public static GuildRankRemoveEvent guildRankRemoved(final Guild guild,
            final GuildRank rank) {
        return callEvent(new GuildRankRemoveEvent(plugin, guild, rank));
    }

    /**
     * Calls a {@link GuildRankModifyEvent} with the {@link RolecraftCore}
     * instance and the given parameters
     *
     * @param guild the {@link Guild} the rank belongs to
     * @param rank the {@link GuildRank} being modified
     * @param perm the {@link GuildAction} permission which is being set
     * @param value the new value of the {@link GuildAction}
     * @return a {@link GuildRankModifyEvent} constructed and called with the
     *         given parameters
     * @since 0.0.5
     */
    public static GuildRankModifyEvent guildRankModified(final Guild guild,
            final GuildRank rank, final GuildAction perm, final boolean value) {
        return callEvent(new GuildRankModifyEvent(plugin, guild, rank, perm,
                value));
    }

    /**
     * Calls a {@link GuildRankCreateEvent} with the {@link RolecraftCore}
     * instance and the given parameters
     *
     * @param guild the {@link Guild} the rank belongs to
     * @param rank the {@link GuildRank} created
     * @return a {@link GuildRankCreateEvent} constructed and called with the
     *         given parameters
     * @since 0.0.5
     */
    public static GuildRankCreateEvent guildRankCreated(final Guild guild,
            final GuildRank rank) {
        return callEvent(new GuildRankCreateEvent(plugin, guild, rank));
    }

    /**
     * Calls a {@link GuildPlayerLeaveEvent} with the {@link RolecraftCore}
     * instance and the given parameters.
     *
     * @param guild the {@link Guild} the player left
     * @param player the {@link Player} leaving
     * @return a {@link GuildPlayerLeaveEvent} constructed and called with the
     *         given parameters
     * @since 0.0.5
     */
    public static GuildPlayerLeaveEvent guildPlayerLeave(final Guild guild,
            final Player player) {
        return callEvent(new GuildPlayerLeaveEvent(plugin, guild, player));
    }

    /**
     * Calls a {@link GuildPlayerKickedEvent} with the {@link RolecraftCore}
     * instance and the given parameters.
     *
     * @param guild the {@link Guild} the player was kicked from
     * @param player the {@link Player} being kicked
     * @return a {@link GuildPlayerKickedEvent} constructed and called with the
     *         given parameters
     * @since 0.0.5
     */
    public static GuildPlayerKickedEvent guildPlayerKicked(final Guild guild,
            final Player player) {
        return callEvent(new GuildPlayerKickedEvent(plugin, guild, player));
    }

    /**
     * Calls a {@link GuildPlayerJoinEvent} with the {@link RolecraftCore}
     * instance and the given parameters.
     *
     * @param guild the {@link Guild} the player is joining
     * @param player the {@link Player} joining the guild
     * @param rank the {@link GuildRank} the player is allocated to
     * @return a {@link GuildPlayerJoinEvent} constructed and called with the
     *         given parameters
     * @since 0.0.5
     */
    public static GuildPlayerJoinEvent guildPlayerJoined(final Guild guild,
            final Player player, final GuildRank rank) {
        return callEvent(new GuildPlayerJoinEvent(plugin, guild, player, rank));
    }

    /**
     * Calls a {@link GuildDisbandEvent} with the {@link RolecraftCore} instance
     * and the given {@link Guild}.
     *
     * @param guild the {@link Guild} being disbanded
     * @return a {@link GuildDisbandEvent} constructed and called with the given
     *         parameters
     * @since 0.0.5
     */
    public static GuildDisbandEvent guildDisbanded(final Guild guild) {
        return callEvent(new GuildDisbandEvent(plugin, guild));
    }

    /**
     * Calls a {@link GuildCreateEvent} with the {@link RolecraftCore} instance
     * and the given {@link Guild}.
     *
     * @param guild the {@link Guild} being created
     * @return a {@link GuildCreateEvent} constructed and called with the given
     *         parameters
     * @since 0.0.5
     */
    public static GuildCreateEvent guildCreated(final Guild guild) {
        return callEvent(new GuildCreateEvent(plugin, guild));
    }

    /**
     * Calls a {@link SpellCastEvent} with the {@link RolecraftCore} instance
     * and the given parameters.
     *
     * @param spell the {@link Spell} being cast
     * @param caster the {@link Entity} casting the {@link Spell}
     * @param manaCost the cost in mana of the {@link Spell}
     * @return a {@link SpellCastEvent} constructed and called with the given
     *         parameters
     * @since 0.0.5
     */
    public static SpellCastEvent spellCast(final Spell spell,
            final Entity caster, final float manaCost,
            final SpellCastType type) {
        return callEvent(new SpellCastEvent(plugin, spell, caster, manaCost,
                type));
    }

    /**
     * Creates and calls the appropriate event given the parameter values.
     *
     * @param plugin the associated {@link RolecraftCore} instance
     * @param player the affected player
     * @param amount the additional experience (may be negative)
     * @param reason the reason for this change
     * @return the appropriate {@link RCExperienceChangeEvent} after calling it
     * @since 0.0.5
     */
    public static RCExperienceChangeEvent callRCExpEvent(
            final RolecraftCore plugin, final Player player, final float amount,
            final RCExperienceEvent.ChangeReason reason) {
        final RCExperienceChangeEvent temp;
        final float experience = plugin.getDataManager()
                .getPlayerData(player.getUniqueId()).getExperience();

        if (ExperienceHelper.getLevel(experience) !=
                ExperienceHelper.getLevel(experience + amount)) {
            temp = new RCLevelChangeEvent(plugin, player, amount, reason);
        } else {
            temp = new RCExperienceChangeEvent(plugin, player, amount, reason);
        }

        Bukkit.getPluginManager().callEvent(temp);
        return temp;
    }

    /**
     * Calls the given {@link Event} and returns that {@link Event}, making it
     * easier to call an {@link Event} and then check the values set by any
     * {@link Listener} objects that may have made use of setters in the
     * {@link Event} object.
     *
     * @param event the {@link Event} to call and return
     * @param <T> the type which the {@link Event} returned is
     * @return the given {@link Event} after it has been called
     * @since 0.0.5
     */
    public static <T extends Event> T callEvent(final T event) {
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    /**
     * Set the associated {@link RolecraftCore} instance.
     *
     * @param plugin the new associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public static void setPlugin(@Nonnull final RolecraftCore plugin) {
        Validate.notNull(plugin);
        RolecraftEventFactory.plugin = plugin;
    }
}
