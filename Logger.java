package com.internship.bank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class Logger
{
    public enum log
    {
        INFO,
        WARNING,
        ERROR
    }

    private static final String logFile = "transactions.log";
    private static final Logger INSTANCE = new Logger();

    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    private Logger()
    {
        Thread consumerThread = new Thread(this::consume, "log-writer");
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    public static Logger getInstance()
    {
        return INSTANCE;
    }

    public void produce(String message, log level)
    {
        String entry = String.format("%s | %-5s | %s", LocalDateTime.now(), level, message);
        queue.offer(entry);
    }
    private void consume()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true)))
        {
            while(true)
            {
                String entry = queue.take();
                writer.write(entry);
                writer.newLine();
                writer.flush();
            }
        }
        catch (IOException | InterruptedException e)
        {
            System.err.println("Log writer stopped: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}