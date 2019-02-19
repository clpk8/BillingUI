package com.shelter.majesco.stgbilling.test.output.modifier;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateValidator {

    DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    //need to be deleted in the future, and use format instead
    //DateFormat oldFormat = new SimpleDateFormat("dd-MMM-yyyy");

    //deternmind whether a date is valid
    public boolean isThisDateValid(Cell cell) {
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return true;
            }
        }
        return false;
    }



    //deternmind if a string is a valid date in MM/dd/yyyy format
    public boolean isStringDateValid(String dateString) {
        format.setLenient(false);

        try {
            format.parse(dateString);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    //deternmind if a date string is a valid month-end
    public boolean isMonthEnd(String dataString) {
        Date effectiveDate = new Date();
        try {
            effectiveDate = format.parse(dataString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(effectiveDate);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth == 29 || dayOfMonth == 30 || dayOfMonth == 31) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public static int daysBetween(Date oldDate, Date newDate) {
        int days = 0;
        days = Days.daysBetween(
                new LocalDate(oldDate.getTime()),
                new LocalDate(newDate.getTime())).getDays();
//        if (oldDate.getTime() - newDate.getTime() > 0) {
//            return days;
//        } else {
//            days = -days;
//            return days;
//        }
        return days;
    }

    public int daysBetweenDates(String dateString1, String dateString2) {

        try {
            Date date1 = format.parse(dateString1);
            Date date2 = format.parse(dateString2);

            int days = 0;
            days = Days.daysBetween(
                    new LocalDate(date1.getTime()),
                    new LocalDate(date2.getTime())).getDays();
            if (date1.getTime() - date2.getTime() > 0) {
                return days;
            } else {
                days = -days;
                return days;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

}