package com.github.rolecraftdev.event;

import org.apache.commons.lang.Validate;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.event.guild.GuildCreateEvent;
import com.github.rolecraftdev.event.guild.GuildDisbandEvent;
import com.github.rolecraftdev.event.guild.GuildPlayerJoinEvent;
import com.github.rolecraftdev.event.spell.SpellCastEvent;
import com.github.rolecraftdev.guild.Guild;
import com.github.rolecraftdev.guild.GuildRank;
import com.github.rolecraftdev.magic.Spell;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * A utility factory for constructing and calling Bukkit {@link Event}s easily
 *
 * @since 0.0.5
 */
public class RolecraftEventFactory {
    /**
     * Calls a {@link GuildPlayerJoinEvent} with the {@link RolecraftCore}
     * object and the given parameters
     *
     * @param guild the {@link Guild} the player is joining
     * @param player the {@link Player} joining the guild
     * @param rank the {@link GuildRank} the player is allocated to
     * @return a {@link GuildPlayerJoinEvent} constructed and called with the
     *         given parameters
     */
    public static GuildPlayerJoinEvent guildPlayerJoined(Guild guild,
            Player player, GuildRank rank) {
        return callEvent(new GuildPlayerJoinEvent(plugin, guild, player, rank));
    }

    /**
     * Calls a {@link GuildDisbandEvent} with the {@link RolecraftCore} object
     * and the given {@link Guild}
     *
     * @param guild the {@link Guild} being disbanded
     * @return a {@link GuildDisbandEvent} constructed and called with the given
     *         parameters
     */
    public static GuildDisbandEvent guildDisbanded(Guild guild) {
        return callEvent(new GuildDisbandEvent(plugin, guild));
    }

    /**
     * Calls a {@link GuildCreateEvent} with the {@link RolecraftCore} object
     * and the given {@link Guild}
     *
     * @param guild the {@link Guild} being created
     * @return a {@link GuildCreateEvent} constructed and called with the given
     *         parameters
     */
    public static GuildCreateEvent guildCreated(Guild guild) {
        return callEvent(new GuildCreateEvent(plugin, guild));
    }

    /**
     * Calls a {@link SpellCastEvent} with the {@link RolecraftCore} object and
     * the given parameters
     *
     * @param spell the {@link Spell} being cast
     * @param caster the {@link Entity} casting the {@link Spell}
     * @param manaCost the cost in mana of the spell
     * @return a {@link SpellCastEvent} constructed and called with the given
     *         parameters
     */
    public static SpellCastEvent spellCast(Spell spell, Entity caster,
            float manaCost) {
        return callEvent(new SpellCastEvent(plugin, spell, caster, manaCost));
    }

    /**
     * Calls the given event and returns that event, making it easier to call an
     * event and then check the values set by any {@link
     * org.bukkit.event.Listener} objects who may have made use of setters in
     * the {@link Event} object
     *
     * @param event the {@link Event} to call and return
     * @param <T> the type which the {@link Event} returned is
     * @return the given {@link Event} after it has been called
     */
    public static <T extends Event> T callEvent(T event) {
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }

    // Reference to the plugin object - set in onEnable()
    private static RolecraftCore plugin;

    public static void setPlugin(RolecraftCore plugin) {
        Validate.notNull(plugin);
        RolecraftEventFactory.plugin = plugin;
    }

    /**
     * Should never be called.
     */
    private RolecraftEventFactory() {
        throw new UnsupportedOperationException();
    }
}
