package bean;

/**
 * Created by USER-PC on 2016/4/22.
 * 服务器返回的多个FieldInfo集合
 * id
 * 单个集合抓换成的字符串
 */
public class FieldInfoList {

    private String fieldsId;
    private String listContent;


    public String getListContent() {
        return listContent;
    }

    public void setListContent(String listContent) {
        this.listContent = listContent;
    }

    public String getFieldsId() {
        return fieldsId;
    }

    public void setFieldsId(String fieldsId) {
        this.fieldsId = fieldsId;
    }

}