package com.carhut.datatransfer;

import com.carhut.constants.FileConstants;
import com.carhut.datatransfer.util.CarIdGenerator;
import com.carhut.enums.DataTransferEnum;
import com.carhut.models.AutobazarEUCarObject;
import com.carhut.services.DataTransferService;
import com.carhut.util.loggers.Logger;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Transfers data from predefined Excel form to specified endpoint.
**/
@Deprecated
@Service
public class ExcelDataTransfer implements DataTransfer {

    @Autowired
    private Logger logger;

    private DataTransferService dataTransferService;

    @Autowired
    public ExcelDataTransfer(DataTransferService dataTransferService) {
        this.dataTransferService = dataTransferService;
    }

    @Override
    public void transferData(DataTransferEnum endpoint) {
        List<AutobazarEUCarObject> cars = prepareData();

        for (AutobazarEUCarObject car : cars) {
            dataTransferService.saveCar(car);
            logger.saveLogToFile("[LOG][ExcelDataTransfer]: transferring car data of " + car.getHeader() + ".");
        }

        logger.saveLogToFile("[LOG][ExcelDataTransfer]: all car data was transferred successfully.");
    }

    @Override
    public List<AutobazarEUCarObject> prepareData() {
        return readDataFromCSV(FileConstants.pathToExcelFile);
    }

    @Override
    public String createIdFromData(CarIdGenerator carIdGenerator, AutobazarEUCarObject autobazarEUCarObject) {
        return carIdGenerator.generateId(autobazarEUCarObject);
    }

    public List<AutobazarEUCarObject> readDataFromCSV(String pathToCSVFile) {

        List<AutobazarEUCarObject> autobazarEUCarObjects = new ArrayList<>();

        boolean headerSwitch = true;
        int counter = 0;

        try (CSVReader reader = new CSVReader(new FileReader(pathToCSVFile))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {

                if (!headerSwitch) {
                    StringBuilder concatenatedString = new StringBuilder();
                    for (String cell : nextLine) {
                        concatenatedString.append(cell);
                    }

                    AutobazarEUCarObject autobazarEUCarObject = createCarObject(concatenatedString.toString());

                    if (autobazarEUCarObject != null) {
                        autobazarEUCarObjects.add(autobazarEUCarObject);
                    }
                }

                headerSwitch = !headerSwitch;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return autobazarEUCarObjects;

    }

    private AutobazarEUCarObject createCarObject(String string) {
        String[] columns = string.split(";");

        if (columns.length != 16) {
            System.out.println("Problem while parsing line from .csv file to AutobazarEUCarObject.");
            return null;
        }

        return new AutobazarEUCarObject(columns[0], columns[1], columns[2], columns[3], columns[4],
                columns[5], columns[6], columns[7], columns[8], columns[9], columns[10], columns[11],
                columns[12], columns[13], columns[14], columns[15]);

    }
}
