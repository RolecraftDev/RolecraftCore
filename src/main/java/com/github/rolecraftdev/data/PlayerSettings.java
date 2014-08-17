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

public class PlayerSettings {
    private boolean showMana;
    private boolean spellChatMessage;

    private PlayerSettings() {
        showMana = true;
        spellChatMessage = true;
    }

    public static PlayerSettings fromString(String string) {
        String[] strings = string.split(",");
        PlayerSettings temp = new PlayerSettings();
        temp.showMana = strings[0].equals("true");
        temp.spellChatMessage = strings[1].equals("true");

        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(showMana));
        sb.append(',');
        sb.append(String.valueOf(spellChatMessage));
        return sb.toString();
    }

    public static final PlayerSettings defaultSettings = new PlayerSettings();
}