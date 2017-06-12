package sk.miroc.whitebikes.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserStatus implements Parcelable {

    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("rented")
    @Expose
    private String rented;
    @SerializedName("usercredit")
    @Expose
    private String usercredit;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getRented() {
        return rented;
    }

    public void setRented(String rented) {
        this.rented = rented;
    }

    public String getUsercredit() {
        return usercredit;
    }

    public void setUsercredit(String usercredit) {
        this.usercredit = usercredit;
    }


    protected UserStatus(Parcel in) {
        limit = in.readByte() == 0x00 ? null : in.readInt();
        rented = in.readString();
        usercredit = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "limit=" + limit +
                ", rented='" + rented + '\'' +
                ", usercredit='" + usercredit + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (limit == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(limit);
        }
        dest.writeString(rented);
        dest.writeString(usercredit);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserStatus> CREATOR = new Parcelable.Creator<UserStatus>() {
        @Override
        public UserStatus createFromParcel(Parcel in) {
            return new UserStatus(in);
        }

        @Override
        public UserStatus[] newArray(int size) {
            return new UserStatus[size];
        }
    };
}
