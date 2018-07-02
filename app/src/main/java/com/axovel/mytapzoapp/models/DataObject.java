package com.axovel.mytapzoapp.models;

/**
 * Created by axovel on 8/22/2017.
 */

public class DataObject {
    private String date;
    private String sunsign;
    private String horoscope;
    private int horoview;
    public DataObject(String date,String sunsign,String horoscope,int horoview)
    {
        this.date=date;
        this.sunsign=sunsign;
        this.horoscope=horoscope;
        this.horoview=horoview;
    }
    public int getHoroview(){
        return horoview;
    }
    public void setHoroview(){
        this.horoview=horoview;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getSunsign(){
        return sunsign;
    }
    public void setSunsign(String sunsign){
        this.sunsign=sunsign;
    }
    public String getHoroscope(){
        return horoscope;
    }
    public void setHoroscope(String horoscope){
        this.horoscope=horoscope;
    }

}
