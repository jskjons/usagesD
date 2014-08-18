package com.rei.stats;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class UsageDTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void test() throws IOException, InterruptedException {
        UsagesD usageD = new UsagesD(tmp.getRoot().toPath(), 9085, 8085);
        usageD.start();
        
        UsagesDClient client = new UsagesDClient("localhost", 9085);
        
        for (int i = 0; i < 100; i++) {
            client.recordUsage("usages", "thing");
        }
        
        Thread.sleep(1000 * 60 * 5);
    }

}
