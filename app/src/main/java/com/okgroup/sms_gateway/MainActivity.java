package com.okgroup.sms_gateway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {


    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmsApplication app = new SmsApplication();

//        mSocket = app.getMsocket();
        mSocket = app.getPhoneSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        Button btn = (Button) findViewById(R.id.sendBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("incoming_sms","hello Mesasge");
            }
        });
        mSocket.on("send_sms_to_phone",onSendSmsToPhone);
        mSocket.connect();
    }

    private  Emitter.Listener onSendSmsToPhone = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.i("SOCKETTAMVAN","OnSendSMSToPHone");
                            JSONObject object = (JSONObject) args[0];
                            Log.i("SOCKETTAMVAN",object.toString());
                        }
                    });

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
