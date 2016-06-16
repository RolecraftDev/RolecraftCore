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
package com.github.rolecraftdev.event;

import org.bukkit.event.Cancellable;

import javax.annotation.Nonnull;

/**
 * A form of {@link Cancellable} event which requires a message to be sent to
 * involved parties if the event is cancelled.
 *
 * @since 0.1.0
 */
public interface RolecraftCancellable extends Cancellable {
    /**
     * Gets the message which is to be sent to involved parties if
     * {@link #isCancelled()} is equal to {@code true}.
     *
     * @return the cancellation message
     * @since 0.1.0
     */
    @Nonnull
    String getCancelMessage();

    /**
     * Sets the message which is to be sent to involved parties if
     * {@link #isCancelled()} is equal to {@code true}.
     *
     * @param message the new cancellation message
     * @since 0.1.0
     */
    void setCancelMessage(@Nonnull String message);
}
