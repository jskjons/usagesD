package com.rei.stats;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.rei.stats.H2UsageStore;
import com.rei.stats.Usage;
import com.rei.stats.UsageStore;

public class H2UsageStoreTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    private UsageStore store;
    
    @Before
    public void before() {
        store = new H2UsageStore(tmp.getRoot().toPath());
    }
    
    @Test
    public void canRecordAndFindUsgae() {
        store.init();
        store.recordUsage(new Usage("usages", "some thing", Instant.now().toEpochMilli()));
        store.recordUsage(new Usage("usages", "some thing", Instant.now().toEpochMilli()));
        Collection<Usage> usages = store.findUsages("usages", Instant.now().minusMillis(1000), 10);
        assertEquals(2, usages.size());
        
        usages = store.findUsages("usages", Instant.now().minusMillis(1000), 1);
        assertEquals(1, usages.size());
        
        usages = store.findUsages("usages", 10);
        assertEquals(2, usages.size());
        
        usages = store.findUsages("usages", 1);
        assertEquals(1, usages.size());
        
        usages = store.findUsages(1);
        assertEquals(1, usages.size());
    }
}