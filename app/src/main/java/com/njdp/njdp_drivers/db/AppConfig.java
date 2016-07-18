package com.njdp.njdp_drivers.db;

public class AppConfig {

    public static String URL_TEST ="http://172.28.145.4:8888/db_xskq/test.php";// 测试 url

    public static String URL_IP="http://218.12.43.229:81/";//厅里服务器地址1

//    public static String URL_IP="http://211.68.180.9:88/";//学校服务器地址2

    public static String URL_LOGIN = URL_IP+"appLogin"; // 登录 url

    public static String URL_GET_VERIFYCODE = URL_IP+"sendMessage";// 获取手机验证码 url

    public static String URL_VERIFY_MACHINE_ID = URL_IP+"machineIDVerify";// 验证农机id url

    public static String URL_REGISTER = URL_IP+"machineRegister";// 注册 url

    public static String URL_GETPASSWORD1= URL_IP+"forgetPassword";// 找回密码1 url

    public static String URL_GETPASSWORD2= URL_IP+"changePassword";// 找回密码2 url

    public static String URL_RELEASE= URL_IP+"app/farmMachine/store";//农机发布

    public static String URL_MACHINELOCATION= URL_IP+"app/getMachineLocation";//获取农机位置，经纬度

    public static String URL_AROUNDMACHINE= URL_IP+"aapp/searchAroundMachine";//查询固定点周围农机的位置

    public static String URL_GETFIELD= URL_IP+"app/basicDeploy/store";//查询农田

    public static String URL_BASICDEPLOY= URL_IP+"app/execDeploy";//智能调配

    public static String URL_UPLOADDEPLOY= URL_IP+"app/deployChoice";//选择方案上传

    public static String URL_SEARCHFIELD= URL_IP+"app/farmMachine/searchFarmlands";//农田查询

    public static String URL_SERVEROBJECT= URL_IP+"app/measuredFarmlandArea/store";//服务对象

    public static String URL_JOBHISTORY= URL_IP+"app/farmMachineHistory/index";//作业历史查询

    public static String URL_QUERYPERSONINFO= URL_IP+"app/getUserInfo";//查询个人信息

    public static String URL_FIXPERSONINFO= URL_IP+"app/userInfo";//修改个人信息

    public static String URL_REPAIRE=URL_IP+"app/repairStation/index";//维修站

    public static String URL_FIXPASSWORD=URL_IP+"resetPassword";//修改密码


}