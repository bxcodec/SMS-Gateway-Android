package com.okgroup.sms_gateway;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.okgroup.sms_gateway.model.SMSClass;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private EditText msg ;
    private EditText phone;
    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";
    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;
    private TextView hello;
    private SMSClass  smsToSend;

    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hello = (TextView) findViewById(R.id.hello);
        SmsApplication app = new SmsApplication();

//        mSocket = app.getMsocket();
        mSocket = app.getPhoneSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
         msg = (EditText) findViewById(R.id.message);
         phone = (EditText) findViewById(R.id.phone_number);
        Button btn = (Button) findViewById(R.id.sendBtn);
        btn.setEnabled(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pesan = msg.getText().toString();
                String nomor = phone.getText().toString();

                sendSMS(nomor,pesan);
            }
        });

        mSocket.on("send_sms_to_phone",onSendSmsToPhone);
        mSocket.connect();


    }


    private void sendSMS(String phone_number , String message){

        if (phone_number.isEmpty()){
            Toast.makeText(this,"Phone number invalid ",Toast.LENGTH_LONG);
        }else{
             SmsManager  sms = SmsManager.getDefault();
            PendingIntent sentIntent = PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);
            sms.sendTextMessage(phone_number,null,message,sentIntent,deliveredIntent);
        }

    }


    private void registerAllReciver(){
        sentStatusReceiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = "Unknown Error";
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        s="Message sent Succesfully";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s= "Generic Failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s="No Service Available";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s= "Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s="Radio is off";
                        break;
                }
                hello.setText(s);
            }
        };

        deliveredStatusReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s  = "Message not delivered";
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        s = "Message Delivered Succesfully to " + smsToSend.getSMS().getPhone_number() ;
                        smsToSend.getSMS().setSent(true);
                        mSocket.emit("success_delivered_sms",smsToSend);
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
                hello.setText(s);
                msg.setText("");
            }
        };

        registerReceiver(sentStatusReceiver,new IntentFilter(SENT));
        registerReceiver(deliveredStatusReceiver,new IntentFilter(DELIVERED));

    }

     private void unregisterAllReceiver(){
         unregisterReceiver(sentStatusReceiver);
         unregisterReceiver(deliveredStatusReceiver);
     }

    @Override
    protected void onResume() {
        super.onResume();
        registerAllReciver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterAllReceiver();
    }

    private  Emitter.Listener onSendSmsToPhone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

                            Log.i("SOCKETTAMVAN","OnSendSMSToPHone");
                            Gson gson = new Gson();
                            JSONObject jsonObject= (JSONObject) args[0];
                            smsToSend = gson.fromJson(jsonObject.toString(), SMSClass.class);
                            Log.i("SOCKETTAMVAN",smsToSend.toString());
                            sendSMS(smsToSend.getSMS().getPhone_number() ,smsToSend.getSMS().getMessage());


        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("SOCKETTAMVAN","TIMEOUT MAMMEN");
            Object [] res = args.clone();
            Log.i("SOCKETTAMVAN",res[0].toString());
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("SOCKETTAMVAN","DISCONNECTED MAMMEN");
        }
    }    ;
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            Log.i("SOCKETTAMVAN","CONNECTED MAMMEN");
        }
    };


}
