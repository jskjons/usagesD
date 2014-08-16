package com.github.jeffskj;

import java.time.Instant;
import java.util.Collection;

public interface UsageStore {
    void init();
    
    void recordUsage(Usage usage);
    Collection<Usage> findUsages(String category, Instant cutoff, int limit);
    Collection<Usage> findUsages(String category, int limit);
    Collection<Usage> findUsages(int limit);
}
