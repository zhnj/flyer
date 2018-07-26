package com.njdp.njdp_drivers.items.mywork.bean;

import java.util.List;

/**
 * Created by Rock on 2018/7/26.
 */

public class DeployBean {
    /**
     * status : 0
     * result : [{"plan_id":8111,"income":64990.4,"cost":32509.6,"beginDate":"2018-07-26 06:00:00","endDate":"2018-07-30 07:00:00","route":[2263,2225,2266]},{"plan_id":8112,"income":64427.4,"cost":28072.6,"beginDate":"2018-07-26 06:00:00","endDate":"2018-07-30 06:30:00","route":[2263,2225]},{"plan_id":8113,"income":64197.4,"cost":25802.6,"beginDate":"2018-07-26 06:00:00","endDate":"2018-07-26 18:00:00","route":[2263]},{"plan_id":8114,"income":1601.9,"cost":9528.1,"beginDate":"2018-07-26 06:00:00","endDate":"2018-07-30 07:00:00","route":[2224,2225,2266]},{"plan_id":8115,"income":1038.8,"cost":5091.2,"beginDate":"2018-07-26 06:00:00","endDate":"2018-07-30 06:30:00","route":[2224,2225]}]
     */

    private int status;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * plan_id : 8111
         * income : 64990.4
         * cost : 32509.6
         * beginDate : 2018-07-26 06:00:00
         * endDate : 2018-07-30 07:00:00
         * route : [2263,2225,2266]
         */

        private int plan_id;
        private double income;
        private double cost;
        private String beginDate;
        private String endDate;
        private List<Integer> route;

        public int getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(int plan_id) {
            this.plan_id = plan_id;
        }

        public double getIncome() {
            return income;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        public double getCost() {
            return cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public String getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(String beginDate) {
            this.beginDate = beginDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public List<Integer> getRoute() {
            return route;
        }

        public void setRoute(List<Integer> route) {
            this.route = route;
        }
    }
}
