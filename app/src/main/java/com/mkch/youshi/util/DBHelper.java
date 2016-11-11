package com.mkch.youshi.util;

import android.text.TextUtils;
import android.util.Log;

import com.mkch.youshi.bean.CloudFileBean;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.CollectFile;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.model.Schjoiner;
import com.mkch.youshi.model.Schreport;
import com.mkch.youshi.model.Schtime;
import com.mkch.youshi.model.YoupanFile;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by SunnyJiang on 2016/9/13.
 */
public class DBHelper {


    private static DbManager dbManager = null;

    public static DbManager getDbManager() {
        if (dbManager == null) {
            Log.d("jlj", "----------------dbManager new ");
            DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
//            .setAllowTransaction(true)
                    .setDbDir(new File("/mnt/sdcard/"))
                    .setDbName("yoshi.db")
                    .setDbOpenListener(new DbManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(DbManager db) {
                            db.getDatabase().enableWriteAheadLogging();//开启数据库多线程
                        }
                    })
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            Log.d("jlj", "onUpgrade<<" + "oldVersion = " + oldVersion + ",newVersion =" + newVersion);
                            if (newVersion > oldVersion) {
                                try {
                                    db.dropDb();
                                } catch (DbException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    })
                    .setDbVersion(76)
                    .setTableCreateListener(new DbManager.TableCreateListener() {
                        @Override
                        public void onTableCreated(DbManager db, TableEntity<?> table) {
                            Log.d("jlj", "onTableCreated<<" + table.getName());
                        }
                    });
            dbManager = x.getDb(daoConfig);
            return dbManager;
        } else {
            return dbManager;
        }
    }



    /**
     * 优盘文件
     * 用Type查找一个文件集合
     */
    public static ArrayList<YoupanFile> findYoupanFile(int type) {
        DbManager mDbManager = DBHelper.getDbManager();
        ArrayList<YoupanFile> youpanFiles = null;
        try {
            youpanFiles = (ArrayList<YoupanFile>) mDbManager.selector(YoupanFile.class).where("type", "=",
                    type).findAll();
            return youpanFiles;
        } catch (DbException e) {
            e.printStackTrace();
            return youpanFiles;
        }
    }
    /**
     * 收藏文件
     * 用Type查找一个文件集合
     */
    public static ArrayList<CollectFile> findCollectFile(int type) {
        DbManager mDbManager = DBHelper.getDbManager();
        ArrayList<CollectFile> collectFiles = null;
        try {
            collectFiles = (ArrayList<CollectFile>) mDbManager.selector(CollectFile.class).where("type", "=",
                    type).findAll();
            return collectFiles;
        } catch (DbException e) {
            e.printStackTrace();
            return collectFiles;
        }
    }

    /**
     * 用日程id查找该日程的报送人
     *
     * @param sid
     */
    public static ArrayList<Schreport> findRepPer(int sid) {
        DbManager mDbManager = DBHelper.getDbManager();
        try {
            ArrayList<Schreport> schreports = (ArrayList<Schreport>) mDbManager.selector(Schreport.class).where("sid", "=",
                    sid).findAll();
            return schreports;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用日程id删除该日程的报送人
     *
     * @param sid
     */
    public static void DeleteRepPer(int sid) {
        try {
            DbManager mDbManager = DBHelper.getDbManager();
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("sid", "=", sid + "");
            mDbManager.delete(Schreport.class, whereBuilder);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用日程id删除该日程的参与人
     *
     * @param sid
     */
    public static void DeleteJoinPer(int sid) {
        try {
            DbManager mDbManager = DBHelper.getDbManager();
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("sid", "=", sid + "");
            mDbManager.delete(Schjoiner.class, whereBuilder);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用日程id删除该日程的时间段
     *
     * @param sid
     */
    public static void DeleteSchTime(int sid) {
        try {
            DbManager mDbManager = DBHelper.getDbManager();
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("sid", "=", sid + "");
            mDbManager.delete(Schtime.class, whereBuilder);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用日程id查找该日程的时间段
     *
     * @param sid
     */
    public static ArrayList<Schtime> findSchTime(int sid) {
        DbManager mDbManager = DBHelper.getDbManager();
        try {
            ArrayList<Schtime> schreports = (ArrayList<Schtime>) mDbManager.selector(Schtime.class).where("sid", "=",
                    sid).findAll();
            return schreports;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用报送人id查找该报送人的姓名
     * 有设置昵称返回昵称
     * 没有设置昵称返回friendID
     */
    public static String findFriName(String friendId) {
        DbManager mDbManager = DBHelper.getDbManager();
        ArrayList<Friend> friends;
        try {
            friends = (ArrayList<Friend>) mDbManager.selector(Friend.class).where("friendid", "=",
                    friendId).findAll();
            Log.d("haha5", friends.size() + "");
            if (!TextUtils.isEmpty(friends.get(0).getNickname() + "")) {
                return friends.get(0).getNickname() + " ";
            } else {
                return friends.get(0).getPhone() + " ";
            }
        } catch (DbException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 用日程id查找该日程的参与人
     */
    public static ArrayList<Schjoiner> findJoinPer(int sid) {
        DbManager mDbManager = DBHelper.getDbManager();
        try {
            ArrayList<Schjoiner> schjoiners = (ArrayList<Schjoiner>) mDbManager.selector(Schjoiner.class).where("sid", "=",
                    sid).findAll();
            return schjoiners;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用id查找一个日程
     */
    public static ArrayList<Schedule> findSch(String SchId) {
        DbManager mDbManager = DBHelper.getDbManager();
        ArrayList<Schedule> Scheduls = null;
        try {
            Scheduls = (ArrayList<Schedule>) mDbManager.selector(Schedule.class).where("id", "=",
                    SchId).findAll();
            return Scheduls;
        } catch (DbException e) {
            e.printStackTrace();
            return Scheduls;
        }
    }

    /**
     * 用id查找一个日程
     */
    public static ArrayList<Schedule> findHabitSch() {
        DbManager mDbManager = DBHelper.getDbManager();
        ArrayList<Schedule> Scheduls = null;
        try {
            Scheduls = (ArrayList<Schedule>) mDbManager.selector(Schedule.class).where("type", "=",
                    2).findAll();
            return Scheduls;
        } catch (DbException e) {
            e.printStackTrace();
            return Scheduls;
        }
    }

    /**
     * 用Fileid查找一个文件
     */
    public static ArrayList<YoupanFile> findFile(String fileID) {
        DbManager mDbManager = DBHelper.getDbManager();
        ArrayList<YoupanFile> Scheduls = null;
        try {
            Scheduls = (ArrayList<YoupanFile>) mDbManager.selector(YoupanFile.class).where("file_id", "=",
                    fileID).findAll();
            return Scheduls;
        } catch (DbException e) {
            e.printStackTrace();
            return Scheduls;
        }
    }

    /**
     * 添加一个File文件
     */
    public static void saveFile(CloudFileBean.DatasBean file) {
        try {
            DbManager mDbManager = DBHelper.getDbManager();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = sdf.format(new java.util.Date());
            YoupanFile youpanFile = new YoupanFile();
            youpanFile.setCreate_time(date);
            youpanFile.setLocal_address("");
            youpanFile.setServer_address(CommonConstants.FILE_ROOT_ADDRESS + file.getUrl());
            youpanFile.setName(file.getFileName());
            youpanFile.setSuf(file.getFileSuf());
            youpanFile.setFile_id(file.getFileID() + "");
            youpanFile.setType(file.getType());//文件类型（1：文档，2：相册，3：视频，4：音频，5：其他）
            mDbManager.saveOrUpdate(youpanFile);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加一个收藏File文件
     */
    public static void saveCollectFile(YoupanFile youpanFile,String localPath) {
        try {
            DbManager mDbManager = DBHelper.getDbManager();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = sdf.format(new java.util.Date());
            CollectFile collectFile = new CollectFile();
            collectFile.setCreate_time(date);
            collectFile.setLocal_address(localPath);
            collectFile.setName(youpanFile.getName());
            collectFile.setSuf(youpanFile.getSuf());
            collectFile.setFile_id(youpanFile.getFile_id());
            collectFile.setType(youpanFile.getType());//文件类型（1：文档，2：相册，3：视频，4：音频，5：其他）
            mDbManager.saveOrUpdate(collectFile);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用FileID删除该文件
     */
    public static void deleteFile(ArrayList<YoupanFile> youpanFiles) {
        for (int i = 0; i < youpanFiles.size(); i++) {
            try {
                DbManager mDbManager = DBHelper.getDbManager();
                WhereBuilder whereBuilder = WhereBuilder.b();
                whereBuilder.and("file_id", "=", youpanFiles.get(i).getFile_id() + "");
                mDbManager.delete(YoupanFile.class, whereBuilder);
                //本地有文件就删除本地文件
                File file = new File(youpanFiles.get(i).getLocal_address());
                if (file.exists())
                    file.delete();
                UIUtils.LogUtils(youpanFiles.get(i).getLocal_address() + file.exists() + "");
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
//    /**
//     * 用FileID删除一个收藏文件文件
//     */
//    public static void deleteCollectFile(CollectFile collectFiles) {
//
//            try {
//                DbManager mDbManager = DBHelper.getDbManager();
//                WhereBuilder whereBuilder = WhereBuilder.b();
//                whereBuilder.and("file_id", "=", collectFiles.getFile_id() + "");
//                mDbManager.delete(YoupanFile.class, whereBuilder);
//                //本地有文件就删除本地文件
//                File file = new File(collectFiles.getLocal_address());
//                if (file.exists())
//                    file.delete();
//                UIUtils.LogUtils(collectFiles.getLocal_address() + file.exists() + "");
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
//
//    }


    /**
     * 用FileID删除该文件
     */
    public static void deleteCollectFile(CollectFile collectFiles) {

            try {
                DbManager mDbManager = DBHelper.getDbManager();
                WhereBuilder whereBuilder = WhereBuilder.b();
                whereBuilder.and("file_id", "=", collectFiles.getFile_id() + "");
                mDbManager.delete(CollectFile.class, whereBuilder);
                //本地有文件就删除本地文件
                File file = new File(collectFiles.getLocal_address());
                if (file.exists())
                    file.delete();
                UIUtils.LogUtils(collectFiles.getLocal_address() + file.exists() + "");
            } catch (DbException e) {
                e.printStackTrace();
            }

    }





}
