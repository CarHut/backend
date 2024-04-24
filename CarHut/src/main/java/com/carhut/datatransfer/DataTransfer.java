package com.carhut.datatransfer;

import com.carhut.datatransfer.util.CarIdGenerator;
import com.carhut.enums.DataTransferEnum;
import com.carhut.models.deprecated.AutobazarEUCarObject;

import java.util.List;

@Deprecated
public interface DataTransfer {

    void transferData(DataTransferEnum endpoint);
    List<AutobazarEUCarObject> prepareData();
    String createIdFromData(CarIdGenerator carIdGenerator, AutobazarEUCarObject autobazarEUCarObject);

}
