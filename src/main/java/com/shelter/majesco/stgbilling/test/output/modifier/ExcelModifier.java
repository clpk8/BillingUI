package com.shelter.majesco.stgbilling.test.output.modifier;

import com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles.Transaction;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ExcelModifier {
    private PolicyDates policyDates;
    private File excelFile;
    private File originalFile;
    private String policy = "0000";
    private final int dateDifferenceBegin;
    private final int dateDifferenceEnd;
    private DateModifier dateModifier;
    private DateValidator dateValidator;
    private final DateFormat newDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
    private int dayOfMonthBeginning;
    private int dayOfMonthEnd;

    ExcelModifier(File excelFile,
                  Date beginning,
                  Date end,
                  PolicyDates policyDates) {
        DateModifier dateModifier = new DateModifier();
        this.dateModifier = dateModifier;
        int days = dateModifier.findDifferenceBetweenDate(beginning, end);
        this.excelFile = excelFile;
        this.dateDifferenceBegin = dateModifier.differenceEffective;
        this.dateDifferenceEnd = dateModifier.differenceExpiry;
        this.dayOfMonthBeginning = dateModifier.getDayOfMonthBeginning();
        this.dayOfMonthEnd = dateModifier.getDayOfMonthEnd();
        this.policyDates = policyDates;
    }

    //First modify the excel and only add duration to dates.
    public void saveExcelFile(File excel) {
        dateModifier.calculatePossibleEFTDates();
        int virableDateDifference = 0;
        Date newDate = new Date();
        dateValidator = new DateValidator();
        System.out.println("day of month beginning is " + dayOfMonthBeginning);
        System.out.println("day of month end is " + dayOfMonthEnd);
        int rowCount = 0;
        int colCount = 0;

        int daysForEFTDifference = 0;
        int paymentMethod = 0; // 0 for EFT - Recurring, 1 for Credit Card Recurring
        Boolean isCurrentTransactionEFTDraft = false;
        Boolean isEFTDraftFound = false;
        Boolean isEFTDateCalculated = false;
        Boolean isPaymentFound = false;
        boolean isEFTSubmissionFound = false;
        boolean isCombinedPaymentFound = false;
        boolean isEndorsementOrReinstatementFound = false;
        boolean isSecondPaymentFoundOnCombinedPayment = false;
        boolean isLateTermReinstatementFound = false;
        boolean isRenewalFound = false;
        boolean isSystemPaymentFound = false;
        boolean isReminderFound = false;
        boolean isEndorsementOrCancellationFound = false;
        boolean isImmediatePaymentFound = false;
        boolean isReinstatementFound = false;
        //endorsement or cancellation needed for systemPayment
        Date endorsementOrCancellationDate = new Date();
        Date renewalDate = new Date();
        Date immediatePaymentDate = new Date();
        ArrayList<Date> systemPaymentDates = new ArrayList<>();
        ArrayList<Transaction> EFTInfoList = new ArrayList<>();
        ArrayList<ArrayList<Transaction>> EFTInfoTable = new ArrayList<ArrayList<Transaction>>();
        int term = 1;
        int newTerm = 1;
        String currentPolicy = "0000";
        try {
            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIt = sheet.iterator();
            while (rowIt.hasNext()) {
                Row row = rowIt.next();
                Iterator<Cell> cellIt = row.cellIterator();
                while (cellIt.hasNext()) {
                    Cell cell = cellIt.next();
                    if (colCount == 1 || colCount == 0) {
                        cell.setCellValue(cell.toString().replace(".0", ""));
                        if (colCount == 1) {
                            if (!policy.equals(cell.toString())) {
                                policy = cell.toString();
                                System.out.println("PInfo:" + policy);
                                isEFTDraftFound = false;
                                EFTInfoTable.clear();
                            }
                        }
                    }
                    if (colCount == 1) {
                        if (!currentPolicy.equals(cell.toString())) {
                            //new policy found, reset
                            currentPolicy = cell.toString();
                            isLateTermReinstatementFound = false;
                        }
                    }
                    if (colCount == 2) {
                        if (cell.toString().equals("First")) {
                            term = 1;
                            virableDateDifference = dateDifferenceBegin;
                            if (newTerm != 1) {
                                newTerm = 1;
                                isReminderFound = false;
                                isPaymentFound = false;
                                isEFTDraftFound = false;
                                isEFTDateCalculated = false;
                                isCombinedPaymentFound = false;
                                isEndorsementOrReinstatementFound = false;
                                isSecondPaymentFoundOnCombinedPayment = false;
                                isEndorsementOrCancellationFound = false;
                                isRenewalFound = false;
                                isSystemPaymentFound = false;
                                isImmediatePaymentFound = false;
                                isReinstatementFound = false;
                                daysForEFTDifference = 0;
                                EFTInfoTable.clear();
                            }
                        } else {
                            virableDateDifference = dateDifferenceEnd;
                            if (cell.toString().equals("Second - b")) {
                                if (term != 3) {
                                    isEFTDateCalculated = false;
                                    isEFTDraftFound = false;
                                    term = 3;
                                }
                            } else {
                                term = 2;
                            }
                            if (newTerm != 2) {
                                newTerm = 2;
                                isReminderFound = false;
                                isPaymentFound = false;
                                isEFTDraftFound = false;
                                isEFTDateCalculated = false;
                                isCombinedPaymentFound = false;
                                isEndorsementOrReinstatementFound = false;
                                isEndorsementOrCancellationFound = false;
                                isSecondPaymentFoundOnCombinedPayment = false;
                                isRenewalFound = false;
                                isSystemPaymentFound = false;
                                isImmediatePaymentFound = false;
                                isReinstatementFound = false;
                                daysForEFTDifference = 0;
                                EFTInfoTable.clear();
                            }
                        }
                    } else if (colCount == 10 && (cell.toString().equals("Renewal")
                            || cell.toString().equals("Demand Notice for Policy")
                            || cell.toString().equals("Earned Premium Bill")
                            || cell.toString().equals("In Collection")
                            || cell.toString().equals("Returned Payment")
                            || cell.toString().equals("Returned Payment Letter")
                            || cell.toString().equals("Write-off")
                            || cell.toString().equals("Billing Endorsement")
                            || cell.toString().equals("Cancellation")
                            || cell.toString().equals("Endorsement")
                            || (cell.toString().equals("Payment") && !isEFTSubmissionFound)
                            || cell.toString().equals("Non Money Endorsement"))) {
                        virableDateDifference = dateDifferenceBegin;
                    }

                    if (cell.toString().equals("EFT - Recurring")) {
                        paymentMethod = 0;
                    } else if (cell.toString().equals("Credit Card Recurring")) {
                        paymentMethod = 1;
                    }
                    if (cell.toString().equals("Immediate Payment")) {
                        isImmediatePaymentFound = true;
                    }
                    if (cell.toString().equals("Late term reinstatement")) {
                        isLateTermReinstatementFound = true;
                    }
                    if (cell.toString().equals("Combined Billing")) {
                        isCombinedPaymentFound = true;
                    }
                    if (cell.toString().equals("Reinstatement") || cell.toString().equals("Endorsement")) {
                        isEndorsementOrReinstatementFound = true;
                    }
                    if (cell.toString().equals("Reinstatement")) {
                        isReinstatementFound = true;
                    }
                    if (cell.toString().equals("Cancellation") || cell.toString().equals("Endorsement")) {
                        isEndorsementOrCancellationFound = true;
                    }
                    if (cell.toString().equals("Renewal")) {
                        isRenewalFound = true;
                    }
                    if (cell.toString().equals("Reminder Notice")) {
                        isReminderFound = true;
                    }
                    if (cell.toString().equals("EFT Draft Notice")) {
                        isEFTDateCalculated = false;
                        isCurrentTransactionEFTDraft = true;
                        isEFTDraftFound = true;
                        isPaymentFound = false;
                        daysForEFTDifference = 0;
                    } else if (colCount == 10 && !cell.toString().equals("EFT Draft Notice")) {
                        isCurrentTransactionEFTDraft = false;
                    }
                    if (cell.toString().equals("System Payment / Credit Adjustment")) {
                        isSystemPaymentFound = true;
                    }
                    if (cell.toString().equals("Payment")) {
                        isPaymentFound = true;
                        isEFTSubmissionFound = false;
                        if (isCombinedPaymentFound && isEndorsementOrReinstatementFound) {
                            isSecondPaymentFoundOnCombinedPayment = true;
                        }
                    }
                    if (cell.toString().equals("EFT_SUBMISSION")) {
                        isEFTSubmissionFound = true;
                    }
                    if (isEFTDraftFound && isPaymentFound && colCount == 13) {
                        isPaymentFound = false;
                        isEFTDraftFound = false;
                        isEFTDateCalculated = false;
                        daysForEFTDifference = 0;
                    }
                    if (cell.toString().equals(newDateFormat.format(policyDates.oldEffectiveDate))) {
                        cell.setCellValue(newDateFormat.format(policyDates.newEffectiveDate));
                    } else if (cell.toString().equals(newDateFormat.format(policyDates.oldExpiryDate))) {
                        cell.setCellValue(newDateFormat.format(policyDates.newExpiryDate));
                    } else if (cell.toString().equals(newDateFormat.format(policyDates.oldEffectiveDateNextTerm))) {
                        cell.setCellValue(newDateFormat.format(policyDates.newEffectiveDateNextTerm));
                    } else if (dateValidator.isStringDateValid(cell.toString())) {
                        Date date = new Date();
                        String dateString = "";
                        try {

                            date = newDateFormat.parse(cell.toString());
                            newDate = dateModifier.addDaysToDate(date, virableDateDifference);
                            //system payment
                            if (isEndorsementOrCancellationFound && colCount == 12) {
                                endorsementOrCancellationDate = newDate;
                                isEndorsementOrCancellationFound = false;
                            }
                            if (isSystemPaymentFound) {
                                //case 1, compare the systempayment to expiryDate-12
                                DateTime expiryDateTime = new DateTime(policyDates.newExpiryDate);
                                expiryDateTime = expiryDateTime.plusDays(-12);
                                Date expiryDate = expiryDateTime.toDate();
                                if (endorsementOrCancellationDate.compareTo(expiryDate) < 0) {
                                    //If this is the case, system payment date shoulbd be expiry date -12
                                    newDate = expiryDate;

                                }
                                if (colCount == 12) {
                                    isSystemPaymentFound = false;
                                }
                                if (term == 1) {
                                    systemPaymentDates.add(newDate);
                                } else if (term == 2) {
                                    for (Date systemPaymentDate : systemPaymentDates) {
                                        int days = dateModifier.findDifferenceBetweenDate(newDate, systemPaymentDate);
                                        if (days <= 1 && days >= -1) {
                                            newDate = systemPaymentDate;
                                        }
                                    }
                                }
                            }
                            if (isReminderFound && !EFTInfoTable.isEmpty() && colCount == 11) {
                                for (int i = 0; i < EFTInfoTable.size(); i++) {
                                    for (int j = 0; j < EFTInfoTable.get(i).size(); j++) {
                                        if (EFTInfoTable.get(i).get(j).transactionType == "Reminder") {
                                            if (EFTInfoTable.get(i).get(j).entryDate.equals(newDateFormat.format(newDate)) && i != 0) {
                                                EFTInfoTable.remove(0);
                                                break;
                                            }
                                        }
                                    }
                                }

                                if (EFTInfoTable.get(0).size() == 2) {
                                    EFTInfoTable.remove(0);
                                }

                                for (int i = 0; i < EFTInfoTable.get(0).size(); i++) {
                                    if (EFTInfoTable.get(0).get(i).transactionType == "Reminder") {
                                        System.out.println("--------------------------Entry ----------------------------------");
                                        System.out.println("Reminder Entry date is " + EFTInfoTable.get(0).get(i).entryDate);
                                        //special case for 1122 and 1123
                                        DateTime reminderDateTime = new DateTime(newDateFormat.parse(EFTInfoTable.get(0).get(i).entryDate));
                                        reminderDateTime = reminderDateTime.plusMonths(1);
                                        int dayDifference = dateModifier.findDifferenceBetweenDate(newDate, reminderDateTime.toDate());
                                        if (dayDifference >= -3 && dayDifference <= 3) {
                                            //here we need to add a month to all fields
                                            for (int j = 0; j < EFTInfoTable.get(0).size(); j++) {
                                                DateTime dateTime = new DateTime(newDateFormat.parse(EFTInfoTable.get(0).get(j).entryDate));
                                                dateTime = dateTime.plusMonths(1);
                                                EFTInfoTable.get(0).get(j).entryDate = dateTimeFormatter.print(dateTime);
                                                dateTime = new DateTime(newDateFormat.parse(EFTInfoTable.get(0).get(j).effectiveDate));
                                                dateTime = dateTime.plusMonths(1);
                                                EFTInfoTable.get(0).get(j).effectiveDate = dateTimeFormatter.print(dateTime);
                                            }
                                        }
                                        newDate = newDateFormat.parse(EFTInfoTable.get(0).get(i).entryDate);

                                    }
                                }
                            }
                            if (isReminderFound && !EFTInfoTable.isEmpty() && colCount == 12) {
                                for (int i = 0; i < EFTInfoTable.get(0).size(); i++) {
                                    if (EFTInfoTable.get(0).get(i).transactionType == "Reminder") {
                                        System.out.println("---------------------------Effective ----------------------------------");
                                        System.out.println("Reminder Effective date is " + EFTInfoTable.get(0).get(i).effectiveDate);
                                        newDate = newDateFormat.parse(EFTInfoTable.get(0).get(i).effectiveDate);
                                        EFTInfoTable.get(0).remove(i);
                                        isReminderFound = false;
                                    }
                                }
                            }
                            if (isEFTSubmissionFound && !EFTInfoTable.isEmpty() && colCount == 11) {
                                for (int i = 0; i < EFTInfoTable.get(0).size(); i++) {
                                    if (EFTInfoTable.get(0).get(i).transactionType == "EFT_SUBMISSION") {
                                        System.out.println("--------------------------Entry ----------------------------------");
                                        System.out.println("EFTSubmission date is " + EFTInfoTable.get(0).get(i).entryDate);
                                        newDate = newDateFormat.parse(EFTInfoTable.get(0).get(i).entryDate);
                                    }
                                }
                            }
                            if (isEFTSubmissionFound && !EFTInfoTable.isEmpty() && colCount == 12) {
                                for (int i = 0; i < EFTInfoTable.get(0).size(); i++) {
                                    if (EFTInfoTable.get(0).get(i).transactionType == "EFT_SUBMISSION") {
                                        System.out.println("---------------------------Effective ----------------------------------");
                                        System.out.println("EFTSubmission Effective date is " + EFTInfoTable.get(0).get(i).effectiveDate);
                                        newDate = newDateFormat.parse(EFTInfoTable.get(0).get(i).effectiveDate);
                                        EFTInfoTable.get(0).remove(i);
                                        isEFTSubmissionFound = false;
                                    }

                                }
                            }
                            if (isPaymentFound && !EFTInfoTable.isEmpty() && colCount == 11) {
                                for (int i = 0; i < EFTInfoTable.get(0).size(); i++) {
                                    if (EFTInfoTable.get(0).get(i).transactionType == "EFT_SUBMISSION") {
                                        break;
                                    }
                                    if (EFTInfoTable.get(0).get(i).transactionType == "Payment") {
                                        System.out.println("--------------------------Entry ----------------------------------");
                                        System.out.println("Payment Entry date is " + EFTInfoTable.get(0).get(i).entryDate);
                                        newDate = newDateFormat.parse(EFTInfoTable.get(0).get(i).entryDate);
                                    }
                                }
                            }
                            if (isPaymentFound && !EFTInfoTable.isEmpty() && colCount == 12) {
                                for (int i = 0; i < EFTInfoTable.get(0).size(); i++) {
                                    if (EFTInfoTable.get(0).get(i).transactionType == "EFT_SUBMISSION") {
                                        break;
                                    }
                                    if (EFTInfoTable.get(0).get(i).transactionType == "Payment") {
                                        System.out.println("---------------------------Effective ----------------------------------");
                                        System.out.println("Payment Effective date is " + EFTInfoTable.get(0).get(i).effectiveDate);
                                        newDate = newDateFormat.parse(EFTInfoTable.get(0).get(i).effectiveDate);
                                        EFTInfoTable.get(0).remove(i);
                                        isPaymentFound = false;
                                    }
                                }
                                EFTInfoTable.remove(0);
                            }
                            if (isRenewalFound && isLateTermReinstatementFound && colCount == 11) {
                                renewalDate = newDate;
                                isRenewalFound = false;
                            }
                            if (isImmediatePaymentFound && colCount == 11 && isReinstatementFound) {
                                immediatePaymentDate = newDate;
                            }
                            if (isSecondPaymentFoundOnCombinedPayment && isCombinedPaymentFound && isEndorsementOrReinstatementFound) {
                                newDate = dateModifier.isDateACombinedPaymentDate(newDate);
                            }
                            if (isEFTDraftFound) {
                                if (isLateTermReinstatementFound && colCount == 11 && isEFTDateCalculated == false && term == 3) {
                                    int days = dateModifier.findDifferenceBetweenDate(newDate, renewalDate);
                                    if (days != -1 || days != 1) {
                                        if (days >= -3 && days <= 3) {
                                            daysForEFTDifference = dateModifier.findDifferenceBetweenDate(newDate, dateModifier.addDaysToDate(renewalDate, 1));
                                            newDate = dateModifier.addDaysToDate(renewalDate, 0);
                                            isEFTDateCalculated = true;
                                        }
                                    }
                                }
                                if (isEFTDateCalculated == false) {
                                    daysForEFTDifference = dateModifier.daysDifferentToAccetableEFT(newDate);
                                    isEFTDateCalculated = true;
                                }
                                if (isImmediatePaymentFound && isReinstatementFound) {
                                    if (colCount == 11) {
                                        //Entry date
                                        int days = dateModifier.findDifferenceBetweenDate(newDate, immediatePaymentDate);
                                        if (days <= 1 && days >= -1) {
                                            //here EFT should be a day after the immediate payment
                                            newDate = new DateTime(immediatePaymentDate).plusDays(1).toDate();
                                        }
                                    } else if (colCount == 12 && isCurrentTransactionEFTDraft) {
                                        newDate = new DateTime(immediatePaymentDate).plusDays(12).toDate();
                                        EFTInfoList = calculateInfoGivenEFTDraftDate(newDate, paymentMethod);
                                        EFTInfoTable.add(EFTInfoList);
                                        isCurrentTransactionEFTDraft = false;
                                        daysForEFTDifference = 0;
                                        isImmediatePaymentFound = false;
                                        isReinstatementFound = false;
                                    }
                                } else {
                                    if (daysForEFTDifference >= -3 && daysForEFTDifference <= 3) {
                                        newDate = dateModifier.addDaysToDate(newDate, daysForEFTDifference);
                                    }
                                    if (colCount == 12 && isCurrentTransactionEFTDraft) {
                                        //Subtract the number of days what was added due to Month-end rule from last month.
                                        //EFT Effective Date found
                                        EFTInfoList = calculateInfoGivenEFTDraftDate(newDate, paymentMethod);
                                        EFTInfoTable.add(EFTInfoList);
                                        isCurrentTransactionEFTDraft = false;
                                        daysForEFTDifference = 0;
                                    }
                                }
                            }
                            dateString = newDateFormat.format(newDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cell.setCellValue(dateString);
                    }
                    System.out.print(cell.toString() + "/");
                    colCount++;
                }
                rowCount++;
                colCount = 0;
                System.out.println();
            }

            FileOutputStream fos = new FileOutputStream(excel);
            workbook.write(fos);
            fis.close();
            fos.close();
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //loop through the modified excel again and find month end
    public void checkForMonthEnd(String filePath) {
        boolean isMonthEndForSecondTerm = false;
        boolean EFTSubmissionFound = false;
        boolean isMonthEnd = false;
        int dayBumped = 0;
        int rowCount = 0;
        int colCount = 0;
        boolean isEFTDraftNoticeFound = false;
        boolean isPaymentFound = false;
        int EFTDraftNoticeDateCount = 2;
        int reminderNoticeDateCount = 2;
        int EFTSubmissionDateCount = 2;
        int paymentDateCount = 2;
        int refundDateCount = 2;
        String term = "";
        try {
            FileInputStream fis = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIt = sheet.iterator();
            while (rowIt.hasNext()) {
                Row row = rowIt.next();
                Iterator<Cell> cellIt = row.cellIterator();
                while (cellIt.hasNext()) {
                    Cell cell = cellIt.next();
                    if (cell.toString().equals("First")) {
                        term = "First";
                        isMonthEndForSecondTerm = false;
                    } else if (cell.toString().startsWith("Second")) {
                        term = "Second";
                    }
                    if (cell.toString().equals("EFT Draft Notice")) {
                        System.out.print(cell.toString() + "(" + rowCount + "," + colCount + ")");
                        EFTDraftNoticeDateCount = 0;
                        dayBumped = 0;
                        isEFTDraftNoticeFound = true;
                    }
                    if (isEFTDraftNoticeFound && isMonthEnd) {
                        if (cell.toString().equals("Reminder Notice")) {
                            System.out.println(cell.toString() + "(" + rowCount + "," + colCount + ")");
                            reminderNoticeDateCount = 0;
                        } else if (cell.toString().equals("EFT_SUBMISSION")) {
                            System.out.println(cell.toString() + "(" + rowCount + "," + colCount + ")");
                            EFTSubmissionDateCount = 0;
                            EFTSubmissionFound = true;
                        } else if (cell.toString().equals("Payment") && EFTSubmissionFound) {
                            System.out.println(cell.toString() + "(" + rowCount + "," + colCount + ")");
                            paymentDateCount = 0;
                            System.out.println("One set is done");
                            isEFTDraftNoticeFound = false;
                            isMonthEnd = false;
                            EFTSubmissionFound = false;
                            isPaymentFound = true;
                        }
                    }
                    if (isMonthEndForSecondTerm && term.equals("Second") && cell.toString().equals("Refund") && isPaymentFound) {
                        System.out.println("Refund");
                        System.out.println(cell.toString() + "(" + rowCount + "," + colCount + ")");
                        refundDateCount = 0;
                        isPaymentFound = false;
                    }
                    if (dateValidator.isStringDateValid(cell.toString())) {
                        if (EFTDraftNoticeDateCount < 2) {
                            System.out.println(cell.toString());
                            if (EFTDraftNoticeDateCount == 1) {
                                //here is we found eft effective date
                                if (dateValidator.isMonthEnd(cell.toString())) {
                                    System.out.println("Month end encountered");
                                    String updatedDateString = dateModifier.bumpDateToFirstDayNextMonth(cell.toString());
                                    dayBumped = dateValidator.daysBetweenDates(updatedDateString, cell.toString());
                                    cell.setCellValue(updatedDateString);
                                    isMonthEnd = true;
                                }
                            }
                            EFTDraftNoticeDateCount++;
                        } else if (reminderNoticeDateCount < 2) {
                            cell.setCellValue(performDateAdditionToMonthEnd(cell.toString(), dayBumped));
                            reminderNoticeDateCount++;
                        } else if (EFTSubmissionDateCount < 2) {
                            cell.setCellValue(performDateAdditionToMonthEnd(cell.toString(), dayBumped));
                            EFTSubmissionDateCount++;
                        } else if (paymentDateCount < 2) {
                            cell.setCellValue(performDateAdditionToMonthEnd(cell.toString(), dayBumped));
                            paymentDateCount++;
                        } else if (refundDateCount < 2) {
                            cell.setCellValue(performDateAdditionToMonthEnd(cell.toString(), dayBumped));
                            refundDateCount++;
                        }
                    }
                    colCount++;
                }
                System.out.println();
                rowCount++;
                colCount = 0;
            }

            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            workbook.close();
            fis.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //update a date  that contain month-end
    private String performDateAdditionToMonthEnd(String stringFromCell, int dayBumped) {
        System.out.println("Month end encountered");
        String updatedDateString = dateModifier.addDaystoDateString(stringFromCell, dayBumped);
        System.out.println(updatedDateString);
        System.out.println("------------------------------");
        dayBumped = dateValidator.daysBetweenDates(updatedDateString, stringFromCell);
        System.out.println(stringFromCell + " : " + updatedDateString + " = " + dayBumped);
        System.out.println("------------------------------");
        return updatedDateString;
    }

    //give an EFT draft date, calculate Reminder, EFT_SUBMISSIOn and Payment
    private ArrayList<Transaction> calculateInfoGivenEFTDraftDate(Date EFTDraftEffectiveDate, int paymentMethod) {

        if (dateValidator.isMonthEnd(newDateFormat.format(EFTDraftEffectiveDate))) {
            try {
                String updatedDateString = dateModifier.bumpDateToFirstDayNextMonth(newDateFormat.format(EFTDraftEffectiveDate));
                EFTDraftEffectiveDate = newDateFormat.parse(updatedDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Transaction> EFTInfoList = new ArrayList<>();
        //this is EFT Recurring case
        if (paymentMethod == 0) {
            //Reminder is 5 days before the EFTDraft Effective Date
            //EFT_SUBMISSION and Payment is 1 day before effective date
            DateTime dateTime = new DateTime(EFTDraftEffectiveDate);
            Transaction reminder = new Transaction();
            reminder.transactionType = "Reminder";
            reminder.entryDate = dateTimeFormatter.print(dateTime.plusDays(-5));
            reminder.effectiveDate = dateTimeFormatter.print(dateTime.plusDays(-5));
            Transaction EFT_SUBMISSION = new Transaction();
            EFT_SUBMISSION.transactionType = "EFT_SUBMISSION";
            EFT_SUBMISSION.entryDate = dateTimeFormatter.print(dateTime.plusDays(-1));
            EFT_SUBMISSION.effectiveDate = dateTimeFormatter.print(dateTime.plusDays(-1));
            Transaction payment = new Transaction();
            payment.transactionType = "Payment";
            payment.entryDate = dateTimeFormatter.print(dateTime.plusDays(-1));
            payment.effectiveDate = dateTimeFormatter.print(dateTime.plusDays(-1));
            EFTInfoList.add(reminder);
            EFTInfoList.add(EFT_SUBMISSION);
            EFTInfoList.add(payment);
        }
        //this is the credit card recurring case
        else if (paymentMethod == 1) {
            //Reminder is 3 days before the EFTDraft Effective date
            //EFT_SUBMISSION and Payment is same as effective date
            DateTime dateTime = new DateTime(EFTDraftEffectiveDate);
            Transaction reminder = new Transaction();
            reminder.transactionType = "Reminder";
            reminder.entryDate = dateTimeFormatter.print(dateTime.plusDays(-3));
            reminder.effectiveDate = dateTimeFormatter.print(dateTime.plusDays(-3));
            Transaction EFT_SUBMISSION = new Transaction();
            EFT_SUBMISSION.transactionType = "EFT_SUBMISSION";
            EFT_SUBMISSION.effectiveDate = newDateFormat.format(EFTDraftEffectiveDate);
            EFT_SUBMISSION.entryDate = newDateFormat.format(EFTDraftEffectiveDate);
            Transaction payment = new Transaction();
            payment.transactionType = "Payment";
            payment.effectiveDate = newDateFormat.format(EFTDraftEffectiveDate);
            payment.entryDate = newDateFormat.format(EFTDraftEffectiveDate);
            EFTInfoList.add(reminder);
            EFTInfoList.add(EFT_SUBMISSION);
            EFTInfoList.add(payment);
        }
        System.out.println("---------------------------Here is the EFT info ----------------------------------");
        for (Transaction transaction : EFTInfoList) {
            System.out.println("Type : " + transaction.transactionType + " Effective Date : " + transaction.effectiveDate
                    + " Entry Date : " + transaction.entryDate);
        }
        System.out.println("----------------------------------DONE -------------------------------------");
        return EFTInfoList;
    }
}
