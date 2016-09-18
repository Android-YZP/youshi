package com.mkch.youshi.util;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

/**
 * Created by SunnyJiang on 2016/8/25.
 */
public class RosterHelper {
    private static RosterHelper instance;
    private Roster roster;

    private RosterHelper(){

    }

    public static RosterHelper getInstance(AbstractXMPPConnection connection){
        if (instance==null){
            instance = new RosterHelper();

        }
        instance.roster = Roster.getInstanceFor(connection);
        instance.roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);//自动是接受所有好友请求
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
            //如果有该好友的信息，先删除
            RosterEntry _entry = roster.getEntry(jid);
            if (_entry!=null){
                roster.removeEntry(_entry);
            }
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


    /**
     * 删除好友
     * @param jid
     * @return
     */
    public boolean removeEntry(String jid){
        try {
            RosterEntry _entry = roster.getEntry(jid);
            if (_entry!=null){
                roster.removeEntry(_entry);
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
