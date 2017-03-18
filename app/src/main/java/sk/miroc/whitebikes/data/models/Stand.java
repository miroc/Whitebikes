package sk.miroc.whitebikes.data.models;

import android.os.Parcel;
import android.os.Parcelable;

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
    private Double lon;
    @SerializedName("lat")
    @Expose
    private Double lat;

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

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }


    protected Stand(Parcel in) {
        standId = in.readString();
        bikecount = in.readString();
        standDescription = in.readString();
        standName = in.readString();
        standPhoto = in.readString();
        lon = in.readByte() == 0x00 ? null : in.readDouble();
        lat = in.readByte() == 0x00 ? null : in.readDouble();
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
        if (lon == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(lon);
        }
        if (lat == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(lat);
        }
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
}
