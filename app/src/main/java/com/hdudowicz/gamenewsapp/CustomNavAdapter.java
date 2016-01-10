package com.hdudowicz.gamenewsapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomNavAdapter extends ArrayAdapter<String>{
    private LayoutInflater mInflater;

    private String[] mStrings;
    private TypedArray mIcons;
    private int mViewResourceId;


    public CustomNavAdapter(Context context, int viewResourceId, String[] strings, TypedArray icons,  String[] url) {
        super(context, viewResourceId, strings);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mStrings = strings;
        mIcons = icons;

        mViewResourceId = viewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        ImageView iconView = (ImageView) convertView.findViewById(R.id.icon);
        iconView.setImageDrawable(mIcons.getDrawable(position));

        TextView titleView = (TextView) convertView.findViewById(R.id.optionTitle);
        titleView.setText(mStrings[position]);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
