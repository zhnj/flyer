package bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/5.
 */
public class RepairInfo implements Serializable{
    private int id;
    private String repair_name;//维修站名称
    private String repair_address;//维修站地址
    private String distance;    //维修站距离
    private String repair_telephone;//维修站电话
    private String repair_weixin;//微信
    private String repair_qq;//qq
    private double repair_latitude;//纬度
    private double repair_longitude;//经度
    private String rerepair_chief;//简介


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepair_name() {
        return repair_name;
    }

    public void setRepair_name(String repair_name) {
        this.repair_name = repair_name;
    }

    public String getRepair_address() {
        return repair_address;
    }

    public void setRepair_address(String repair_address) {
        this.repair_address = repair_address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRepair_telephone() {
        return repair_telephone;
    }

    public void setRepair_telephone(String repair_telephone) {
        this.repair_telephone = repair_telephone;
    }

    public String getRepair_weixin() {
        return repair_weixin;
    }

    public void setRepair_weixin(String repair_weixin) {
        this.repair_weixin = repair_weixin;
    }

    public String getRepair_qq() {
        return repair_qq;
    }

    public void setRepair_qq(String repair_qq) {
        this.repair_qq = repair_qq;
    }

    public double getRepair_latitude() {
        return repair_latitude;
    }

    public void setRepair_latitude(double repair_latitude) {
        this.repair_latitude = repair_latitude;
    }

    public double getRepair_longitude() {
        return repair_longitude;
    }

    public void setRepair_longitude(double repair_longitude) {
        this.repair_longitude = repair_longitude;
    }

    public String getRerepair_chief() {
        return rerepair_chief;
    }

    public void setRerepair_chief(String rerepair_chief) {
        this.rerepair_chief = rerepair_chief;
    }
}
