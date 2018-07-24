package com.njdp.njdp_drivers.items.jikai.bean;

import java.util.List;

/**
 * Created by Rock on 2018/7/18.
 */

public class DroneBean {

    /**
     * scheduleUavNorms : [{"brandId":"DJI/大疆mavic pro","id":1},{"brandId":"DJI/大疆Phantom 4 Advanced","id":2},{"brandId":"普宙O2无人机双电版","id":3}]
     * total : 1
     * totalPage : 1
     * result : [{"createdAt":1531324800000,"scheduleUavNorm":{"brandId":"DJI/大疆mavic pro","id":1},"fmId":9,"machineId":"1234","schMachineType":"HCO","id":1,"workingAbility":100,"schMachineRemark":"fly","updatedAt":1531324800000}]
     * states : true
     * msg : 操作成功！
     */

    private int total;
    private int totalPage;
    private boolean states;
    private String msg;
    private List<ScheduleUavNormsBean> scheduleUavNorms;
    private List<ResultBean> result;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isStates() {
        return states;
    }

    public void setStates(boolean states) {
        this.states = states;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ScheduleUavNormsBean> getScheduleUavNorms() {
        return scheduleUavNorms;
    }

    public void setScheduleUavNorms(List<ScheduleUavNormsBean> scheduleUavNorms) {
        this.scheduleUavNorms = scheduleUavNorms;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ScheduleUavNormsBean {
        /**
         * brandId : DJI/大疆mavic pro
         * id : 1
         */

        private String brandId;
        private int id;

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class ResultBean {
        /**
         * createdAt : 1531324800000
         * scheduleUavNorm : {"brandId":"DJI/大疆mavic pro","id":1}
         * fmId : 9
         * machineId : 1234
         * schMachineType : HCO
         * id : 1
         * workingAbility : 100
         * schMachineRemark : fly
         * updatedAt : 1531324800000
         */

        private long createdAt;
        private ScheduleUavNormBean scheduleUavNorm;
        private int fmId;
        private String machineId;
        private String schMachineType;
        private int id;
        private int workingAbility;
        private String schMachineRemark;
        private long updatedAt;

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public ScheduleUavNormBean getScheduleUavNorm() {
            return scheduleUavNorm;
        }

        public void setScheduleUavNorm(ScheduleUavNormBean scheduleUavNorm) {
            this.scheduleUavNorm = scheduleUavNorm;
        }

        public int getFmId() {
            return fmId;
        }

        public void setFmId(int fmId) {
            this.fmId = fmId;
        }

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getSchMachineType() {
            return schMachineType;
        }

        public void setSchMachineType(String schMachineType) {
            this.schMachineType = schMachineType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getWorkingAbility() {
            return workingAbility;
        }

        public void setWorkingAbility(int workingAbility) {
            this.workingAbility = workingAbility;
        }

        public String getSchMachineRemark() {
            return schMachineRemark;
        }

        public void setSchMachineRemark(String schMachineRemark) {
            this.schMachineRemark = schMachineRemark;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public static class ScheduleUavNormBean {
            /**
             * brandId : DJI/大疆mavic pro
             * id : 1
             */

            private String brandId;
            private int id;

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
