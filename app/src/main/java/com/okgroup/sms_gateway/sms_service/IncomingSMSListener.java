package com.okgroup.sms_gateway.sms_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by ww3 on 21/03/17.
 */

public class IncomingSMSListener  extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){
            Bundle bundle = intent.getExtras();
            SmsMessage [] smsMessages = null;
            String msg_from;
            if (bundle!= null){
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    smsMessages = new SmsMessage[pdus.length];
                    for (int i =0 ; i < pdus.length ; i++){
                        smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = smsMessages[i].getOriginatingAddress();
                        String messageBody = smsMessages[i].getMessageBody();

                        Log.i("SMSTAMVAN",msg_from);
                        Log.i("SMSTAMVAN",messageBody);

                    }
                }catch (Exception e){

                }
            }
        }
    }
}
