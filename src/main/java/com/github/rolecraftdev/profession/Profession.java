package com.github.rolecraftdev.profession;

public class Profession {
    public static final Profession WARRIOR = new Profession("Warrior",
            new ProfessionRuleSet("Warrior"));
    public static final Profession ARCHER = new Profession("Archer",
            new ProfessionRuleSet("Archer"));

    private final String name;
    private final ProfessionRuleSet rules;

    public Profession(final String name, final ProfessionRuleSet rules) {
        this.name = name;
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public ProfessionRuleSet getRuleSet() {
        return rules;
    }
}
