package com.diyalotech.bussewaoperator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.diyalotech.bussewaoperator.AppUtils.AppLog;
import com.diyalotech.bussewaoperator.Model.ChildView;
import com.diyalotech.bussewaoperator.Model.ExpandableTripsDto;
import com.diyalotech.bussewaoperator.R;

import java.util.ArrayList;

/**
 * Created by aqhtar on 5/21/15.
 */
public class TripBusesCustomAdapter extends BaseExpandableListAdapter implements ExpandableListAdapter {


    private Context context;
    private ArrayList<ExpandableTripsDto> items;

    public TripBusesCustomAdapter(Context context, ArrayList<ExpandableTripsDto> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ChildView chList = items.get(groupPosition).getChildView();
        return chList;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ChildView child = (ChildView) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_view_expandable_listvew, null);
        }
        TextView ticketPriceTextView = (TextView) convertView.findViewById(R.id.ticket_price);
        TextView ammenitiesTextView = (TextView) convertView.findViewById(R.id.facilities);
        TextView departureDateTextView = (TextView) convertView.findViewById(R.id.departure_date);
        TextView departureTimeTextView = (TextView) convertView.findViewById(R.id.departure_time);
        String ticketPrice = child.getTicketPrice();
        AppLog.showLog("Inside Controller  ticketPrice::::::" + ticketPrice);
        if (ticketPrice.contains(",")) {
            ticketPrice = child.getTicketPrice().replace(",", "\n");
            if (ticketPrice.contains(",")) {
                ticketPrice = ticketPrice.replace(":", ":  ");
            }
        }
        ticketPriceTextView.setText(ticketPrice);
        ammenitiesTextView.setText(child.getAmmenities());
        departureDateTextView.setText(child.getDate());
        departureTimeTextView.setText(child.getDepartureTime());

        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ExpandableTripsDto group = (ExpandableTripsDto) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_view_expandable_listview, null);
        }
        TextView routeTextView = (TextView) convertView.findViewById(R.id.route);
        TextView busNumberTextView = (TextView) convertView.findViewById(R.id.bus_number);
        TextView busTypeTextView = (TextView) convertView.findViewById(R.id.bustype);
        TextView operatorTypeTextView = (TextView) convertView.findViewById(R.id.operator);
        busTypeTextView.setText(group.getBusType());
        operatorTypeTextView.setText(group.getOperator());
        busNumberTextView.setText(group.getBusNo());
        routeTextView.setText(group.getRoute());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public int getChildTypeCount(){
        return 1;
    }

    @Override
    public int getGroupTypeCount(){
        return 1;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}



