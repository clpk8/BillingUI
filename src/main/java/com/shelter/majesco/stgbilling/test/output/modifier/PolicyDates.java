package com.shelter.majesco.stgbilling.test.output.modifier;

import java.util.Calendar;
import java.util.Date;

public class PolicyDates {
    public Date oldEffectiveDate;
    public Date oldExpiryDate;
    public Date oldEffectiveDateNextTerm;
    public Date newEffectiveDate;
    public Date newEffectiveDateNextTerm;
    public Date newExpiryDate;

    public void calculateDates(Date oldEffectiveDate, Date newEffectiveDate){
        this.oldEffectiveDate = oldEffectiveDate;
        this.newEffectiveDate = newEffectiveDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(oldEffectiveDate);
        cal.add(Calendar.MONTH, 6);
        oldExpiryDate = cal.getTime();
        cal.add(Calendar.MONTH, 6);
        oldEffectiveDateNextTerm = cal.getTime();

        cal.setTime(newEffectiveDate);
        cal.add(Calendar.MONTH, 6);
        newExpiryDate = cal.getTime();
        cal.add(Calendar.MONTH, 6);
        newEffectiveDateNextTerm = cal.getTime();
    }
}
