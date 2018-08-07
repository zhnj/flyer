package com.njdp.njdp_drivers.items.myplan;

import java.util.List;

/**
 * Created by Rock on 2018/7/28.
 */

public class PlanDetailBean {
    /**
     * status : 0
     * msg : 操作成功！
     * result : [{"area_num":"600.0","arriveTime":"2018-07-26 06:14:13","beginTime":"2018-07-26 06:14:13","cost":"25801.422","costTime":"6.237","endTime":"2018-07-26 18:14:13","farm_city":"保定","farm_county":"易县","farm_province":"河北","farm_town":"桥头乡","farm_village":"中山南村","farmer_phone":"","id":"2263","income":"90000","latitude":"38.85648284","longitude":"115.4897855","returnStatus":"0","unit_price":"150.0"},{"area_num":"500.0","arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-28 06:00:00","cost":"22350.391","costTime":"5","endTime":"2018-07-28 17:00:00","farm_city":"邯郸","farm_county":"磁县","farm_province":"河北","farm_town":"磁州乡","farm_village":"陈家庄村","farmer_phone":"","id":"2262","income":"50000","latitude":"36.49465516","longitude":"114.2922365","returnStatus":"1","unit_price":"100.0"},{"area_num":"100.0","arriveTime":"2018-08-01 09:48:28","beginTime":"2018-08-01 09:48:28","cost":"4772.848","costTime":"79.808","endTime":"2018-08-01 16:48:28","farm_city":"邢台","farm_county":"宁晋县","farm_province":"河北","farm_town":"苏家庄乡","farm_village":"苏家庄村","farmer_phone":"","id":"2264","income":"8000","latitude":"37.757591872136835","longitude":"115.10196904243853","returnStatus":"0","unit_price":"80.0"},{"area_num":"200.0","arriveTime":"2018-08-02 16:15:15","beginTime":"2018-08-02 16:15:15","cost":"8836.679","costTime":"41.4465","endTime":"2018-08-02 18:15:15","farm_city":"石家庄","farm_county":"新乐市","farm_province":"河北","farm_town":"木村乡","farm_village":"东田村","farmer_phone":"","id":"2265","income":"20000","latitude":"38.3733754","longitude":"114.6531936","returnStatus":"0","unit_price":"100.0"},{"area_num":"100.0","arriveTime":"","beginTime":"","cost":"4543.69","costTime":"41.615","endTime":"","farm_city":"保定","farm_county":"清苑县","farm_province":"河北","farm_town":"白团乡","farm_village":"西壁阳城村","farmer_phone":"","id":"2266","income":"5000","latitude":"38.76443405686449","longitude":"115.44308259374245","returnStatus":"0","unit_price":"50.0"}]
     */

    private String status;
    private String msg;
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * area_num : 600.0
         * arriveTime : 2018-07-26 06:14:13
         * beginTime : 2018-07-26 06:14:13
         * cost : 25801.422
         * costTime : 6.237
         * endTime : 2018-07-26 18:14:13
         * farm_city : 保定
         * farm_county : 易县
         * farm_province : 河北
         * farm_town : 桥头乡
         * farm_village : 中山南村
         * farmer_phone :
         * id : 2263
         * income : 90000
         * latitude : 38.85648284
         * longitude : 115.4897855
         * returnStatus : 0
         * unit_price : 150.0
         */

        private String area_num;
        private String arriveTime;
        private String beginTime;
        private String cost;
        private String costTime;
        private String endTime;
        private String farm_city;
        private String farm_county;
        private String farm_province;
        private String farm_town;
        private String farm_village;
        private String farmer_phone;
        private String id;
        private String income;
        private String latitude;
        private String longitude;
        private String returnStatus;
        private String modifyStatus;
        private String unit_price;

        public String getModifyStatus() {
            return modifyStatus;
        }

        public void setModifyStatus(String modifyStatus) {
            this.modifyStatus = modifyStatus;
        }

        public String getArea_num() {
            return area_num;
        }

        public void setArea_num(String area_num) {
            this.area_num = area_num;
        }

        public String getArriveTime() {
            return arriveTime;
        }

        public void setArriveTime(String arriveTime) {
            this.arriveTime = arriveTime;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getCostTime() {
            return costTime;
        }

        public void setCostTime(String costTime) {
            this.costTime = costTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFarm_city() {
            return farm_city;
        }

        public void setFarm_city(String farm_city) {
            this.farm_city = farm_city;
        }

        public String getFarm_county() {
            return farm_county;
        }

        public void setFarm_county(String farm_county) {
            this.farm_county = farm_county;
        }

        public String getFarm_province() {
            return farm_province;
        }

        public void setFarm_province(String farm_province) {
            this.farm_province = farm_province;
        }

        public String getFarm_town() {
            return farm_town;
        }

        public void setFarm_town(String farm_town) {
            this.farm_town = farm_town;
        }

        public String getFarm_village() {
            return farm_village;
        }

        public void setFarm_village(String farm_village) {
            this.farm_village = farm_village;
        }

        public String getFarmer_phone() {
            return farmer_phone;
        }

        public void setFarmer_phone(String farmer_phone) {
            this.farmer_phone = farmer_phone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getReturnStatus() {
            return returnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            this.returnStatus = returnStatus;
        }

        public String getUnit_price() {
            return unit_price;
        }

        public void setUnit_price(String unit_price) {
            this.unit_price = unit_price;
        }
    }
}
