/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javafx.beans;

import java.util.List;
import java.util.Objects;

/**
 * A subscription encapsulates how to cancel it without having
 * to keep track of how it was created.
 *
 * @since 21
 */
@FunctionalInterface
public interface Subscription {

    /**
     * An empty subscription. Does nothing when cancelled.
     */
    static final Subscription EMPTY = () -> {};

    /**
     * Returns a {@code Subscription} which combines all of the given
     * subscriptions.
     *
     * @param subscriptions an array of subscriptions to combine, cannot be {@code null} or contain {@code null}
     * @return a {@code Subscription}, never {@code null}
     * @throws NullPointerException when {@code subscriptions} is {@code null} or contains {@code null}
     */
    static Subscription combine(Subscription... subscriptions) {
        List<Subscription> list = List.of(subscriptions);

        return () -> list.forEach(Subscription::unsubscribe);
    }

    /**
     * Cancels this subscription, or does nothing if already cancelled.<p>
     *
     * Implementors must ensure the implementation is idempotent.
     */
    void unsubscribe();

    /**
     * Combines this {@link Subscription} with the given {@code Subscription}
     * and returns a new {@code Subscription} which will cancel both when
     * cancelled.
     *
     * <p>This is equivalent to {@code Subscription.combine(this, other)}.
     *
     * @param other another {@code Subscription}, cannot be {@code null}
     * @return a combined {@code Subscription} which will cancel both when
     *     cancelled, never {@code null}
     * @throws NullPointerException when {@code other} is {@code null}
     */
    default Subscription and(Subscription other) {
        Objects.requireNonNull(other, "other cannot be null");

        return () -> {
            unsubscribe();
            other.unsubscribe();
        };
    }
}
