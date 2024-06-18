package io.github.chitchat.common.dos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Dos {
    private static final int PORT = 12345;
    private static final int MAX_MESSAGES_PER_MINUTE = 30;
    private static final int MAX_CONNECTIONS_PER_MINUTE = 10;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(1); // Use Duration for flexibility

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ConcurrentHashMap<InetAddress, Integer> connectionCounts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<InetAddress, Integer> messageCounts = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<InetAddress, Long> blockedIPs = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                InetAddress ipAddress = socket.getInetAddress();

                if (isBlocked(ipAddress)) {
                    socket.close();
                    continue;
                }

                if (isRateLimited(ipAddress, connectionCounts, MAX_CONNECTIONS_PER_MINUTE)) {
                    blockIP(ipAddress);
                    socket.close();
                    continue;
                }

                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isRateLimited(InetAddress ipAddress, ConcurrentHashMap<InetAddress, Integer> rateMap, int maxRate) {
        rateMap.putIfAbsent(ipAddress, 0);
        rateMap.put(ipAddress, rateMap.get(ipAddress) + 1);

        if (rateMap.get(ipAddress) > maxRate) {
            return true;
        }

        scheduleReset(rateMap, ipAddress, BLOCK_DURATION.toMillis());

        return false;
    }

    private static void scheduleReset(ConcurrentHashMap<InetAddress, Integer> rateMap, InetAddress ipAddress, long delay) {
        scheduler.schedule(() -> rateMap.put(ipAddress, 0), delay, TimeUnit.MILLISECONDS);
    }

    private static boolean isBlocked(InetAddress ipAddress) {
        Long blockEndTime = blockedIPs.get(ipAddress);
        if (blockEndTime == null || System.currentTimeMillis() >= blockEndTime) {
            blockedIPs.remove(ipAddress);
            return false;
        }

        return true;
    }

    private static void blockIP(InetAddress ipAddress) {
        blockedIPs.put(ipAddress, System.currentTimeMillis() + BLOCK_DURATION.toMillis());
        System.out.println("Blocked IP: " + ipAddress);
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            InetAddress ipAddress = socket.getInetAddress();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String message;
                while ((message = in.readLine())!= null) {
                    if (isRateLimited(ipAddress, messageCounts, MAX_MESSAGES_PER_MINUTE)) {
                        blockIP(ipAddress);
                        socket.close();
                        return;
                    }

                    System.out.println("Message from " + ipAddress + ": " + message);
                    // Broadcast message to other clients (omitted for brevity)
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
