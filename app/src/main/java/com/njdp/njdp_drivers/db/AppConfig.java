package com.njdp.njdp_drivers.db;

public class AppConfig {

    public static String URL_TEST ="http://172.28.145.4:8888/db_xskq/test.php";// 二教实验室网线网络，本机服务器测试url

    public static String URL_IP="http://211.68.183.50:88/";//测试服务器

//    public static String URL_IP="http://211.68.180.9:88/";//学校服务器地址

    public static String URL_IP_JAVA="http://211.68.183.50:8088/";//学校服务器地址

    public static String URL_LOGIN = URL_IP+"appLogin"; // 登录 url

    public static String URL_GET_VERIFYCODE = URL_IP+"sendMessage";// 获取手机验证码 url

    public static String URL_VERIFY_MACHINE_ID = URL_IP+"machineIDVerify";// 验证农机id url

    public static String URL_REGISTER = URL_IP+"flyerRegister";// 注册 url

    public static String URL_GETPASSWORD1= URL_IP+"forgetPassword";// 找回密码1 url

    public static String URL_GETPASSWORD2= URL_IP+"changePassword";// 找回密码2 url

    public static String URL_RELEASE= URL_IP+"app/farmMachine/store";//农机发布

    public static String URL_MACHINELOCATION= URL_IP+"app/getMachineLocation";//获取农机位置，经纬度

    public static String URL_AROUNDMACHINE= URL_IP+"app/searchAroundMachine";//查询固定点周围农机的位置

    //public static String URL_GETFIELD= URL_IP+"app/Deploy/store";//查询农田

    //public static String URL_BASICDEPLOY= URL_IP+"app/execDeploy";//智能调配uav

    public static String URL_GETFIELD= URL_IP+"app/uavDeploy/store";//查询农田

    public static String URL_BASICDEPLOY= URL_IP+"app/uavDeploy/execDeploy";//智能调配uav

    public static String URL_UPLOADDEPLOY= URL_IP_JAVA+"Mobile/UAV/deployChoice";//选择方案上传

    public static String URL_SEARCHFIELD= URL_IP+"app/farmMachine/searchFarmlands";//农田查询

    public static String URL_SERVEROBJECT= URL_IP+"app/measuredFarmlandArea/store";//服务对象

    public static String URL_JOBHISTORY= URL_IP+"app/farmMachineHistory/index";//作业历史查询

    public static String URL_QUERYPERSONINFO= URL_IP+"app/getUserInfo";//查询个人信息

    public static String URL_FIXPERSONINFO= URL_IP+"app/userInfo";//修改个人信息

    public static String URL_REPAIRE=URL_IP+"app/repairStation/index";//维修站

    public static String URL_FIXPASSWORD=URL_IP+"resetPassword";//修改密码


    ///////////////////无人机修改
    public static String URL_searchFarmlands=URL_IP+"app/farmMachine/searchFarmlands";//周边可用农田
    //飞机服务公司信息
    public static String URL_findFlyComByUser="http://211.68.183.50:8088/Mobile/UAV/findFlyComByUser";
    //天气数据
    public static String URL_WeatherData=URL_IP+"app/getWeather";

    public static String URL_UAV_listCompanys="http://211.68.183.50:8088/Mobile/UAV/listCompanys";//所有无人机公司列表

    public static String URL_PREFERE=URL_IP+"UAV-platform/mvc/mobile/myUav/addPreference";//添加偏好信息

    public static String URL_GET_FAVORorADD=URL_IP_JAVA+"UAV-platform/mvc/mobile/myWork/addAndOver";//收藏地块

    public static String DRONE_DISPALY=URL_IP_JAVA+"UAV-platform/mvc/mobile/myUav/findMyUav";//无人机查看

    public static String DRONE_DELETE=URL_IP_JAVA+"UAV-platform/mvc/mobile/myUav/deleteUav";//删除无人机

    public static String DRONE_ADDorUPDATE=URL_IP_JAVA+"UAV-platform/mvc/mobile/myUav/addMyUav";//增加、修改无人机

    public static String URL_MYJOB_PART= URL_IP_JAVA+"UAV-platform/mvc/mobile/myWork/searchWork";//我的作业查询

    public static String URL_MYJOB_PART_DELETE= URL_IP_JAVA+"UAV-platform/mvc/mobile/myWork/deleteWork";//我的作业查询

    public static String URL_MYJOB_PART_CHECK= URL_IP_JAVA+"UAV-platform/mvc/mobile/myWork/addAndOver";//地块确认完成

    public static String URL_MYJOB_PART_COMMENT= URL_IP_JAVA+"UAV-platform/mvc/mobile/myWork/searchComment";//地块确认完成

    public static String URL_Weather= URL_IP+"app/getWeather";//需求查询天气接口

    public static String URL_DEPLOY= URL_IP_JAVA+"Mobile/UAV/listMyDeploy";//需求查询天气接口

}