package com.example.aimsh.movementcontrol;

public class MovementData {

    private String myCar;

    private String myBadge;

    private String myDate;

    private String myLocation;

    public MovementData(){

    }

    public MovementData(String theCar, String theBadge, String theDate, String theLocation){
        myCar = theCar;
        myBadge = theBadge;
        myDate = theDate;
        myLocation = theLocation;
    }

    public String getMyCar(){ return myCar; }

    public String getMyBadge(){ return myBadge; }

    public String getMyDate(){ return myDate; }

    public String getMyLocation() { return myLocation; }

    public void setMyCar(String theCar) { myCar = theCar;}

    public void setMyBadge(String theBadge) { myBadge = theBadge;}

    public void setMyDate(String theDate) {myDate = theDate; }

    public void setMyLocation(String theLocation) {myLocation = theLocation;}

    public String toString(){
        return "Badge: " + myBadge + " VIN: "+ myCar + " Location: " + myLocation + " Date: " + myDate;
    }
}
