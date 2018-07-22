package com.njdp.njdp_drivers;

/**
 * Created by Rock on 2018/7/22.
 */

public class InfoBean {

    /**
     * result : {"fmId":1,"id":3,"uavHotelCost":2,"uavLaborCost":4,"uavRunFuel":3,"uavRunVelocity":2,"uavWorkCost":43,"uavWorkTime":44}
     * states : true
     * msg : 操作成功！
     */

    private ResultBean result;
    private boolean states;
    private String msg;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
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

    public static class ResultBean {
        /**
         * fmId : 1
         * id : 3
         * uavHotelCost : 2
         * uavLaborCost : 4
         * uavRunFuel : 3
         * uavRunVelocity : 2
         * uavWorkCost : 43
         * uavWorkTime : 44
         */

        private int fmId;
        private int id;
        private int uavHotelCost;
        private int uavLaborCost;
        private int uavRunFuel;
        private int uavRunVelocity;
        private int uavWorkCost;
        private int uavWorkTime;

        public int getFmId() {
            return fmId;
        }

        public void setFmId(int fmId) {
            this.fmId = fmId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUavHotelCost() {
            return uavHotelCost;
        }

        public void setUavHotelCost(int uavHotelCost) {
            this.uavHotelCost = uavHotelCost;
        }

        public int getUavLaborCost() {
            return uavLaborCost;
        }

        public void setUavLaborCost(int uavLaborCost) {
            this.uavLaborCost = uavLaborCost;
        }

        public int getUavRunFuel() {
            return uavRunFuel;
        }

        public void setUavRunFuel(int uavRunFuel) {
            this.uavRunFuel = uavRunFuel;
        }

        public int getUavRunVelocity() {
            return uavRunVelocity;
        }

        public void setUavRunVelocity(int uavRunVelocity) {
            this.uavRunVelocity = uavRunVelocity;
        }

        public int getUavWorkCost() {
            return uavWorkCost;
        }

        public void setUavWorkCost(int uavWorkCost) {
            this.uavWorkCost = uavWorkCost;
        }

        public int getUavWorkTime() {
            return uavWorkTime;
        }

        public void setUavWorkTime(int uavWorkTime) {
            this.uavWorkTime = uavWorkTime;
        }
    }
}
