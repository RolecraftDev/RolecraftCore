package com.github.rolecraftdev.profession;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfessionRuleSet {
    private static final Map<String, ProfessionRuleSet> defaultRuleSets = new HashMap<String, ProfessionRuleSet>();

    static {
        final ProfessionRuleSet warriorDefault = new ProfessionRuleSet(
                "Warrior");
        // TODO: Set settings
        defaultRuleSets.put("Warrior", warriorDefault);
    }

    private final String professionName;

    public ProfessionRuleSet(final String professionName) {
        this.professionName = professionName;
    }

    public static void writeDefaults(final String profession, final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }

        final ProfessionRuleSet rules = defaultRuleSets.get(profession);
        if (rules != null) {
            rules.saveRules(file);
        }
    }

    public void loadRules(final File directory) {
        final File file = new File(directory, professionName + ".yml");
        if (!file.exists()) {
            writeDefaults(professionName, file);
        }

        // TODO: Load settings from file
    }

    public void saveRules(final File file) {
        // TODO
    }
}
