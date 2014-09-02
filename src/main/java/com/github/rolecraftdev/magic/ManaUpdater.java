package com.github.rolecraftdev.magic;

import com.github.rolecraftdev.RolecraftCore;
import com.github.rolecraftdev.data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

/**
 * Regenerates the mana on all available {@link PlayerData} and updates the
 * mana-showing scoreboards of all of them.
 *
 * @since 0.0.5
 */
public class ManaUpdater {
    /**
     * The name of the mana-objective and -score.
     */
    private static final String MANA = "Mana";

    private final RolecraftCore plugin;
    private final Map<UUID, Objective> manaDisplays;

    /**
     * Create an updater used for magic mana. This will automatically cause
     * magic mana to be regenerated in a cycle.
     *
     * @param spellManager the associated {@link RolecraftCore} instance
     * @since 0.0.5
     */
    public ManaUpdater(final RolecraftCore plugin) {
        this.plugin = plugin;
        manaDisplays = new HashMap<UUID, Objective>();

        new RegenerationTask().runTaskTimer(plugin, 20, 40);
    }

    /**
     * Creates a new mana-scoreboard and -objective, which are together referred
     * to as a mana-display, for the specified player.
     *
     * @param player the player for which a new mana-display should be created
     * @since 0.0.5
     */
    public void createDisplayFor(final Player player) {
        // Name is display name if that remains undefined
        final Objective display = Bukkit.getScoreboardManager()
                .getNewScoreboard().registerNewObjective(MANA, "dummy");

        display.setDisplaySlot(DisplaySlot.SIDEBAR);
        manaDisplays.put(player.getUniqueId(), display);
        player.setScoreboard(display.getScoreboard());
    }

    /**
     * Remove the mana-display of a player. Note that this will solely remove
     * the used objects from the backing map and thus, will <strong>not</strong>
     * alter scoreboard visibility.
     *
     * @param player
     * @since 0.0.5
     */
    public void disposeDisplayOf(final Player player) {
        manaDisplays.remove(player.getUniqueId());
    }

    /**
     * The task used to regenerate magic mana and update all available
     * mana-displays.
     */
    private class RegenerationTask extends BukkitRunnable {
        @Override
        public void run() {
            for (final PlayerData data : plugin.getDataManager()
                    .getPlayerDatum()) {
                data.addMana(data.getManaRegenRate());

                if (manaDisplays.containsKey(data.getPlayerId())) {
                    manaDisplays.get(data.getPlayerId()).getScore(MANA)
                            .setScore((int) data.getMana());
                }
            }
        }
    }
}
