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

import java.util.Comparator;
import java.util.Set;

/**
 * review: comments need some more details. what is a "class", what is this
 * class used for.. <br/>
 * <br/>
 * orders the elements according to their membership to equivalence classes.
 * Elements not explicitely contained in one specific class belong to a class
 * encoded by null.<br/>
 * <hr/>
 * Example: <br/>
 * say you want to order a list lst = [ 1 , 2 , 3 , 4 , 5 ] such that 1,3 and 4
 * appear after the other values:<br/>
 * <br/>
 * Set&lt;Integer&gt;[] equivalenceClasses = new Set[]{null,new
 * HashSet&lt;Integer&gt;(Arrays.asList(1,3,4))};<br/>
 * EquivalenceComparator&lt;Integer&gt; comparator = new
 * EquivalenceClassComparator&lt;Integer&gt;(equivalenceClasses);<br/>
 * Collections.sort(lst, comparator);<br/>
 * <br/>
 * This would yield lst = [ 2, 5, 1, 3, 4]
 * 
 * 
 * @author Michael Kutschke
 * 
 * @param <T>
 */
public class EquivalenceClassComparator<T> implements Comparator<T> {

    private final Set<T>[] classes;

    public EquivalenceClassComparator(final Set<T>[] classes) {
        this.classes = classes;
    }

    @Override
    public int compare(final T o1, final T o2) {
        boolean beforeNull = true;
        boolean found = false;
        // review: found 2?
        boolean found2 = false;
        for (final Set<T> clazz : classes) {
            if (clazz == null) {
                beforeNull = false;
            } else {
                // review: use smaller methods with "speaking names" to reduce
                // complexity
                if (clazz.contains(o1)) {
                    if (clazz.contains(o2)) {
                        return 0;
                    } else if (beforeNull) {
                        // class 2 is after us
                        return -1;
                    } else if (found2) {
                        // after us, use find2 flag
                        return 1;
                    } else {
                        found = true;
                    }
                } else if (clazz.contains(o2)) {
                    if (beforeNull) {
                        return 1;
                    } else if (found) {
                        // could be the null class
                        return -1;
                    } else {
                        found2 = true;
                    }
                }
            }
        }
        return found ? 1 : found2 ? -1 : 0; // it holds that found NAND found2 =
                                            // true
    }

}
