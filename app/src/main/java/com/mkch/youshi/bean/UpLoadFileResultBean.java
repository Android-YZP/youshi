package com.mkch.youshi.bean;

/**
 * 上传文件返回值的解析bean
 * Created by Smith on 2016/10/27.
 */

public class UpLoadFileResultBean {


    /**
     * Success : true
     * NeedVerifyCode : false
     * Message :
     * ErrorCode :
     * Datas : {"FileName":"ic_launcher.png","FileSuf":"png","Url":"/CloudDisk/2016-10-27/1b50f665-34a7-4821-a41c-1ae893b6bfcb.png","Type":2,"Size":"0.02M","FileID":0}
     * Total : 0
     */

    private boolean Success;
    private boolean NeedVerifyCode;
    private String Message;
    private String ErrorCode;
    /**
     * FileName : ic_launcher.png
     * FileSuf : png
     * Url : /CloudDisk/2016-10-27/1b50f665-34a7-4821-a41c-1ae893b6bfcb.png
     * Type : 2
     * Size : 0.02M
     * FileID : 0
     */

    private DatasBean Datas;
    private int Total;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public boolean isNeedVerifyCode() {
        return NeedVerifyCode;
    }

    public void setNeedVerifyCode(boolean NeedVerifyCode) {
        this.NeedVerifyCode = NeedVerifyCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public DatasBean getDatas() {
        return Datas;
    }

    public void setDatas(DatasBean Datas) {
        this.Datas = Datas;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public static class DatasBean {
        private String FileName;
        private String FileSuf;
        private String Url;
        private int Type;
        private String Size;
        private int FileID;

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String FileName) {
            this.FileName = FileName;
        }

        public String getFileSuf() {
            return FileSuf;
        }

        public void setFileSuf(String FileSuf) {
            this.FileSuf = FileSuf;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String Size) {
            this.Size = Size;
        }

        public int getFileID() {
            return FileID;
        }

        public void setFileID(int FileID) {
            this.FileID = FileID;
        }
    }
}
