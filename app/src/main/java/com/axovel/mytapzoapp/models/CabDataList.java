package com.axovel.mytapzoapp.models;

import android.app.Application;

import java.io.Serializable;

/**
 * Created by axovel on 8/2/2017.
 */

public class CabDataList extends Application implements Serializable {

    private String idCab="";
    private String displayNmae="";
    private String currency="";
    private String distanceUnit="";
    private String time_Unit="";
    private String cab_Icon_image;
    private String distance;
    private int costPerDistance;
    private String car_icon;
    private int estimateTime;
    private int minimumFare;
    private int maximumFare;
    private String bookingId;
    private String driverName;
    private String cabType;
    private String cabNumber;
    private String carModel;
    private String surChargeValue;
    private double driverLatitude;
    private boolean switcdata;
    private String crn;
    String costShare;


    private String pickUP_Address;
    private String destination_Address;
    private String time_rideLater;
    private String date_rideLater;
    private int checkDestCode;



    public String getPickUP_Address() {
        return pickUP_Address;
    }

    public void setPickUP_Address(String pickUP_Address) {
        this.pickUP_Address = pickUP_Address;
    }

    public String getDestination_Address() {
        return destination_Address;
    }

    public void setDestination_Address(String destination_Address) {
        destination_Address = destination_Address;
    }

    public String getTime_rideLater() {
        return time_rideLater;
    }

    public void setTime_rideLater(String time_rideLater) {
        this.time_rideLater = time_rideLater;
    }

    public String getDate_rideLater() {
        return date_rideLater;
    }

    public void setDate_rideLater(String date_rideLater) {
        this.date_rideLater = date_rideLater;
    }







    public String getCostShare() {
        return costShare;
    }

    public void setCostShare(String costShare) {
        this.costShare = costShare;
    }

    public int getCheckDestCode() {
        return checkDestCode;
    }

    public void setCheckDestCode(int checkDestCode) {
        this.checkDestCode = checkDestCode;
    }



    public CabDataList(String dNmae, String ava, String dimg, int estimateTime, String costshare, int withoutDestinationCode) {

        this.displayNmae=dNmae;
        this.isCheck=ava;
        this.cab_Icon_image=dimg;
        this.estimateTime=estimateTime;
        this.costShare=costshare;
        this.checkDestCode=withoutDestinationCode;
    }


    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public CabDataList(String cab_Icon_image,String displayNmae,String pickUP_Address,String destination_Address,String date_rideLater,String time_rideLater){
        this.cab_Icon_image=cab_Icon_image;
        this.displayNmae=displayNmae;
        this.pickUP_Address=pickUP_Address;
        this.destination_Address=destination_Address;
        this.date_rideLater=date_rideLater;
        this.time_rideLater=time_rideLater;

    }



    public CabDataList(String dNmae, String ava, String dimg, int estimateTime, int cost,int checkDestCode) {


        this.displayNmae=dNmae;
        this.isCheck=ava;
        this.cab_Icon_image=dimg;
        this.estimateTime=estimateTime;
        this.costPerDistance=cost;
        this.checkDestCode=checkDestCode;

    }

    public CabDataList(String dNmae, String ava, int estimateTime, int maximum, int minimum, String dimg, int checkDestCode) {

        this.displayNmae=dNmae;
        this.isCheck=ava;
        this.estimateTime=estimateTime;
        this.maximumFare=maximum;
        this.minimumFare=minimum;
        this.cab_Icon_image=dimg;
        this.checkDestCode=checkDestCode;


    }


    public boolean isSwitcdata() {
        return switcdata;
    }

    public void setSwitcdata(boolean switcdata) {
        this.switcdata = switcdata;
    }



    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCabType() {
        return cabType;
    }

    public void setCabType(String cabType) {
        this.cabType = cabType;
    }

    public String getCabNumber() {
        return cabNumber;
    }

    public void setCabNumber(String cabNumber) {
        this.cabNumber = cabNumber;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getSurChargeValue() {
        return surChargeValue;
    }

    public void setSurChargeValue(String surChargeValue) {
        this.surChargeValue = surChargeValue;
    }

    public double getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(double driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public double getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(double driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    private double driverLongitude;


    public int getMinimumFare() {
        return minimumFare;
    }

    public void setMinimumFare(int minimumFare) {
        this.minimumFare = minimumFare;
    }

    public int getMaximumFare() {
        return maximumFare;
    }

    public void setMaximumFare(int maximumFare) {
        this.maximumFare = maximumFare;
    }



    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    private String isCheck;






    public String getCar_icon() {
        return car_icon;
    }

    public void setCar_icon(String car_icon) {
        this.car_icon = car_icon;
    }



//    public CabDataList(String idCab, String displayNmae, String currency, String distanceUnit, String time_Unit, String cab_Icon_image, String distance, int costPerDistance, int estimateTime) {
//        this.idCab = idCab;
//        this.displayNmae = displayNmae;
//        this.currency = currency;
//        this.distanceUnit = distanceUnit;
//        this.time_Unit = time_Unit;
//        this.cab_Icon_image = cab_Icon_image;
//        this.distance = distance;
//        this.costPerDistance = costPerDistance;
//        this.estimateTime = estimateTime;
//    }


    public String getIdCab() {
        return idCab;
    }

    public void setIdCab(String idCab) {
        this.idCab = idCab;
    }

    public String getDisplayNmae() {
        return displayNmae;
    }

    public void setDisplayNmae(String displayNmae) {
        this.displayNmae = displayNmae;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getTime_Unit() {
        return time_Unit;
    }

    public void setTime_Unit(String time_Unit) {
        this.time_Unit = time_Unit;
    }

    public String getCab_Icon_image() {
        return cab_Icon_image;
    }

    public void setCab_Icon_image(String cab_Icon_image) {
        this.cab_Icon_image = cab_Icon_image;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getCostPerDistance() {
        return costPerDistance;
    }

    public void setCostPerDistance(int costPerDistance) {
        this.costPerDistance = costPerDistance;
    }

    public int getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(int estimateTime) {
        this.estimateTime = estimateTime;
    }
    public int getCheckDest() {
        return checkDestCode;
    }

    public void setCheckDest(int checkDest) {
        this.checkDestCode = checkDest;
    }







}
