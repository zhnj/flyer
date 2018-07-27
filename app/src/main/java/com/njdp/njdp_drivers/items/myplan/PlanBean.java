package com.njdp.njdp_drivers.items.myplan;

import java.util.List;

/**
 * Created by Rock on 2018/7/26.
 */

public class PlanBean {
    /**
     * status : 0
     * msg : 操作成功！
     * result : [{"plan_id":"8163","income":"106695","cost":"66305","beginDate":"2018-07-24","endDate":"2018-09-17","plan_finish":"0"},{"plan_id":"8196","income":"14591.2","cost":"13408.8","beginDate":"2018-07-25","endDate":"2018-08-22","plan_finish":"0"}]
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
         * plan_id : 8163
         * income : 106695
         * cost : 66305
         * beginDate : 2018-07-24
         * endDate : 2018-09-17
         * plan_finish : 0
         */

        private String plan_id;
        private String income;
        private String cost;
        private String beginDate;
        private String endDate;
        private String plan_finish;

        public String getPlan_id() {
            return plan_id;
        }

        public void setPlan_id(String plan_id) {
            this.plan_id = plan_id;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
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

        public String getPlan_finish() {
            return plan_finish;
        }

        public void setPlan_finish(String plan_finish) {
            this.plan_finish = plan_finish;
        }
    }
}
