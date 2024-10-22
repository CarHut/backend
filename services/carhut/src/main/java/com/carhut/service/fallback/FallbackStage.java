package com.carhut.service.fallback;

public enum FallbackStage {
    STAGE_1, // fails to add car to database
    STAGE_2, // fails to update users count of offers
    STAGE_3  // fails to save images
}
