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
package com.github.rolecraftdev.data;

/**
 * Holds persistent settings of a player.
 *
 * @since 0.0.5
 */
public final class PlayerSettings {
    /**
     * The default settings.
     *
     * @since 0.0.5
     */
    public static final PlayerSettings DEFAULT_SETTINGS = new PlayerSettings();

    /**
     * Whether the player is spying on guild chat - volatile to ensure thread
     * safety in Bukkit's AsyncPlayerChatEvent
     */
    private volatile boolean guildSpy;

    /**
     * Whether to show mana for the player on a scoreboard
     */
    private boolean showMana;
    /**
     * Whether to send a chat message to the player when they cast a spell
     */
    private boolean spellChatMessage;

    /**
     * Constructor.
     *
     * @since 0.0.5
     */
    private PlayerSettings() {
        showMana = true;
        spellChatMessage = true;
        guildSpy = true;
    }

    /**
     * Set the <em>show mana</em> setting.
     *
     * @param showMana the new <em>show mana</em> value
     * @since 0.0.5
     */
    public void setShowMana(final boolean showMana) {
        this.showMana = showMana;
    }

    /**
     * Set the <em>spell chat message</em> setting.
     *
     * @param spellChatMessage the new <em>spell chat message</em> value
     * @since 0.0.5
     */
    public void setSpellChatMessage(final boolean spellChatMessage) {
        this.spellChatMessage = spellChatMessage;
    }

    /**
     * Get the value of the <em>show mana</em> setting.
     *
     * @return the value of <em>show mana</em>
     * @since 0.0.5
     */
    public boolean isShowMana() {
        return showMana;
    }

    /**
     * Get the value of the <em>spell chat message</em> setting.
     *
     * @return the value of <em>spell chat message</em>
     * @since 0.0.5
     */
    public boolean isSpellChatMessage() {
        return spellChatMessage;
    }

    /**
     * Get the value of the <em>guild spy</em> setting. This setting is volatile
     * and thus thread-safe to some extend.
     *
     * @return the value of <em>guild spy</em>
     * @since 0.0.5
     */
    public boolean isGuildChatSpy() {
        return guildSpy;
    }

    /**
     * Set the <em>guild spy</em> setting. This setting is volatile and thus
     * thread-safe to some extend.
     *
     * @param guildSpy the new <em>guild spy</em> value
     * @since 0.0.5
     */
    public void setGuildSpy(final boolean guildSpy) {
        this.guildSpy = guildSpy;
    }

    /**
     * @since 0.0.5
     */
    @Override
    public String toString() {
        return String.valueOf(showMana) + ',' + String.valueOf(spellChatMessage)
                + ',' + String.valueOf(guildSpy);
    }

    /**
     * Obtain a new {@link PlayerSettings} object from a string in which every
     * value is separated by a comma (only). The values should equal "true" if
     * the linked setting should be {@code true} and may be anything else to
     * represent {@code false}. Below is a list of the setting each element
     * represents:
     * <ol>
     * <li>show mana</li>
     * <li>spell chat message</li>
     * <li>guild spy</li>
     * </ol>
     * This works as the reverse of {@link #toString()}.
     *
     * @param string the string that should be parsed to construct a new
     *        {@link PlayerSettings} object
     * @return the constructed {@link PlayerSettings}
     * @since 0.0.5
     */
    public static PlayerSettings fromString(final String string) {
        final String[] strings = string.split(",");
        final PlayerSettings temp = new PlayerSettings();
        temp.showMana = strings[0].equals("true");
        temp.spellChatMessage = strings[1].equals("true");
        temp.guildSpy = strings[2].equals("true");
        return temp;
    }
}
