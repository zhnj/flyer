package bean;

/**
 * Created by USER-PC on 2016/4/20.
 */
public class BasicDeployRes {
    private String plan_id;
    private String cost;
    private String income;
    private String beginDate;
    private String endDate;
//    private String workTime;
    private StringBuffer[] route;
//    private Map<String,FieldCost> routes;


    public String getId() {
        return plan_id;
    }

    public void setId(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getEarnings() {
        return income;
    }

    public void setEarnings(String earnings) {
        this.income = earnings;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

//    public String getWorkTime() {
//        return workTime;
//    }
//
//    public void setWorkTime(String workTime) {
//        this.workTime = workTime;
//    }

    public StringBuffer[] getRoute() {
        return route;
    }

    public void setRoute(StringBuffer[] route) {
        this.route = route;
    }
//
//    public Map<String, FieldCost> getRoutes() {
//        return routes;
//    }
//
//    public void setRoutes(Map<String, FieldCost> routes) {
//        this.routes = routes;
//    }

}