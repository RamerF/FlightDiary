/**
 *
 */
package org.ramer.diary.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Utils for Collections.
 *
 * @author Ramer
 * @date Dec 31, 2016
 */
public class CollectionsUtils {

  /**
   * Removes the same.
   *
   * @param <E> the element type
   * @param tags the tags
   * @return the list
   */
  public static <E> List<E> removeSame(Collection<E> tags) {
    return new ArrayList<>(new LinkedHashSet<>(tags));
  }
}
