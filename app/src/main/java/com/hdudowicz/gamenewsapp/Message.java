package com.hdudowicz.gamenewsapp;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message> {
    static SimpleDateFormat FORMATTER =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    private String title;
    private String link;
    private String description;
    private Date date;
    private String imageSrc;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    public void setLink(String link){
        this.link = link;
    }

    public void setImageSrc(String imageSrc){
        this.imageSrc = imageSrc;
    }

    public String getImageSrc(){
        return this.imageSrc;
    }

    public String getDate() {
        return FORMATTER.format(this.date);
    }

    public void setDate(String date){
        while(!date.endsWith("00")){
            date += "0";
        }
        try {
            this.date = FORMATTER.parse(date.trim());
        } catch (ParseException e){
            Log.e("DATE", "Unparsable Date");
        }
    }

    public String getTitle(){return this.title;}
    public String getDescription(){return this.description;}
    public String getLink() {return this.link;}

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int compareTo(Message another) {
        if(another == null) return 1;

        return another.date.compareTo(date);
    }
}
