package com.diyalotech.bussewaoperator.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;

/**
 * Created by aqhtar on 8/21/15.
 */
public class SelectedSeatDto implements Parcelable {

    String seatId;
    ImageButton seatName;
    String selectionType;
    String seatNameToMerchant;
    String seatNameToCustomer;

    public SelectedSeatDto(){

    }

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public static final Creator<SelectedSeatDto> CREATOR = new Creator<SelectedSeatDto>() {
        @Override
        public SelectedSeatDto createFromParcel(Parcel in) {
            return new SelectedSeatDto(in);
        }

        @Override
        public SelectedSeatDto[] newArray(int size) {
            return new SelectedSeatDto[size];
        }
    };

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }


    public String getSeatNameToMerchant() {
        return seatNameToMerchant;
    }


    public void setSeatNameToMerchant(String seatNameToMerchant) {
        this.seatNameToMerchant = seatNameToMerchant;
    }

    public String getSeatNameToCustomer() {
        return seatNameToCustomer;
    }

    public void setSeatNameToCustomer(String seatNameToCustomer) {
        this.seatNameToCustomer = seatNameToCustomer;
    }

    public ImageButton getSeatName() {
        return seatName;
    }

    public void setSeatName(ImageButton seatName) {
        this.seatName = seatName;
    }

    public SelectedSeatDto(Parcel in){
        seatId = in.readString();
        seatNameToMerchant = in.readString();
        selectionType = in.readString();
        seatNameToCustomer = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(seatId);
        dest.writeString(seatNameToMerchant);
        dest.writeString(selectionType);
        dest.writeString(seatNameToCustomer);

    }
}
