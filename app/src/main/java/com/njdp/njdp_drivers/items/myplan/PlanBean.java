package com.njdp.njdp_drivers.items.myplan;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rock on 2018/7/26.
 */

public class PlanBean {
    /**
     * status : 0
     * msg : 操作成功！
     * result : [{"plan_id":"8164","income":"106356.5","cost":"66643.5","route":{"2262":{"farm_province":"河北","farm_city":"邯郸","farm_county":"磁县","farm_town":"磁州乡","farm_village":"陈家庄村","area_num":"500.0","unit_price":"100.0","longitude":"114.2922365","latitude":"36.49465516","cost":22349.089,"costTime":5,"income":50000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-28 06:00:00","endTime":"2018-07-28 17:00:00","returnStatus":1},"2265":{"farm_province":"河北","farm_city":"石家庄","farm_county":"新乐市","farm_town":"木村乡","farm_village":"东田村","area_num":"200.0","unit_price":"100.0","longitude":"114.6531936","latitude":"38.3733754","cost":9235.564,"costTime":2,"income":20000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-29 06:00:00","endTime":"2018-07-29 08:00:00","returnStatus":1},"2266":{"farm_province":"河北","farm_city":"保定","farm_county":"清苑县","farm_town":"白团乡","farm_village":"西壁阳城村","area_num":"100.0","unit_price":"50.0","longitude":"115.44308259374245","latitude":"38.76443405686449","cost":4545.69,"costTime":1,"income":5000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-30 06:00:00","endTime":"2018-07-30 07:00:00","returnStatus":1},"2263":{"farm_province":"河北","farm_city":"保定","farm_county":"易县","farm_town":"桥头乡","farm_village":"中山南村","area_num":"600.0","unit_price":"150.0","longitude":"115.4897855","latitude":"38.85648284","cost":25832.997,"costTime":11.4995,"income":90000,"arriveTime":"2018-07-30 18:29:58","beginTime":"2018-07-30 18:29:58","endTime":"2018-07-31 16:29:58","returnStatus":0},"2264":{"farm_province":"河北","farm_city":"邢台","farm_county":"宁晋县","farm_town":"苏家庄乡","farm_village":"苏家庄村","area_num":"100.0","unit_price":"80.0","longitude":"115.10196904243853","latitude":"37.757591872136835","cost":4680.13,"costTime":64.355,"income":8000,"arriveTime":"2018-08-02 09:51:16","beginTime":"2018-08-02 09:51:16","endTime":"2018-08-02 16:51:16","returnStatus":0}},"beginDate":"2018-07-24","endDate":"2018-09-17","plan_finish":"0"},{"plan_id":"8163","income":"106695","cost":"66305","route":{"2263":{"farm_province":"河北","farm_city":"保定","farm_county":"易县","farm_town":"桥头乡","farm_village":"中山南村","area_num":"600.0","unit_price":"150.0","longitude":"115.4897855","latitude":"38.85648284","cost":25801.422,"costTime":6.237,"income":90000,"arriveTime":"2018-07-26 06:14:13","beginTime":"2018-07-26 06:14:13","endTime":"2018-07-26 18:14:13","returnStatus":0},"2262":{"farm_province":"河北","farm_city":"邯郸","farm_county":"磁县","farm_town":"磁州乡","farm_village":"陈家庄村","area_num":"500.0","unit_price":"100.0","longitude":"114.2922365","latitude":"36.49465516","cost":22350.391,"costTime":5,"income":50000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-28 06:00:00","endTime":"2018-07-28 17:00:00","returnStatus":1},"2264":{"farm_province":"河北","farm_city":"邢台","farm_county":"宁晋县","farm_town":"苏家庄乡","farm_village":"苏家庄村","area_num":"100.0","unit_price":"80.0","longitude":"115.10196904243853","latitude":"37.757591872136835","cost":4772.848,"costTime":79.808,"income":8000,"arriveTime":"2018-08-01 09:48:28","beginTime":"2018-08-01 09:48:28","endTime":"2018-08-01 16:48:28","returnStatus":0},"2265":{"farm_province":"河北","farm_city":"石家庄","farm_county":"新乐市","farm_town":"木村乡","farm_village":"东田村","area_num":"200.0","unit_price":"100.0","longitude":"114.6531936","latitude":"38.3733754","cost":8836.679,"costTime":41.4465,"income":20000,"arriveTime":"2018-08-02 16:15:15","beginTime":"2018-08-02 16:15:15","endTime":"2018-08-02 18:15:15","returnStatus":0},"2266":{"farm_province":"河北","farm_city":"保定","farm_county":"清苑县","farm_town":"白团乡","farm_village":"西壁阳城村","area_num":"100.0","unit_price":"50.0","longitude":"115.44308259374245","latitude":"38.76443405686449","cost":4543.69,"costTime":41.615,"income":5000,"arriveTime":null,"beginTime":null,"endTime":null,"returnStatus":0}},"beginDate":"2018-07-24","endDate":"2018-09-17","plan_finish":"0"}]
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
         * plan_id : 8164
         * income : 106356.5
         * cost : 66643.5
         * route : {"2262":{"farm_province":"河北","farm_city":"邯郸","farm_county":"磁县","farm_town":"磁州乡","farm_village":"陈家庄村","area_num":"500.0","unit_price":"100.0","longitude":"114.2922365","latitude":"36.49465516","cost":22349.089,"costTime":5,"income":50000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-28 06:00:00","endTime":"2018-07-28 17:00:00","returnStatus":1},"2265":{"farm_province":"河北","farm_city":"石家庄","farm_county":"新乐市","farm_town":"木村乡","farm_village":"东田村","area_num":"200.0","unit_price":"100.0","longitude":"114.6531936","latitude":"38.3733754","cost":9235.564,"costTime":2,"income":20000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-29 06:00:00","endTime":"2018-07-29 08:00:00","returnStatus":1},"2266":{"farm_province":"河北","farm_city":"保定","farm_county":"清苑县","farm_town":"白团乡","farm_village":"西壁阳城村","area_num":"100.0","unit_price":"50.0","longitude":"115.44308259374245","latitude":"38.76443405686449","cost":4545.69,"costTime":1,"income":5000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-30 06:00:00","endTime":"2018-07-30 07:00:00","returnStatus":1},"2263":{"farm_province":"河北","farm_city":"保定","farm_county":"易县","farm_town":"桥头乡","farm_village":"中山南村","area_num":"600.0","unit_price":"150.0","longitude":"115.4897855","latitude":"38.85648284","cost":25832.997,"costTime":11.4995,"income":90000,"arriveTime":"2018-07-30 18:29:58","beginTime":"2018-07-30 18:29:58","endTime":"2018-07-31 16:29:58","returnStatus":0},"2264":{"farm_province":"河北","farm_city":"邢台","farm_county":"宁晋县","farm_town":"苏家庄乡","farm_village":"苏家庄村","area_num":"100.0","unit_price":"80.0","longitude":"115.10196904243853","latitude":"37.757591872136835","cost":4680.13,"costTime":64.355,"income":8000,"arriveTime":"2018-08-02 09:51:16","beginTime":"2018-08-02 09:51:16","endTime":"2018-08-02 16:51:16","returnStatus":0}}
         * beginDate : 2018-07-24
         * endDate : 2018-09-17
         * plan_finish : 0
         */

        private String plan_id;
        private String income;
        private String cost;
        private RouteBean route;
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

        public RouteBean getRoute() {
            return route;
        }

        public void setRoute(RouteBean route) {
            this.route = route;
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

        public static class RouteBean {
            /**
             * 2262 : {"farm_province":"河北","farm_city":"邯郸","farm_county":"磁县","farm_town":"磁州乡","farm_village":"陈家庄村","area_num":"500.0","unit_price":"100.0","longitude":"114.2922365","latitude":"36.49465516","cost":22349.089,"costTime":5,"income":50000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-28 06:00:00","endTime":"2018-07-28 17:00:00","returnStatus":1}
             * 2265 : {"farm_province":"河北","farm_city":"石家庄","farm_county":"新乐市","farm_town":"木村乡","farm_village":"东田村","area_num":"200.0","unit_price":"100.0","longitude":"114.6531936","latitude":"38.3733754","cost":9235.564,"costTime":2,"income":20000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-29 06:00:00","endTime":"2018-07-29 08:00:00","returnStatus":1}
             * 2266 : {"farm_province":"河北","farm_city":"保定","farm_county":"清苑县","farm_town":"白团乡","farm_village":"西壁阳城村","area_num":"100.0","unit_price":"50.0","longitude":"115.44308259374245","latitude":"38.76443405686449","cost":4545.69,"costTime":1,"income":5000,"arriveTime":"2018-07-27 06:00:00","beginTime":"2018-07-30 06:00:00","endTime":"2018-07-30 07:00:00","returnStatus":1}
             * 2263 : {"farm_province":"河北","farm_city":"保定","farm_county":"易县","farm_town":"桥头乡","farm_village":"中山南村","area_num":"600.0","unit_price":"150.0","longitude":"115.4897855","latitude":"38.85648284","cost":25832.997,"costTime":11.4995,"income":90000,"arriveTime":"2018-07-30 18:29:58","beginTime":"2018-07-30 18:29:58","endTime":"2018-07-31 16:29:58","returnStatus":0}
             * 2264 : {"farm_province":"河北","farm_city":"邢台","farm_county":"宁晋县","farm_town":"苏家庄乡","farm_village":"苏家庄村","area_num":"100.0","unit_price":"80.0","longitude":"115.10196904243853","latitude":"37.757591872136835","cost":4680.13,"costTime":64.355,"income":8000,"arriveTime":"2018-08-02 09:51:16","beginTime":"2018-08-02 09:51:16","endTime":"2018-08-02 16:51:16","returnStatus":0}
             */

            @SerializedName("2262")
            private _$2262Bean _$2262;
            @SerializedName("2265")
            private _$2265Bean _$2265;
            @SerializedName("2266")
            private _$2266Bean _$2266;
            @SerializedName("2263")
            private _$2263Bean _$2263;
            @SerializedName("2264")
            private _$2264Bean _$2264;

            public _$2262Bean get_$2262() {
                return _$2262;
            }

            public void set_$2262(_$2262Bean _$2262) {
                this._$2262 = _$2262;
            }

            public _$2265Bean get_$2265() {
                return _$2265;
            }

            public void set_$2265(_$2265Bean _$2265) {
                this._$2265 = _$2265;
            }

            public _$2266Bean get_$2266() {
                return _$2266;
            }

            public void set_$2266(_$2266Bean _$2266) {
                this._$2266 = _$2266;
            }

            public _$2263Bean get_$2263() {
                return _$2263;
            }

            public void set_$2263(_$2263Bean _$2263) {
                this._$2263 = _$2263;
            }

            public _$2264Bean get_$2264() {
                return _$2264;
            }

            public void set_$2264(_$2264Bean _$2264) {
                this._$2264 = _$2264;
            }

            public static class _$2262Bean {
                /**
                 * farm_province : 河北
                 * farm_city : 邯郸
                 * farm_county : 磁县
                 * farm_town : 磁州乡
                 * farm_village : 陈家庄村
                 * area_num : 500.0
                 * unit_price : 100.0
                 * longitude : 114.2922365
                 * latitude : 36.49465516
                 * cost : 22349.089
                 * costTime : 5
                 * income : 50000
                 * arriveTime : 2018-07-27 06:00:00
                 * beginTime : 2018-07-28 06:00:00
                 * endTime : 2018-07-28 17:00:00
                 * returnStatus : 1
                 */

                private String farm_province;
                private String farm_city;
                private String farm_county;
                private String farm_town;
                private String farm_village;
                private String area_num;
                private String unit_price;
                private String longitude;
                private String latitude;
                private double cost;
                private double costTime;
                private double income;
                private String arriveTime;
                private String beginTime;
                private String endTime;
                private double returnStatus;

                public String getFarm_province() {
                    return farm_province;
                }

                public void setFarm_province(String farm_province) {
                    this.farm_province = farm_province;
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

                public String getArea_num() {
                    return area_num;
                }

                public void setArea_num(String area_num) {
                    this.area_num = area_num;
                }

                public String getUnit_price() {
                    return unit_price;
                }

                public void setUnit_price(String unit_price) {
                    this.unit_price = unit_price;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public double getCost() {
                    return cost;
                }

                public void setCost(double cost) {
                    this.cost = cost;
                }

                public double getCostTime() {
                    return costTime;
                }

                public void setCostTime(int costTime) {
                    this.costTime = costTime;
                }

                public double getIncome() {
                    return income;
                }

                public void setIncome(int income) {
                    this.income = income;
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

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public double getReturnStatus() {
                    return returnStatus;
                }

                public void setReturnStatus(int returnStatus) {
                    this.returnStatus = returnStatus;
                }
            }

            public static class _$2265Bean {
                /**
                 * farm_province : 河北
                 * farm_city : 石家庄
                 * farm_county : 新乐市
                 * farm_town : 木村乡
                 * farm_village : 东田村
                 * area_num : 200.0
                 * unit_price : 100.0
                 * longitude : 114.6531936
                 * latitude : 38.3733754
                 * cost : 9235.564
                 * costTime : 2
                 * income : 20000
                 * arriveTime : 2018-07-27 06:00:00
                 * beginTime : 2018-07-29 06:00:00
                 * endTime : 2018-07-29 08:00:00
                 * returnStatus : 1
                 */

                private String farm_province;
                private String farm_city;
                private String farm_county;
                private String farm_town;
                private String farm_village;
                private String area_num;
                private String unit_price;
                private String longitude;
                private String latitude;
                private double cost;
                private double costTime;
                private double income;
                private String arriveTime;
                private String beginTime;
                private String endTime;
                private double returnStatus;

                public String getFarm_province() {
                    return farm_province;
                }

                public void setFarm_province(String farm_province) {
                    this.farm_province = farm_province;
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

                public String getArea_num() {
                    return area_num;
                }

                public void setArea_num(String area_num) {
                    this.area_num = area_num;
                }

                public String getUnit_price() {
                    return unit_price;
                }

                public void setUnit_price(String unit_price) {
                    this.unit_price = unit_price;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public double getCost() {
                    return cost;
                }

                public void setCost(double cost) {
                    this.cost = cost;
                }

                public double getCostTime() {
                    return costTime;
                }

                public void setCostTime(int costTime) {
                    this.costTime = costTime;
                }

                public double getIncome() {
                    return income;
                }

                public void setIncome(int income) {
                    this.income = income;
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

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public double getReturnStatus() {
                    return returnStatus;
                }

                public void setReturnStatus(int returnStatus) {
                    this.returnStatus = returnStatus;
                }
            }

            public static class _$2266Bean {
                /**
                 * farm_province : 河北
                 * farm_city : 保定
                 * farm_county : 清苑县
                 * farm_town : 白团乡
                 * farm_village : 西壁阳城村
                 * area_num : 100.0
                 * unit_price : 50.0
                 * longitude : 115.44308259374245
                 * latitude : 38.76443405686449
                 * cost : 4545.69
                 * costTime : 1
                 * income : 5000
                 * arriveTime : 2018-07-27 06:00:00
                 * beginTime : 2018-07-30 06:00:00
                 * endTime : 2018-07-30 07:00:00
                 * returnStatus : 1
                 */

                private String farm_province;
                private String farm_city;
                private String farm_county;
                private String farm_town;
                private String farm_village;
                private String area_num;
                private String unit_price;
                private String longitude;
                private String latitude;
                private double cost;
                private double costTime;
                private double income;
                private String arriveTime;
                private String beginTime;
                private String endTime;
                private double returnStatus;

                public String getFarm_province() {
                    return farm_province;
                }

                public void setFarm_province(String farm_province) {
                    this.farm_province = farm_province;
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

                public String getArea_num() {
                    return area_num;
                }

                public void setArea_num(String area_num) {
                    this.area_num = area_num;
                }

                public String getUnit_price() {
                    return unit_price;
                }

                public void setUnit_price(String unit_price) {
                    this.unit_price = unit_price;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public double getCost() {
                    return cost;
                }

                public void setCost(double cost) {
                    this.cost = cost;
                }

                public double getCostTime() {
                    return costTime;
                }

                public void setCostTime(int costTime) {
                    this.costTime = costTime;
                }

                public double getIncome() {
                    return income;
                }

                public void setIncome(int income) {
                    this.income = income;
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

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public double getReturnStatus() {
                    return returnStatus;
                }

                public void setReturnStatus(int returnStatus) {
                    this.returnStatus = returnStatus;
                }
            }

            public static class _$2263Bean {
                /**
                 * farm_province : 河北
                 * farm_city : 保定
                 * farm_county : 易县
                 * farm_town : 桥头乡
                 * farm_village : 中山南村
                 * area_num : 600.0
                 * unit_price : 150.0
                 * longitude : 115.4897855
                 * latitude : 38.85648284
                 * cost : 25832.997
                 * costTime : 11.4995
                 * income : 90000
                 * arriveTime : 2018-07-30 18:29:58
                 * beginTime : 2018-07-30 18:29:58
                 * endTime : 2018-07-31 16:29:58
                 * returnStatus : 0
                 */

                private String farm_province;
                private String farm_city;
                private String farm_county;
                private String farm_town;
                private String farm_village;
                private String area_num;
                private String unit_price;
                private String longitude;
                private String latitude;
                private double cost;
                private double costTime;
                private double income;
                private String arriveTime;
                private String beginTime;
                private String endTime;
                private double returnStatus;

                public String getFarm_province() {
                    return farm_province;
                }

                public void setFarm_province(String farm_province) {
                    this.farm_province = farm_province;
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

                public String getArea_num() {
                    return area_num;
                }

                public void setArea_num(String area_num) {
                    this.area_num = area_num;
                }

                public String getUnit_price() {
                    return unit_price;
                }

                public void setUnit_price(String unit_price) {
                    this.unit_price = unit_price;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public double getCost() {
                    return cost;
                }

                public void setCost(double cost) {
                    this.cost = cost;
                }

                public double getCostTime() {
                    return costTime;
                }

                public void setCostTime(double costTime) {
                    this.costTime = costTime;
                }

                public double getIncome() {
                    return income;
                }

                public void setIncome(int income) {
                    this.income = income;
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

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public double getReturnStatus() {
                    return returnStatus;
                }

                public void setReturnStatus(int returnStatus) {
                    this.returnStatus = returnStatus;
                }
            }

            public static class _$2264Bean {
                /**
                 * farm_province : 河北
                 * farm_city : 邢台
                 * farm_county : 宁晋县
                 * farm_town : 苏家庄乡
                 * farm_village : 苏家庄村
                 * area_num : 100.0
                 * unit_price : 80.0
                 * longitude : 115.10196904243853
                 * latitude : 37.757591872136835
                 * cost : 4680.13
                 * costTime : 64.355
                 * income : 8000
                 * arriveTime : 2018-08-02 09:51:16
                 * beginTime : 2018-08-02 09:51:16
                 * endTime : 2018-08-02 16:51:16
                 * returnStatus : 0
                 */

                private String farm_province;
                private String farm_city;
                private String farm_county;
                private String farm_town;
                private String farm_village;
                private String area_num;
                private String unit_price;
                private String longitude;
                private String latitude;
                private double cost;
                private double costTime;
                private double income;
                private String arriveTime;
                private String beginTime;
                private String endTime;
                private double returnStatus;

                public String getFarm_province() {
                    return farm_province;
                }

                public void setFarm_province(String farm_province) {
                    this.farm_province = farm_province;
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

                public String getArea_num() {
                    return area_num;
                }

                public void setArea_num(String area_num) {
                    this.area_num = area_num;
                }

                public String getUnit_price() {
                    return unit_price;
                }

                public void setUnit_price(String unit_price) {
                    this.unit_price = unit_price;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public double getCost() {
                    return cost;
                }

                public void setCost(double cost) {
                    this.cost = cost;
                }

                public double getCostTime() {
                    return costTime;
                }

                public void setCostTime(double costTime) {
                    this.costTime = costTime;
                }

                public double getIncome() {
                    return income;
                }

                public void setIncome(int income) {
                    this.income = income;
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

                public String getEndTime() {
                    return endTime;
                }

                public void setEndTime(String endTime) {
                    this.endTime = endTime;
                }

                public double getReturnStatus() {
                    return returnStatus;
                }

                public void setReturnStatus(int returnStatus) {
                    this.returnStatus = returnStatus;
                }
            }
        }
    }
}
