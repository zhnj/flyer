package com.njdp.njdp_drivers.items.mywork.bean;

/**
 * Created by Rock on 2018/7/14.
 */

public class CommentBean {
    /**
     * farmlandsPingjia : 很认真，点赞，太棒了！！！！
     * pingjiaStartCount : 1
     * states : true
     * msg : 操作成功！
     */

    private String farmlandsPingjia;
    private int pingjiaStartCount;
    private boolean states;
    private String msg;

    public String getFarmlandsPingjia() {
        return farmlandsPingjia;
    }

    public void setFarmlandsPingjia(String farmlandsPingjia) {
        this.farmlandsPingjia = farmlandsPingjia;
    }

    public int getPingjiaStartCount() {
        return pingjiaStartCount;
    }

    public void setPingjiaStartCount(int pingjiaStartCount) {
        this.pingjiaStartCount = pingjiaStartCount;
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
}
