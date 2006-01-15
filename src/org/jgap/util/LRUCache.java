package org.jgap.util;

import java.util.*;

public class LRUCache extends LinkedHashMap {
  // Create cache

  private int m_maxEntries;

  public LRUCache(int a_maxExtries) {
    super(a_maxExtries+1, 0.75F, true);
    m_maxEntries = a_maxExtries;
  }
    // This method is called just after a new entry has been added
    public boolean removeEldestEntry(Map.Entry eldest) {
      return size() > m_maxEntries;
    }

  // If the cache is to be used by multiple threads,
  // the cache must be wrapped with code to synchronize the methods
//  cache = (Map) Collections.synchronizedMap(cache);
}
