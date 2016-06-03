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
package com.github.rolecraftdev.command.parser;

import com.github.rolecraftdev.command.parser.parameters.Parameter;
import com.github.rolecraftdev.command.parser.parameters.Params;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple and easy to use method of parsing arguments into different primitive
 * types and parsing flags.
 *
 * @since 0.0.5
 */
public class Arguments {
    /**
     * A List of all of the arguments in ChatSection form.
     */
    private final List<ChatSection> all;
    /**
     * A List of arguments, not including flag arguments, in ChatSection form.
     */
    private final List<ChatSection> arguments;
    /**
     * A List of all flags prefixed with -
     */
    private final Set<Flag> flags;
    /**
     * A List of all flags prefixed with --
     */
    private final Set<Flag> doubleFlags;
    /**
     * The raw String[] of arguments for this Arguments object
     */
    private final String[] raw;

    /**
     * The {@link Params} object for this Arguments object. This contains a
     * {@link Map} of parameter names to {@link Parameter} values for each
     * registered parameter.
     */
    private Params parameters;

    /**
     * Creates a new Arguments object and immediately parses the given String[]
     * of arguments into {@link ChatSection}s and {@link Flag}s.
     *
     * @param parse the raw arguments to parse in the form of {@link String}s
     * @since 0.0.5
     */
    public Arguments(final String... parse) {
        this.raw = parse;
        this.all = new ArrayList<ChatSection>();
        this.arguments = new ArrayList<ChatSection>();
        this.flags = new HashSet<Flag>();
        this.doubleFlags = new HashSet<Flag>();

        for (int i = 0; i < raw.length; i++) {
            final String arg = raw[i];
            all.add(new ChatSection(arg));

            switch (arg.charAt(0)) {
                case '-':
                    if (arg.length() < 2) {
                        arguments.add(new ChatSection(arg));
                        continue;
                    }
                    if (arg.charAt(1) == '-') {
                        if (arg.length() < 3) {
                            arguments.add(new ChatSection(arg));
                            continue;
                        }
                        // flag with double -- (no value)
                        doubleFlags.add(new Flag(arg.substring(2, arg.length()),
                                null));
                    } else {
                        if (raw.length - 1 == i) {
                            arguments.add(new ChatSection(arg));
                            continue;
                        }
                        // flag with single - (plus value)
                        flags.add(new Flag(arg.substring(1, arg.length()),
                                raw[++i]));
                    }
                    break;
                default:
                    // normal argument
                    arguments.add(new ChatSection(raw[i]));
                    break;
            }
        }
    }

    /**
     * Gets the {@link ChatSection} for the argument at the given index.
     *
     * @param index the index to get the argument from
     * @return a {@link ChatSection} object for the argument at the given index
     * @since 0.0.5
     */
    public ChatSection get(final int index) {
        return get(index, true);
    }

    /**
     * Gets the {@link ChatSection} for the argument at the given index.
     *
     * @param index the index to get the argument from
     * @param includeFlagArgs whether to include flag args in the index
     * @return a {@link ChatSection} object for the argument at the given index
     * @since 0.0.5
     */
    public ChatSection get(final int index, final boolean includeFlagArgs) {
        if (includeFlagArgs) {
            return all.get(index);
        } else {
            return arguments.get(index);
        }
    }

    /**
     * Gets the raw {@link String} for the argument at the given index.
     *
     * @param index the index to get the argument from
     * @return a raw {@link String} for the argument at the given index
     * @since 0.0.5
     */
    public String getRaw(final int index) {
        return getRaw(index, true);
    }

    /**
     * Gets the raw {@link String} for the argument at the given index.
     *
     * @param index the index to get the argument from
     * @param includeFlagArgs whether to include flag args in the index
     * @return a raw {@link String} for the argument at the given index
     * @since 0.0.5
     */
    public String getRaw(final int index, final boolean includeFlagArgs) {
        return get(index, includeFlagArgs).rawString();
    }

    /**
     * Gets the {@link Params} for this set of Arguments.
     *
     * @return this Arguments object's {@link Params} object
     * @since 0.0.5
     */
    public Params getParams() {
        return parameters;
    }

    /**
     * Checks whether {@link Params} are available for these Arguments.
     *
     * @return {@code true} if this Arguments object has a {@link Params}
     *         object, otherwise {@code false}
     * @since 0.0.5
     */
    public boolean hasParams() {
        return getParams() != null;
    }

    /**
     * Gets a {@link Parameter} value for the parameter with the given name, if
     * there is a {@link Params} object available for these Arguments and said
     * {@link Params} object contains a value for the given parameter. If either
     * of these conditions are not true, {@code null} is returned.
     *
     * @param parameter the parameter to get the {@link Parameter} value for
     * @return a {@link Parameter} for the given parameter, or {@code null} if
     *         there isn't one
     * @since 0.0.5
     */
    public Parameter getParam(String parameter) {
        if (!hasParams()) {
            return null;
        }
        return getParams().get(parameter);
    }

    /**
     * Gets a {@link Parameter} value for the parameter with the given name, if
     * there is a {@link Params} object available for these Arguments and said
     * Params object contains a value for the given parameter. If either of
     * these conditions are not true, {@code null} is returned. This method is a
     * redirect to {@link #getParam(String)}
     *
     * @param parameter the parameter to get the {@link Parameter} value for
     * @return a {@link Parameter} for the given parameter, or {@code null} if
     *         there isn't one
     * @since 0.0.5
     */
    public Parameter param(String parameter) {
        return getParam(parameter);
    }

    /**
     * Gets the raw {@link String} value for the parameter with the given name,
     * if there is a {@link Params} object available for these Arguments and
     * said Params object contains a value for the given parameter. If either of
     * these conditions are not true, {@code null} is returned.
     *
     * @param parameter the parameter to get the raw string value for
     * @return a string value for the given parameter, or {@code null} if there
     *         isn't one
     * @since 0.0.5
     */
    public String rawParam(String parameter) {
        Parameter param = getParam(parameter);
        if (param == null) {
            return null;
        }
        return param.rawString();
    }

    /**
     * Checks whether the given parameter is available in this Arguments'
     * {@link Params} object.
     *
     * @param parameter the parameter to check for the presence of
     * @return whether the given parameter is available
     * @since 0.0.5
     */
    public boolean hasParam(final String parameter) {
        return hasParams() && getParams().has(parameter);
    }

    /**
     * Gets the {@link Flag} object with the given name, or {@code null} if it
     * doesn't exist.
     *
     * @param flag the name of the flag to get the Flag object for
     * @return the {@link Flag} object for the flag with the given name or
     *         {@code null} if there isn't one
     * @since 0.0.5
     */
    public Flag getValueFlag(final String flag) {
        for (final Flag f : flags) {
            if (f.getName().equalsIgnoreCase(flag)) {
                return f;
            }
        }
        return null;
    }

    /**
     * Checks whether these arguments contain a flag with a value with the given
     * name.
     *
     * @param flag the name of the flag to check for
     * @return whether these arguments contain a value flag with the given name
     * @since 0.0.5
     */
    public boolean hasValueFlag(final String flag) {
        for (final Flag f : flags) {
            if (f.getName().equalsIgnoreCase(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether these arguments contain a flag with no value with the
     * given name.
     *
     * @param flag the name of the flag to check for
     * @return whether these arguments contain a non-value flag with the given
     *         name
     * @since 0.0.5
     */
    public boolean hasNonValueFlag(final String flag) {
        for (final Flag f : doubleFlags) {
            if (f.getName().equalsIgnoreCase(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the length of the arguments.
     *
     * @return the amount of arguments in this Arguments object
     * @since 0.0.5
     */
    public int length() {
        return length(true);
    }

    /**
     * Gets the length of the arguments.
     *
     * @param includeFlagArgs whether to include flag args in the arg count
     * @return the amount of arguments in this Arguments object
     * @since 0.0.5
     */
    public int length(boolean includeFlagArgs) {
        if (includeFlagArgs) {
            return all.size();
        } else {
            return arguments.size();
        }
    }

    /**
     * Converts this Arguments object to a raw String[] of arguments.
     *
     * @return a raw String[] of arguments for this object
     * @since 0.0.5
     */
    public String[] toStringArray() {
        String[] result = new String[raw.length];
        System.arraycopy(raw, 0, result, 0, raw.length);
        return result;
    }

    /**
     * Sets the {@link Params} object for this Arguments object. Should only be
     * called directly after creation. If this is called multiple times an
     * {@link IllegalStateException} will be thrown.
     *
     * @param parameters the {@link Params} to set for this Arguments object
     * @return this Arguments object
     * @throws {@link IllegalStateException} if it is called multiple times
     * @since 0.0.5
     */
    public Arguments withParams(final Params parameters) {
        if (this.parameters != null) {
            throw new IllegalStateException();
        }
        this.parameters = parameters;
        return this;
    }
}
