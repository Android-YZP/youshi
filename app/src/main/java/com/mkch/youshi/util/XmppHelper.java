package com.mkch.youshi.util;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

/**
 * Created by SunnyJiang on 2016/8/22.
 */
public class XmppHelper {
    private static XMPPTCPConnection connection;
    private static XMPPTCPConnectionConfiguration.Builder builder;

    /**
     * 获取连接对象
     * @return
     */
    public static XMPPTCPConnection getConnection(){
        if (builder==null){
            String server="192.168.3.9";
//            String server="192.168.1.100";
            int port=5222;
            builder = XMPPTCPConnectionConfiguration.builder();
            builder.setServiceName("yoshi.maikejia.com");
//            builder.setServiceName("pc201607142319");
            builder.setHost(server);
            builder.setPort(port);
            builder.setCompressionEnabled(false);
            builder.setDebuggerEnabled(true);
            builder.setSendPresence(true);
            //SASL
//            SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
//            SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");

            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        }
        connection = getInstance();
        return connection;
    }

    public static XMPPTCPConnection getInstance(){
        if (connection==null){
            connection = new XMPPTCPConnection(builder.build());
        }
        return connection;
    }

    public static boolean connectAndLogin(XMPPTCPConnection connection,String _login_user,String _login_pwd){
        try{
            if (!connection.isConnected()) {
                connection.connect();
            }
            connection.login(_login_user, _login_pwd);
            return true;
        }catch (SmackException.AlreadyLoggedInException e){
            Log.d("jlj","---------------------connectAndLogin logined");
            e.printStackTrace();
            return true;
        }catch (Exception e){
            Log.d("jlj","---------------------connectAndLogin error");
            e.printStackTrace();
            return false;
        }


    }
}
