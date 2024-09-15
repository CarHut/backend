package com.carhut.savedcarsservice.util.loggers;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ControllerLogger {

    private static ControllerLogger controllerLogger = null;
    private String logsPath = System.getProperty("user.dir") + "/logs";

    public static synchronized ControllerLogger getLogger() {
        if (controllerLogger == null) {
            controllerLogger = new ControllerLogger();
        }
        return controllerLogger;
    }

    public synchronized void saveToFile(String log) {
        LocalDate localDateTime = LocalDate.now();
        String filePath = logsPath + "/daily-log-" + localDateTime;

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(formattedTime + " | " + log);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
