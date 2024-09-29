package com.carhut.proxy.util.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProxyLogger {

    private static ProxyLogger proxyLogger = null;
    private SimpleDateFormat format;
    private BufferedWriter writer;

    private ProxyLogger() {
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        try  {
            File file = new File(System.getProperty("user.dir") + "/logs/proxy-logs.log");
            this.writer = new BufferedWriter(new FileWriter(file.getPath(), true));
        } catch (IOException exception) {
            throw new RuntimeException("Proxy logger is not available. Problem: " + exception.getMessage());
        }
    }

    public static ProxyLogger getInstance() {
        if (proxyLogger == null) {
            proxyLogger = new ProxyLogger();
        }
        return proxyLogger;
    }

    public synchronized void logInfo(String log) {
        log(log, LogType.INFO);
    }

    public synchronized void logError(String log) {
        log(log, LogType.ERROR);
    }

    public synchronized void logWarn(String log) {
        log(log, LogType.WARN);
    }

    private synchronized void log(String log, LogType type) {
        final String timestamp = format.format(new Date());
        String logType;
        switch (type) {
            case ERROR -> logType = "ERROR";
            case INFO -> logType = "INFO";
            case WARN -> logType = "WARN";
            default -> logType = "UNDEFINED";
        }

        try {
            writer.write("[" + timestamp + "]" + "[" + logType + "]: " + log);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

}
