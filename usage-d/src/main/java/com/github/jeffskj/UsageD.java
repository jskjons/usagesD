package com.github.jeffskj;

import io.undertow.Undertow;

import java.io.IOException;
import java.nio.file.Path;

public class UsageD {
    private UsageStore usageStore;
    private UdpServer udpServer;
    private Undertow httpServer;
    
    public UsageD(Path homeDir, int udpPort, int httpPort) throws IOException {
        usageStore = new H2UsageStore(homeDir);
        udpServer = new UdpServer(udpPort);
        udpServer.setPacketHandler(new UsageDPacketHandler(usageStore));
        httpServer = Undertow.builder().addHttpListener(httpPort, "0.0.0.0")
                                       .setHandler(new UsageDHttpRequestHandler(usageStore))
                                       .build();
    }
    
    public void start() {
        usageStore.init();
        udpServer.start();
        httpServer.start();
    }
    
    public void stop() {
        udpServer.stop();
        httpServer.stop();
    }
}
