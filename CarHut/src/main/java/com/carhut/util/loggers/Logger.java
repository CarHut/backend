package com.carhut.util.loggers;

import com.carhut.constants.FileConstants;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Logger {

    private String directoryPath;
    private String currentTime;

    public Logger() {
        this.currentTime = LocalDate.now().toString();
        this.directoryPath = FileConstants.pathToLogDirectory;
    }

    public synchronized void saveLogToFile(String log) {
        String filePath = directoryPath + "\\daily-log-" + currentTime;

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
