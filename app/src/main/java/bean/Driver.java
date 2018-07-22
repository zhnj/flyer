package bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by USER-PC on 2016/4/7.
 */
@DatabaseTable(tableName = "tb_driver")
public class Driver {

    @DatabaseField(generatedId  = true)
    private int tb_id;
    @DatabaseField(columnName = "id")
    private int id;
    @DatabaseField(columnName = "machine_id")
    private String machine_id;
    @DatabaseField(columnName = "password")
    private String password;
    @DatabaseField(columnName = "image_url")
    private String image_url;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "telephone")
    private String telephone;
    @DatabaseField(columnName = "qq")
    private String qq;
    @DatabaseField(columnName = "wechart")
    private String wechart;
    @DatabaseField(columnName = "province")
    private String province;
    @DatabaseField(columnName = "city")
    private String city;
    @DatabaseField(columnName = "county")
    private String county;
    @DatabaseField(columnName = "village")
    private String village;
    @DatabaseField(columnName = "site")
    private String site;



    //新添加
    @DatabaseField(columnName = "sex")
    private String sex;
    @DatabaseField(columnName = "sfzh")
    private String sfzh;
    @DatabaseField(columnName = "populationnum")
    private String populationnum;

    @DatabaseField(columnName = "personcomid")
    private String personcomid;



    @DatabaseField(columnName = "farmlandarea")
    private String farmlandarea;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getQQ() {
        return qq;
    }

    public void setQQ(String qq) {
        this.qq = qq;
    }

    public String getWechart() {
        return wechart;
    }

    public void setWechart(String wechart) {
        this.wechart = wechart;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getSite() {
        return province+city+county+village;
    }

    public String getSite_0() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
    public String getSex() {     return sex;    }

    public void setSex(String sex) {     this.sex = sex;    }

    public String getSfzh() {     return sfzh;  }

    public void setSfzh(String sfzh) {     this.sfzh = sfzh;  }

    public String getPopulationnum() {       return populationnum;    }

    public void setPopulationnum(String populationnum) { this.populationnum = populationnum;    }

    public String getFarmlandarea() {    return farmlandarea;   }

    public void setFarmlandarea(String farmlandarea) {  this.farmlandarea = farmlandarea;   }

    public String getPersoncomid() {      return personcomid;    }

    public void setPersoncomid(String personcomid) {     this.personcomid = personcomid;    }

}
