package com.fiki.app.wifi.wifidocumentation.src.domain.confi;

/**
 * Created by root on 2017/10/16.
 */

public class Validations {

    public static boolean validateSize(String value,int size) {

        if(value.length() == size)
        {
            return true;
        }
        return false;

    }
    public static boolean moreThan(String value,int size) {

        if(value.length() > size)
        {
            return true;
        }
        return false;

    }
    public static boolean lessThan(String value,int size) {

        if(value.length() < size)
        {
            return true;
        }
        return false;
    }
    public static boolean checkDataWasEntered(String value) {

        if(value.trim().length() > 0)
        {
            if(value.trim() != "")
            {
                return true;
            }
        }
        return false;

    }

}
