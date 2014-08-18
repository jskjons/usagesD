package com.rei.stats;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsagesDPacketHandler implements PacketHandler {

    private static final Logger logger = LoggerFactory.getLogger(UsagesDPacketHandler.class);
    private UsageStore usageStore;
    
    public UsagesDPacketHandler(UsageStore usageStore) {
        this.usageStore = usageStore;
    }

    @Override
    public void handle(String data) {
        int pipe = data.indexOf('|');
        if (pipe <= 0) {
            logger.warn("invalid UsageD data packet {}", data);
        }
        String category = data.substring(0, pipe);
        String key = data.substring(data.indexOf('|')+1, data.length());
        usageStore.recordUsage(new Usage(category, key, Instant.now().toEpochMilli()));
    }

}
