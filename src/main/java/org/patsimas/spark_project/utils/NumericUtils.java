package org.patsimas.spark_project.utils;

public class NumericUtils {

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            if (strNum.contains(".")) {
                strNum = strNum.substring(0, strNum.indexOf("."));
            }
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
