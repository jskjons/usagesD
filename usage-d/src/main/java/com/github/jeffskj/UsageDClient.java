package com.github.jeffskj;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageDClient {
    private static final Logger logger = LoggerFactory.getLogger(UsageDClient.class);
    
    private final DatagramSocket clientSocket;

    private final ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
        final ThreadFactory delegate = Executors.defaultThreadFactory();
        @Override public Thread newThread(Runnable r) {
            Thread t = delegate.newThread(r);
            t.setName("UsageD-" + t.getName());
            t.setDaemon(true);
            return t;
        }
    });
    
    public UsageDClient(String host, int port) {
        try {
            clientSocket = new DatagramSocket();
            clientSocket.connect(new InetSocketAddress(host, port));
        } catch (Exception e) {
            throw new RuntimeException("Failed to start StatsD connection", e);
        }
    }   
    
    public void recordUsage(String category, String key) {
        send(category + "|" + key);
    }
    
    private void send(final String message) {
        try {
            executor.execute(new Runnable() {
                @Override public void run() {
                    blockingSend(message);
                }
            });
        }catch (Exception e) {
            logger.warn("Unable to send message to StatsD.", e);
        }
    }

    private void blockingSend(String message) {
        try {
            final byte[] sendData = message.getBytes();
            final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
            clientSocket.send(sendPacket);
        } catch (Exception e) {
            logger.warn("Unable to send message to StatsD.", e);
        }
    }
}
