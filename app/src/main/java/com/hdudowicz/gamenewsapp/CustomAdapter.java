package com.hdudowicz.gamenewsapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.util.List;


public class CustomAdapter extends ArrayAdapter<Message> {
    private List<Message> messageList;
    private List<Message> imageList;
    public int BASE_HEIGHT = 300;

    public CustomAdapter(Context context, int textViewResourceId, List<Message> messages, List<Message> images) {
        super(context, textViewResourceId, messages);
        this.messageList = messages;
        this.imageList = images;
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_entry, null);
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, BASE_HEIGHT));
        }

        Message currentMessage = messageList.get(position);
        Message currentImage = imageList.get(position);

        if (convertView != null) {
            if (convertView.getLayoutParams().height != BASE_HEIGHT) {
                convertView.getLayoutParams().height = BASE_HEIGHT;
            }
        }

        if (currentMessage != null) {
            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView descriptionView = (TextView) view.findViewById(R.id.description);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            String title = currentMessage.getTitle();
            String description = currentMessage.getDescription();

            if (descriptionView.getLineCount() >= 10) {
                convertView.getLayoutParams().height = BASE_HEIGHT;
            }

            String imageUrl = currentImage.getImageSrc();

            Picasso.with(getContext()).load(imageUrl).into(imageView);


            if (title != null) {
                titleView.setText(html2text(title));
            }
            if (description != null) {
                descriptionView.setText(html2text(description));
            }
        }
        return view;
    }


    @Override
    public int getCount() {
        return messageList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

}
