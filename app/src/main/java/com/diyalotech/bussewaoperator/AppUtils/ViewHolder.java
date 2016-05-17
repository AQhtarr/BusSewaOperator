package com.diyalotech.bussewaoperator.AppUtils;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.diyalotech.bussewaoperator.R;

public class ViewHolder {
    public TextView busTypeTextView;
    public TextView operatorTypeTextView;
    public TextView amountTextView;
    public TextView facilitiesTextView;
    public TextView depatureDateTextView;
    public TextView depatureDateTimeView;
    public ViewHolder(View v) {
        this.busTypeTextView = (TextView)v.findViewById(R.id.bustype);
        this.operatorTypeTextView = (TextView)v.findViewById(R.id.operator);
        this.amountTextView = (TextView)v.findViewById(R.id.ticket_price);
        this.amountTextView = (TextView)v.findViewById(R.id.ticket_price);
        this.facilitiesTextView = (TextView)v.findViewById(R.id.facilities);
        this.depatureDateTextView = (TextView)v.findViewById(R.id.departure_date);
        this.depatureDateTimeView = (TextView)v.findViewById(R.id.departure_time);
    }

}