package com.shelter.majesco.stgbilling.test.fileComparators;

public class AccountsPayable {
    public String inputFileName;
    public static String[] columns ={
    "AP_DISB_TYPE",
    "AP_ACCT_YYYYMM",
    "AP_TAX_ID",
    "AP_INVOICE_NO",
    "AP_BROKER_NO",
    "AP_VENDER_NO",
    "AP_ACCOUNT_NO",
    "AP_POLICY_NO",
    "AP_POLICY_EFF_DT",
    "AP_INSURED_NAME",
    "AP_PAYEE_TYPE",
    "AP_PAYEE_NAME",
    "AP_PAYEE_ADDR_1",
    "AP_PAYEE_ADDR_2",
    "AP_PAYEE_CITY",
    "AP_PAYEE_STATE",
    "AP_PAYEE_ZIP_CODE",
    "AP_PAYEE_COUNTRY_CD",
    "AP_AMOUNT",
    "AP_PAYMENT_DATE",
    "AP_BANK_CODE",
    "AP_CHECK_NO",
    "AP_ORIG_CHECK_NO",
    "AP_REFUND_METHOD",
    "AP_REFUND_REASON",
    "AP_REFUND_DESC",
    "AP_CLIENT_ID",
    "AP_USER_ID",
    "AP_SOURCE_POLICY_ID",
    "AP_MAILING_NAME"
    };

    public String AP_INVOICE_NO;
    public String AP_DISB_TYPE;
    public String AP_ACCT_YYYYMM;
    public String AP_TAX_ID;
    public String AP_BROKER_NO;
    public String AP_VENDER_NO;
    public String AP_ACCOUNT_NO;
    public String AP_POLICY_NO;
    public String AP_POLICY_EFF_DT;
    public String AP_INSURED_NAME;
    public String AP_PAYEE_TYPE;
    public String AP_PAYEE_NAME;
    public String AP_PAYEE_ADDR_1;
    public String AP_PAYEE_ADDR_2;
    public String AP_PAYEE_CITY;
    public String AP_PAYEE_STATE;
    public String AP_PAYEE_ZIP_CODE;
    public String AP_PAYEE_COUNTRY_CD;
    public String AP_AMOUNT;
    public String AP_PAYMENT_DATE;
    public String AP_BANK_CODE;
    public String AP_CHECK_NO;
    public String AP_ORIG_CHECK_NO;
    public String AP_REFUND_METHOD;
    public String AP_REFUND_REASON;
    public String AP_REFUND_DESC;
    public String AP_CLIENT_ID;
    public String AP_USER_ID;
    public String AP_SOURCE_POLICY_ID;
    public String AP_MAILING_NAME;

    public String AP_SUPRESS_CHECK_YN;
    public void print(){
        System.out.println(AP_INVOICE_NO + " " + AP_ACCT_YYYYMM + AP_PAYEE_NAME + " " + AP_POLICY_NO);
        System.out.println(getMaskedPolicyNumber());
    }

    public boolean checkAPByPolicyNumber(String policyPrefix){
        if (AP_POLICY_NO.startsWith(policyPrefix)){
            return true;
        }
        else{
            return false;
        }
    }

    public int getMaskedPolicyNumber(){
        String policy = AP_POLICY_NO.substring(5);
        return Integer.parseInt(policy);
    }
}

