package com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles;

import javafx.util.Pair;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Data {
    public String policyNumber;
    public String term;
    public String expiryDate;
    public String effectiveDate;
    public String paymentPlan;
    public String paymentMethod;
    public String status;
    public String calcellationDate;
    public String equityDate;
    public List<Transaction> transactionList;
    public Data(){
        policyNumber = "0";
    }

    //wipe all data
    public void cleanDate(){
        policyNumber = "";
        expiryDate = "";
        effectiveDate = "";
        paymentPlan = "";
        paymentMethod = "";
        status = "";
        calcellationDate = "";
        equityDate = "";
        transactionList = new ArrayList<>();
    }
}
