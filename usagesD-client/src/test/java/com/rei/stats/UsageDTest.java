package com.rei.stats;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class UsageDTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void test() throws IOException, InterruptedException {
        
        UsagesDClient client = new UsagesDClient("localhost", 9125);
        
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int x = 0; x < 5; x++) {
            executor.execute(() -> {
                for (int i = 0; i < 10000; i++) {
                    client.recordUsage("usages", "thing" + i % 50);
                }
            });
        }
        executor.awaitTermination(1, TimeUnit.MINUTES);
        executor.shutdown();
    }
}
