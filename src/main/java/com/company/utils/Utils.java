package com.company.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Utils {
    public static double roundValue(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.UP)
                .doubleValue();
    }
}
