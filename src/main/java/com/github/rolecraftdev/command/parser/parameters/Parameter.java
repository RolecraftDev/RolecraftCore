package com.github.rolecraftdev.command.parser.parameters;

import com.github.rolecraftdev.command.parser.ChatSection;

/**
 * An extension of {@link ChatSection}, used for parameters.
 *
 * @since 0.0.5
 */
public class Parameter extends ChatSection {
    /**
     * The information (name, whether it is optional) for this parameter.
     */
    private final ParamsBase.ParamInfo info;

    /**
     * Creates a new Parameter, using the given {@link String} argument as a raw
     * string.
     *
     * @param arg the raw string for this Parameter
     * @param info information about this parameter
     * @since 0.0.5
     */
    public Parameter(String arg, ParamsBase.ParamInfo info) {
        super(arg);
        this.info = info;
    }

    /**
     * Gets the {@link ParamsBase.ParamInfo} for this Parameter.
     *
     * @return This Parameter's {@link ParamsBase.ParamInfo} object
     * @since 0.0.5
     */
    public ParamsBase.ParamInfo getInfo() {
        return info;
    }
}
