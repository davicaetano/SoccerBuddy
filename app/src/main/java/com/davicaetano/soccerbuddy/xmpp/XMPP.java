package com.davicaetano.soccerbuddy.xmpp;

import android.os.AsyncTask;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by davicaetano on 1/21/16.
 */
public class XMPP {
    private String serverAddress;
    private XMPPTCPConnection connection;
    private String loginUser;
    private String passwordUser;

    public XMPP(String serverAddress, String user, String password){
        this.serverAddress = serverAddress;
        this.loginUser = loginUser;
        this.passwordUser = passwordUser;
    }

    public void connect(){
        final AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... arg0){
                boolean isConnected = false;

                XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
                config.setUsernameAndPassword("davicaetano@gmail.com", "abc123");
                config.setServiceName("http://");
                config.setPort(5330);
                config.setDebuggerEnabled(true);



                connection = new XMPPTCPConnection(config.build());
                connection.setPacketReplyTimeout(10000);

                try {
                    connection.connect();
                    connection.login();
                    return true;
                } catch (SmackException | IOException | XMPPException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
        connectionThread.execute();
    }

}
