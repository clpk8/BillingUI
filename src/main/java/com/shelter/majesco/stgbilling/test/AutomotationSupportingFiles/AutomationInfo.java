package com.shelter.majesco.stgbilling.test.AutomotationSupportingFiles;

//Update username, passoword etc.
public class AutomationInfo {
    private String userName = "user2";
    public String getUserName(){
        return this.userName;
    }
    private String passWord = "icd123";
    public String getPassWord(){
        return this.passWord;
    }
    private String URL = "https://mbsu.shelterinsurance.com:8643/MajescoBilling";
    public String getURL(){
        return this.URL;
    }
    private String driverLocation = "//columbia/DFSR1/QDoc/Payment Services/Code/chromedriver/chromedriver.exe";
    public String getDriverLocation(){
        return this.driverLocation;
    }
    private String policy = "812311100";
    public String getPolicy(){
        return this.policy;
    }
    public void setPolicy(String newPolcy){
        this.policy = newPolcy;
    }
}
