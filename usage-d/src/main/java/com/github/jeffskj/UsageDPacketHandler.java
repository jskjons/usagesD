package com.github.jeffskj;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageDPacketHandler implements PacketHandler {

    private static final Logger logger = LoggerFactory.getLogger(UsageDPacketHandler.class);
    private UsageStore usageStore;
    
    public UsageDPacketHandler(UsageStore usageStore) {
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
