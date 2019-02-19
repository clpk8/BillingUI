package com.shelter.majesco.stgbilling.test.output.modifier;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateModifier {
    static DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Date beginning;
    Date end;
    int dayOfMonthBeginning;
    int dayOfMonthEnd;
    int differenceEffective;
    int differenceExpiry;
    List<Date> possibleEFTDraftDates;
    //user input and find difference between dates.
    public int findDifferenceBetweenDate(Date beginning, Date end) {
        DateValidator dateValidator = new DateValidator();
        this.beginning = beginning;
        this.end = end;

        int daysDifferenceDueToDifferentMonth = 0;
        int durationOfBeginning = 0;
        int durationOfEnd = 0;
        Date date = beginning;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 6);
        Date date2 = cal.getTime();
        durationOfBeginning = dateValidator.daysBetween(date, date2);

        date = end;
        cal.setTime(date);
        cal.add(Calendar.MONTH, 6);
        date2 = cal.getTime();
        durationOfEnd = dateValidator.daysBetween(date, date2);
        daysDifferenceDueToDifferentMonth =  durationOfEnd - durationOfBeginning;

        int days = dateValidator.daysBetween(beginning, end) ;
        differenceEffective = days;
        days += daysDifferenceDueToDifferentMonth;
        differenceExpiry = days;
        System.out.println("Difference between two date is " + days);
        System.out.println("Duration difference is " + daysDifferenceDueToDifferentMonth);

        return (int) days;
    }

    //get day of the month for base line date
    public int getDayOfMonthBeginning() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginning);
        dayOfMonthBeginning = cal.get(Calendar.DAY_OF_MONTH);
        return dayOfMonthBeginning;
    }

    //get day of the month for the converted date
    public int getDayOfMonthEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        dayOfMonthEnd = cal.get(Calendar.DAY_OF_MONTH);
        return dayOfMonthEnd;
    }

    //add certain days to particular date
    public Date addDaysToDate(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    //add certain days to particular date and return string
    public String addDaystoDateString(String dateString, int days) {
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = newDateFormat.parse(dateString);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, days);
            return newDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Invalid Date";

    }

    //bump dates when month end encountered.
    public String bumpDateToFirstDayNextMonth(String dateString) {
        Date date = new Date();
        try {
            date = newDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar oldCal = Calendar.getInstance();
        oldCal.setTime(date);
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(date);
        if (oldCal.get(Calendar.MONTH) == 12) {
            //special case
            newCal.set(Calendar.YEAR, oldCal.get(Calendar.YEAR) + 1);
            newCal.set(Calendar.MONTH, 1);
            newCal.set(Calendar.DAY_OF_MONTH, 1);
            return newDateFormat.format(newCal.getTime());
        }
        newCal.set(Calendar.YEAR, oldCal.get(Calendar.YEAR));
        newCal.set(Calendar.MONTH, oldCal.get(Calendar.MONTH) + 1);
        newCal.set(Calendar.DAY_OF_MONTH, 1);
        return newDateFormat.format(newCal.getTime());
    }

    public int daysDifferentToAccetableEFT(Date EFTDate){
        for (Date possibleEFTDraftDate : possibleEFTDraftDates){
            int days = Days.daysBetween(
                    new LocalDate(EFTDate.getTime()),
                    new LocalDate(possibleEFTDraftDate.getTime())).getDays();
            if (Math.abs(days) >=0 && Math.abs(days) <=3){
                return days;
            }

        }
        //signal error
        return -4;
    }


    public void calculatePossibleEFTDates(){
        possibleEFTDraftDates = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        int year = Calendar.getInstance().get(Calendar.YEAR);


        for (int i = 1; i <= 12; i++){
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, i);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd);
            cal.add(Calendar.DAY_OF_MONTH, -12);
            possibleEFTDraftDates.add(cal.getTime());
            System.out.println(newDateFormat.format(cal.getTime()));
        }
    }

    public Date isDateACombinedPaymentDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonthEnd-1);
        return cal.getTime();
    }

}
