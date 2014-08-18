package com.rei.stats;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServer {
        private static final int PACKET_SIZE = 256;
        private final DatagramSocket server;
        private boolean started;

        private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);
        
        private PacketHandler handler = (data) -> {};
        
        private ExecutorService handlerPool = Executors.newCachedThreadPool();
        
        public UdpServer(int port) throws SocketException {
            server = new DatagramSocket(port);
            
        }
        
        public void start() {
            new Thread(() -> {
                final DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
                while (started) {
                    try {
                        server.receive(packet);
                        String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
                        handlerPool.submit(() -> {
                            handler.handle(data);
                        });
                    } catch (IOException e) {
                        logger.error("failed to receive packet", e);
                    } catch (Exception e) {
                        
                    }
                }
            }).start();
            started = true;
        }
        
        public void stop() {
            started = false;
            handlerPool.shutdownNow();
            server.disconnect();
        }

        public void setPacketHandler(PacketHandler handler) {
            this.handler = handler;
            
        }
}
