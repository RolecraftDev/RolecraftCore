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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper around a {@link Map} to provide a set of parameters for commands.
 *
 * @since 0.0.5
 */
public class Params {
    /**
     * A {@link Map} of all of the parameters.
     */
    private final Map<String, Parameter> params;
    /**
     * Base information for these Params.
     */
    private final ParamsBase base;

    /**
     * Whether this set of parameters is valid.
     */
    private boolean valid = true;

    /**
     * Creates a new set of Params from the given {@link Map} of parameters to
     * values.
     *
     * @param base base information for these params
     * @param params the parameters and their values for this Params object
     * @since 0.0.5
     */
    public Params(ParamsBase base, Map<String, Parameter> params) {
        this.params = params;
        this.base = base;
    }

    /**
     * Gets a {@link Parameter} value for the given parameter.
     *
     * @param parameter the parameter to get the value for
     * @return a {@link Parameter} for the given parameter
     * @since 0.0.5
     */
    public Parameter get(String parameter) {
        return params.get(parameter);
    }

    /**
     * Gets the {@link ParamsBase} object for this set of Params. Contains basic
     * info about the parameters involved.
     *
     * @return basic information about these Params
     * @since 0.0.5
     */
    public ParamsBase getBaseInfo() {
        return base;
    }

    /**
     * Checks whether these Params contain a value for the given parameter.
     *
     * @param parameter the parameter to check for the presence of
     * @return whether the given parameter has a value in this Params object
     * @since 0.0.5
     */
    public boolean has(String parameter) {
        return params.containsKey(parameter);
    }

    /**
     * Gets a Set of all of the parameter names contained by this Params
     * object's Map of parameters
     *
     * @return A Set of all of the parameter names for this Params
     * @since 0.0.5
     */
    public Set<String> parameters() {
        return new HashSet<String>(params.keySet());
    }

    /**
     * Gets a {@link Set} of all of the parameter values contained by this
     * Params object's {@link Map} of parameters.
     *
     * @return a {@link Set} of all of the parameter values for this Params
     * @since 0.0.5
     */
    public Set<Parameter> values() {
        return new HashSet<Parameter>(params.values());
    }

    /**
     * Gets a {@link Set} of all of the entries to the {@link Map} of parameters
     * contained by this Params object.
     *
     * @return a {@link Set} of all entries to this Params' {@link Map}
     * @since 0.0.5
     */
    public Set<Map.Entry<String, Parameter>> entries() {
        return new HashSet<Map.Entry<String, Parameter>>(params.entrySet());
    }

    /**
     * Checks whether this set of parameters is valid for the base it was
     * constructed for. Used for automatic validation of arguments
     *
     * @return whether this set of parameters is valid
     * @since 0.0.5
     */
    public boolean valid() {
        return valid;
    }

    /**
     * Invalidates this set of parameters. Should only be used in automatic
     * validation.
     *
     * @since 0.0.5
     */
    void invalidate() {
        valid = false;
    }
}
