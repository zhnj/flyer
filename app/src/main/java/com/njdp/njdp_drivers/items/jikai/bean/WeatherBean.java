package com.njdp.njdp_drivers.items.jikai.bean;

import java.util.List;

/**
 * Created by Rock on 2018/7/23.
 */

public class WeatherBean {
    /**
     * status : 0
     * message : [{"date":"2018-07-23","type":"雷阵雨","windrage":"4"},{"date":"2018-07-24","type":"多云","windrage":"4"},{"date":"2018-07-25","type":"多云","windrage":"4"},{"date":"2018-07-26","type":"小雨","windrage":"3"},{"date":"2018-07-27","type":"阴","windrage":"3"}]
     */

    private int status;
    private List<MessageBean> message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * date : 2018-07-23
         * type : 雷阵雨
         * windrage : 4
         */

        private String date;
        private String type;
        private String windrage;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWindrage() {
            return windrage;
        }

        public void setWindrage(String windrage) {
            this.windrage = windrage;
        }
    }
}
