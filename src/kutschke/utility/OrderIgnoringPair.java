/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Kutschke - initial API and implementation.
 */
package kutschke.utility;

public class OrderIgnoringPair<T> extends Pair<T, T> {

    int hashcode;

    public OrderIgnoringPair(final T o1, final T o2) {
        super(o1, o2);
        hashcode = Math.min(o1.hashCode(), o2.hashCode()) + 67 * Math.max(o1.hashCode(), o2.hashCode());
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof OrderIgnoringPair)) {
            return false;
        }
        final OrderIgnoringPair<?> other = (OrderIgnoringPair<?>) o;
        return super.equals(other) || equalsReverse(other);
    }

    private boolean equalsReverse(final OrderIgnoringPair<?> other) {
        return bothNullOrEqual(getSecond(), other.getFirst()) && bothNullOrEqual(getFirst(), other.getSecond());
    }

    @Override
    public int hashCode() {
        return hashcode;
    }
}
