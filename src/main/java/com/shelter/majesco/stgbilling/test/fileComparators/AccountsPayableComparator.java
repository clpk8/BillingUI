package com.shelter.majesco.stgbilling.test.fileComparators;

import org.apache.xpath.operations.Bool;

import java.util.Comparator;

public class AccountsPayableComparator implements Comparator <AccountsPayable>{
    public int compare(AccountsPayable ap1, AccountsPayable ap2) {
        int result = Integer.compare(ap1.getMaskedPolicyNumber(), ap2.getMaskedPolicyNumber());
        if (result == 0) {
            result = ap1.AP_PAYMENT_DATE.compareTo(ap2.AP_PAYMENT_DATE);
        }
        if (result == 0) {
            result = Double.compare(Double.parseDouble(ap1.AP_AMOUNT), Double.parseDouble(ap2.AP_AMOUNT));
        }
        return result;
    }
}
