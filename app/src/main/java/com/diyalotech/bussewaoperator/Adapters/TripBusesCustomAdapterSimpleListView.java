package com.diyalotech.bussewaoperator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.diyalotech.bussewaoperator.Model.ResponseClass;
import com.diyalotech.bussewaoperator.R;

import java.util.List;

/**
 * Created by aqhtar on 5/21/15.
 */
public class TripBusesCustomAdapterSimpleListView extends BaseAdapter {

    private Context context;
    private List<ResponseClass.TripsDto> items;

    public TripBusesCustomAdapterSimpleListView(Context context, List<ResponseClass.TripsDto> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_simple_listview, parent, false);
            viewHolder.route = (TextView) convertView.findViewById(R.id.route);
            viewHolder.bus_number = (TextView) convertView.findViewById(R.id.bus_number);
           // viewHolder.date = (TextView) convertView.findViewById(R.id.departure_date);
            viewHolder.time = (TextView) convertView.findViewById(R.id.departure_time);
            //  viewHolder.busType = (TextView) convertView.findViewById(R.id.bustype);
            viewHolder.operator = (TextView) convertView.findViewById(R.id.operator);
            viewHolder.price = (TextView) convertView.findViewById(R.id.ticket_price);
            // viewHolder.facilities = (TextView) convertView.findViewById(R.id.facilities);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.route.setText(items.get(position).getRoute());
        viewHolder.bus_number.setText(items.get(position).getBusNo());
        //viewHolder.date.setText(items.get(position).getDate());
        viewHolder.time.setText(items.get(position).getDepartureTime());
        //viewHolder.busType.setText(items.get(position).getBusType());
        viewHolder.operator.setText(items.get(position).getOperator());
        viewHolder.price.setText(items.get(position).getTicketPrice() + "");
        // viewHolder.facilities.setText(items.get(position).getAmmenities());
        return convertView;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    private class ViewHolder {
        TextView route;
        TextView bus_number;
       // TextView date;
        TextView time;
        //  TextView busType;
        TextView operator;
        TextView price;
        //  TextView facilities;
    }


}
