package com.okgroup.sms_gateway;

import android.app.Application;

import com.okgroup.sms_gateway.socket_service.Constants;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by ww3 on 21/03/17.
 */

public class SmsApplication extends Application {

    private Socket msocket;
    private Socket phoneSocket;
    {
        try {

            IO.Options opt= new IO.Options();
            opt.path="/socket.io";

            msocket = IO.socket(Constants.URL_SOCKET);


//            msocket = msocket.io().socket("/phone");
//            msocket.connect();
            phoneSocket = new Socket(msocket.io(),"/phone");
//            phoneSocket.connect();
        }catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public Socket getMsocket(){
        return msocket;
    }
    public Socket getPhoneSocket(){
        return  phoneSocket;
    }
}
