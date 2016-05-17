package com.diyalotech.bussewaoperator.Model;

/**
 * Created by AQhtar on 1/1/2016.
 */
public class ExpandableTripsDto {

    String operator;
    String busType;
    String busNo;
    String route;
    ChildView childView;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public ChildView getChildView() {
        return childView;
    }

    public void setChildView(ChildView childView) {
        this.childView = childView;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }
}
