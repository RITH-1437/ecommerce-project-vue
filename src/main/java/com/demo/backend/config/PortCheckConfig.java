package com.demo.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class PortCheckConfig {

    @Value("${server.port:8080}")
    private int port;

    @PostConstruct
    public void checkPort() {
        // Check if port is already in use by trying to connect to it
        try (Socket socket = new Socket("localhost", port)) {
            // If connection succeeds, port is in use
            System.err.println("\n❌ ERROR: Port " + port + " is already in use!");
            System.err.println("➡ Please stop the program using this port OR change your application.properties:");
            System.err.println("   server.port=8081\n");
            System.exit(1);
        } catch (IOException e) {
            // Port is available (connection refused) → OK
            System.out.println("✅ Port " + port + " is available");
        }
    }
}
