package com.njdp.njdp_drivers.items.mywork.bean;

import java.util.List;

/**
 * Created by Rock on 2018/7/22.
 */

public class ShixiaoBean {
    /**
     * total : 9
     * totalPage : 9
     * result : [{"flyerId":10,"planId":0,"farmlandsInfo":{"farmlandsLongitude":"115.4430826","farmlandsTown":"白团乡","farmlandsBlockType":"规则","farmlandsCity":"保定","farmlandsUnitPrice":60,"farmlandsArea":50,"farmlandsRemark":"","farmlandsPingjia":"很认真，点赞，太棒了！！！！ ","farmlandsCounty":"清苑县","createdAt":1463673600000,"farmlandsCropsKind":"HWH","pingjiaStartCount":0,"farmlandsEndTime":1529251200000,"farmlandsStartTime":1529164800000,"farmlandsLatitude":"38.76443406","farmlandsStatus":"1","farmlandsVillage":"西壁阳城村","id":2182,"personInfoFarmerMachine":{"populationNum":10,"personAddress":"河北-保定-保定市南市区-南大园乡-金庄村","personSfzh":"123456789 ","personSex":"女","personName":"苑迎春","personPhone":"15033289254","fmId":12,"personComid":88888,"personPhoto":"/upload/UserInfo/12/20180722/20180722120641.png","farmlandArea":600,"id":12,"personWeixin":"11111","personQq":"123456789","updatedAt":1531805856000},"farmlandsProvince":"河北","updatedAt":1479744000000},"id":9,"state":1}]
     * states : true
     * msg : 操作成功！
     */

    private int total;
    private int totalPage;
    private boolean states;
    private String msg;
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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * flyerId : 10
         * planId : 0
         * farmlandsInfo : {"farmlandsLongitude":"115.4430826","farmlandsTown":"白团乡","farmlandsBlockType":"规则","farmlandsCity":"保定","farmlandsUnitPrice":60,"farmlandsArea":50,"farmlandsRemark":"","farmlandsPingjia":"很认真，点赞，太棒了！！！！ ","farmlandsCounty":"清苑县","createdAt":1463673600000,"farmlandsCropsKind":"HWH","pingjiaStartCount":0,"farmlandsEndTime":1529251200000,"farmlandsStartTime":1529164800000,"farmlandsLatitude":"38.76443406","farmlandsStatus":"1","farmlandsVillage":"西壁阳城村","id":2182,"personInfoFarmerMachine":{"populationNum":10,"personAddress":"河北-保定-保定市南市区-南大园乡-金庄村","personSfzh":"123456789 ","personSex":"女","personName":"苑迎春","personPhone":"15033289254","fmId":12,"personComid":88888,"personPhoto":"/upload/UserInfo/12/20180722/20180722120641.png","farmlandArea":600,"id":12,"personWeixin":"11111","personQq":"123456789","updatedAt":1531805856000},"farmlandsProvince":"河北","updatedAt":1479744000000}
         * id : 9
         * state : 1
         */

        private int flyerId;
        private int planId;
        private FarmlandsInfoBean farmlandsInfo;
        private int id;
        private int state;

        public int getFlyerId() {
            return flyerId;
        }

        public void setFlyerId(int flyerId) {
            this.flyerId = flyerId;
        }

        public int getPlanId() {
            return planId;
        }

        public void setPlanId(int planId) {
            this.planId = planId;
        }

        public FarmlandsInfoBean getFarmlandsInfo() {
            return farmlandsInfo;
        }

        public void setFarmlandsInfo(FarmlandsInfoBean farmlandsInfo) {
            this.farmlandsInfo = farmlandsInfo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public static class FarmlandsInfoBean {
            /**
             * farmlandsLongitude : 115.4430826
             * farmlandsTown : 白团乡
             * farmlandsBlockType : 规则
             * farmlandsCity : 保定
             * farmlandsUnitPrice : 60
             * farmlandsArea : 50
             * farmlandsRemark :
             * farmlandsPingjia : 很认真，点赞，太棒了！！！！
             * farmlandsCounty : 清苑县
             * createdAt : 1463673600000
             * farmlandsCropsKind : HWH
             * pingjiaStartCount : 0
             * farmlandsEndTime : 1529251200000
             * farmlandsStartTime : 1529164800000
             * farmlandsLatitude : 38.76443406
             * farmlandsStatus : 1
             * farmlandsVillage : 西壁阳城村
             * id : 2182
             * personInfoFarmerMachine : {"populationNum":10,"personAddress":"河北-保定-保定市南市区-南大园乡-金庄村","personSfzh":"123456789 ","personSex":"女","personName":"苑迎春","personPhone":"15033289254","fmId":12,"personComid":88888,"personPhoto":"/upload/UserInfo/12/20180722/20180722120641.png","farmlandArea":600,"id":12,"personWeixin":"11111","personQq":"123456789","updatedAt":1531805856000}
             * farmlandsProvince : 河北
             * updatedAt : 1479744000000
             */

            private String farmlandsLongitude;
            private String farmlandsTown;
            private String farmlandsBlockType;
            private String farmlandsCity;
            private int farmlandsUnitPrice;
            private int farmlandsArea;
            private String farmlandsRemark;
            private String farmlandsPingjia;
            private String farmlandsCounty;
            private long createdAt;
            private String farmlandsCropsKind;
            private int pingjiaStartCount;
            private long farmlandsEndTime;
            private long farmlandsStartTime;
            private String farmlandsLatitude;
            private String farmlandsStatus;
            private String farmlandsVillage;
            private int id;
            private PersonInfoFarmerMachineBean personInfoFarmerMachine;
            private String farmlandsProvince;
            private long updatedAt;

            public String getFarmlandsLongitude() {
                return farmlandsLongitude;
            }

            public void setFarmlandsLongitude(String farmlandsLongitude) {
                this.farmlandsLongitude = farmlandsLongitude;
            }

            public String getFarmlandsTown() {
                return farmlandsTown;
            }

            public void setFarmlandsTown(String farmlandsTown) {
                this.farmlandsTown = farmlandsTown;
            }

            public String getFarmlandsBlockType() {
                return farmlandsBlockType;
            }

            public void setFarmlandsBlockType(String farmlandsBlockType) {
                this.farmlandsBlockType = farmlandsBlockType;
            }

            public String getFarmlandsCity() {
                return farmlandsCity;
            }

            public void setFarmlandsCity(String farmlandsCity) {
                this.farmlandsCity = farmlandsCity;
            }

            public int getFarmlandsUnitPrice() {
                return farmlandsUnitPrice;
            }

            public void setFarmlandsUnitPrice(int farmlandsUnitPrice) {
                this.farmlandsUnitPrice = farmlandsUnitPrice;
            }

            public int getFarmlandsArea() {
                return farmlandsArea;
            }

            public void setFarmlandsArea(int farmlandsArea) {
                this.farmlandsArea = farmlandsArea;
            }

            public String getFarmlandsRemark() {
                return farmlandsRemark;
            }

            public void setFarmlandsRemark(String farmlandsRemark) {
                this.farmlandsRemark = farmlandsRemark;
            }

            public String getFarmlandsPingjia() {
                return farmlandsPingjia;
            }

            public void setFarmlandsPingjia(String farmlandsPingjia) {
                this.farmlandsPingjia = farmlandsPingjia;
            }

            public String getFarmlandsCounty() {
                return farmlandsCounty;
            }

            public void setFarmlandsCounty(String farmlandsCounty) {
                this.farmlandsCounty = farmlandsCounty;
            }

            public long getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(long createdAt) {
                this.createdAt = createdAt;
            }

            public String getFarmlandsCropsKind() {
                return farmlandsCropsKind;
            }

            public void setFarmlandsCropsKind(String farmlandsCropsKind) {
                this.farmlandsCropsKind = farmlandsCropsKind;
            }

            public int getPingjiaStartCount() {
                return pingjiaStartCount;
            }

            public void setPingjiaStartCount(int pingjiaStartCount) {
                this.pingjiaStartCount = pingjiaStartCount;
            }

            public long getFarmlandsEndTime() {
                return farmlandsEndTime;
            }

            public void setFarmlandsEndTime(long farmlandsEndTime) {
                this.farmlandsEndTime = farmlandsEndTime;
            }

            public long getFarmlandsStartTime() {
                return farmlandsStartTime;
            }

            public void setFarmlandsStartTime(long farmlandsStartTime) {
                this.farmlandsStartTime = farmlandsStartTime;
            }

            public String getFarmlandsLatitude() {
                return farmlandsLatitude;
            }

            public void setFarmlandsLatitude(String farmlandsLatitude) {
                this.farmlandsLatitude = farmlandsLatitude;
            }

            public String getFarmlandsStatus() {
                return farmlandsStatus;
            }

            public void setFarmlandsStatus(String farmlandsStatus) {
                this.farmlandsStatus = farmlandsStatus;
            }

            public String getFarmlandsVillage() {
                return farmlandsVillage;
            }

            public void setFarmlandsVillage(String farmlandsVillage) {
                this.farmlandsVillage = farmlandsVillage;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public PersonInfoFarmerMachineBean getPersonInfoFarmerMachine() {
                return personInfoFarmerMachine;
            }

            public void setPersonInfoFarmerMachine(PersonInfoFarmerMachineBean personInfoFarmerMachine) {
                this.personInfoFarmerMachine = personInfoFarmerMachine;
            }

            public String getFarmlandsProvince() {
                return farmlandsProvince;
            }

            public void setFarmlandsProvince(String farmlandsProvince) {
                this.farmlandsProvince = farmlandsProvince;
            }

            public long getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(long updatedAt) {
                this.updatedAt = updatedAt;
            }

            public static class PersonInfoFarmerMachineBean {
                /**
                 * populationNum : 10
                 * personAddress : 河北-保定-保定市南市区-南大园乡-金庄村
                 * personSfzh : 123456789
                 * personSex : 女
                 * personName : 苑迎春
                 * personPhone : 15033289254
                 * fmId : 12
                 * personComid : 88888
                 * personPhoto : /upload/UserInfo/12/20180722/20180722120641.png
                 * farmlandArea : 600
                 * id : 12
                 * personWeixin : 11111
                 * personQq : 123456789
                 * updatedAt : 1531805856000
                 */

                private int populationNum;
                private String personAddress;
                private String personSfzh;
                private String personSex;
                private String personName;
                private String personPhone;
                private int fmId;
                private int personComid;
                private String personPhoto;
                private int farmlandArea;
                private int id;
                private String personWeixin;
                private String personQq;
                private long updatedAt;

                public int getPopulationNum() {
                    return populationNum;
                }

                public void setPopulationNum(int populationNum) {
                    this.populationNum = populationNum;
                }

                public String getPersonAddress() {
                    return personAddress;
                }

                public void setPersonAddress(String personAddress) {
                    this.personAddress = personAddress;
                }

                public String getPersonSfzh() {
                    return personSfzh;
                }

                public void setPersonSfzh(String personSfzh) {
                    this.personSfzh = personSfzh;
                }

                public String getPersonSex() {
                    return personSex;
                }

                public void setPersonSex(String personSex) {
                    this.personSex = personSex;
                }

                public String getPersonName() {
                    return personName;
                }

                public void setPersonName(String personName) {
                    this.personName = personName;
                }

                public String getPersonPhone() {
                    return personPhone;
                }

                public void setPersonPhone(String personPhone) {
                    this.personPhone = personPhone;
                }

                public int getFmId() {
                    return fmId;
                }

                public void setFmId(int fmId) {
                    this.fmId = fmId;
                }

                public int getPersonComid() {
                    return personComid;
                }

                public void setPersonComid(int personComid) {
                    this.personComid = personComid;
                }

                public String getPersonPhoto() {
                    return personPhoto;
                }

                public void setPersonPhoto(String personPhoto) {
                    this.personPhoto = personPhoto;
                }

                public int getFarmlandArea() {
                    return farmlandArea;
                }

                public void setFarmlandArea(int farmlandArea) {
                    this.farmlandArea = farmlandArea;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getPersonWeixin() {
                    return personWeixin;
                }

                public void setPersonWeixin(String personWeixin) {
                    this.personWeixin = personWeixin;
                }

                public String getPersonQq() {
                    return personQq;
                }

                public void setPersonQq(String personQq) {
                    this.personQq = personQq;
                }

                public long getUpdatedAt() {
                    return updatedAt;
                }

                public void setUpdatedAt(long updatedAt) {
                    this.updatedAt = updatedAt;
                }
            }
        }
    }
}
