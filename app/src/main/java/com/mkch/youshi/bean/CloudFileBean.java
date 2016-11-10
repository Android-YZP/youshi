package com.mkch.youshi.bean;

import java.util.List;

/**
 * Created by Smith on 2016/11/8.
 */

public class CloudFileBean {

    /**
     * Success : true
     * NeedVerifyCode : false
     * Message : 获取成功
     * ErrorCode :
     * Datas : [{"FileName":"8d43617c2f8e1f08408717d44dfc5970fb7d4de3.apk","FileSuf":"apk","Url":"/CloudDisk/2016-10-26/2254f097-c885-4df6-9fde-e2cb302782ee.apk","Type":5,"Size":"0.00M","FileID":17180},{"FileName":"yoshi.db-shm","FileSuf":"db-shm","Url":"/CloudDisk/2016-10-27/3f197dd4-7d3b-4b41-918a-87da95f1eae3.db-shm","Type":5,"Size":"0.03M","FileID":17263},{"FileName":"0241de0a0b064f409e1801bc43632a9a.amr","FileSuf":"amr","Url":"/CloudDisk/2016-10-27/00b6b653-3cfc-4b55-9177-39adab5d0a7d.amr","Type":5,"Size":"0.00M","FileID":17331},{"FileName":"2aaf4d0e1b89403782696c18dd330b18.amr","FileSuf":"amr","Url":"/CloudDisk/2016-10-27/eb7cae67-b401-413e-b2a5-b164a2a27685.amr","Type":5,"Size":"0.00M","FileID":17334},{"FileName":"umeng_android_socialize_demo.apk","FileSuf":"apk","Url":"/CloudDisk/2016-10-27/3b187984-d805-48b8-a9c5-50a4b804aa3b.apk","Type":5,"Size":"6.28M","FileID":17337},{"FileName":"umeng_android_socialize_demo.apk","FileSuf":"apk","Url":"/CloudDisk/2016-10-27/5d1a61b4-7549-49d3-91b3-ab5e5336a51a.apk","Type":5,"Size":"6.28M","FileID":17420},{"FileName":"umeng_android_socialize_demo.apk","FileSuf":"apk","Url":"/CloudDisk/2016-10-27/e402e321-72ef-422f-ab5d-3ed1727b494a.apk","Type":5,"Size":"6.28M","FileID":17458},{"FileName":"umeng_android_socialize_demo.apk","FileSuf":"apk","Url":"/CloudDisk/2016-10-27/a523fd3a-5bc6-4b00-ba86-904e5ac0c8bf.apk","Type":5,"Size":"6.28M","FileID":17475},{"FileName":"umeng_android_socialize_demo.apk","FileSuf":"apk","Url":"/CloudDisk/2016-10-27/16fab46b-7afc-433c-bd25-b821010a7ff1.apk","Type":5,"Size":"6.28M","FileID":17478},{"FileName":"yoshi.db","FileSuf":"db","Url":"/CloudDisk/2016-10-27/592e288a-b5ce-4d00-8388-07dfa9aa56f6.db","Type":5,"Size":"0.02M","FileID":17704},{"FileName":"yoshi.db","FileSuf":"db","Url":"/CloudDisk/2016-11-04/9ae72c5a-b611-4a4b-b4c2-e8d0170e3042.db","Type":5,"Size":"0.04M","FileID":19811},{"FileName":"umeng_android_socialize_demo.apk","FileSuf":"apk","Url":"/CloudDisk/2016-11-08/3cdfebc5-9ce4-4235-815e-ba57cfc0c981.apk","Type":5,"Size":"6.28M","FileID":20086},{"FileName":"yoshi.db","FileSuf":"db","Url":"/CloudDisk/2016-11-08/276e2edc-1102-4ccc-acaa-b078d250c396.db","Type":5,"Size":"0.03M","FileID":20088},{"FileName":"yoshi.db","FileSuf":"db","Url":"/CloudDisk/2016-11-08/97340611-720e-4207-ad91-b1f06d156c7c.db","Type":5,"Size":"0.03M","FileID":20091}]
     * Total : 0
     */

    private boolean Success;
    private boolean NeedVerifyCode;
    private String Message;
    private String ErrorCode;
    private int Total;
    /**
     * FileName : 8d43617c2f8e1f08408717d44dfc5970fb7d4de3.apk
     * FileSuf : apk
     * Url : /CloudDisk/2016-10-26/2254f097-c885-4df6-9fde-e2cb302782ee.apk
     * Type : 5
     * Size : 0.00M
     * FileID : 17180
     */

    private List<DatasBean> Datas;

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

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<DatasBean> getDatas() {
        return Datas;
    }

    public void setDatas(List<DatasBean> Datas) {
        this.Datas = Datas;
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
