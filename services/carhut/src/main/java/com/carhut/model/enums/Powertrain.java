package com.carhut.model.enums;

import java.util.ArrayList;
import java.util.List;

public class Powertrain {

    public static final String FRONT_WHEEL = "FrontWheel";
    public static final String REAR_WHEEL = "RearWheel";
    public static final String ALL_WHEEL = "AllWheel";
    public static final String NOT_STATED = "NotStated";
    private List<String> powertrainList = new ArrayList<>();

    public Powertrain() {
        powertrainList.add(FRONT_WHEEL);
        powertrainList.add(REAR_WHEEL);
        powertrainList.add(ALL_WHEEL);
        powertrainList.add(NOT_STATED);
    }

    public List<String> getPowertrainTypes() {
        return powertrainList;
    }


}
