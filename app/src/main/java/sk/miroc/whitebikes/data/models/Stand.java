package sk.miroc.whitebikes.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Double2;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stand implements Parcelable {

    @SerializedName("standId")
    @Expose
    private String standId;
    @SerializedName("bikecount")
    @Expose
    private String bikecount;
    @SerializedName("standDescription")
    @Expose
    private String standDescription;
    @SerializedName("standName")
    @Expose
    private String standName;
    @SerializedName("standPhoto")
    @Expose
    private String standPhoto;
    @SerializedName("lon")
    @Expose
    private String lon;
    @SerializedName("lat")
    @Expose
    private String lat;

    public String getStandId() {
        return standId;
    }

    public void setStandId(String standId) {
        this.standId = standId;
    }

    public String getBikecount() {
        return bikecount;
    }

    public void setBikecount(String bikecount) {
        this.bikecount = bikecount;
    }

    public String getStandDescription() {
        return standDescription;
    }

    public void setStandDescription(String standDescription) {
        this.standDescription = standDescription;
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public String getStandPhoto() {
        return standPhoto;
    }

    public void setStandPhoto(String standPhoto) {
        this.standPhoto = standPhoto;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }


    protected Stand(Parcel in) {
        standId = in.readString();
        bikecount = in.readString();
        standDescription = in.readString();
        standName = in.readString();
        standPhoto = in.readString();
        lon = in.readString();
        lat = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(standId);
        dest.writeString(bikecount);
        dest.writeString(standDescription);
        dest.writeString(standName);
        dest.writeString(standPhoto);
        dest.writeString(lon);
        dest.writeString(lat);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Stand> CREATOR = new Parcelable.Creator<Stand>() {
        @Override
        public Stand createFromParcel(Parcel in) {
            return new Stand(in);
        }

        @Override
        public Stand[] newArray(int size) {
            return new Stand[size];
        }
    };

    @Override
    public String toString() {
        return "Stand{" +
                "standId='" + standId + '\'' +
                ", bikecount='" + bikecount + '\'' +
                ", standDescription='" + standDescription + '\'' +
                ", standName='" + standName + '\'' +
                ", standPhoto='" + standPhoto + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                '}';
    }

    public LatLng getLatLng() throws InvalidLocationException{
        try {
            double latDouble = Double.parseDouble(getLat());
            double lonDouble = Double.parseDouble(getLon());
            return new LatLng(latDouble, lonDouble);
        } catch (NumberFormatException nfe){
            throw new InvalidLocationException(String.format("Location of this stand cannot be converted to GPS: %s",  this));
        }
    }

    public static class InvalidLocationException extends Exception{
        public InvalidLocationException(String error){
            super(error);
        }
    }
}
