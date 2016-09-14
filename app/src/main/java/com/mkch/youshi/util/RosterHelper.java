package com.mkch.youshi.util;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;

/**
 * Created by SunnyJiang on 2016/8/25.
 */
public class RosterHelper {
    private static RosterHelper instance;
    private Roster roster;

    private RosterHelper(){

    }

    public static RosterHelper getInstance(AbstractXMPPConnection connection){
        instance = new RosterHelper();
        instance.roster = Roster.getInstanceFor(connection);
        return instance;

    }

    /**
     * 添加好友
     * @param jid
     * @param nickname
     * @param group
     */
    public void addEntry(String jid,String nickname,String group){
        try {
            roster.createEntry(jid,nickname,new String[]{group});
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
