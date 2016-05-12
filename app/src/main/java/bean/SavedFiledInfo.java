package bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by USER-PC on 2016/4/29.
 */
@DatabaseTable(tableName = "tb_saveFieldInfo")
public class SavedFiledInfo {
    @DatabaseField(generatedId  = true)
    private int tb_id;
    @DatabaseField(columnName = "id")
    private int id;
    @DatabaseField(columnName = "farm_id")
    private String farm_id;
    @DatabaseField(columnName = "longitude")
    private double longitude;//经度
    @DatabaseField(columnName = "latitude")
    private double latitude;//纬度
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "user_name")
    private String user_name;
    @DatabaseField(columnName = "crops_kind")
    private String crops_kind;
    @DatabaseField(columnName = "unit_price")
    private String unit_price;//单价
    @DatabaseField(columnName = "block_type")
    private String block_type;//规则或者不规则
    @DatabaseField(columnName = "area_num")
    private String area_num;
    @DatabaseField(columnName = "cropLand_site")
    private String cropLand_site;
    @DatabaseField(columnName = "start_time")
    private String start_time;
    @DatabaseField(columnName = "end_time")
    private String end_time;
    @DatabaseField(columnName = "qq")
    private String qq;

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCrops_kind() {
        return crops_kind;
    }

    public void setCrops_kind(String crops_kind) {
        this.crops_kind = crops_kind;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getBlock_type() {
        return block_type;
    }

    public void setBlock_type(String block_type) {
        this.block_type = block_type;
    }

    public String getArea_num() {
        return area_num;
    }

    public void setArea_num(String area_num) {
        this.area_num = area_num;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setCropLand_site(String cropLand_site){this.cropLand_site=cropLand_site;}

    public String getCropLand_site() {
        return cropLand_site;
    }

    //FieldInfo转换成SavedFiledInfo
    public static SavedFiledInfo getSavedFiledInfo(FieldInfo savedFiledInfo)
    {
        SavedFiledInfo fieldInfo=new SavedFiledInfo();
        fieldInfo.setId(savedFiledInfo.getId());
        fieldInfo.setName(savedFiledInfo.getName());//姓名
        fieldInfo.setUser_name(savedFiledInfo.getUser_name());//电话
        fieldInfo.setLongitude(savedFiledInfo.getLongitude());
        fieldInfo.setLatitude(savedFiledInfo.getLatitude());
        fieldInfo.setFarm_id(savedFiledInfo.getFarm_id());
        fieldInfo.setBlock_type(savedFiledInfo.getBlock_type());
        fieldInfo.setCropLand_site(savedFiledInfo.getCropLand_site());//省市县村拼接成的地址
        fieldInfo.setArea_num(savedFiledInfo.getArea_num());
        fieldInfo.setUnit_price(savedFiledInfo.getUnit_price());
        fieldInfo.setCrops_kind(savedFiledInfo.getCrops_kind());
        fieldInfo.setStart_time(savedFiledInfo.getStart_time());
        fieldInfo.setEnd_time(savedFiledInfo.getEnd_time());
        fieldInfo.setQq(savedFiledInfo.getQq());
        return fieldInfo;
    }
}
