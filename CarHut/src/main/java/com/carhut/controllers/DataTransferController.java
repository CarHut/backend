package com.carhut.controllers;

import com.carhut.datatransfer.ExcelDataTransfer;
import com.carhut.enums.DataTransferEnum;
import com.carhut.models.deprecated.AutobazarEUCarObject;
import com.carhut.services.DataTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Deprecated
@Controller
@RequestMapping(path = "/database")
public class DataTransferController {

    @Autowired
    private ExcelDataTransfer excelDataTransfer;
    @Autowired
    private DataTransferService dataTransferService;


    @GetMapping("/addToPosgreSQL")
    public void addToPosgreSQL() {
        excelDataTransfer.transferData(DataTransferEnum.TO_POSTGRESQL);
    }

    @GetMapping("/getAllCars")
    @ResponseBody
    public List<AutobazarEUCarObject> getAllCars() {
        return dataTransferService.getAllCars();
    }

    @GetMapping("/saveToTempDatabase")
    public void transferCarsFromAutobazarEUToTmpCarList() {
        dataTransferService.transferCarsFromAutobazarEUToTmpCarList();
    }

}
