package com.njdp.njdp_drivers.db;

public class AppConfig {

    public static String URL_TEST ="http://172.28.145.4:8888/db_xskq/test.php";// 测试 url

    public static String URL_IP="http://218.12.43.229:81/";//服务器地址

    public static String URL_LOGIN = URL_IP+"appLogin"; // 登录 url

    public static String URL_REGISTER = URL_IP+"db_xskq/register1.php";// 注册 url


    public static String URL_GETPASSWORD1= URL_IP+"db_xskq/forget_password_isAccess.php";// 找回密码1 url

    public static String URL_GETPASSWORD2= URL_IP+"db_xskq/forget_password_finish.php";// 找回密码2 url

    public static String URL_RELEASE= URL_IP+"app/farmMachine/store";//农机发布

    public static String URL_MACHINELOCATION= URL_IP+"app/getMachineLocation";//获取农机位置，经纬度

    public static String URL_GETFIELD= URL_IP+"app/basicDeploy/store";//查询农田

    public static String URL_BASICDEPLOY= URL_IP+"app/execDeploy";//智能调配

    public static String URL_UPLOADDEPLOY= URL_IP+"app/deployChoice";//选择方案上传

    public static String URL_SEARCHFIELD= URL_IP+"app/farmMachine/searchFarmlands";//农田查询

    public static String URL_SERVEROBJECT= URL_IP+"app/measuredFarmlandArea/store";//服务对象

    public static String URL_JOBHISTORY= URL_IP+"app/farmMachineHistory/index";//作业历史查询




}