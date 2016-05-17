/**
 * Copyright 2013 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diyalotech.bussewaoperator.Model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseClass {


    public ResponseClass() {

    }

    @SerializedName("status")
    private String mStatus;

    @SerializedName("trips")
    private ArrayList<TripsDto> mtripArrayList = new ArrayList<>();

    @SerializedName("case")
    private String mCase;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("bookedSeats")
    private String mBookedSeats;


    @SerializedName("unbookedSeats")
    private String mUnbookedSeats;

    @SerializedName("code")
    private int mCode;


    @SerializedName("bookedByCustomer")
    private String mBookedByCustomer;

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmCase() {
        return mCase;
    }

    public void setmCase(String mCase) {
        this.mCase = mCase;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getmCode() {
        return mCode;
    }

    public void setmCode(int mCode) {
        this.mCode = mCode;
    }

    public String getmBookedSeats() {
        return mBookedSeats;
    }

    public void setmBookedSeats(String mBookedSeats) {
        this.mBookedSeats = mBookedSeats;
    }

    public String getmUnbookedSeats() {
        return mUnbookedSeats;
    }

    public void setmUnbookedSeats(String mUnbookedSeats) {
        this.mUnbookedSeats = mUnbookedSeats;
    }

    public String getmBookedByCustomer() {
        return mBookedByCustomer;
    }

    public void setmBookedByCustomer(String mBookedByCustomer) {
        this.mBookedByCustomer = mBookedByCustomer;
    }

    public ArrayList<TripsDto> getMtripArrayList() {
        return mtripArrayList;
    }

    public void setMtripArrayList(ArrayList<TripsDto> mtripArrayList) {
        this.mtripArrayList = mtripArrayList;
    }



    public static class TripsDto implements Parcelable {
        String id;
        String from;
        String busNo;
        String to;
        String route;
        String operator;
        String busType;
        String vehicleType;
        String date;
        String ticketPrice;
        String departureTime;
        String bookedSeats;
        String unbookedSeats;
        String bookedByCustomer;
        String invisibleSeats;
        String removableSeats;
        String totalSeats;
        String ammenities;
        String multiDetail;
        String inputField;
        String isRefresh;
        String ticketSrlNo;

        public TripsDto() {

        }

        public String getMultiDetail() {
            return multiDetail;
        }

        public void setMultiDetail(String multiDetail) {
            this.multiDetail = multiDetail;
        }

        public String getInputField() {
            return inputField;
        }

        public void setInputField(String inputField) {
            this.inputField = inputField;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTicketPrice() {
            return ticketPrice;
        }

        public void setTicketPrice(String ticketPrice) {
            this.ticketPrice = ticketPrice;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }

        public String getBookedSeats() {
            return bookedSeats;
        }

        public void setBookedSeats(String bookedSeats) {
            this.bookedSeats = bookedSeats;
        }

        public String getUnbookedSeats() {
            return unbookedSeats;
        }

        public void setUnbookedSeats(String unbookedSeats) {
            this.unbookedSeats = unbookedSeats;
        }


        public String getBusNo() {
            return busNo;
        }

        public void setBusNo(String busNo) {
            this.busNo = busNo;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getBookedByCustomer() {
            return bookedByCustomer;
        }
        public String getRoute() {
            return route;
        }

        public void setBookedByCustomer(String bookedByCustomer) {
            this.bookedByCustomer = bookedByCustomer;
        }

        public String getInvisibleSeats() {
            return invisibleSeats;
        }

        public void setInvisibleSeats(String invisibleSeats) {
            this.invisibleSeats = invisibleSeats;
        }

        public String getRemovableSeats() {
            return removableSeats;
        }

        public void setRemovableSeats(String removableSeats) {
            this.removableSeats = removableSeats;
        }

        public String getTotalSeats() {
            return totalSeats;
        }

        public void setTotalSeats(String totalSeats) {
            this.totalSeats = totalSeats;
        }

        public String getAmmenities() {
            return ammenities;
        }

        public void setAmmenities(String ammenities) {
            this.ammenities = ammenities;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getIsRefresh() {
            return isRefresh;
        }

        public void setIsRefresh(String isRefresh) {
            this.isRefresh = isRefresh;
        }

        public String getTicketSrlNo() {
            return ticketSrlNo;
        }

        public void setTicketSrlNo(String ticketSrlNo) {
            this.ticketSrlNo = ticketSrlNo;
        }

        public TripsDto(Parcel in) {
            id = in.readString();
            from = in.readString();
            to = in.readString();
            operator = in.readString();
            busType = in.readString();
            vehicleType = in.readString();
            date = in.readString();
            ticketPrice = in.readString();
            departureTime = in.readString();
            bookedSeats = in.readString();
            unbookedSeats = in.readString();
            bookedByCustomer = in.readString();
            invisibleSeats = in.readString();
            removableSeats = in.readString();
            inputField = in.readString();
            multiDetail = in.readString();
            totalSeats = in.readString();
            ammenities = in.readString();
            isRefresh = in.readString();
            ticketSrlNo = in.readString();
            busNo = in.readString();
            route = in.readString();
        }

        public static final Creator<TripsDto> CREATOR = new Creator<TripsDto>() {
            @Override
            public TripsDto createFromParcel(Parcel in) {
                return new TripsDto(in);
            }

            @Override
            public TripsDto[] newArray(int size) {
                return new TripsDto[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }


        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(from);
            dest.writeString(to);
            dest.writeString(operator);
            dest.writeString(busType);
            dest.writeString(vehicleType);
            dest.writeString(date);
            dest.writeString(ticketPrice);
            dest.writeString(departureTime);
            dest.writeString(bookedSeats);
            dest.writeString(unbookedSeats);
            dest.writeString(bookedByCustomer);
            dest.writeString(invisibleSeats);
            dest.writeString(removableSeats);
            dest.writeString(multiDetail);
            dest.writeString(inputField);
            dest.writeString(totalSeats);
            dest.writeString(ammenities);
            dest.writeString(isRefresh);
            dest.writeString(ticketSrlNo);
            dest.writeString(busNo);
            dest.writeString(route);
        }
    }
}
