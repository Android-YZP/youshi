package com.mkch.youshi.bean;

import java.util.List;

/**
 * Created by Smith on 2016/9/18.
 */
public class NetScheduleModel {

    /**
     * ScheduleType : 1
     * Subject : 玩
     * Label : 1
     * Place : 江苏宜兴
     * Latitude : 21.323231
     * Longitude : 1.2901921
     * RoomID : 12332
     * Description :
     * StartTime : 2016-09-23 11:22:22
     * StopTime : 2016-11-23 11:22:22
     * IsOneDay : true
     * RemindType : 1
     * TotalTime : 10
     * SingleTime :
     * Weeks : 1,2,3,4
     * WeekTimes : 1
     * Id : 0
     * SendOpenFireNameList : []
     * JoinOpenFireNameList : ["712149833","334964477"]
     * FileIDList : []
     * TimeSpanList : [{"Status":1,"StartTime":"2016-09-28","EndTime":"2016-09-29","RemindTime":"0","Tdate":"2016-09-10"}]
     * MemberShipID : 0
     */

    private ViewModelBean viewModel;

    public ViewModelBean getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModelBean viewModel) {
        this.viewModel = viewModel;
    }

    public static class ViewModelBean {
        private int ScheduleType;
        private String Subject;
        private int Label;
        private String Place;
        private String Latitude;
        private String Longitude;
        private String RoomID;
        private String Description;
        private String StartTime;
        private String StopTime;
        private boolean IsOneDay;
        private int RemindType;
        private String TotalTime;
        private String SingleTime;
        private String Weeks;
        private int WeekTimes;
        private int Id;
        private int MemberShipID;
        private List<String> SendOpenFireNameList;
        private List<String> JoinOpenFireNameList;
        private List<String> FileIDList;
        /**
         * Status : 1
         * StartTime : 2016-09-28
         * EndTime : 2016-09-29
         * RemindTime : 0
         * Tdate : 2016-09-10
         */

        private List<TimeSpanListBean> TimeSpanList;

        public int getScheduleType() {
            return ScheduleType;
        }

        public void setScheduleType(int ScheduleType) {
            this.ScheduleType = ScheduleType;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String Subject) {
            this.Subject = Subject;
        }

        public int getLabel() {
            return Label;
        }

        public void setLabel(int Label) {
            this.Label = Label;
        }

        public String getPlace() {
            return Place;
        }

        public void setPlace(String Place) {
            this.Place = Place;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }

        public String getRoomID() {
            return RoomID;
        }

        public void setRoomID(String RoomID) {
            this.RoomID = RoomID;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getStopTime() {
            return StopTime;
        }

        public void setStopTime(String StopTime) {
            this.StopTime = StopTime;
        }

        public boolean isIsOneDay() {
            return IsOneDay;
        }

        public void setIsOneDay(boolean IsOneDay) {
            this.IsOneDay = IsOneDay;
        }

        public int getRemindType() {
            return RemindType;
        }

        public void setRemindType(int RemindType) {
            this.RemindType = RemindType;
        }

        public String getTotalTime() {
            return TotalTime;
        }

        public void setTotalTime(String TotalTime) {
            this.TotalTime = TotalTime;
        }

        public String getSingleTime() {
            return SingleTime;
        }

        public void setSingleTime(String SingleTime) {
            this.SingleTime = SingleTime;
        }

        public String getWeeks() {
            return Weeks;
        }

        public void setWeeks(String Weeks) {
            this.Weeks = Weeks;
        }

        public int getWeekTimes() {
            return WeekTimes;
        }

        public void setWeekTimes(int WeekTimes) {
            this.WeekTimes = WeekTimes;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getMemberShipID() {
            return MemberShipID;
        }

        public void setMemberShipID(int MemberShipID) {
            this.MemberShipID = MemberShipID;
        }

        public List<String> getSendOpenFireNameList() {
            return SendOpenFireNameList;
        }

        public void setSendOpenFireNameList(List<String> SendOpenFireNameList) {
            this.SendOpenFireNameList = SendOpenFireNameList;
        }

        public List<String> getJoinOpenFireNameList() {
            return JoinOpenFireNameList;
        }

        public void setJoinOpenFireNameList(List<String> JoinOpenFireNameList) {
            this.JoinOpenFireNameList = JoinOpenFireNameList;
        }

        public List<?> getFileIDList() {
            return FileIDList;
        }

        public void setFileIDList(List<String> FileIDList) {
            this.FileIDList = FileIDList;
        }

        public List<TimeSpanListBean> getTimeSpanList() {
            return TimeSpanList;
        }

        public void setTimeSpanList(List<TimeSpanListBean> TimeSpanList) {
            this.TimeSpanList = TimeSpanList;
        }

        public static class TimeSpanListBean {
            private int Status;
            private String StartTime;
            private String EndTime;
            private String RemindTime;
            private String Tdate;

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public String getStartTime() {
                return StartTime;
            }

            public void setStartTime(String StartTime) {
                this.StartTime = StartTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public String getRemindTime() {
                return RemindTime;
            }

            public void setRemindTime(String RemindTime) {
                this.RemindTime = RemindTime;
            }

            public String getTdate() {
                return Tdate;
            }

            public void setTdate(String Tdate) {
                this.Tdate = Tdate;
            }
        }
    }
}
