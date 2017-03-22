package com.okgroup.sms_gateway.model;

/**
 * Created by ww3 on 22/03/17.
 */

public class SMSClass {
    private SMSBody SMS;

    public SMSBody getSMS() {
        return SMS;
    }

    public void setSMS(SMSBody SMS) {
        this.SMS = SMS;
    }

    @Override
    public String toString() {
        return "SMSClass{" +
                "SMS=" + SMS.toString() +
                '}';
    }
}
