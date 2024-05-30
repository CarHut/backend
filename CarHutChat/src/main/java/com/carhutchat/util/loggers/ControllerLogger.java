package com.carhutchat.util.loggers;

import com.carhutchat.constants.FileConstants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControllerLogger {

    private static ControllerLogger controllerLogger = null;
    private static final String logsBasePath = FileConstants.baseLogsPath;

    public static synchronized ControllerLogger getControllerLogger() {
        if (controllerLogger == null) {
            controllerLogger = new ControllerLogger();
        }
        return controllerLogger;
    }

    public synchronized void saveToFile(String log) {
        LocalDate localDate = LocalDate.now();
        String filePath = logsBasePath + "/daily-log-" + localDate;

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(formattedTime + " | " + log);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
