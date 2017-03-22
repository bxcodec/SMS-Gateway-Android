package com.okgroup.sms_gateway.model;

/**
 * Created by ww3 on 22/03/17.
 */

public class SMSBody {
    String message;
    String phone_number;
    boolean sent;

    public SMSBody(String message, String phone_number, boolean sent) {
        this.message = message;
        this.phone_number = phone_number;
        this.sent = sent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;

    }

    @Override
    public String toString() {
        return "SMSBody{" +
                "message='" + message + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", sent=" + sent +
                '}';
    }
}
