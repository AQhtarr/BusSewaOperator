package com.diyalotech.bussewaoperator.Model;

/**
 * Created by AQhtar on 1/1/2016.
 */
public  class ChildView{

    String ticketPrice;
    String ammenities;
    String date;
    String departureTime;

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getAmmenities() {
        return ammenities;
    }

    public void setAmmenities(String ammenities) {
        this.ammenities = ammenities;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}