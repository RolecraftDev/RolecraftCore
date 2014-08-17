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
        sb.append(',');
        return sb.toString();
    }

    public static final PlayerSettings defaultSettings = new PlayerSettings();
}
