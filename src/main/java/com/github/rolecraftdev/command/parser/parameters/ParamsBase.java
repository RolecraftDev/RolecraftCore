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
package com.github.rolecraftdev.command.parser.parameters;

import com.github.rolecraftdev.command.parser.Arguments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A base to create Params objects from - used so that we don't parse the usage
 * string every time a command is executed.
 *
 * @since 0.0.5
 */
public class ParamsBase {
    /**
     * The character which implies the beginning of a required parameter
     */
    public static final char REQUIRED_OPEN_DENOTATION = '<';
    /**
     * The character which implies the closing of a required parameter
     */
    public static final char REQUIRED_CLOSE_DENOTATION = '>';
    /**
     * The character which implies the opening of an optional parameter
     */
    public static final char OPTIONAL_OPEN_DENOTATION = '[';
    /**
     * The character which implies the closing of an optional parameter
     */
    public static final char OPTIONAL_CLOSE_DENOTATION = ']';
    /**
     * The character which separates arguments
     */
    public static final char ARGUMENT_SEPARATOR = ' ';

    /**
     * A {@link List} of all of the parameters.
     */
    private final List<ParamInfo> params;
    /**
     * The number of arguments before the first parameter.
     */
    private final int argsBeforeParams;
    /**
     * The amount of arguments required for a valid {@link Params} object for
     * this ParamsBase, used for argument validation.
     */
    private final int amtRequired;
    /**
     * Flag information for validation.
     */
    private final List<FlagInfo> flags;

    /**
     * Creates a new ParamsBase for the given {@link List} of parameters and the
     * given amount of arguments before the first parameter.
     *
     * @param params the parameters for this ParamsBase
     * @param argsBeforeParams the amount of arguments before the first param
     * @param amtRequired the amount of required parameters
     * @since 0.0.5
     */
    private ParamsBase(List<ParamInfo> params, int argsBeforeParams,
            int amtRequired, List<FlagInfo> flags) {
        this.params = params;
        this.argsBeforeParams = argsBeforeParams;
        this.amtRequired = amtRequired;
        this.flags = flags;
    }

    /**
     * Gets the amount of parameters.
     *
     * @return the amount of parameters in this ParamsBase
     * @since 0.0.5
     */
    public int length() {
        return params.size();
    }

    /**
     * Gets the amount of arguments before the first parameter.
     *
     * @return the amount of arguments before the first parameter
     * @since 0.0.5
     */
    public int getArgsBeforeParams() {
        return argsBeforeParams;
    }

    /**
     * Gets the amount of required parameters.
     *
     * @return the amount of required parameters
     * @since 0.0.5
     */
    public int getAmountRequired() {
        return amtRequired;
    }

    /**
     * Gets the amount of non-required (optional) parameters.
     *
     * @return the amount of optional parameters
     * @since 0.0.5
     */
    public int getAmountOptional() {
        return length() - getAmountRequired();
    }

    /**
     * Gets the amount of flag-based parameters which can be inputted.
     *
     * @return the quantity of flag-based parameters
     * @since 0.0.5
     */
    public int getAmountFlags() {
        return flags.size();
    }

    /**
     * Creates a set of {@link Params} for this base using the given
     * {@link Arguments}.
     *
     * @param args the arguments to create the parameters from
     * @return {@link Params} for the given arguments
     * @since 0.0.5
     */
    public Params createParams(Arguments args) {
        Map<String, Parameter> paramsMap = new HashMap<String, Parameter>();
        int curArgument = argsBeforeParams;
        int curParam = 0;
        int curFlag = 0;
        boolean invalid = false;

        while (curArgument < args.length(false) && curParam < params.size()) {
            if (curFlag < flags.size()) {
                if (!args.hasValueFlag(flags.get(curFlag++).getName())) {
                    invalid = true;
                }
            }

            String val = args.getRaw(curArgument, false);
            ParamInfo info = params.get(curParam);

            paramsMap.put(info.getName(), new Parameter(val, info));
            curArgument++;
            curParam++;
        }

        while (curFlag < flags.size()) {
            if (!args.hasValueFlag(flags.get(curFlag++).getName())) {
                invalid = true;
            }
        }

        Params params = new Params(this, paramsMap);
        if (invalid || amtRequired > paramsMap.size()) {
            params.invalidate();
        }

        return params;
    }

    /**
     * Builds a new ParamsBase by parsing the given usage string for a command.
     *
     * @param usageString the command usage string to parse
     * @return a new ParamsBase created from parsing the given usage string
     * @since 0.0.5
     */
    public static ParamsBase fromUsageString(String usageString) {
        List<ParamInfo> res = new ArrayList<ParamInfo>();
        boolean required = false;
        boolean optional = false;
        StringBuilder builder = null;
        boolean reachedFirst = false;
        int before = 0;
        boolean passedCommand = false;
        int amtRequired = 0;
        List<FlagInfo> flags = new ArrayList<FlagInfo>();

        final char[] characters = usageString.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            final char ch = characters[i];

            if (!reachedFirst && ch == ARGUMENT_SEPARATOR) {
                if (passedCommand) {
                    before++;
                }
                passedCommand = true;
                continue;
            }

            if (required && ch == REQUIRED_CLOSE_DENOTATION) {
                required = false;
                res.add(new ParamInfo(builder.toString(), false));
                amtRequired++;
                builder = null;
                continue;
            } else if (optional && ch == OPTIONAL_CLOSE_DENOTATION) {
                optional = false;
                res.add(new ParamInfo(builder.toString(), true));
                builder = null;
                continue;
            }

            if (required || optional) {
                final char next = characters[i + 1];
                if (ch == '-' && next != REQUIRED_CLOSE_DENOTATION
                        && next != OPTIONAL_CLOSE_DENOTATION
                        && characters[i + 2] == ' ') {
                    StringBuilder desc = new StringBuilder();
                    int breakPoint = Integer.MAX_VALUE;
                    boolean isOptional = false;
                    for (int j = 3; j < breakPoint; j++) {
                        char toAppend = characters[i + j];
                        if (toAppend == REQUIRED_CLOSE_DENOTATION
                                || toAppend == OPTIONAL_CLOSE_DENOTATION) {
                            if (toAppend == OPTIONAL_CLOSE_DENOTATION) {
                                isOptional = true;
                            }
                            breakPoint = j;
                            continue;
                        }

                        desc.append(toAppend);
                    }
                    flags.add(
                            new FlagInfo(String.valueOf(next), desc.toString(),
                                    isOptional));

                    i += 2;
                    required = false;
                    optional = false;
                    builder = null;
                    continue;
                }

                builder.append(ch);
            } else {
                if (ch == REQUIRED_OPEN_DENOTATION) {
                    reachedFirst = true;
                    required = true;
                    builder = new StringBuilder();
                } else if (ch == OPTIONAL_OPEN_DENOTATION) {
                    reachedFirst = true;
                    optional = true;
                    builder = new StringBuilder();
                }
            }
        }

        return new ParamsBase(res, before, amtRequired, flags);
    }

    /**
     * Basic flag information.
     */
    public static class FlagInfo {
        /**
         * Name of the flag
         */
        private final String name;
        /**
         * One-word description.
         */
        private final String desc;
        /**
         * Whether or not the flag is optional.
         */
        private final boolean optional;

        public FlagInfo(String name, String desc, boolean optional) {
            this.name = name;
            this.desc = desc;
            this.optional = optional;
        }

        /**
         * @since 0.0.5
         */
        public String getName() {
            return name;
        }

        /**
         * @since 0.0.5
         */
        public String getDesc() {
            return desc;
        }

        /**
         * @since 0.0.5
         */
        public boolean isOptional() {
            return optional;
        }
    }

    /**
     * Represents information about a parameter, which can be required or optional.
     *
     * @since 0.0.5
     */
    public static class ParamInfo {
        /**
         * The name of this parameter.
         */
        private final String name;
        /**
         * Whether this parameter is optional.
         */
        private final boolean optional;

        /**
         * Constructs a new ParamInfo with the given name.
         *
         * @param name the name of this Parameter
         * @param optional whether this parameter is optional
         * @since 0.0.5
         */
        public ParamInfo(String name, boolean optional) {
            this.name = name;
            this.optional = optional;
        }

        /**
         * Gets the name of this parameter.
         *
         * @return the name of this parameter
         * @since 0.0.5
         */
        public String getName() {
            return name;
        }

        /**
         * Returns whether this parameter is optional.
         *
         * @return {@code true} if the parameter is optional, {@code false} if it is
         *          required
         * @since 0.0.5
         */
        public boolean isOptional() {
            return optional;
        }
    }
}
