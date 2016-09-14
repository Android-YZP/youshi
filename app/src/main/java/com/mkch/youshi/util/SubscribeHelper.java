package com.mkch.youshi.util;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Presence;

/**
 * Created by SunnyJiang on 2016/8/25.
 */
public class SubscribeHelper {
    /**
     * 请求通过
     * @param connection
     */
    public static void subscribed(AbstractXMPPConnection connection){
        Presence _p = new Presence(Presence.Type.subscribed);
        try {
            connection.sendStanza(_p);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求拒绝
     * @param connection
     */
    public static void unsubscribe(AbstractXMPPConnection connection){
        Presence _p = new Presence(Presence.Type.unsubscribe);
        try {
            connection.sendStanza(_p);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
